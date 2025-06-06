package com.sunya.netchdf.netcdfClib

import com.sunya.cdm.api.*
import com.sunya.cdm.array.*
import com.sunya.cdm.layout.MaxChunker
import com.sunya.netchdf.hdf5Clib.ffm.hdf5_h
import com.sunya.netchdf.netcdfClib.ffm.nc_vlen_t
import com.sunya.netchdf.netcdfClib.ffm.netcdf_h.*
import java.lang.foreign.*
import java.lang.foreign.ValueLayout.*
import java.nio.*

/*
apt-cache search netcdf
dpkg -L libnetcdf-dev
 /usr/include/netcdf.h
 /usr/lib/x86_64-linux-gnu/libnetcdf.so

apt-cache search libhdf5-dev
dpkg -L libhdf5-dev
 /usr/include/hdf5/serial/hdf5.h
 /usr/lib/x86_64-linux-gnu/hdf5/serial/libhdf5.so

netcdf library version 4.9.2-development of Mar 19 2023 10:42:31


cd /home/stormy/install/jextract-21/bin
./jextract --source \
    --header-class-name netcdf_h \
    --target-package com.sunya.netchdf.netcdfClib.ffm \
    -I /home/stormy/install/netcdf4/include/netcdf.h \
    -l /home/stormy/install/netcdf4/lib/libnetcdf.so \
    --output /home/stormy/dev/github/netcdf/netchdf/clibs/src/main/java \
    /home/stormy/install/netcdf4/include/netcdf.h

./jextract --source \
    --header-class-name hdf5_h \
    --target-package com.sunya.netchdf.hdf5Clib.ffm \
    -I /usr/include/hdf5/serial/hdf5.h \
    -l /usr/lib/x86_64-linux-gnu/hdf5/serial/libhdf5.so \
    --output /home/stormy/dev/github/netcdf/netchdf/clibs/src/main/java \
    /usr/include/hdf5/serial/hdf5.h

 */

class NClibFile(val filename: String) : Netchdf {
    private val header: NCheader = NCheader(filename)
    private val rootGroup: Group = header.rootGroup.build(null)

    override fun rootGroup() = rootGroup
    override fun location() = filename
    override fun cdl() = com.sunya.cdm.api.cdl(this)
    override fun type() = header.formatType

    override fun close() {
        // NOOP
    }

    override fun <T> readArrayData(v2: Variable<T>, section: SectionPartial?): ArrayTyped<T> {
        return readArrayData(v2, SectionPartial.fill(section, v2.shape))
    }

    internal fun <T> readArrayData(v2: Variable<T>, wantSection: Section): ArrayTyped<T> {
        val nelems = wantSection.totalElements
        require(nelems < Int.MAX_VALUE)

        val vinfo = v2.spObject as NCheader.Vinfo
        val datatype = header.convertType(vinfo.typeid)
        val userType = header.userTypes[vinfo.typeid]

        Arena.ofConfined().use { session ->
            val longArray = MemoryLayout.sequenceLayout(v2.rank.toLong(), C_LONG)
            val origin_p = session.allocateArray(longArray, v2.rank.toLong())
            val shape_p = session.allocateArray(longArray, v2.rank.toLong())
            val stride_p = session.allocateArray(longArray, v2.rank.toLong())
            for (idx in 0 until wantSection.rank) {
                val range = wantSection.ranges[idx]
                origin_p.setAtIndex(hdf5_h.C_LONG, idx.toLong(), range.first)
                shape_p.setAtIndex(hdf5_h.C_LONG, idx.toLong(), wantSection.shape[idx])
                stride_p.setAtIndex(hdf5_h.C_LONG, idx.toLong(), range.step)
            }

            val shape = wantSection.shape.toIntArray()
            when (datatype) {
                Datatype.VLEN -> {
                    val basetype = header.convertType(userType!!.baseTypeid)
                    // an array of vlen structs. each vlen has an address and a size
                    val vlen_p = nc_vlen_t.allocateArray(nelems, session)
                    checkErr("vlen nc_get_vars", nc_get_vars(vinfo.g4.grpid, vinfo.varid, origin_p, shape_p, stride_p, vlen_p))

                    // each vlen pointer is the address of the vlen array of length arraySize
                    val listOfVlen = mutableListOf<Array<*>>()
                    for (elem in 0 until nelems) {
                        val arraySize = nc_vlen_t.getLength(vlen_p, elem).toInt()
                        val address = nc_vlen_t.getAddress(vlen_p, elem)
                        listOfVlen.add( readVlenArray(arraySize, address, basetype))
                    }
                    return ArrayVlen.fromArray(shape, listOfVlen, basetype) as ArrayTyped<T>
                    // TODO nc_free_vlen(nc_vlen_t *vl);
                    //      nc_free_string(size_t len, char **data);
                }

                Datatype.COMPOUND -> {
                    requireNotNull(userType)
                    requireNotNull(datatype.typedef)
                    require(datatype.typedef is CompoundTypedef)

                    val nbytes = nelems * userType.size // LOOK relation of userType.size to datatype.size ??
                    val val_p = session.allocate(nbytes)
                    checkErr("compound nc_get_vars", nc_get_vars(vinfo.g4.grpid, vinfo.varid, origin_p, shape_p, stride_p, val_p))
                    val raw = val_p.toArray(ValueLayout.JAVA_BYTE)
                    val bb = ByteBuffer.wrap(raw)
                    bb.order(ByteOrder.LITTLE_ENDIAN)

                    val members = (datatype.typedef as CompoundTypedef).members
                    val sdataArray = ArrayStructureData(shape, bb, userType.size, members)
                    // strings vs array of strings, also duplicate readCompoundAttValues
                    sdataArray.putStringsOnHeap {  member, offset ->
                        val address = val_p.get(ValueLayout.ADDRESS, (offset).toLong())
                        listOf(address.getUtf8String(0)) // LOOK not right
                    }
                    sdataArray.putVlensOnHeap { member, offset ->
                        // look duplicate (maybe)
                        val listOfVlen = mutableListOf<Array<*>>()
                        for (elem in 0 until member.nelems) {
                            val arraySize = val_p.get(ValueLayout.JAVA_LONG, (offset).toLong()).toInt()
                            val address = val_p.get(ValueLayout.ADDRESS, (offset + 8).toLong())
                            listOfVlen.add( readVlenArray(arraySize, address, member.datatype.typedef!!.baseType))
                        }
                        ArrayVlen.fromArray(member.dims, listOfVlen, member.datatype.typedef!!.baseType)
                    }
                    return sdataArray as ArrayTyped<T>
                }

                Datatype.ENUM1, Datatype.ENUM2, Datatype.ENUM4 -> {
                    val nbytes = nelems * datatype.size
                    val val_p = session.allocate(nbytes)
                    // int 	nc_get_var (int ncid, int varid, void *ip)
                    // 	Read an entire variable in one call.
                    // nc_get_vara (int ncid, int varid, const size_t *startp, const size_t *countp, void *ip)
                    checkErr("enum nc_get_vars", nc_get_vars(vinfo.g4.grpid, vinfo.varid, origin_p, shape_p, stride_p, val_p))
                    val raw = val_p.toArray(ValueLayout.JAVA_BYTE)
                    val values = ByteBuffer.wrap(raw)
                    when (datatype) {
                        Datatype.ENUM1 -> return ArrayUByte(shape, datatype as Datatype<UByte>, values) as ArrayTyped<T>
                        Datatype.ENUM2 -> return ArrayUShort(shape, datatype as Datatype<UShort>, values) as ArrayTyped<T>
                        Datatype.ENUM4 -> return ArrayUInt(shape, datatype as Datatype<UInt>, values) as ArrayTyped<T>
                        else -> throw RuntimeException()
                    }
                }

                Datatype.BYTE -> {
                    val val_p = session.allocate(nelems)
                    checkErr("nc_get_vars_schar",
                        nc_get_vars_schar(vinfo.g4.grpid, vinfo.varid, origin_p, shape_p, stride_p, val_p))
                    val raw = val_p.toArray(ValueLayout.JAVA_BYTE)
                    val values = ByteBuffer.wrap(raw)
                    return ArrayByte(shape, values) as ArrayTyped<T>
                }

                Datatype.UBYTE -> {
                    val val_p = session.allocate(nelems)
                    checkErr("nc_get_vars_uchar",
                        nc_get_vars_uchar(vinfo.g4.grpid, vinfo.varid, origin_p, shape_p, stride_p, val_p))
                    val raw = val_p.toArray(ValueLayout.JAVA_BYTE)
                    val values = ByteBuffer.wrap(raw)
                    return ArrayUByte(shape, values) as ArrayTyped<T>
                }

                Datatype.CHAR -> {
                    val val_p = session.allocate(nelems)
                    checkErr("nc_get_vars_text",
                        nc_get_vars_text(vinfo.g4.grpid, vinfo.varid, origin_p, shape_p, stride_p, val_p))
                    val raw = val_p.toArray(ValueLayout.JAVA_BYTE)
                    val values = ByteBuffer.wrap(raw)
                    return ArrayUByte(shape, Datatype.CHAR, values) as ArrayTyped<T>
                }

                Datatype.DOUBLE -> {
                    // can you allocate DoubleBuffer on heap directly?
                    val val_p = session.allocateArray(C_DOUBLE, nelems)
                    checkErr("nc_get_vars_double",
                        nc_get_vars_double(vinfo.g4.grpid, vinfo.varid, origin_p, shape_p, stride_p, val_p))
                    val values = ByteBuffer.allocate(8 * nelems.toInt())
                    val dvalues = values.asDoubleBuffer()
                    for (i in 0 until nelems) {
                        dvalues.put(i.toInt(), val_p.getAtIndex(C_DOUBLE, i))
                    }
                    return ArrayDouble(shape, values) as ArrayTyped<T>
                }

                Datatype.FLOAT -> {
                    val val_p = session.allocateArray(C_FLOAT, nelems)
                    checkErr("nc_get_vars_float",
                        nc_get_vars_float(vinfo.g4.grpid, vinfo.varid, origin_p, shape_p, stride_p, val_p))
                    val values = ByteBuffer.allocate(4 * nelems.toInt())
                    val fvalues = values.asFloatBuffer()
                    for (i in 0 until nelems) {
                        fvalues.put(i.toInt(), val_p.getAtIndex(C_FLOAT, i))
                    }
                    return ArrayFloat(shape, values) as ArrayTyped<T>
                }

                Datatype.INT -> {
                    // nc_get_vars_int(int ncid, int varid, const size_t *startp, const size_t *countp, const ptrdiff_t *stridep, int *ip);
                    val val_p = session.allocateArray(C_INT, nelems)
                    checkErr("nc_get_vars_int",
                        nc_get_vars_int(vinfo.g4.grpid, vinfo.varid, origin_p, shape_p, stride_p, val_p))
                    val values = ByteBuffer.allocate(4 * nelems.toInt())
                    val ivalues = values.asIntBuffer()
                    for (i in 0 until nelems) {
                        ivalues.put(i.toInt(), val_p.getAtIndex(C_INT, i))
                    }
                    return ArrayInt(shape, values) as ArrayTyped<T>
                }

                Datatype.UINT -> {
                    // nc_get_vars_int(int ncid, int varid, const size_t *startp, const size_t *countp, const ptrdiff_t *stridep, int *ip);
                    val val_p = session.allocateArray(C_INT, nelems)
                    checkErr("nc_get_vars_uint",
                        nc_get_vars_uint(vinfo.g4.grpid, vinfo.varid, origin_p, shape_p, stride_p, val_p))
                    val values = ByteBuffer.allocate(4 * nelems.toInt())
                    val ivalues = values.asIntBuffer()
                    for (i in 0 until nelems) {
                        ivalues.put(i.toInt(), val_p.getAtIndex(C_INT, i))
                    }
                    return ArrayUInt(shape, values) as ArrayTyped<T>
                }

                Datatype.LONG -> {
                    // nc_get_vars_int(int ncid, int varid, const size_t *startp, const size_t *countp, const ptrdiff_t *stridep, int *ip);
                    val val_p = session.allocateArray(C_LONG as MemoryLayout, nelems)
                    checkErr("nc_get_vars_long",
                        nc_get_vars_long(vinfo.g4.grpid, vinfo.varid, origin_p, shape_p, stride_p, val_p))
                    val values = ByteBuffer.allocate(8 * nelems.toInt())
                    val lvalues = values.asLongBuffer()
                    for (i in 0 until nelems) {
                        lvalues.put(i.toInt(), val_p.getAtIndex(C_LONG, i))
                    }
                    return ArrayLong(shape, values) as ArrayTyped<T>
                }

                Datatype.ULONG -> {
                    // nc_get_vars_int(int ncid, int varid, const size_t *startp, const size_t *countp, const ptrdiff_t *stridep, int *ip);
                    val val_p = session.allocateArray(C_LONG  as MemoryLayout, nelems)
                    checkErr("nc_get_vars_ulonglong",
                        nc_get_vars_ulonglong(vinfo.g4.grpid, vinfo.varid, origin_p, shape_p, stride_p, val_p))
                    val values = ByteBuffer.allocate(8 * nelems.toInt())
                    val lvalues = values.asLongBuffer()
                    for (i in 0 until nelems) {
                        lvalues.put(i.toInt(), val_p.getAtIndex(C_LONG, i))
                    }
                    return ArrayULong(shape, values) as ArrayTyped<T>
                }

                Datatype.SHORT -> {
                    val val_p = session.allocateArray(C_SHORT, nelems)
                    checkErr("nc_get_vars_short",
                        nc_get_vars_short(vinfo.g4.grpid, vinfo.varid, origin_p, shape_p, stride_p, val_p))
                    val values = ByteBuffer.allocate(2 * nelems.toInt())
                    val svalues = values.asShortBuffer()
                    for (i in 0 until nelems) {
                        svalues.put(i.toInt(), val_p.getAtIndex(C_SHORT, i))
                    }
                    return ArrayShort(shape, values) as ArrayTyped<T>
                }

                Datatype.USHORT -> {
                    val val_p = session.allocateArray(C_SHORT, nelems)
                    checkErr("nc_get_vars_ushort",
                        nc_get_vars_ushort(vinfo.g4.grpid, vinfo.varid, origin_p, shape_p, stride_p, val_p))
                    val values = ByteBuffer.allocate(2 * nelems.toInt())
                    val svalues = values.asShortBuffer()
                    for (i in 0 until nelems) {
                        svalues.put(i.toInt(), val_p.getAtIndex(C_SHORT, i))
                    }
                    return ArrayUShort(shape, values) as ArrayTyped<T>
                }

                Datatype.STRING -> {
                    val val_p = session.allocateArray(ValueLayout.ADDRESS, nelems)
                    checkErr("nc_get_vars_string",
                        nc_get_vars_string(vinfo.g4.grpid, vinfo.varid, origin_p, shape_p, stride_p, val_p))
                    val values = mutableListOf<String>()
                    for (i in 0 until nelems) {
                        values.add(val_p.getAtIndex(ValueLayout.ADDRESS, i).getUtf8String(0))
                    }
                    return ArrayString(shape, values) as ArrayTyped<T>
                }

                Datatype.OPAQUE -> {
                    val val_p = session.allocate(nelems * userType!!.size)
                    checkErr("opaque nc_get_var", nc_get_vars(vinfo.g4.grpid, vinfo.varid, origin_p, shape_p, stride_p, val_p))
                    val raw = val_p.toArray(ValueLayout.JAVA_BYTE)
                    val bb = ByteBuffer.wrap(raw)
                    return ArrayOpaque(shape, bb, userType.size) as ArrayTyped<T>
                }

                else -> throw IllegalArgumentException("unsupported datatype ${datatype}")
            }
        }
    }

    override fun <T> chunkIterator(v2: Variable<T>, section: SectionPartial?, maxElements : Int?): Iterator<ArraySection<T>> {
        val filled = SectionPartial.fill(section, v2.shape)
        return NCmaxIterator(v2, filled, maxElements ?: 100_000)
    }

    private inner class NCmaxIterator<T>(val v2: Variable<T>, wantSection : Section, maxElems: Int) : AbstractIterator<ArraySection<T>>() {
        private val debugChunking = false
        private val maxIterator  = MaxChunker(maxElems,  wantSection)

        override fun computeNext() {
            if (maxIterator.hasNext()) {
                val indexSection = maxIterator.next()
                if (debugChunking) println("  chunk=${indexSection}")

                val section = indexSection.section(v2.shape)
                val array = readArrayData(v2, section)
                setNext(ArraySection(array, section))
            } else {
                done()
            }
        }
    }
}

// TODO ENUMS seem to be wrong
private fun <T> readVlenArray(arraySize : Int, address : MemorySegment, datatype : Datatype<T>) : Array<T> {
    val result = when (datatype) {
        Datatype.FLOAT -> Array(arraySize) { idx -> address.getAtIndex(JAVA_FLOAT, idx.toLong()) }
        Datatype.DOUBLE -> Array(arraySize) { idx -> address.getAtIndex(JAVA_DOUBLE, idx.toLong()) }
        Datatype.BYTE -> Array(arraySize) { idx -> address.get(JAVA_BYTE, idx.toLong()) }
        Datatype.UBYTE, Datatype.ENUM1 -> Array(arraySize) { idx -> address.get(JAVA_BYTE, idx.toLong()).toUByte() }
        Datatype.SHORT -> Array(arraySize) { idx -> address.getAtIndex(JAVA_SHORT, idx.toLong()) }
        Datatype.USHORT, Datatype.ENUM2 -> Array(arraySize) { idx -> address.getAtIndex(JAVA_SHORT, idx.toLong()).toUShort() }
        Datatype.INT -> Array(arraySize) { idx -> address.getAtIndex(JAVA_INT, idx.toLong()) }
        Datatype.UINT, Datatype.ENUM4 -> Array(arraySize) { idx -> address.getAtIndex(JAVA_INT, idx.toLong()).toUInt() }
        Datatype.LONG -> Array(arraySize) { idx -> address.getAtIndex(JAVA_LONG, idx.toLong()) }
        Datatype.ULONG -> Array(arraySize) { idx -> address.getAtIndex(JAVA_LONG, idx.toLong()).toULong() }
        else -> throw IllegalArgumentException("unsupported datatype ${datatype}")
    }
    return result as Array<T>
}