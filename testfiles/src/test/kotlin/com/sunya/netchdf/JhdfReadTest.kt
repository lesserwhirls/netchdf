package com.sunya.netchdf

import com.sunya.netchdf.testfiles.JhdfFiles
import com.sunya.netchdf.testutils.readNetchdfData
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.Test

class JhdfReadTest {
    companion object {
        @JvmStatic
        fun files(): Iterator<String> {
            return JhdfFiles.files()
        }
    }

    @Test
    fun problem() {
        val filename = "/home/stormy/dev/github/netcdf/jhdf/jhdf/src/test/resources/hdf5/100B_max_dimension_size.hdf5"
        println(filename)
        readNetchdfData(filename, null, null, true, false)
    }

    // HDF5 "/home/stormy/dev/github/netcdf/jhdf/jhdf/src/test/resources/hdf5/bitshuffle_datasets.hdf5" {
    //GROUP "/" {
    //   DATASET "float32_bs0_comp0" {
    //      DATATYPE  H5T_IEEE_F32LE
    //      DATASPACE  SIMPLE { ( 20 ) / ( 20 ) }
    //   }
    //   DATASET "float32_bs0_comp2" {
    //      DATATYPE  H5T_IEEE_F32LE
    //      DATASPACE  SIMPLE { ( 20 ) / ( 20 ) }
    //   } ...
    @Test
    fun testBitShuffle() { // 3
        val filename = "/home/stormy/dev/github/netcdf/jhdf/jhdf/src/test/resources/hdf5/bitshuffle_datasets.hdf5"
        println(filename)
        readNetchdfData(filename, null, null, true, false)
    }

    @Test
    fun testBTree2() { // 4
        val filename = "/home/stormy/dev/github/netcdf/jhdf/jhdf/src/test/resources/hdf5/chunked_v4_datasets.hdf5 "
        println(filename)
        readNetchdfData(filename, "/filtered_btree_v2/int8", null, false, true)
        readNetchdfData(filename, "/filtered_extensible_array/int8", null, false, true)
        readNetchdfData(filename, "/filtered_extensible_array/large_int16", null, false, true)
        readNetchdfData(filename, null, null, true, true)
    }

    // getHeapDataAsArray datatype=vlen null
    @Test
    fun testVlenHeapData() { // 6
        val filename = "/home/stormy/dev/github/netcdf/jhdf/jhdf/src/test/resources/hdf5/compound_datasets_earliest.hdf5"
        println(filename)
        readNetchdfData(filename, "vlen_chunked_compound", null, true, false)
    }

    // see jhdf/src/main/java/io/jhdf/object/datatype/FloatingPoint.java
    @Test
    fun testHalfFloat() { // 12
        val filename = "/home/stormy/dev/github/netcdf/jhdf/jhdf/src/test/resources/hdf5/float_special_values_earliest.hdf5"
        println(filename)
        readNetchdfData(filename, null, null, true, false)
    }

    // private typedefs
    @Test
    fun testCompoundTypedef() { // 18
        val filename = "/home/stormy/dev/github/netcdf/jhdf/jhdf/src/test/resources/hdf5/isssue-523.hdf5"
        println(filename)
        readNetchdfData(filename, null, null, true, false)
    }

    @Test
    fun testFractalHeap() { // 28
        val filename = "/home/stormy/dev/github/netcdf/jhdf/jhdf/src/test/resources/hdf5/test_attribute_latest.hdf5"
        println(filename)
        readNetchdfData(filename, null, null, true, false)
    }

    // Unknown filter type= lzf name = lzf
    @Test
    fun testLzfFilter() { // 37
        val filename = "/home/stormy/dev/github/netcdf/jhdf/jhdf/src/test/resources/hdf5/test_compressed_chunked_datasets_earliest.hdf5"
        println(filename)
        readNetchdfData(filename, null, null, true, false)
    }

    // private typedefs
    @Test
    fun testVlen() { // 63
        val filename = "/home/stormy/dev/github/netcdf/jhdf/jhdf/src/test/resources/hdf5/test_vlen_datasets_latest.hdf5"
        println(filename)
        readNetchdfData(filename, null, null, true, false)
    }

    @Test
    fun testCompoundAttribute() {
        val filename = "/home/stormy/dev/github/netcdf/jhdf/jhdf/src/test/resources/hdf5/test_compound_scalar_attribute.hdf5"
        println(filename)
        readNetchdfData(filename, null, null, true, false)
    }

    @Test
    fun testCommittedDatatype() {
        val filename = "/home/stormy/dev/github/netcdf/jhdf/jhdf/src/test/resources/hdf5/issue255_example.hdf5"
        println(filename)
        readNetchdfData(filename, null, null, true, false)
    }

    /////////////////////////////////////
    @ParameterizedTest
    @MethodSource("files")
    fun testReadN3data(filename: String) {
        println(filename)
        readNetchdfData(filename, null, null, true, false)
    }

}

// compoundAtt      test_compound_scalar_attribute.hdf5
// compoundTypedef  compound_datasets_earliest.hdf5, compound_datasets_latest.hdf5
// unknownDatatype  isssue-523 (Time type?)
// *committed       issue255_example.hdf5 (https://github.com/jamesmudd/jhdf/issues/255)
// vlen             test_vlen_datasets_latest.hdf5
// typedef          committed_datatypes.hdf5

// btree2       1
// bitshuffle   1
// lz4          1
// lzf          11
// halffloat    111111
// szip         1