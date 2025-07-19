package com.sunya.netchdf.hdf5

import com.sunya.netchdf.testutil.readNetchdfData
import kotlin.test.Test

import com.sunya.netchdf.testutil.testData

class JhdfReadTest {
    companion object {
        @JvmStatic
        fun files(): Iterator<String> {
            return JhdfFiles.files()
        }
    }

    @Test
    fun problem() {
        val filename = "$testData/jhdf/compound_datasets_earliest.hdf5"
        println(filename)
        readNetchdfData(filename, "vlen_chunked_compound", null, true, true)
    }

    // HDF5 "$testData/jhdf/bitshuffle_datasets.hdf5" {
    //GROUP "/" {
    //   DATASET "float32_bs0_comp0" {
    //      DATATYPE  H5T_IEEE_F32LE
    //      DATASPACE  SIMPLE { ( 20 ) / ( 20 ) }
    //   }
    //   DATASET "float32_bs0_comp2" {
    //      DATATYPE  H5T_IEEE_F32LE
    //      DATASPACE  SIMPLE { ( 20 ) / ( 20 ) }
    //   } ...
   //  @Test
    fun testBitShuffle() { // 3
        val filename = "$testData/jhdf/bitshuffle_datasets.hdf5"
        println(filename)
        readNetchdfData(filename, null, null, true, false)
    }

    @Test
    fun testBTree2() { // 4
        val filename = "$testData/jhdf/chunked_v4_datasets.hdf5 "
        println(filename)
        readNetchdfData(filename, "/filtered_btree_v2/int8", null, false, true)
        readNetchdfData(filename, "/filtered_extensible_array/int8", null, false, true)
        readNetchdfData(filename, "/filtered_extensible_array/large_int16", null, false, true)
        readNetchdfData(filename, null, null, true, true)
    }

    // getHeapDataAsArray datatype=vlen null
    @Test
    fun testVlenHeapData() { // 6
        val filename = "$testData/jhdf/compound_datasets_earliest.hdf5"
        println(filename)
        readNetchdfData(filename, "vlen_chunked_compound", null, true, false)
    }

    // see jhdf/src/main/java/io/jhdf/object/datatype/FloatingPoint.java
    @Test
    fun testHalfFloat() { // 12
        val filename = "$testData/jhdf/float_special_values_earliest.hdf5"
        println(filename)
        readNetchdfData(filename, null, null, true, false)
    }

    // private typedefs
    @Test
    fun testCompoundTypedef() { // 18
        val filename = "$testData/jhdf/isssue-523.hdf5"
        println(filename)
        readNetchdfData(filename, null, null, true, false)
    }

    @Test
    fun testFractalHeap() { // 28
        val filename = "$testData/jhdf/test_attribute_latest.hdf5"
        println(filename)
        readNetchdfData(filename, null, null, true, false)
    }

    // Unknown filter type= lzf name = lzf
    // @Test
    fun testLzfFilter() { // 37
        val filename = "$testData/jhdf/test_compressed_chunked_datasets_earliest.hdf5"
        println(filename)
        readNetchdfData(filename, null, null, true, false)
    }

    // private typedefs
    @Test
    fun testVlen() { // 63
        val filename = "$testData/jhdf/test_vlen_datasets_latest.hdf5"
        println(filename)
        readNetchdfData(filename, null, null, true, false)
    }

    @Test
    fun testCompoundAttribute() {
        val filename = "$testData/jhdf/test_compound_scalar_attribute.hdf5"
        println(filename)
        readNetchdfData(filename, null, null, true, false)
    }

    @Test
    fun testCommittedDatatype() {
        val filename = "$testData/jhdf/issue255_example.hdf5"
        println(filename)
        readNetchdfData(filename, null, null, true, false)
    }

    /////////////////////////////////////
    @Test
    fun testReadAllJhdfFiles() {
        files().forEach { filename ->
            println(filename)
            readNetchdfData(filename, null, null, true, false)
        }

    }

}