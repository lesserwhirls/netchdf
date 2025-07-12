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

    // Also has
    // Unsupported data layer type DataLayoutExtensibleArray4(flags=0, chunkDimensions=[2, 3, 1], maxBits=32, indexElements=4, minPointers=4, minElements=16, pageBits=10, indexAddress=125179)
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
        readNetchdfData(filename, null, null, true, false)
    }

    // see jhdf/src/main/java/io/jhdf/object/datatype/FloatingPoint.java
    @Test
    fun testHalfFloat() { // 12
        val filename = "/home/stormy/dev/github/netcdf/jhdf/jhdf/src/test/resources/hdf5/float_special_values_earliest.hdf5"
        println(filename)
        readNetchdfData(filename, null, null, true, false)
    }

    // null cannot be cast to non-null type com.sunya.cdm.api.CompoundTypedef
    @Test
    fun testCompoundTypedef() { // 18
        val filename = "/home/stormy/dev/github/netcdf/jhdf/jhdf/src/test/resources/hdf5/isssue-523.hdf5"
        println(filename)
        readNetchdfData(filename, null, null, true, false)
    }

    // failed to read vlen_int64_data_chunked, java.lang.IllegalArgumentException: datatype vlen null
    @Test
    fun testVlen() { // 63
        val filename = "/home/stormy/dev/github/netcdf/jhdf/jhdf/src/test/resources/hdf5/test_vlen_datasets_latest.hdf5"
        println(filename)
        readNetchdfData(filename, null, null, true, false)
    }

    // HDF5 "/home/stormy/dev/github/netcdf/jhdf/jhdf/src/test/resources/hdf5/test_compound_scalar_attribute.hdf5" {
    //GROUP "/" {
    //   GROUP "GROUP" {
    //      ATTRIBUTE "VERSION" {
    //         DATATYPE  H5T_COMPOUND {
    //            H5T_STD_I32LE "myMajor";
    //            H5T_STD_I32LE "myMinor";
    //            H5T_STD_I32LE "myPatch";
    //         }
    //         DATASPACE  SCALAR
    //      }
    //   }
    //}
    //}
    // hdf5 test_compound_scalar_attribute.hdf5 {
    //
    //  group: GROUP {
    //    types:
    //      compound anon {
    //        int myMajor ;
    //        int myMinor ;
    //        int myPatch ;
    //      }; // anon
    //
    //      // group attributes:
    //    :VERSION = {myMajor = 1, myMinor = 0, myPatch = 0} ;
    //  }
    //}
    @Test
    fun testCompoundAttribute() {
        val filename = "/home/stormy/dev/github/netcdf/jhdf/jhdf/src/test/resources/hdf5/test_compound_scalar_attribute.hdf5"
        println(filename)
        readNetchdfData(filename, null, null, true, false)
    }


    // "committed" aka "named" datatype
    // HDF5 "/home/stormy/dev/github/netcdf/jhdf/jhdf/src/test/resources/hdf5/issue255_example.hdf5" {
    //GROUP "/" {
    //   GROUP "__DATA_TYPES__" {
    //      DATATYPE "Enum_Boolean" H5T_ENUM {
    //         H5T_STD_I8LE;
    //         "FALSE"            0;
    //         "TRUE"             1;
    //      };
    //      DATATYPE "String_VariableLength" H5T_STRING {
    //         STRSIZE H5T_VARIABLE;
    //         STRPAD H5T_STR_NULLTERM;
    //         CSET H5T_CSET_ASCII;
    //         CTYPE H5T_C_S1;
    //      };
    //   }
    //   GROUP "groupA" {
    //      DATASET "date" {
    //         DATATYPE  H5T_STD_I64LE
    //         DATASPACE  SCALAR
    //         ATTRIBUTE "__TYPE_VARIANT__" {
    //            DATATYPE  H5T_ENUM {
    //               H5T_STD_I8LE;
    //               "TIMESTAMP_MILLISECONDS_SINCE_START_OF_THE_EPOCH" 0;
    //               "TIME_DURATION_MICROSECONDS" 1;
    //               "TIME_DURATION_MILLISECONDS" 2;
    //               "TIME_DURATION_SECONDS" 3;
    //               "TIME_DURATION_MINUTES" 4;
    //               "TIME_DURATION_HOURS" 5;
    //               "TIME_DURATION_DAYS" 6;
    //               "ENUM"             7;
    //               "NONE"             8;
    //               "BITFIELD"         9;
    //            }
    //            DATASPACE  SCALAR
    //         }
    //      }
    //      GROUP "groupC" {
    //      }
    //      DATASET "string" {
    //         DATATYPE  H5T_STRING {
    //            STRSIZE 24;
    //            STRPAD H5T_STR_NULLPAD;
    //            CSET H5T_CSET_ASCII;
    //            CTYPE H5T_C_S1;
    //         }
    //         DATASPACE  SCALAR
    //      }
    //   }
    //   GROUP "groupB" {
    //      ATTRIBUTE "__TYPE_VARIANT__timestamp__" {
    //         DATATYPE  H5T_ENUM {
    //            H5T_STD_I8LE;
    //            "TIMESTAMP_MILLISECONDS_SINCE_START_OF_THE_EPOCH" 0;
    //            "TIME_DURATION_MICROSECONDS" 1;
    //            "TIME_DURATION_MILLISECONDS" 2;
    //            "TIME_DURATION_SECONDS" 3;
    //            "TIME_DURATION_MINUTES" 4;
    //            "TIME_DURATION_HOURS" 5;
    //            "TIME_DURATION_DAYS" 6;
    //            "ENUM"             7;
    //            "NONE"             8;
    //            "BITFIELD"         9;
    //         }
    //         DATASPACE  SCALAR
    //      }
    //      ATTRIBUTE "important" {
    //         DATATYPE  "/__DATA_TYPES__/Enum_Boolean"
    //         DATASPACE  SCALAR
    //      }
    //      ATTRIBUTE "timestamp" {
    //         DATATYPE  H5T_STD_I64LE
    //         DATASPACE  SCALAR
    //      }
    //      DATASET "dmat" {
    //         DATATYPE  H5T_IEEE_F64LE
    //         DATASPACE  SIMPLE { ( 3, 3 ) / ( H5S_UNLIMITED, H5S_UNLIMITED ) }
    //      }
    //      SOFTLINK "groupC" {
    //         LINKTARGET "/groupA/groupC"
    //      }
    //      DATASET "inarr" {
    //         DATATYPE  H5T_STD_I32LE
    //         DATASPACE  SIMPLE { ( 3 ) / ( H5S_UNLIMITED ) }
    //      }
    //   }
    //}
    //}
    // hdf5 issue255_example.hdf5 {
    //  types:
    //    ubyte enum anon {0 = TIMESTAMP_MILLISECONDS_SINCE_START_OF_THE_EPOCH, 1 = TIME_DURATION_MICROSECONDS, 2 = TIME_DURATION_MILLISECONDS, 3 = TIME_DURATION_SECONDS, 4 = TIME_DURATION_MINUTES, 5 = TIME_DURATION_HOURS, 6 = TIME_DURATION_DAYS, 7 = ENUM, 8 = NONE, 9 = BITFIELD};
    //
    //  group: __DATA_TYPES__ {
    //    types:
    //      ubyte enum Enum_Boolean {0 = FALSE, 1 = TRUE};
    //      ubyte(*) String_VariableLength ;
    //  }
    //
    //  group: groupA {
    //    variables:
    //      int64 date ;
    //        :__TYPE_VARIANT__ = TIMESTAMP_MILLISECONDS_SINCE_START_OF_THE_EPOCH ;
    //      string string ;
    //
    //    group: groupC {
    //    }
    //  }
    //
    //  group: groupB {
    //    variables:
    //      double dmat(3, 3) ;
    //      int inarr(3) ;
    //
    //      // group attributes:
    //    :__TYPE_VARIANT__timestamp__ = TIMESTAMP_MILLISECONDS_SINCE_START_OF_THE_EPOCH ;
    //    :important = FALSE ;
    //    :timestamp = 1550033296762 ;
    //  }
    //}
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