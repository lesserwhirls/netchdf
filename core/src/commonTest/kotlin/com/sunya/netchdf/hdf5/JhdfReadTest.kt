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

    // hdf5 compound_datasets_earliest.hdf5 {
    //  types:
    //    compound 2d_chunked_compound {
    //      float real ;
    //      float img ;
    //    }; // 2d_chunked_compound
    //    compound array_vlen_chunked_compound {
    //      string name(2) ;
    //    }; // array_vlen_chunked_compound
    //    compound chunked_compound {
    //      string firstName ;
    //      string surname ;
    //      gender gender ;
    //      ubyte age ;
    //      float fav_number ;
    //      float vector(3) ;
    //    }; // chunked_compound
    //    compound nested_chunked_compound {
    //      firstNumber firstNumber ;
    //      secondNumber secondNumber ;
    //    }; // nested_chunked_compound
    //    compound vlen_chunked_compound {
    //      one one ;
    //      two two ;
    //    }; // vlen_chunked_compound
    //  variables:
    //    2d_chunked_compound 2d_chunked_compound(3, 3) ;
    //    2d_chunked_compound 2d_contiguous_compound(3, 3) ;
    //    array_vlen_chunked_compound array_vlen_chunked_compound(1) ;
    //    array_vlen_chunked_compound array_vlen_contiguous_compound(1) ;
    //    chunked_compound chunked_compound(4) ;
    //    chunked_compound contiguous_compound(4) ;
    //    nested_chunked_compound nested_chunked_compound(3) ;
    //    nested_chunked_compound nested_contiguous_compound(3) ;
    //    vlen_chunked_compound vlen_chunked_compound(3) ;
    //    vlen_chunked_compound vlen_contiguous_compound(3) ;
    //}
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
    fun testReadN3data() {
        files().forEach { filename ->
            println(filename)
            readNetchdfData(filename, null, null, true, false)
        }

    }

}