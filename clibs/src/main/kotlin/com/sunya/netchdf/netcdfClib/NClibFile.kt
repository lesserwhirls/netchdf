package com.sunya.netchdf.netcdfClib

import com.fleeksoft.charset.Platform
import com.sunya.cdm.api.*
import com.sunya.cdm.array.*
import com.sunya.cdm.layout.MaxChunker
import com.sunya.netchdf.NetchdfFileFormat
import com.sunya.netchdf.hdf5Clib.ffm.hdf5_h
import com.sunya.netchdf.netcdfClib.ffm.nc_vlen_t
import com.sunya.netchdf.netcdfClib.ffm.netcdf_h.*
import java.lang.foreign.*
import java.lang.foreign.ValueLayout.*
import java.nio.ByteBuffer

/*
1. Run /home/stormy/dev/github/netcdf/netcdf-c/rebuild.sh:

make clean
CPPFLAGS=-I/home/stormy/install/HDF_Group/HDF5/1.14.6/include/ LDFLAGS=-L/home/stormy/install/HDF_Group/HDF5/1.14.6/lib ./configure --prefix=/home/stormy/install/netcdf4
make check install

~:$ ldd /home/stormy/install/netcdf4/lib/libnetcdf.so
	linux-vdso.so.1 (0x00007fffaffe5000)
	linux-vdso.so.1 (0x00007fffaffe5000)
	libhdf5_hl.so.310 => /home/stormy/install/HDF_Group/HDF5/1.14.6/lib/libhdf5_hl.so.310 (0x00007a6633082000)
	libhdf5.so.310 => /home/stormy/install/HDF_Group/HDF5/1.14.6/lib/libhdf5.so.310 (0x00007a6632a00000)
	libm.so.6 => /lib/x86_64-linux-gnu/libm.so.6 (0x00007a6632917000)
	libxml2.so.2 => /lib/x86_64-linux-gnu/libxml2.so.2 (0x00007a6632735000)
	libcurl.so.4 => /lib/x86_64-linux-gnu/libcurl.so.4 (0x00007a6632674000)
	libc.so.6 => /lib/x86_64-linux-gnu/libc.so.6 (0x00007a6632400000)
	/lib64/ld-linux-x86-64.so.2 (0x00007a66330a9000)
	libicuuc.so.74 => /lib/x86_64-linux-gnu/libicuuc.so.74 (0x00007a6632000000)
	libz.so.1 => /lib/x86_64-linux-gnu/libz.so.1 (0x00007a663304d000)
	liblzma.so.5 => /lib/x86_64-linux-gnu/liblzma.so.5 (0x00007a663301b000)
	libnghttp2.so.14 => /lib/x86_64-linux-gnu/libnghttp2.so.14 (0x00007a6632649000)
	libidn2.so.0 => /lib/x86_64-linux-gnu/libidn2.so.0 (0x00007a6632627000)
	librtmp.so.1 => /lib/x86_64-linux-gnu/librtmp.so.1 (0x00007a66323e2000)
	libssh.so.4 => /lib/x86_64-linux-gnu/libssh.so.4 (0x00007a6632371000)
	libpsl.so.5 => /lib/x86_64-linux-gnu/libpsl.so.5 (0x00007a6632613000)
	libssl.so.3 => /lib/x86_64-linux-gnu/libssl.so.3 (0x00007a66322c7000)
	libcrypto.so.3 => /lib/x86_64-linux-gnu/libcrypto.so.3 (0x00007a6631a00000)
	libgssapi_krb5.so.2 => /lib/x86_64-linux-gnu/libgssapi_krb5.so.2 (0x00007a6632273000)
	libldap.so.2 => /lib/x86_64-linux-gnu/libldap.so.2 (0x00007a6632215000)
	liblber.so.2 => /lib/x86_64-linux-gnu/liblber.so.2 (0x00007a6631ff0000)
	libzstd.so.1 => /lib/x86_64-linux-gnu/libzstd.so.1 (0x00007a6631f36000)
	libbrotlidec.so.1 => /lib/x86_64-linux-gnu/libbrotlidec.so.1 (0x00007a6631f28000)
	libicudata.so.74 => /lib/x86_64-linux-gnu/libicudata.so.74 (0x00007a662fc00000)
	libstdc++.so.6 => /lib/x86_64-linux-gnu/libstdc++.so.6 (0x00007a662f800000)
	libgcc_s.so.1 => /lib/x86_64-linux-gnu/libgcc_s.so.1 (0x00007a66319d2000)
	libunistring.so.5 => /lib/x86_64-linux-gnu/libunistring.so.5 (0x00007a662f653000)
	libgnutls.so.30 => /lib/x86_64-linux-gnu/libgnutls.so.30 (0x00007a662f459000)
	libhogweed.so.6 => /lib/x86_64-linux-gnu/libhogweed.so.6 (0x00007a663198a000)
	libnettle.so.8 => /lib/x86_64-linux-gnu/libnettle.so.8 (0x00007a662fbab000)
	libgmp.so.10 => /lib/x86_64-linux-gnu/libgmp.so.10 (0x00007a662fb27000)
	libkrb5.so.3 => /lib/x86_64-linux-gnu/libkrb5.so.3 (0x00007a662f390000)
	libk5crypto.so.3 => /lib/x86_64-linux-gnu/libk5crypto.so.3 (0x00007a662fafb000)
	libcom_err.so.2 => /lib/x86_64-linux-gnu/libcom_err.so.2 (0x00007a663220f000)
	libkrb5support.so.0 => /lib/x86_64-linux-gnu/libkrb5support.so.0 (0x00007a6631f1b000)
	libsasl2.so.2 => /lib/x86_64-linux-gnu/libsasl2.so.2 (0x00007a6631970000)
	libbrotlicommon.so.1 => /lib/x86_64-linux-gnu/libbrotlicommon.so.1 (0x00007a662fad8000)
	libp11-kit.so.0 => /lib/x86_64-linux-gnu/libp11-kit.so.0 (0x00007a662f1ec000)
	libtasn1.so.6 => /lib/x86_64-linux-gnu/libtasn1.so.6 (0x00007a662fac2000)
	libkeyutils.so.1 => /lib/x86_64-linux-gnu/libkeyutils.so.1 (0x00007a6631f14000)
	libresolv.so.2 => /lib/x86_64-linux-gnu/libresolv.so.2 (0x00007a662faaf000)
	libffi.so.8 => /lib/x86_64-linux-gnu/libffi.so.8 (0x00007a6631964000)


added

        System.load("/home/stormy/install/HDF_Group/HDF5/1.14.6/lib/libhdf5_hl.so.310"); // added

in RuntimeHelper


/home/stormy/install/netcdf4/bin/ncdump
netcdf library version 4.10.0-development of May 23 2025 14:45:19

2. make ffm classes

cd /home/stormy/install/jextract-21/bin
./jextract --source \
    --header-class-name netcdf_h \
    --target-package com.sunya.netchdf.netcdfClib.ffm \
    -I /home/stormy/install/netcdf4/include/netcdf.h \
    -l /home/stormy/install/netcdf4/lib/libnetcdf.so \
    --output /home/stormy/dev/github/netcdf/netchdf/clibs/src/main/java \
    /home/stormy/install/netcdf4/include/netcdf.h

You can search for this string in generated ffm file: "4.10.0"
 See if theres build errors
 See if theres test errors.
 */

// TODO im skeptical isBE shouldnt always be nativeByteOrder
class NClibFile(val filename: String) : Netchdf {
    private val header: NCheader = NCheader(filename)
    private val rootGroup: Group = header.rootGroup.build(null)

    override fun rootGroup() = rootGroup
    override fun location() = filename
    override fun cdl() = com.sunya.cdm.api.cdl(this)
    override fun type() = when (header.formatType) {
        NetchdfFileFormat.NC_FORMAT_CLASSIC -> "netcdf3"
        NetchdfFileFormat.NC_FORMAT_64BIT_OFFSET -> "netcdf3.2"
        NetchdfFileFormat.NC_FORMAT_64BIT_DATA -> "netcdf3.5"
        else -> "netcdf4"
    }

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
                        //println("elem=$elem arraySize=$arraySize address=$address")
                        val adata = readVlenArray(arraySize, address, basetype)
                        //println("   data=${adata.contentToString()}")
                        listOfVlen.add( adata)
                    }
                    return ArrayVlen.fromArray(shape, listOfVlen, basetype) as ArrayTyped<T>
                    // TODO nc_free_vlen(nc_vlen_t *vl);
                    //      nc_free_string(size_t len, char **data);
                    //      nc_reclaim_data()
                }

                Datatype.COMPOUND -> {
                    requireNotNull(userType)
                    requireNotNull(datatype.typedef)
                    require(datatype.typedef is CompoundTypedef)

                    val nbytes = nelems * userType.size // LOOK relation of userType.size to datatype.size ??
                    val val_p = session.allocate(nbytes)
                    checkErr("compound nc_get_vars", nc_get_vars(vinfo.g4.grpid, vinfo.varid, origin_p, shape_p, stride_p, val_p))
                    val raw = val_p.toArray(ValueLayout.JAVA_BYTE)!!

                    val isBE = false // TODO im skeptical isBE shouldnt always be nativeByteOrder
                    val members = (datatype.typedef as CompoundTypedef).members
                    val sdataArray = ArrayStructureData(shape, raw, isBE = isBE, userType.size, members)

                    /*
                    sdataArray.putVlenStringsOnHeap { member, offset ->
                        val zaddress = val_p.get(ValueLayout.ADDRESS, offset.toLong())
                        val address = zaddress.reinterpret(Long.MAX_VALUE)
                        val cString = address.reinterpret(Long.MAX_VALUE)

                        // TODO strings vs array of strings
                        val s = cString.getUtf8String(0)
                        if (debugUserTypes) println("OK CompoundAttribute read string offset=$offset value=$s")
                        listOf(s)
                    } */

                    sdataArray.putVlenStringsOnHeap { member, offset ->
                        val result = mutableListOf<String>()
                        repeat(member.nelems) {
                            val zaddress = val_p.get(ValueLayout.ADDRESS, (offset + it * 16).toLong())
                            val address = zaddress.reinterpret(Long.MAX_VALUE)
                            val cString = address.reinterpret(Long.MAX_VALUE)
                            val sval = cString.getUtf8String(0)
                            result.add(sval!!)
                        }
                        result
                    }

                    sdataArray.putVlensOnHeap { member, offset ->
                        // look duplicate (maybe)
                        val listOfVlen = mutableListOf<Array<*>>()
                        for (elem in 0 until member.nelems) {
                            val arraySize = val_p.get(JAVA_LONG, (offset).toLong()).toInt()
                            val zaddress = val_p.get(ValueLayout.ADDRESS, (offset + 8).toLong())
                            val address = zaddress.reinterpret(Long.MAX_VALUE)
                            listOfVlen.add( readVlenArray(arraySize, address, member.datatype.typedef!!.baseType))
                        }
                        ArrayVlen.fromArray(member.shape, listOfVlen, member.datatype.typedef!!.baseType)
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
                    val ba = val_p.toArray(ValueLayout.JAVA_BYTE)!!
                    // TODO im skeptical isBE shouldnt always be nativeByteOrder
                    val tba = TypedByteArray(v2.datatype, ba, 0, isBE = true)
                    return tba.convertToArrayTyped(shape)
                    /*
                    val values = ByteBuffer.wrap(raw)
                    when (datatype) {
                        Datatype.ENUM1 -> return ArrayUByte(shape, datatype as Datatype<UByte>, values) as ArrayTyped<T>
                        Datatype.ENUM2 -> return ArrayUShort(shape, datatype as Datatype<UShort>, values) as ArrayTyped<T>
                        Datatype.ENUM4 -> return ArrayUInt(shape, datatype as Datatype<UInt>, values) as ArrayTyped<T>
                        else -> throw RuntimeException()
                    } */
                }

                Datatype.BYTE -> {
                    val val_p = session.allocate(nelems)
                    checkErr("nc_get_vars_schar",
                        nc_get_vars_schar(vinfo.g4.grpid, vinfo.varid, origin_p, shape_p, stride_p, val_p))
                    val raw = val_p.toArray(ValueLayout.JAVA_BYTE)!!
                    return ArrayByte(shape, raw) as ArrayTyped<T>
                }

                Datatype.UBYTE -> {
                    val val_p = session.allocate(nelems)
                    checkErr("nc_get_vars_uchar",
                        nc_get_vars_uchar(vinfo.g4.grpid, vinfo.varid, origin_p, shape_p, stride_p, val_p))
                    val raw = val_p.toArray(ValueLayout.JAVA_BYTE)!!
                    return ArrayUByte.fromByteArray(shape, raw) as ArrayTyped<T>
                }

                Datatype.CHAR -> {
                    val val_p = session.allocate(nelems)
                    checkErr("nc_get_vars_text",
                        nc_get_vars_text(vinfo.g4.grpid, vinfo.varid, origin_p, shape_p, stride_p, val_p))
                    val raw = val_p.toArray(ValueLayout.JAVA_BYTE)
                    return ArrayUByte.fromByteArray(shape, Datatype.CHAR, raw) as ArrayTyped<T>
                }

                Datatype.DOUBLE -> {
                    // can you allocate DoubleBuffer on heap directly?
                    val val_p = session.allocateArray(C_DOUBLE, nelems)
                    checkErr("nc_get_vars_double", nc_get_vars_double(vinfo.g4.grpid, vinfo.varid, origin_p, shape_p, stride_p, val_p))
                    val values = val_p.toArray(C_DOUBLE)!!
                    return ArrayDouble(shape, values) as ArrayTyped<T>
                }

                Datatype.FLOAT -> {
                    val val_p = session.allocateArray(C_FLOAT, nelems)
                    checkErr("nc_get_vars_float", nc_get_vars_float(vinfo.g4.grpid, vinfo.varid, origin_p, shape_p, stride_p, val_p))
                    val values = val_p.toArray(C_FLOAT)!!
                    return ArrayFloat(shape, values) as ArrayTyped<T>
                }

                Datatype.INT -> {
                    // nc_get_vars_int(int ncid, int varid, const size_t *startp, const size_t *countp, const ptrdiff_t *stridep, int *ip);
                    val val_p = session.allocateArray(C_INT, nelems)
                    checkErr("nc_get_vars_int", nc_get_vars_int(vinfo.g4.grpid, vinfo.varid, origin_p, shape_p, stride_p, val_p))
                    val values = val_p.toArray(C_INT)!!
                    return ArrayInt(shape, values) as ArrayTyped<T>
                }

                Datatype.UINT -> {
                    // nc_get_vars_int(int ncid, int varid, const size_t *startp, const size_t *countp, const ptrdiff_t *stridep, int *ip);
                    val val_p = session.allocateArray(C_INT, nelems)
                    checkErr("nc_get_vars_uint", nc_get_vars_uint(vinfo.g4.grpid, vinfo.varid, origin_p, shape_p, stride_p, val_p))
                    val values = val_p.toArray(C_INT)!!
                    return ArrayUInt.fromIntArray(shape, values) as ArrayTyped<T>
                }

                Datatype.LONG -> {
                    // nc_get_vars_int(int ncid, int varid, const size_t *startp, const size_t *countp, const ptrdiff_t *stridep, int *ip);
                    val val_p = session.allocateArray(C_LONG as MemoryLayout, nelems)
                    checkErr("nc_get_vars_long", nc_get_vars_long(vinfo.g4.grpid, vinfo.varid, origin_p, shape_p, stride_p, val_p))
                    val values = val_p.toArray(C_LONG)!!

                    return ArrayLong(shape, values) as ArrayTyped<T>
                }

                Datatype.ULONG -> {
                    // nc_get_vars_int(int ncid, int varid, const size_t *startp, const size_t *countp, const ptrdiff_t *stridep, int *ip);
                    val val_p = session.allocateArray(C_LONG  as MemoryLayout, nelems)
                    checkErr("nc_get_vars_ulonglong", nc_get_vars_ulonglong(vinfo.g4.grpid, vinfo.varid, origin_p, shape_p, stride_p, val_p))
                    val values = val_p.toArray(C_LONG)!!
                    /*
                    val values = ByteBuffer.allocate(8 * nelems.toInt())
                    val lvalues = values.asLongBuffer()
                    for (i in 0 until nelems) {
                        lvalues.put(i.toInt(), val_p.getAtIndex(C_LONG, i))
                    } */
                    return ArrayULong.fromLongArray(shape, values) as ArrayTyped<T>
                }

                Datatype.SHORT -> {
                    val val_p = session.allocateArray(C_SHORT, nelems)
                    checkErr("nc_get_vars_short", nc_get_vars_short(vinfo.g4.grpid, vinfo.varid, origin_p, shape_p, stride_p, val_p))
                    val values = val_p.toArray(C_SHORT)!!
                    return ArrayShort(shape, values) as ArrayTyped<T>
                }

                Datatype.USHORT -> {
                    val val_p = session.allocateArray(C_SHORT, nelems)
                    checkErr("nc_get_vars_ushort",
                        nc_get_vars_ushort(vinfo.g4.grpid, vinfo.varid, origin_p, shape_p, stride_p, val_p))
                    val values = val_p.toArray(C_SHORT)!!
                    return ArrayUShort.fromShortArray(shape, values) as ArrayTyped<T>
                }

                Datatype.STRING -> {
                    val val_p = session.allocateArray(ValueLayout.ADDRESS, nelems)
                    checkErr("nc_get_vars_string", nc_get_vars_string(vinfo.g4.grpid, vinfo.varid, origin_p, shape_p, stride_p, val_p))
                    val values = mutableListOf<String>()
                    for (i in 0 until nelems) {
                        val address = val_p.getAtIndex(ValueLayout.ADDRESS, i)
                        val cString = address.reinterpret(Long.MAX_VALUE)
                        values.add(cString.getUtf8String(0))
                    }
                    return ArrayString(shape, values) as ArrayTyped<T>
                }

                Datatype.OPAQUE -> {
                    val val_p = session.allocate(nelems * userType!!.size)
                    checkErr("opaque nc_get_var", nc_get_vars(vinfo.g4.grpid, vinfo.varid, origin_p, shape_p, stride_p, val_p))
                    val ba = val_p.toArray(ValueLayout.JAVA_BYTE)!!
                    return ArrayOpaque.fromByteArray(shape, ba, userType.size) as ArrayTyped<T>
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