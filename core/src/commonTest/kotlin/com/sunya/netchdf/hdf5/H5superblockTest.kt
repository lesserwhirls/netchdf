package com.sunya.netchdf.hdf5

import com.sunya.netchdf.testfiles.SuperblockFiles
import com.sunya.netchdf.testfiles.testData
import com.sunya.netchdf.testutil.readNetchdfData

import kotlin.test.*

// Sanity check read Hdf5File header, for non-netcdf4 files
class H5superblockTest {

    @Test
    fun testEnum8() {
        val filename = testData + "netcdf-c_hdf5_superblocks/netcdf-c-test-files/unmodified/ncdump__ref_tst_irish_rover.nc"
        val ok = openH5(filename, null)
        assertTrue(ok)
    }

    @Test
    fun testHeapDataCompound() {
        val filename = testData + "netcdf-c_hdf5_superblocks/netcdf-c-test-files/unmodified/nc_test4__ref_tst_xplatform2_1.nc"
        val ok = openH5(filename, null)
        assertTrue(ok)
    }

    // seriously nasty
    // netcdf nc_test4__tst_compounds3 {
    //
    //// global attributes:
    //		/g2/t2 :a1 = {{13.3, 13.3}} ;
    //
    //group: g1 {
    //  types:
    //    compound t1 {
    //      float x ;
    //      double y ;
    //    }; // t1
    //  } // group g1
    //
    //group: g2 {
    //  types:
    //    compound t2 {
    //      /g1/t1 s1 ;
    //    }; // t2
    //  } // group g2
    //}

    @Test
    fun testNastyTypes() {
        val filename = testData + "netcdf-c_hdf5_superblocks/netcdf-c-test-files/unmodified/nc_test4__tst_compounds3.nc"
        val ok = openH5(filename, null)
        assertTrue(ok)
    }

    // ~:$ /home/stormy/install/netcdf4/bin/ncdump /home/all/testdata/netcdf-c_hdf5_superblocks/netcdf-c-test-files/unmodified/nc_test4__tst_xplatform2_3.nc
    //netcdf nc_test4__tst_xplatform2_3 {
    //types:
    //  compound cmp_t {
    //    float x ;
    //    double y ;
    //  }; // cmp_t
    //  cmp_t(*) Magna_Carta_VLEN ;
    //  compound barons {
    //    Magna_Carta_VLEN No\ scutage\ or\ aid\ may\ be\ levied\ in\ our\ kingdom\ without\ its\ general\ consent(1) ;
    //  }; // barons
    //
    //// global attributes:
    //		barons :King_John = {{{{42.42, 2}}}} ;

    @Test
    fun testNastyTypes2() {
        val filename = testData + "netcdf-c_hdf5_superblocks/netcdf-c-test-files/unmodified/nc_test4__tst_xplatform2_3.nc"
        val ok = openH5(filename, null)
        assertTrue(ok)
    }

    // netcdf nc_test4__tst_xplatform2_4 {
    //types:
    //  compound cmp_t {
    //    float x ;
    //    double y ;
    //  }; // cmp_t
    //  cmp_t(*) Magna_Carta_VLEN ;
    //  compound barons {
    //    Magna_Carta_VLEN No\ scutage\ or\ aid\ may\ be\ levied\ in\ our\ kingdom\ without\ its\ general\ consent(1) ;
    //  }; // barons
    //dimensions:
    //	DIMENSION-\>The\ city\ of\ London\ shall\ enjoy\ all\ its\ ancient\ liberties\ and\ free\ customs\,\ both\ by\ land\ and\ by\ water. = 1 ;
    //variables:
    //	barons VARIABLE-\>In\ future\ we\ will\ allow\ no\ one\ to\ levy\ an\ \`aid\'\ from\ his\ free\ men\,\ except\ to\ ransom\ his\ person\,\ to\ make\ his\ eldest\ son\ a\ knight\,\ and\ \(once\)\ to\ marry\ his\ eldest\ daughter.(DIMENSION-\>The\ city\ of\ London\ shall\ enjoy\ all\ its\ ancient\ liberties\ and\ free\ customs\,\ both\ by\ land\ and\ by\ water.) ;
    //data:
    //
    // VARIABLE-\>In\ future\ we\ will\ allow\ no\ one\ to\ levy\ an\ \`aid\'\ from\ his\ free\ men\,\ except\ to\ ransom\ his\ person\,\ to\ make\ his\ eldest\ son\ a\ knight\,\ and\ \(once\)\ to\ marry\ his\ eldest\ daughter. =
    //    {{{{42.42, 2}}}} ;
    //}
    @Test
    fun testNastyTypes3() {
        val filename = testData + "netcdf-c_hdf5_superblocks/netcdf-c-test-files/unmodified/nc_test4__tst_xplatform2_4.nc"
        val ok = openH5(filename, null)
        assertTrue(ok)
    }

    @Test
    fun testNewLayouts() {
        val filename = testData + "netcdf-c_hdf5_superblocks/netcdf-c-test-files/v1_10/nc_test4__tst_bug1442.nc"
        val ok = openH5(filename, null, showCdl = true)
        assertTrue(ok)
    }

    // ~:$ h5dump -d var /home/all/testdata/netcdf-c_hdf5_superblocks/netcdf-c-test-files/v1_10/examples__bzip2.nc
    //HDF5 "/home/all/testdata/netcdf-c_hdf5_superblocks/netcdf-c-test-files/v1_10/examples__bzip2.nc" {
    //DATASET "var" {
    //   DATATYPE  H5T_IEEE_F32LE
    //   DATASPACE  SIMPLE { ( 4, 4, 4, 4 ) / ( 4, 4, 4, 4 ) }
    //   DATA {h5dump error: unable to print data
    //
    //   }
    //   ATTRIBUTE "DIMENSION_LIST" {
    //      DATATYPE  H5T_VLEN { H5T_REFERENCE { H5T_STD_REF_OBJECT }}
    //      DATASPACE  SIMPLE { ( 4 ) / ( 4 ) }
    //      DATA {
    //      (0): (), (), (), ()
    //      }
    //   }
    //   ATTRIBUTE "_Netcdf4Coordinates" {
    //      DATATYPE  H5T_STD_I32LE
    //      DATASPACE  SIMPLE { ( 4 ) / ( 4 ) }
    //      DATA {
    //      (0): 0, 1, 2, 3
    //      }
    //   }
    //}
    //}
    //netcdf examples__bzip2 {
    //dimensions:
    //	dim0 = 4 ;
    //	dim1 = 4 ;
    //	dim2 = 4 ;
    //	dim3 = 4 ;
    //variables:
    //	float var(dim0, dim1, dim2, dim3) ;
    //data:
    //
    // var =
    //  0, 1, 2, 3,
    //  4, 5, 6, 7,
    //  8, 9, 10, 11,
    // ...
    //  244, 245, 246, 247,
    //  248, 249, 250, 251,
    //  252, 253, 254, 255 ;
    //}

    // netcdf4 examples__bzip2.nc {
    //  dimensions:
    //    dim0 = 4 ;
    //    dim1 = 4 ;
    //    dim2 = 4 ;
    //    dim3 = 4 ;
    //  variables:
    //    float var(dim0, dim1, dim2, dim3) ;
    //}
    @Test
    fun testSingleChunk() {
        val filename = testData + "netcdf-c_hdf5_superblocks/netcdf-c-test-files/v1_10/examples__bzip2.nc"
        val ok = openH5(filename, "var", showCdl = true)
        assertTrue(ok)
    }

    // this one needs bzip2
    @Test
    fun testSingleChunkVer1_8() {
        val filename = testData + "netcdf-c_hdf5_superblocks/netcdf-c-test-files/v1_8/examples__bzip2.nc"
        val ok = openH5(filename, "var", showCdl = true)
        assertTrue(ok)
    }

    // /home/all/testdata/netcdf-c_hdf5_superblocks/netcdf-c-test-files/v1_10/nc_test4__testfilter_reg.nc
    //  chunkSize 1024 nextBytes [0, 0, 0, 0, 3, 27, 0, 0, 0, 0, 0, 0, 21, 28, 0, 4, 0, 0, 0, 3, 2, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1]
    //  filterMask 0 chunkAddress 6915
    //     im guessing the 0 means no filter. 4^5 = 1024
    //  var = class com.sunya.cdm.array.ArrayFloat shape=[4, 4, 4, 4] data=0.0,1.0,2.0,3.0,4.0,5.0,6.0,7.0,8.0,9.0,10.0,11.0,12.0,13.0,14.0,15.0,16.0,17.0,18.0,19.0,20.0,21.0,22.0,23.0,24.0,25.0,26.0,27.0,28.0,29.0,30.0,31.0,32.0,33.0,34.0,35.0,36.0,37.0,38.0,39.0,40.0,41.0,42.0,43.0,44.0,45.0,46.0,47.0,48.0,49.0,50.0,51.0,52.0,53.0,54.0,55.0,56.0,57.0,58.0,59.0,60.0,61.0,62.0,63.0,64.0,65.0,66.0,67.0,68.0,69.0,70.0,71.0,72.0,73.0,74.0,75.0,76.0,77.0,78.0,79.0,80.0,81.0,82.0,83.0,84.0,85.0,86.0,87.0,88.0,89.0,90.0,91.0,92.0,93.0,94.0,95.0,96.0,97.0,98.0,99.0,100.0,101.0,102.0,103.0,104.0,105.0,106.0,107.0,108.0,109.0,110.0,111.0,112.0,113.0,114.0,115.0,116.0,117.0,118.0,119.0,120.0,121.0,122.0,123.0,124.0,125.0,126.0,127.0,128.0,129.0,130.0,131.0,132.0,133.0,134.0,135.0,136.0,137.0,138.0,139.0,140.0,141.0,142.0,143.0,144.0,145.0,146.0,147.0,148.0,149.0,150.0,151.0,152.0,153.0,154.0,155.0,156.0,157.0,158.0,159.0,160.0,161.0,162.0,163.0,164.0,165.0,166.0,167.0,168.0,169.0,170.0,171.0,172.0,173.0,174.0,175.0,176.0,177.0,178.0,179.0,180.0,181.0,182.0,183.0,184.0,185.0,186.0,187.0,188.0,189.0,190.0,191.0,192.0,193.0,194.0,195.0,196.0,197.0,198.0,199.0,200.0,201.0,202.0,203.0,204.0,205.0,206.0,207.0,208.0,209.0,210.0,211.0,212.0,213.0,214.0,215.0,216.0,217.0,218.0,219.0,220.0,221.0,222.0,223.0,224.0,225.0,226.0,227.0,228.0,229.0,230.0,231.0,232.0,233.0,234.0,235.0,236.0,237.0,238.0,239.0,240.0,241.0,242.0,243.0,244.0,245.0,246.0,247.0,248.0,249.0,250.0,251.0,252.0,253.0,254.0,255.0
    @Test
    fun testSingleChunkBZipFilter() {
        readNetchdfData(testData + "netcdf-c_hdf5_superblocks/netcdf-c-test-files/v1_10/nc_test4__testfilter_reg.nc",
            "var", showCdl = true, showData = true)
    }

    // h5dump  -H /home/all/testdata/netcdf-c_hdf5_superblocks/netcdf-c-test-files/v1_10/nc_test4__tst_chunks.nc
    //HDF5 "/home/all/testdata/netcdf-c_hdf5_superblocks/netcdf-c-test-files/v1_10/nc_test4__tst_chunks.nc" {
    //GROUP "/" {
    //   ATTRIBUTE "_NCProperties" {
    //      DATATYPE  H5T_STRING {
    //         STRSIZE 35;
    //         STRPAD H5T_STR_NULLTERM;
    //         CSET H5T_CSET_ASCII;
    //         CTYPE H5T_C_S1;
    //      }
    //      DATASPACE  SCALAR
    //   }
    //   DATASET "Boat_Size" {
    //      DATATYPE  H5T_STD_I64LE
    //      DATASPACE  SIMPLE { ( 10000, 10000 ) / ( 10000, 10000 ) }
    //      ATTRIBUTE "DIMENSION_LIST" {
    //         DATATYPE  H5T_VLEN { H5T_REFERENCE { H5T_STD_REF_OBJECT }}
    //         DATASPACE  SIMPLE { ( 2 ) / ( 2 ) }
    //      }
    //      ATTRIBUTE "_Netcdf4Coordinates" {
    //         DATATYPE  H5T_STD_I32LE
    //         DATASPACE  SIMPLE { ( 2 ) / ( 2 ) }
    //      }
    //   }
    //   DATASET "Deck_Size" {
    //      DATATYPE  H5T_STD_I64LE
    //      DATASPACE  SIMPLE { ( 10000, 10000 ) / ( 10000, 10000 ) }
    //      ATTRIBUTE "DIMENSION_LIST" {
    //         DATATYPE  H5T_VLEN { H5T_REFERENCE { H5T_STD_REF_OBJECT }}
    //         DATASPACE  SIMPLE { ( 2 ) / ( 2 ) }
    //      }
    //      ATTRIBUTE "_Netcdf4Coordinates" {
    //         DATATYPE  H5T_STD_I32LE
    //         DATASPACE  SIMPLE { ( 2 ) / ( 2 ) }
    //      }
    //   }
    //   DATASET "Height" {
    //      DATATYPE  H5T_IEEE_F32BE
    //      DATASPACE  SIMPLE { ( 10000 ) / ( 10000 ) }
    //      ATTRIBUTE "CLASS" {
    //         DATATYPE  H5T_STRING {
    //            STRSIZE 16;
    //            STRPAD H5T_STR_NULLTERM;
    //            CSET H5T_CSET_ASCII;
    //            CTYPE H5T_C_S1;
    //         }
    //         DATASPACE  SCALAR
    //      }
    //      ATTRIBUTE "NAME" {
    //         DATATYPE  H5T_STRING {
    //            STRSIZE 64;
    //            STRPAD H5T_STR_NULLTERM;
    //            CSET H5T_CSET_ASCII;
    //            CTYPE H5T_C_S1;
    //         }
    //         DATASPACE  SCALAR
    //      }
    //      ATTRIBUTE "REFERENCE_LIST" {
    //         DATATYPE  H5T_COMPOUND {
    //            H5T_REFERENCE { H5T_STD_REF_OBJECT } "dataset";
    //            H5T_STD_I32LE "dimension";
    //         }
    //         DATASPACE  SIMPLE { ( 3 ) / ( 3 ) }
    //      }
    //      ATTRIBUTE "_Netcdf4Dimid" {
    //         DATATYPE  H5T_STD_I32LE
    //         DATASPACE  SCALAR
    //      }
    //   }
    //   DATASET "House_Size" {
    //      DATATYPE  H5T_STD_I64LE
    //      DATASPACE  SIMPLE { ( 10000, 10000 ) / ( 10000, 10000 ) }
    //      ATTRIBUTE "DIMENSION_LIST" {
    //         DATATYPE  H5T_VLEN { H5T_REFERENCE { H5T_STD_REF_OBJECT }}
    //         DATASPACE  SIMPLE { ( 2 ) / ( 2 ) }
    //      }
    //      ATTRIBUTE "_Netcdf4Coordinates" {
    //         DATATYPE  H5T_STD_I32LE
    //         DATASPACE  SIMPLE { ( 2 ) / ( 2 ) }
    //      }
    //   }
    //   DATASET "Width" {
    //      DATATYPE  H5T_IEEE_F32BE
    //      DATASPACE  SIMPLE { ( 10000 ) / ( 10000 ) }
    //      ATTRIBUTE "CLASS" {
    //         DATATYPE  H5T_STRING {
    //            STRSIZE 16;
    //            STRPAD H5T_STR_NULLTERM;
    //            CSET H5T_CSET_ASCII;
    //            CTYPE H5T_C_S1;
    //         }
    //         DATASPACE  SCALAR
    //      }
    //      ATTRIBUTE "NAME" {
    //         DATATYPE  H5T_STRING {
    //            STRSIZE 64;
    //            STRPAD H5T_STR_NULLTERM;
    //            CSET H5T_CSET_ASCII;
    //            CTYPE H5T_C_S1;
    //         }
    //         DATASPACE  SCALAR
    //      }
    //      ATTRIBUTE "REFERENCE_LIST" {
    //         DATATYPE  H5T_COMPOUND {
    //            H5T_REFERENCE { H5T_STD_REF_OBJECT } "dataset";
    //            H5T_STD_I32LE "dimension";
    //         }
    //         DATASPACE  SIMPLE { ( 3 ) / ( 3 ) }
    //      }
    //      ATTRIBUTE "_Netcdf4Dimid" {
    //         DATATYPE  H5T_STD_I32LE
    //         DATASPACE  SCALAR
    //      }
    //   }
    //}
    //}
    // netcdf4 nc_test4__tst_chunks.nc {
    //  dimensions:
    //    Height = 10000 ;
    //    Width = 10000 ;
    //  variables:
    //    int64 Boat_Size(Height, Width) ;
    //    int64 Deck_Size(Height, Width) ;
    //    int64 House_Size(Height, Width) ;
    //}
    // has filter i think
    @Test
    fun testSingleChunkWithFilter() {
        val filename = "/home/all/testdata/netcdf-c_hdf5_superblocks/netcdf-c-test-files/v1_10/nc_test4__tst_chunks.nc"
        val ok = openH5(filename, showCdl = true)
        assertTrue(ok)
    }

    @Test
    fun testFixedArray() {
        val filename = "/home/all/testdata/netcdf-c_hdf5_superblocks/netcdf-c-test-files/v1_10/examples__simple_xy_nc4.nc"
        val ok = openH5(filename, "data", showCdl = true)
        assertTrue(ok)
    }

    @Test
    fun testFixedArrayNoData() {
        val filename = "/home/all/testdata/netcdf-c_hdf5_superblocks/netcdf-c-test-files/v1_10/nc_test4__tst_chunks.nc"
        val ok = openH5(filename, "House_Size", showCdl = true)
        assertTrue(ok)
    }

    // damaged /home/all/testdata/netcdf-c_hdf5_superblocks/netcdf-c-test-files/v1_10/nc_test4__tst_chunks2.nc
    // /home/all/testdata/netcdf-c_hdf5_superblocks/netcdf-c-test-files/v1_10/nc_test4__tst_chunks2.nc
    //*** FixedArray dims= [571710, 1, 1, 4] pageBits=10 indexAddress = -1
    // But wtf:
    // netcdf nc_test4__tst_chunks2 {
    //dimensions:
    //	dim_0 = 336465782 ;
    //	dim_1 = 530 ;
    //	dim_2 = 862 ;
    //variables:
    //	float op-amp(dim_0, dim_1, dim_2) ;
    //}
    @Test
    fun testFixedArrayDamaged() {
        val filename = "/home/all/testdata/netcdf-c_hdf5_superblocks/netcdf-c-test-files/v1_10/nc_test4__tst_chunks2.nc"
        val ok = openH5(filename, showCdl = true)
        assertTrue(ok)
    }

    @Test
    fun testFixedArrayProblem() {
        val filename = "/home/all/testdata/netcdf-c_hdf5_superblocks/netcdf-c-test-files/v1_10/nc_test4__tst_chunks.nc"
        val ok = openH5(filename, showCdl = true)
        assertTrue(ok)
    }

    // /home/all/testdata/netcdf-c_hdf5_superblocks/netcdf-c-test-files/v1_10:$ h5dump  -H /home/all/testdata/netcdf-c_hdf5_superblocks/netcdf-c-test-files/v1_10/nc_test4__tst_dims3.nc
    //HDF5 "/home/all/testdata/netcdf-c_hdf5_superblocks/netcdf-c-test-files/v1_10/nc_test4__tst_dims3.nc" {
    //GROUP "/" {
    //   ATTRIBUTE "_NCProperties" {
    //      DATATYPE  H5T_STRING {
    //         STRSIZE 35;
    //         STRPAD H5T_STR_NULLTERM;
    //         CSET H5T_CSET_ASCII;
    //         CTYPE H5T_C_S1;
    //      }
    //      DATASPACE  SCALAR
    //   }
    //   DATASET "var" {
    //      DATATYPE  H5T_STD_I32LE
    //      DATASPACE  SIMPLE { ( 1, 3 ) / ( H5S_UNLIMITED, H5S_UNLIMITED ) }
    //      ATTRIBUTE "DIMENSION_LIST" {
    //         DATATYPE  H5T_VLEN { H5T_REFERENCE { H5T_STD_REF_OBJECT }}
    //         DATASPACE  SIMPLE { ( 2 ) / ( 2 ) }
    //      }
    //      ATTRIBUTE "_Netcdf4Coordinates" {
    //         DATATYPE  H5T_STD_I32LE
    //         DATASPACE  SIMPLE { ( 2 ) / ( 2 ) }
    //      }
    //   }
    //   DATASET "x" {
    //      DATATYPE  H5T_IEEE_F32BE
    //      DATASPACE  SIMPLE { ( 0 ) / ( H5S_UNLIMITED ) }
    //      ATTRIBUTE "CLASS" {
    //         DATATYPE  H5T_STRING {
    //            STRSIZE 16;
    //            STRPAD H5T_STR_NULLTERM;
    //            CSET H5T_CSET_ASCII;
    //            CTYPE H5T_C_S1;
    //         }
    //         DATASPACE  SCALAR
    //      }
    //      ATTRIBUTE "NAME" {
    //         DATATYPE  H5T_STRING {
    //            STRSIZE 64;
    //            STRPAD H5T_STR_NULLTERM;
    //            CSET H5T_CSET_ASCII;
    //            CTYPE H5T_C_S1;
    //         }
    //         DATASPACE  SCALAR
    //      }
    //      ATTRIBUTE "REFERENCE_LIST" {
    //         DATATYPE  H5T_COMPOUND {
    //            H5T_REFERENCE { H5T_STD_REF_OBJECT } "dataset";
    //            H5T_STD_I32LE "dimension";
    //         }
    //         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
    //      }
    //      ATTRIBUTE "_Netcdf4Dimid" {
    //         DATATYPE  H5T_STD_I32LE
    //         DATASPACE  SCALAR
    //      }
    //   }
    //   DATASET "y" {
    //      DATATYPE  H5T_STD_I32LE
    //      DATASPACE  SIMPLE { ( 3 ) / ( H5S_UNLIMITED ) }
    //      ATTRIBUTE "CLASS" {
    //         DATATYPE  H5T_STRING {
    //            STRSIZE 16;
    //            STRPAD H5T_STR_NULLTERM;
    //            CSET H5T_CSET_ASCII;
    //            CTYPE H5T_C_S1;
    //         }
    //         DATASPACE  SCALAR
    //      }
    //      ATTRIBUTE "NAME" {
    //         DATATYPE  H5T_STRING {
    //            STRSIZE 2;
    //            STRPAD H5T_STR_NULLTERM;
    //            CSET H5T_CSET_ASCII;
    //            CTYPE H5T_C_S1;
    //         }
    //         DATASPACE  SCALAR
    //      }
    //      ATTRIBUTE "REFERENCE_LIST" {
    //         DATATYPE  H5T_COMPOUND {
    //            H5T_REFERENCE { H5T_STD_REF_OBJECT } "dataset";
    //            H5T_STD_I32LE "dimension";
    //         }
    //         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
    //      }
    //      ATTRIBUTE "_Netcdf4Dimid" {
    //         DATATYPE  H5T_STD_I32LE
    //         DATASPACE  SCALAR
    //      }
    //   }
    //}
    //}
    // but do we trust ncdump with superblock 4?
    // netcdf nc_test4__tst_dims3 {
    //  dimensions:
    //	y = UNLIMITED ; // (3 currently)
    //	x = UNLIMITED ; // (3 currently)
    // variables:
    //	int y(y) ;
    //	int var(y, x) ;
    // }
    @Test
    fun testExtensibleArrayIndex() {
        val filename = "/home/all/testdata/netcdf-c_hdf5_superblocks/netcdf-c-test-files/v1_10/nc_test4__tst_dims3.nc"
        val ok = openH5(filename, "var", showCdl = true)
        assertTrue(ok)
    }

    ////////////////////////////////////////////////////////////////
    @Test
    fun testOpenSuperblockUnmodified() {
        var countOK = 0
        var countBad = 0
        SuperblockFiles.files("unmodified").forEach { filename ->
            val ok = openH5(filename, null)
            if (ok) countOK++ else countBad++
        }
        println("ok = $countOK fail = $countBad")
        println("mdlClassCount")
        mdlClassCount.forEach { (key, value) -> println("  ${key} == ${value}") }
    }

    @Test
    fun testOpenSuperblockV1_8() {
        var countOK = 0
        var countBad = 0
        SuperblockFiles.files("v1_8").forEach { filename ->
            val ok = openH5(filename, null)
            if (ok) countOK++ else countBad++
        }
        println("ok = $countOK fail = $countBad")
        println("mdlClassCount")
        mdlClassCount.forEach { (key, value) -> println("  ${key} == ${value}") }
    }

    @Test
    fun testOpenSuperblockV1_10() {
        var countOK = 0
        var countBad = 0
        SuperblockFiles.files("v1_10").forEach { filename ->
            val ok = openH5(filename, null)
            if (ok) countOK++ else countBad++
        }
        println("ok = $countOK fail = $countBad")
        println("mdlClassCount")
        mdlClassCount.forEach { (key, value) -> println("  ${key} == ${value}") }
    }

    @Test
    fun testOpenSuperblockV1_12() {
        var countOK = 0
        var countBad = 0
        SuperblockFiles.files("v1_12").forEach { filename ->
            val ok = openH5(filename, null)
            if (ok) countOK++ else countBad++
        }
        println("ok = $countOK fail = $countBad")
        println("mdlClassCount")
        mdlClassCount.forEach { (key, value) -> println("  ${key} == ${value}") }
    }
}