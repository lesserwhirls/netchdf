@file:OptIn(InternalLibraryApi::class)

package com.sunya.netchdf.hdf5Clib

import com.sunya.cdm.api.*
import com.sunya.cdm.array.*
import com.sunya.cdm.util.Indent
import com.sunya.cdm.util.InternalLibraryApi
import com.sunya.netchdf.hdf4.ODLparser
import com.sunya.netchdf.hdf5.*
import com.sunya.netchdf.hdf5.H5builder.Companion.HDF5_CLASS
import com.sunya.netchdf.hdf5.H5builder.Companion.HDF5_DIMENSION_LIST
import com.sunya.netchdf.hdf5.H5builder.Companion.HDF5_DIMENSION_NAME
import com.sunya.netchdf.hdf5.H5builder.Companion.HDF5_DIMENSION_SCALE
import com.sunya.netchdf.hdf5.H5builder.Companion.HDF5_IGNORE_ATTS
import com.sunya.netchdf.hdf5.H5builder.Companion.HDF5_SKIP_ATTS
import com.sunya.netchdf.hdf5Clib.H5Cbuilder.Companion.debug
import com.sunya.netchdf.hdf5Clib.ffm.*
import com.sunya.netchdf.hdf5Clib.ffm.hdf5_h.*
import com.sunya.netchdf.netcdf4.Netcdf4
import java.io.IOException
import java.lang.foreign.*

const val MAX_NAME = 2048L
const val MAX_DIMS = 255L
const val H5P_DEFAULT_LONG = 0L

const val debugGroup = false
const val debugVersion = false

// Really a builder of the root Group.
class H5Cbuilder(val filename: String) {
    val rootBuilder = Group.Builder("")
    val file_id : Long
    fun formatType() = if (structMetadata.isEmpty()) "hdf5" else "hdf-eos5"

    private val structMetadata = mutableListOf<String>()
    private val typeinfoMap = mutableMapOf<Typedef, MutableList<Group.Builder>>()
    private val typeinfoList = mutableListOf<H5CTypeInfo>()
    private val typeinfoMap2 = mutableMapOf<H5CTypeInfo, MutableList<Group.Builder>>()
    private val datasetMap = mutableMapOf<Long, Pair<Group.Builder, Variable.Builder<*>>>()

    init {
        Arena.ofConfined().use { arena ->
            H5get_libversion(arena)
            H5is_library_threadsafe(arena)

            val filenameSeg: MemorySegment = arena.allocateUtf8String(filename)
            file_id = H5Fopen(filenameSeg, H5F_ACC_RDONLY(), H5P_DEFAULT_LONG)
            if (debug) println("H5Fopen $filename fileHandle ${this.file_id}")

            // read root group
            readGroup("/", GroupContext(arena, rootBuilder, this.file_id, Indent(2, 0)))
        }

        addTypesToGroups()
        convertReferences(rootBuilder)

        // hdf-eos5
        if (structMetadata.isNotEmpty()) {
            val sm = structMetadata.joinToString("")
            ODLparser(rootBuilder, false).applyStructMetadata(sm)
        }
    }

    // call free_memory() when an	API	call returns a pointer	or has	a	**	OUT	parameter.
    fun H5get_libversion(arena: Arena) {
        // herr_t H5get_libversion	(	unsigned *	majnum,
        //unsigned *	minnum,
        //unsigned *	relnum )
        //Returns the HDF library release number.
        //
        //Parameters
        //[out]	majnum	The major version number of the library
        //[out]	minnum	The minor version number of the library
        //[out]	relnum	The release version number of the library

        val majnum_p = arena.allocate(C_INT, 0)
        val minnum_p = arena.allocate(C_INT, 0)
        val relnum_p = arena.allocate(C_INT, 0)

        //     public static int H5get_libversion(MemorySegment majnum, MemorySegment minnum, MemorySegment relnum) {
        checkErr("H5get_libversion", H5get_libversion(majnum_p, minnum_p, relnum_p))

        val majnum = majnum_p[C_INT, 0]
        val minnum = minnum_p[C_INT, 0]
        val relnum = relnum_p[C_INT, 0]

        if (debugVersion) println("majnum = $majnum, minnum = $minnum, relnum = $relnum")

        checkErr("H5check_version", H5check_version(majnum, minnum, relnum))

         // the question is: this the same as we built with ffi ??
        val ffiVersion_p = H5_VERS_INFO()
        val ffiVersion = ffiVersion_p.getUtf8String(0)
        if (debugVersion) println("ffiVersion = $ffiVersion")
        val expected = "$majnum.$minnum.$relnum"
        // netcdf library currently built with 1.10.10. not 1.12.1;
        // So netcdf Clib and HdfC5 clib cant exist independently, whichever gets loaded first wins.
        // So have to build with same Hdf5 version.
        // require(ffiVersion.endsWith(expected)) { "HDF5 library version mismatch.  Expected $expected but got $ffiVersion" }
        if (!ffiVersion.endsWith(expected)) {
            println("HDF5 library version mismatch.  Expected $expected but got $ffiVersion")
        }
    }

    fun H5is_library_threadsafe(arena: Arena) {

        val ts_p = arena.allocate(C_INT, 0)

        //  [out]	is_ts	Boolean value indicating whether the library was built with thread-safety enabled
        checkErr("H5is_library_threadsafe", H5is_library_threadsafe(ts_p))
        val ts = ts_p[C_INT, 0]

        if (debugVersion) println("isThreadsafe = $ts = ${if (ts == 0) "false" else "true"}")
    }

    ////////////////////////////////////////////////////////////////////////////////
    private fun readGroup( g5name: String, context : GroupContext): Boolean {
        if (debug) println("${context.indent}readGroup for '$g5name'")
        // long H5Gopen2(long loc_id, MemorySegment name, long gapl_id)
        // hid_t H5Gopen2(hid_t	loc_id, const char *name, hid_t	gapl_id )
        val groupName: MemorySegment = context.arena.allocateUtf8String(g5name)
        val group_id : Long = H5Gopen2(context.group5id, groupName, H5P_DEFAULT_LONG)
        if (debug) println("${context.indent}H5Gopen2 '$g5name' group_id=${group_id}")
        if (group_id < 0) {
            println("FAILED to open group '$g5name' with id=${group_id}")
            return false
        }
        val nestedContext = GroupContext(context.arena, context.group, group_id, context.indent.incr())
        val indent = context.indent.incr() // ??

        //readGroupDimensions(session, group5)
        //readVariables(session, group5)

        // herr_t H5Gget_num_objs	(hid_t loc_id, hsize_t * 	num_objs)
        val num_objs_p = context.arena.allocate(C_LONG, 0L)
        checkErr("H5Gget_num_objs", H5Gget_num_objs(group_id, num_objs_p))
        val num_objs = num_objs_p[C_LONG, 0]
        if (debug) println("${indent}H5Gget_num_objs num_objs=${num_objs}")

        // iterate over the links
        // herr_t H5Literate2	(
        //   hid_t	grp_id,
        //   H5_index_t	idx_type,
        //   H5_iter_order_t	order,
        //   hsize_t *	idx,
        //   H5L_iterate2_t	op,
        //   void *	op_data )
        //
        // Parameters
        // [in]	grp_id	Group identifier
        // [in]	idx_type	Index type
        // [in]	order	Iteration order
        // [in,out]	idx	Pointer to an iteration index to allow continuing a previous iteration
        // [in]	op	Callback function
        // [in,out]	op_data	User-defined callback function context, gets passed to H5Lreceiver

        // H5Literate2() iterates through the links in a file or group, group_id, in the order of the specified index,
        // idx_type, using a user-defined callback routine op.
        // H5Literate2() does not recursively follow links into subgroups of the specified group.

        val idx_p = context.arena.allocate(C_LONG, 0L) // iteration index set to 0
        val op_p = H5L_iterate2_t.allocate(H5Lreceiver(nestedContext), context.arena)

        // pass group name as op_data to H5Lreceiver
        checkErr("H5Literate2", H5Literate2(group_id, H5_INDEX_NAME(), H5_ITER_INC(), idx_p, op_p, groupName))

        // herr_t H5Gget_info	(	hid_t	loc_id, H5G_info_t *	ginfo )
        val ginfo_p = H5G_info_t.allocate(context.arena)
        checkErr("H5Gget_info", H5Gget_info(group_id, ginfo_p))

        // typedef struct H5G_info_t {
        //    H5G_storage_type_t storage_type;
        //    hsize_t            nlinks;
        //    int64_t            max_corder;
        //    hbool_t            mounted;
        //} H5G_info_t;

        //  herr_t H5Gget_objinfo(	hid_t	loc_id, const char *	name, hbool_t	follow_link, H5G_stat_t *	statbuf )
        // This function is deprecated in favor of the functions H5Oget_info() and H5Lget_info1().
        // H5Oget_info() if Java interface with difficult memory management
        // H5Lget_info1() superceded by H5Lget_info2()

        val oinfo_p = H5O_info2_t.allocate(context.arena)
        checkErr("H5Oget_info", H5Oget_info3(group_id, oinfo_p, H5O_INFO_ALL()))

        val num_attr = H5O_info2_t.`num_attrs$get`(oinfo_p)
        val atts = readAttributes(group_id, g5name, num_attr.toInt(), context)

        atts.forEach { attr ->
            val promoted = !strict && attr.isString && attr.values.size == 1 && (attr.values[0] as String).length > attLengthMax
            if (promoted) { // too big for an attribute
                val vb = Variable.Builder(attr.name, Datatype.STRING)
                vb.spObject = attr
                context.group.addVariable( vb)
            } else {
                context.group.addAttribute(attr)
            }
        }
        return true
    }

    // also see https://support.hdfgroup.org/documentation/hdf5/latest/group___h5_l.html
    private inner class H5Lreceiver(val context : GroupContext) : H5L_iterate2_t {

        // herr_t op_func(hid_t loc_id, const char *name, const H5L_info_t *info, void *operator_data)
        // typedef herr_t (* H5L_iterate2_t) (hid_t group, const char *name, const H5L_info2_t *info, void *op_data)
        // int apply(long location_id, MemorySegment attr_name, MemorySegment ainfo, MemorySegment op_data);

        override fun apply(group_id: Long, linkname_p: MemorySegment, linkinfo_p: MemorySegment, groupName: MemorySegment): Int {
            // TODO linkname_p must be a pointer to a string.
            val linkname = linkname_p.getUtf8String(0)
            if (debug) println("${context.indent}H5Lreceiver link='$linkname'")
            val indent = context.indent.incr() // ??

            // typedef struct {
            //    H5L_type_t type;
            //    hbool_t    corder_valid;
            //    int64_t    corder;
            //    H5T_cset_t cset;
            //    union {
            //        H5O_token_t token;
            //        size_t      val_size;
            //    } u;
            //} H5L_info2_t;
            //
            // TODO linkinfo_p must be a pointer to an H5 malloced structure of type H5L_info2_t.
            //    Note that its not used in example at https://github.com/HDFGroup/hdf5/blob/develop/HDF5Examples/C/H5G/h5ex_g_iterate.c
            //    Which inexplicably changes to H5O API !!
            // copy into local memory
            val linkinfo = H5L_info2_t.ofAddress(linkinfo_p, context.arena)

            // typedef enum {
            //    H5L_TYPE_ERROR    = (-1), /**< Invalid link type id         */
            //    H5L_TYPE_HARD     = 0,    /**< Hard link id                 */
            //    H5L_TYPE_SOFT     = 1,    /**< Soft link id                 */
            //    H5L_TYPE_EXTERNAL = 64,   /**< External link id             */
            //    H5L_TYPE_MAX      = 255   /**< Maximum link type id         */
            //} H5L_type_t;
            val ltype = H5L_info2_t.`type$get`(linkinfo, 0L) // H5L_type_t
            if (ltype == H5L_TYPE_HARD()) {
                val address = H5L_info2_t.u.ofAddress(linkinfo, context.arena)
                if (debug) println("${indent}H5L_TYPE_HARD, address=$address")
            } else if (ltype == H5L_TYPE_SOFT()) {
                val val_size = H5L_info2_t.u.`val_size$get`(linkinfo, 0L)
                if (debug) println("${indent}H5L_TYPE_SOFT, val_size=$val_size")
            }

            // cset specifies the character set in which the link name is encoded. Valid values include the following:
            // typedef enum H5T_cset_t {
            //    H5T_CSET_ERROR       = -1, /**< error                           */
            //    H5T_CSET_ASCII       = 0,  /**< US ASCII                        */
            //    H5T_CSET_UTF8        = 1,  /**< UTF-8 Unicode encoding		     */
            //} H5T_cset_t;
            val cset = H5L_info2_t.`cset$get`(linkinfo, 0L) // H5T_cset_t

            // token is the location that a hard link points to
            // H5O_token_t is used in the VOL layer. It is defined in H5public.h as:
            //
            // typedef struct H5O_token_t {
            //    uint8_t __data[H5O_MAX_TOKEN_SIZE];
            // } H5O_token_t;
            //
            // val_size is the size of a soft link or user-defined link value.
            // If the link is a symbolic link, val_size will be the length of the link value, e.g., the length of the HDF5 path name with a null terminator.

            // typedef enum {
            //    H5L_TYPE_ERROR    = (-1), /**< Invalid link type id         */
            //    H5L_TYPE_HARD     = 0,    /**< Hard link id                 */
            //    H5L_TYPE_SOFT     = 1,    /**< Soft link id                 */
            //    H5L_TYPE_EXTERNAL = 64,   /**< External link id: Link ids at or above this value are "user-defined" link types
            //    H5L_TYPE_MAX      = 255   /**< Maximum link type id         */
            //}

            //// We dont have much choice except to use the H5O API

            val loc_id = H5Oopen(group_id, linkname_p, H5P_DEFAULT_LONG)

            val oinfo_p = H5O_info2_t.allocate(context.arena)
            checkErr("H5Oget_info3", H5Oget_info3(loc_id, oinfo_p, H5O_INFO_ALL()))
            val otype = H5O_info2_t.`type$get`(oinfo_p)
            val num_attr = H5O_info2_t.`num_attrs$get`(oinfo_p)

            if (otype == H5O_TYPE_GROUP()) {
                if (debugGroup) println("  Group name $linkname loc_id=$loc_id in group ${context.group.fullname()} group_id=$group_id")
                // TODO do we need token?
                val nestedGroup = Group.Builder(linkname)
                val nestedContext = context.copy(group = nestedGroup)
                val ok = readGroup(linkname, nestedContext)
                if (ok) context.group.addGroup(nestedGroup) // dont add until its successfully read
                return 0

            } else if (otype == H5O_TYPE_DATASET()) {
                if (debugGroup) println("  Dataset name $linkname loc_id=$loc_id in group ${context.group.fullname()} group_id=$group_id " +
                        " ltype=${if (ltype == H5L_TYPE_HARD()) "HARD" else "SOFT"}")

                if (ltype == H5L_TYPE_HARD() || useSoftLinks) {
                    // TODO do we need val_size ?
                    // the soft links are symbolic links that point to existing datasets
                    // also see H5Gget_linkval()
                    //  herr_t H5Gget_linkval	(	hid_t	loc_id,
                    //  const char *	name,
                    //  size_t	size,
                    //  char *	buf )
                    readDataset(linkname, num_attr.toInt(), context)
                } else {
                    println("  *** only supporting hard, not soft or external links ")
                }
            } else if (otype == H5O_TYPE_NAMED_DATATYPE()) {
                if (debugGroup) println("  Datatype name $linkname loc_id=$loc_id in group ${context.group.fullname()} group_id=$group_id")
                val type_id = H5Topen2(context.group5id,  linkname_p, H5P_DEFAULT_LONG)
                readH5CTypeInfo(context, type_id, linkname, true)
            } else {
                if (debugGroup) println("  NONE otype == $otype name $linkname loc_id=$loc_id in group ${context.group.fullname()} group_id=$group_id")
            }

            return 0
        }
    }

    ////////////////////////////////////////////////////////////////////////////////

    fun convertReferences(gb : Group.Builder) {
        val refAtts = gb.attributes.filter{ it.datatype == Datatype.REFERENCE}
        refAtts.forEach { att ->
            val convertAtt = convertReferenceAttribute(att)
            if (convertAtt != null) {
                gb.addAttribute(convertAtt)
            }
            gb.attributes.remove(att)
        }

        gb.variables.forEach{ vb ->
            val refVAtts = vb.attributes.filter{ it.datatype == Datatype.REFERENCE}
            refVAtts.forEach { att ->
                val convertAtt = convertReferenceAttribute(att)
                if (convertAtt != null) {
                    if (att.name == HDF5_DIMENSION_LIST) {
                        vb.dimNames = convertAtt.values as List<String>
                    } else {
                        vb.addAttribute(convertAtt)
                    }
                }
                vb.attributes.remove(att)
            }
        }

        gb.groups.forEach{ convertReferences(it) }
    }

    fun convertReferenceAttribute(att : Attribute<*>) : Attribute<*>? {
        val svalues = mutableListOf<String>()
        att.values.forEach {
            val dsetId = it as Long
            val pair = datasetMap[dsetId]
            if (pair == null)  {
                println("H5C cant find dataset reference for $att")
                return null
            }
            val (gb, vb) = pair
            val name = vb.fullname(gb)
            svalues.add(name)
        }
        return Attribute(att.name, Datatype.STRING, svalues)
    }

    fun convertReferencesToDataObjectName(refArray: Iterable<Long>): List<String> {
        return refArray.map { convertReferenceToDataObjectName(it) }
    }

    fun convertReferenceToDataObjectName(reference: Long): String {
        val pair = datasetMap[reference]
        if (pair == null)  {
            println("H5C cant find dataset reference for $reference")
            return "N/A"
        }
        val (gb, vb) = pair
        return vb.fullname(gb)
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    internal fun registerTypedef(typeInfo : H5CTypeInfo, gb : Group.Builder) : H5CTypeInfo {
        typeinfoList.add(typeInfo)
        val groups = typeinfoMap.getOrPut(typeInfo.typedef!!) { mutableListOf() }
        groups.add(gb)
        return typeInfo
    }
    internal fun findTypeFromId(typeId : Long) : H5CTypeInfo? {
        return typeinfoList.find { H5Tequal(it.type_id, typeId) > 0 }
    }
    internal fun addTypesToGroups() {
        typeinfoMap.forEach { typedef, groupList ->
            if (groupList.size == 1) {
                groupList[0].addTypedef(typedef)
            } else {
                var topgroup = groupList[0]
                for (idx in 1 until groupList.size) {
                    topgroup = topgroup.commonParent(groupList[idx])
                }
                topgroup.addTypedef(typedef)
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    internal fun registerTypedef2(typeInfo : H5CTypeInfo, gb : Group.Builder) : H5CTypeInfo {
        val groups = typeinfoMap2.getOrPut(typeInfo) { mutableListOf() }
        groups.add(gb)
        return typeInfo
    }
    internal fun findTypeFromId2(typeId : Long) : H5CTypeInfo? {
        return typeinfoMap2.keys.find { H5Tequal(it.type_id, typeId) > 0 }
    }
    internal fun addTypesToGroups2() {
        typeinfoMap2.forEach { typeInfo, groupList ->
            if (groupList.size == 1) {
                groupList[0].addTypedef(typeInfo.typedef!!)
            } else {
                var topgroup = groupList[0]
                for (idx in 1 until groupList.size) {
                    topgroup = topgroup.commonParent(groupList[idx])
                }
                topgroup.addTypedef(typeInfo.typedef!!)
            }
        }
    }

    enum class H5O_TYPE { UNKNOWN, GROUP, DATASET, NAMED_DATATYPE;
        companion object {
            fun of(num: Int) : H5O_TYPE {
                return when (num) {
                    -1 -> UNKNOWN
                    0 -> GROUP
                    1 -> DATASET
                    2 -> NAMED_DATATYPE
                    else -> throw RuntimeException("Unknown H5O_TYPE $num")
                }
            }
        }
    }

    enum class H5L_TYPE { ERROR, HARD, SOFT, EXTERNAL;
        companion object {
            fun of(num: Int) : H5L_TYPE {
                return when (num) {
                    -1 -> ERROR
                    0 -> HARD
                    1 -> SOFT
                    64 -> EXTERNAL
                    else -> throw RuntimeException("Unknown H5O_TYPE $num")
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun readDataset(obj_name: String, numAtts : Int, context: GroupContext) {
        if (debug) println("${context.indent}readDataset for '$obj_name'")
        val indent = context.indent.incr()

        // hid_t H5Dopen2(hid_t loc_id, const char * name, hid_t dapl_id)
        val obj_name_p: MemorySegment = context.arena.allocateUtf8String(obj_name)
        val datasetId = H5Dopen2(context.group5id,  obj_name_p, H5P_DEFAULT_LONG)

        // hid_t H5Dget_space	(	hid_t 	attr_id	)
        val dataspace_id = H5Dget_space(datasetId)
        // hssize_t H5Sget_select_npoints	(	hid_t 	spaceid	)
        val dataspace_npoints = H5Sget_select_npoints(dataspace_id)
        // int H5Sget_simple_extent_dims	(	hid_t 	space_id, hsize_t 	dims[], hsize_t 	maxdims[] )
        //     public static int H5Sget_simple_extent_dims ( long space_id,  Addressable dims,  Addressable maxdims) {
        val dims_p = context.arena.allocateArray(C_LONG as MemoryLayout, MAX_DIMS)
        val maxdims_p = context.arena.allocateArray(C_LONG  as MemoryLayout, MAX_DIMS)
        val ndims = H5Sget_simple_extent_dims(dataspace_id, dims_p, maxdims_p)
        val dims = LongArray(ndims) { dims_p.getAtIndex(C_LONG, it.toLong()) }

        val type_id = H5Dget_type(datasetId)
        val h5ctype = readH5CTypeInfo(context, type_id, obj_name, false)

        try {
            // create the Variable
            val vb = Variable.Builder(obj_name, h5ctype.datatype())

            var isDimensionScale = false
            var isVariable = true
            val atts = readAttributes(datasetId, obj_name, numAtts, context)
            atts.forEach { att ->
                if (att.name == HDF5_CLASS && (att.values[0] as String) == HDF5_DIMENSION_SCALE) {
                    isDimensionScale = true
                } else if (att.name == HDF5_DIMENSION_NAME && (att.values[0] as String).startsWith(Netcdf4.NETCDF4_NOT_VARIABLE)) {
                    isVariable = false
                }
            }
            val fatts = if (!isDimensionScale) {
                vb.setDimensionsAnonymous(dims)
                atts
            } else {
                val dim = Dimension(obj_name, dims[0], true)
                context.group.addDimension(dim)
                vb.addDimension(dim)
                if (hideInternalAttributes) atts.filter { !HDF5_IGNORE_ATTS.contains(it.name) } else atts
            }

            vb.attributes.addAll(fatts)
            vb.spObject = Vinfo5C(datasetId, h5ctype)
            if (isVariable) context.group.addVariable(vb)

            // datasetId is transient
            var address = H5Dget_offset(datasetId)
            /* println("**H5Cbuilder obj_name=$obj_name datasetId=$datasetId address=$address") // maybe there a byte order problem ??
        if (address < 0) {
            val reversed = datasetId.reverseByteOrder() // doesnt work
            address = H5Dget_offset(reversed)
            println("   try again with byte order reversed: datasetId=$reversed address=$address") // maybe there a byte order problem ??
        } */
            if (address > 0) datasetMap[address] = Pair(context.group, vb)

            if (obj_name.startsWith("StructMetadata")) {
                val data = readRegularData(context.arena, datasetId, h5ctype, h5ctype.datatype(), Section(dims))
                require(data is ArrayString)
                structMetadata.add(data.values.get(0))
            }
            if (debugGraph && vb.datatype == Datatype.COMPOUND) println("${context.indent}${vb.name} ${vb.datatype}")

            if (debug) println("${indent}'$obj_name' h5ctype=$h5ctype npoints=$dataspace_npoints dims=${dims.contentToString()}")
        } catch (e : Exception) {
            e.printStackTrace()
            println("skipping variable $obj_name")
        }
    }

    @Throws(IOException::class)
    private fun readAttributes(dataset_id : Long, obj_name: String, numAtts : Int, context: GroupContext): List<Attribute<*>> {
        if (debug) println("${context.indent}readAttributes for '$obj_name'")
        val results = mutableListOf<Attribute<*>>()

        repeat(numAtts) { idx ->
            // hid_t H5Aopen_by_idx	(	hid_t 	loc_id, const char * 	obj_name, H5_index_t 	idx_type, H5_iter_order_t 	order, hsize_t 	n, hid_t 	aapl_id, hid_t 	lapl_id)
            // long H5Aopen_by_idx ( long loc_id,  Addressable obj_name,  int idx_type,  int order,  long n,  long aapl_id,  long lapl_id) {
            val obj_name_p: MemorySegment = context.arena.allocateUtf8String(obj_name)
            val attr_id = H5Aopen_by_idx(
                context.group5id,
                obj_name_p,
                H5_INDEX_NAME(),
                H5_ITER_INC(),
                idx.toLong(),
                H5P_DEFAULT_LONG,
                H5P_DEFAULT_LONG
            )

            // herr_t H5Aget_info	(	hid_t 	attr_id, H5A_info_t * 	ainfo)

            // ssize_t H5Aget_name	(	hid_t 	attr_id, size_t 	buf_size, char * 	buf)
            // long H5Aget_name ( long attr_id,  long buf_size,  Addressable buf)
            val name_p = context.arena.allocate(MAX_NAME)
            val name_len = H5Aget_name(attr_id, MAX_NAME, name_p)
            val aname: String = name_p.getUtf8String(0)
            if (hideInternalAttributes && HDF5_SKIP_ATTS.contains(aname))
                return@repeat

            // hid_t H5Aget_space	(	hid_t 	attr_id	)
            val dataspace_id = H5Aget_space(attr_id)
            // hssize_t H5Sget_select_npoints	(	hid_t 	spaceid	)
            val nelems = H5Sget_select_npoints(dataspace_id)
            // int H5Sget_simple_extent_dims	(	hid_t 	space_id, hsize_t 	dims[], hsize_t 	maxdims[] )
            //     public static int H5Sget_simple_extent_dims ( long space_id,  Addressable dims,  Addressable maxdims) {
            val dims_p = context.arena.allocateArray(C_LONG as MemoryLayout, MAX_DIMS)
            val maxdims_p = context.arena.allocateArray(C_LONG as MemoryLayout, MAX_DIMS)
            val ndims = H5Sget_simple_extent_dims(dataspace_id, dims_p, maxdims_p)
            val dims = IntArray(ndims) { dims_p.getAtIndex(C_LONG, it.toLong()).toInt() }
            // hsize_t H5Aget_storage_size	(	hid_t 	attr_id	)
            val size = H5Aget_storage_size(attr_id)

            // hid_t H5Dget_type	(	hid_t 	attr_id	)
            val type_id = H5Aget_type(attr_id)
            val h5ctype = readH5CTypeInfo(context, type_id, aname, false)

            //if (aname == "PALETTE")
            //    println("heh")

            // read data
            if (h5ctype.isVlenString) {
                val slist = readVlenStrings(context.arena, attr_id, h5ctype.type_id, nelems)
                val att = Attribute(aname, Datatype.STRING, slist)
                results.add(att)

            } else if (h5ctype.datatype5 == Datatype5.Vlen) {
                val vlen_p: MemorySegment = hvl_t.allocateArray(nelems, context.arena)
                checkErr("H5Aread Vlen", H5Aread(attr_id, h5ctype.type_id, vlen_p))
                val base = h5ctype.base!!
                val att = if (base.datatype5 == Datatype5.Reference) {
                    val values = readVlenReferences(attr_id, nelems, vlen_p)
                    Attribute(aname, Datatype.REFERENCE, values)
                } else {
                    val values = readVlenData(nelems, base.datatype(), vlen_p)
                    Attribute.Builder(aname, h5ctype.datatype()).setValues(values).build()
                }
                results.add(att)

            } else if (h5ctype.datatype5 == Datatype5.Reference) {
                val refdata_p = context.arena.allocate(nelems.toInt() * size)
                checkErr("H5Aread Reference", H5Aread(attr_id, h5ctype.type_id, refdata_p))
                //     public static long H5Rdereference1 ( long obj_id,  int ref_type,  Addressable ref) {
                // LOOK nelems??

                // long H5Rdereference2 ( long obj_id,  long oapl_id,  int ref_type,  Addressable ref)
                // Returns identifier of referenced object
                val refobjId = H5Rdereference2(attr_id, H5P_DEFAULT_LONG, H5R_OBJECT(), refdata_p)
                val address = H5Dget_offset(refobjId)
                val att = Attribute(aname, Datatype.REFERENCE, listOf(address))
                results.add(att)

            } else {
                val data_p = context.arena.allocate(size)
                // herr_t H5Aread(hid_t attr_id, hid_t 	type_id, void * 	buf)
                checkErr("H5Aread", H5Aread(attr_id, h5ctype.type_id, data_p))
                val raw = data_p.toArray(ValueLayout.JAVA_BYTE)
                require(size == nelems * h5ctype.elemSize)

                val datatype = h5ctype.datatype()
                val att = if (nelems == 0L) {
                    Attribute.Builder(aname, datatype).build()
                } else if (datatype == Datatype.COMPOUND) {
                    val members = (datatype.typedef as CompoundTypedef).members
                    // class ArrayStructureData(shape : IntArray, val ba : ByteArray, val isBE: Boolean, val recsize : Int, val members : List<StructureMember<*>>)
                    val sdataArray =  ArrayStructureData(dims, raw, h5ctype.isBE, h5ctype.elemSize, members)
                    processCompoundData(context.arena, sdataArray, raw, h5ctype.isBE)
                    Attribute.Builder(aname, datatype).setValues(sdataArray.toList()).build()

                } else {
                    val values = processDataIntoArray(raw, h5ctype.isBE, h5ctype.datatype5, h5ctype.datatype(), dims, h5ctype.elemSize)
                    Attribute.Builder(aname, values.datatype).setValues(values.toList()).build()
                }
                results.add(att)
            }
        }

        return results
    }

    // TODO almost duplicate of H5ClibFile
    //        internal fun readVlenStrings(session : Arena, datasetId : Long, h5ctype : H5CTypeInfo, want : Section) : ArrayString {
    internal fun readVlenStrings(session : Arena, attrId : Long, typeId : Long, nelems : Long) : List<String> {
        val strings_p: MemorySegment = session.allocateArray(ValueLayout.ADDRESS, nelems)
        checkErr("H5Aread VlenString", H5Aread(attrId, typeId, strings_p))

        val slist = mutableListOf<String>()
        for (i in 0 until nelems) {
            val address: MemorySegment = strings_p.getAtIndex(ValueLayout.ADDRESS, i)
            if (address != MemorySegment.NULL) {
                val cString = address.reinterpret(Long.MAX_VALUE)
                val value = cString.getUtf8String(0)
                // val tvalue = transcodeString(value)
                slist.add(value)
            } else {
                slist.add("")
            }
        }
        // not sure about this
        // checkErr("H5Dvlen_reclaim", H5Dvlen_reclaim(attr_id, h5ctype.type_id, H5S_ALL(), strings_p)) // ??
        return slist
    }

    // LOOK same as in nclib UserTypes
    internal fun readVlenData(nelems : Long, basetype : Datatype<*>, vlen_p : MemorySegment) : List<*> {
        val attValues = mutableListOf<List<*>>()
        for (elem in 0 until nelems) {
            val count = hvl_t.`len$get`(vlen_p, elem)
            val address: MemorySegment = hvl_t.`p$get`(vlen_p, elem)
            val vlenValues = mutableListOf<Any>()
            for (idx in 0 until count) {
                val value = when (basetype) {
                    Datatype.BYTE-> address.get(ValueLayout.JAVA_BYTE, idx)
                    Datatype.SHORT -> address.getAtIndex(C_SHORT, idx)
                    Datatype.INT -> address.getAtIndex(C_INT, idx)
                    Datatype.LONG -> address.getAtIndex(C_LONG, idx)
                    Datatype.DOUBLE -> address.getAtIndex(C_DOUBLE,  idx)
                    Datatype.FLOAT -> address.getAtIndex(C_FLOAT, idx)
                    Datatype.STRING -> address.getUtf8String(0)
                    else -> throw RuntimeException("readVlenDataList unknown type = ${basetype}")
                }
                vlenValues.add(value)
            }
            attValues.add(vlenValues)
        }
        return attValues
    }

    internal fun readVlenReferences(obj_id : Long, nelems : Long, vlen_p : MemorySegment) : List<Long> {
        val parray = mutableListOf<Long>()
        for (elem in 0 until nelems) {
            val count = hvl_t.`len$get`(vlen_p, elem)
            val address: MemorySegment = hvl_t.`p$get`(vlen_p, elem)
            // hid_t H5Rdereference1(hid_t obj_id, H5R_type_t ref_type, const void *ref)
            // H5Rdereference1 ( long obj_id,  int ref_type,  Addressable ref)
            val refobjId = H5Rdereference1(obj_id, H5R_OBJECT(), address)
            val refaddress = H5Dget_offset(refobjId)
            parray.add(refaddress)
        }
        return parray
    }

    companion object {
        val debug = false
        val debugGraph = false
        val useSoftLinks = false
        val hideInternalAttributes = true

        private var nextAnon = 1
        fun getNextAnonTypeName(): String {
            val next = "_AnonymousType${nextAnon}"
            nextAnon++
            return next
        }
    }
}

fun checkErr (where : String, ret: Int) {
    if (ret != 0) {
        throw IOException("$where return $ret")
    }
}

internal fun <T> readRegularData(session : Arena, datasetId : Long, h5ctype : H5CTypeInfo, datatype : Datatype<T>, want : Section) : ArrayTyped<T> {
    // int H5Dread ( long dset_id,  long mem_type_id,  long mem_space_id,  long file_space_id,  long plist_id,  Addressable buf) {
    // herr_t H5Dread(hid_t dset_id, hid_t 	mem_type_id, hid_t 	mem_space_id, hid_t file_space_id, hid_t dxpl_id, void *buf)
    //[in]	dset_id	Dataset identifier Identifier of the dataset to read from
    //[in]	mem_type_id	Identifier of the memory datatype
    //[in]	mem_space_id	Identifier of the memory dataspace
    //[in]	file_space_id	Identifier of the dataset's dataspace in the file
    //[in]	dxpl_id	Identifier of a transfer property list
    //[out]	buf	Buffer to receive data read from file

    // file_space_id is used to specify only the selection within the file dataset's dataspace.
    // Any dataspace specified in file_space_id is ignored by the library and the dataset's dataspace is always used. WTF?
    // file_space_id can be the constant H5S_ALL, which indicates that the entire file dataspace, as defined by the
    // current dimensions of the dataset, is to be selected.
    //
    // mem_space_id is used to specify both the memory dataspace and the selection within that dataspace.
    // mem_space_id can be the constant H5S_ALL, in which case the file dataspace is used for the memory dataspace
    // and the selection defined with file_space_id is used for the selection within that dataspace.
    // checkErr("H5Dread", H5Dread(datasetId, h5ctype.type_id, H5S_ALL(), spaceId, H5P_DEFAULT_LONG, data_p))

    val datatype = h5ctype.datatype()
    val size = want.totalElements * h5ctype.elemSize.toLong()
    val data_p = session.allocate(size)

    val (memSpaceId, fileSpaceId) = makeSection(session, datasetId, h5ctype, want)
    checkErr("H5Dread", H5Dread(datasetId, h5ctype.type_id, memSpaceId, fileSpaceId, H5P_DEFAULT_LONG, data_p))

    val raw = data_p.toArray(ValueLayout.JAVA_BYTE)!!

    val dims = want.shape.toIntArray()
    if (datatype == Datatype.COMPOUND) {
        val members = (datatype.typedef as CompoundTypedef).members
        val sdataArray = ArrayStructureData(dims, raw, h5ctype.isBE, h5ctype.elemSize, members)
        return processCompoundData(session, sdataArray, raw, h5ctype.isBE) as ArrayTyped<T>
    }

    return processDataIntoArray(raw, h5ctype.isBE, h5ctype.datatype5, datatype, dims, h5ctype.elemSize) as ArrayTyped<T>
}

internal fun makeSection(session : Arena, datasetId : Long, h5ctype : H5CTypeInfo, want : Section) : Pair<Long, Long> {
    val datatype = h5ctype.datatype()
    val size = want.totalElements * h5ctype.elemSize.toLong()
    if (debug) println("readRegularData want=$want nelems=${want.totalElements} $datatype size=$size")
    if (want.rank == 0) { // scalar
        return Pair(H5S_ALL().toLong(), H5S_ALL().toLong())
    }

    val rank = want.rank.toLong()
    val origin_p = session.allocateArray(C_LONG as MemoryLayout, rank)
    val shape_p = session.allocateArray(C_LONG as MemoryLayout, rank)
    val stride_p = session.allocateArray(C_LONG as MemoryLayout, rank)
    val block_p = session.allocateArray(C_LONG as MemoryLayout, rank)
    for (idx in 0 until want.rank) {
        val range = want.ranges[idx]
        origin_p.setAtIndex(C_LONG, idx.toLong(), range.first)
        shape_p.setAtIndex(C_LONG, idx.toLong(), want.shape[idx])
        stride_p.setAtIndex(C_LONG, idx.toLong(), range.step)
        block_p.setAtIndex(C_LONG, idx.toLong(), 1L)
    }

    val fileSpaceId : Long = H5Dget_space(datasetId)
    //     public static int H5Sselect_hyperslab ( long space_id,  int op,  Addressable start,  Addressable _stride,  Addressable count,  Addressable _block) {
    checkErr("H5Sselect_hyperslab", H5Sselect_hyperslab(fileSpaceId, H5S_SELECT_SET(), origin_p, stride_p, shape_p, block_p))

    val start_p = session.allocateArray(C_LONG as MemoryLayout, rank)
    val end_p = session.allocateArray(C_LONG as MemoryLayout, rank)
    H5Sget_select_bounds(fileSpaceId, start_p, end_p)
    val start = LongArray(rank.toInt()) { start_p.getAtIndex(C_LONG, it.toLong()) }
    val end = LongArray(rank.toInt()) { end_p.getAtIndex(C_LONG, it.toLong()) }
    if (debug) println("  selection ${start.contentToString()} ${end.contentToString()}")

    val dims_p = session.allocateArray(C_LONG as MemoryLayout, rank)
    for (idx in 0 until want.rank) {
        dims_p.setAtIndex(C_LONG, idx.toLong(), want.shape[idx])
    }
    // hid_t H5Screate_simple	(	int 	rank, const hsize_t 	dims[], const hsize_t 	maxdims[]
    // long H5Screate_simple ( int rank,  Addressable dims,  Addressable maxdims)
    val memSpaceId : Long = H5Screate_simple(rank.toInt(), dims_p, dims_p)
    return Pair(memSpaceId, fileSpaceId)
}

internal fun <T> processDataIntoArray(ba: ByteArray, isBE: Boolean, datatype5 : Datatype5, datatype: Datatype<T>, shape : IntArray, elemSize : Int): ArrayTyped<T> {

    // convert to array of Strings by reducing rank by 1, tricky shape shifting for non-scalars
    if (datatype5 == Datatype5.String) {
        val extshape = if (elemSize == 1) shape else IntArray(shape.size + 1) { if (it == shape.size) elemSize else shape[it] }
        val result = ArrayUByte.fromByteArray(extshape, ba)
        return result.makeStringsFromBytes() as ArrayTyped<T>
    }

    if (datatype == Datatype.OPAQUE) {
        return ArrayOpaque.fromByteArray(shape, ba, elemSize) as ArrayTyped<T>
    }

    val tba = TypedByteArray(datatype, ba, 0, isBE = isBE)
    return tba.convertToArrayTyped(shape, charToString = true)
}

// Put the variable length members (vlen, string) on the heap
internal fun processCompoundData(session : Arena, sdataArray : ArrayStructureData, ba : ByteArray, isBE : Boolean) : ArrayStructureData {
    sdataArray.putVlenStringsOnHeap { member, moffset ->
        val values = mutableListOf<String>()
        repeat(member.nelems) { idx ->
            // MemorySegment get(ValueLayout.OfAddress layout, long offset) {
            val longAddress = convertToLong(ba, moffset + idx * 8, isBE)
            val address = MemorySegment.ofAddress(longAddress)
            val cString = address.reinterpret(Long.MAX_VALUE)
            val sval = cString.getUtf8String(0)
            values.add(sval)
        }
        values
    }

    sdataArray.putVlensOnHeap { member, moffset ->
        val listOfVlen = mutableListOf<Array<*>>()
        repeat(member.nelems) { idx ->
            val arraySize = convertToLong(ba, moffset + idx * 8, isBE).toInt()
            val longAddress = convertToLong(ba, moffset + idx * 8 + 8, isBE)
            val address = MemorySegment.ofAddress(longAddress)
            listOfVlen.add( readVlenArray(arraySize, address, member.datatype.typedef!!.baseType))
        }
        ArrayVlen.fromArray(member.shape, listOfVlen, member.datatype.typedef!!.baseType)
    }

    return sdataArray
}

// TODO ENUMS seem to be wrong
private fun readVlenArray(arraySize : Int, address : MemorySegment, datatype : Datatype<*>) : Array<*> {
    return when (datatype) {
        Datatype.FLOAT -> Array(arraySize) { idx -> address.getAtIndex(ValueLayout.JAVA_FLOAT, idx.toLong()) }
        Datatype.DOUBLE -> Array(arraySize) { idx -> address.getAtIndex(ValueLayout.JAVA_DOUBLE, idx.toLong()) }
        Datatype.BYTE, Datatype.UBYTE, Datatype.ENUM1 -> Array(arraySize) { idx -> address.get(ValueLayout.JAVA_BYTE, idx.toLong()) }
        Datatype.SHORT, Datatype.USHORT, Datatype.ENUM2 -> Array(arraySize) { idx -> address.getAtIndex(ValueLayout.JAVA_SHORT, idx.toLong()) }
        Datatype.INT,  Datatype.UINT, Datatype.ENUM4 -> Array(arraySize) { idx -> address.getAtIndex(ValueLayout.JAVA_INT, idx.toLong()) }
        Datatype.LONG, Datatype.ULONG -> Array(arraySize) { idx -> address.getAtIndex(ValueLayout.JAVA_LONG, idx.toLong()) }
        else -> throw IllegalArgumentException("unsupported datatype ${datatype}")
    }
}

data class GroupContext(val arena : Arena, val group: Group.Builder, val group5id: Long, val indent : Indent)
