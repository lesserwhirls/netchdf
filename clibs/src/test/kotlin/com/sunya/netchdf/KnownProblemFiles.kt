package com.sunya.netchdf

import com.sunya.netchdf.hdf4.Hdf4File
import com.sunya.netchdf.hdf5Clib.compareDataWithHdf5Clib
import com.sunya.netchdf.testdata.testData
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

val problemDir = "/home/all/testdata/exclude/"

class KnownProblemFiles {

    // LOOK theory is that a HDF5_DIMENSION_LIST that is a vlen of reference is a netcdf4 file
    // but its not, we dont care, but using Nclib fails to detect missing data for Variable Granule.
    @Test
    fun testConfuseHdf5WithNetcdf4() {
        val filename = "/home/all/testdata/netchdf/knox/SATMS_justdims_npp_d20120619_t1121416_e1122133_b03335_c20120619200237705890_noaa_ops.h5"
        // compareCdlWithClib(filename, true)
        compareDataWithClib(filename, "Granule")
    }

    @Test
    fun testConfusedHdf5() {
        val filename = "/home/all/testdata/netchdf/knox/SATMS_justdims_npp_d20120619_t1121416_e1122133_b03335_c20120619200237705890_noaa_ops.h5"
        // compareCdlWithClib(filename, true)
        compareDataWithHdf5Clib(filename, "Granule", null)
    }
    /* tst_grps
     Maybe netcdf-4 skips opaque type?

(Netcdf4)
netcdf tst_grps.nc4 {
types:
 int(*) vlen-1 ;
 byte(*) vlen-2 ;

group: the_in_crowd {
}

group: the_out_crowd {

 group: the_confused_crowd {
 }
}
}

(netchdf)
netcdf tst_grps.nc4 {
types:
 opaque(10) opaque-1 ;
 int(*) vlen-1 ;
 byte(*) vlen-2 ;

group: the_in_crowd {
 types:
   opaque(7) opaque-2 ;
}

group: the_out_crowd {
 types:
   opaque(4) opaque-3 ;

 group: the_confused_crowd {
   types:
     opaque(13) opaque-4 ;
 }
}
} */
    @Test
    fun tst_grps() {
        compareCdlWithClib(testData + "devcdm/netcdf4/tst_grps.nc4")
    }

    @Test
    // We seem to be suppressing user types that are not used.
    // C library shows
    //     compound compound_att_float {
    //      float field0 ;
    //      float field1 ;
    //      float field2 ;
    //      float field3 ;
    //    }; // compound_att_float
    fun compoundAttributeTest() {
        compareCdlWithClib(testData + "cdmUnitTest/formats/netcdf4/compound-attribute-test.nc")
    }

    // We use HDFEOS_INFORMATION to modify the structure, Nclib does not.
    @Test
    fun testEos() {
        compareDataWithClib(testData + "cdmUnitTest/formats/hdf5/aura/MLS-Aura_L2GP-BrO_v01-52-c01_2007d029.he5")
    }

    // NClibFile has:
    //   group: image9 {
    //    variables:
    //      ubyte image_data(3712, 3712) ;
    //        :DISPLAY_ORIGIN = "UL" ;
    //
    // Hdf5file has
    //   group: image9 {
    //    variables:
    //      ubyte image_data(3712, 3712) ;
    //        :CLASS = "IMAGE" ;
    //        :DISPLAY_ORIGIN = "UL" ;
    //        :PALETTE = "/visualisation9/color_palette" ;
    //
    // Im guessing CLASS and PALETTE attributes are supressed. In c library or our code?

    @Test
    fun problemReferenceToPallette() {
        // compareH5andNclib(testData + "netchdf/esben/level2_MSG2_8bit_VISIR_STD_20091005_0700.H5")
        compareDataWithClib(testData + "netchdf/esben/level2_MSG2_8bit_VISIR_STD_20091005_0700.H5")
    }

    // netcdf library version 4.9.2-development of Mar 19 2023 10:42:31
    // ~/install/netcdf4/bin:$ ./ncdump -h /home/all/testdata/netchdf/austin/H12007_1m_MLLW_1of6.bag
    // netcdf H12007_1m_MLLW_1of6 {
    //
    //group: BAG_root {
    //  dimensions:
    //  	phony_dim_0 = 3027 ;
    //  	phony_dim_1 = 6981 ;
    //  	phony_dim_2 = UNLIMITED ; // (4953 currently)
    //  variables:
    //  	float elevation(phony_dim_0, phony_dim_1) ;
    //  		elevation:Minimum\ Elevation\ Value = -26.52571f ;
    //  		elevation:Maximum\ Elevation\ Value = -4.265297f ;
    //  	char metadata(phony_dim_2) ;
    //  	float uncertainty(phony_dim_0, phony_dim_1) ;
    //  		uncertainty:Minimum\ Uncertainty\ Value = 0.139f ;
    //  		uncertainty:Maximum\ Uncertainty\ Value = 1.001f ;
    //
    //  // group attributes:
    //  		:Bag\ Version = "1.1.0\000\000\000ISO19139/smXML/metadataE" ;
    //  } // group BAG_root
    //}
    //
    // NClibFile:
    // netcdf H12007_1m_MLLW_1of6.bag {
    //
    //  group: BAG_root {
    //    variables:
    //      float elevation(3027, 6981) ;
    //        :Maximum_Elevation_Value = -4.265297f ;
    //        :Minimum_Elevation_Value = -26.525713f ;
    //      char metadata(4953) ;
    //      float uncertainty(3027, 6981) ;
    //        :Maximum_Uncertainty_Value = 1.001f ;
    //        :Minimum_Uncertainty_Value = 0.13900001f ;
    //
    //      // group attributes:
    //    :Bag_Version = "1.1.0" ;
    //  }
    //}
    ////////////////////////////////////////////////////////////////////////

    // Upgrade to netcdf library version 4.10.0-development of May 23 2025 14:45:19 $
    // ~/install/netcdf4/bin:$ ./ncdump -h /home/all/testdata/netchdf/austin/H12007_1m_MLLW_1of6.bag


    //netcdf H12007_1m_MLLW_1of6 {
    //
    //group: BAG_root {
    //  types:
    //    compound _AnonymousCompound1 {
    //      uint row ;
    //      uint col ;
    //      float depth ;
    //      float uncertainty ;
    //      ubyte track_code ;
    //      short list_series ;
    //    }; // _AnonymousCompound1
    //  dimensions:
    //  	phony_dim_0 = 3027 ;
    //  	phony_dim_1 = 6981 ;
    //  	phony_dim_2 = UNLIMITED ; // (4953 currently)
    //  	phony_dim_3 = UNLIMITED ; // (6 currently)
    //  variables:
    //  	float elevation(phony_dim_0, phony_dim_1) ;
    //  		elevation:Minimum\ Elevation\ Value = -26.52571f ;
    //  		elevation:Maximum\ Elevation\ Value = -4.265297f ;
    //  	char metadata(phony_dim_2) ;
    //  	_AnonymousCompound1 tracking_list(phony_dim_3) ;
    //  		tracking_list:Tracking\ List\ Length = 6U ;
    //  	float uncertainty(phony_dim_0, phony_dim_1) ;
    //  		uncertainty:Minimum\ Uncertainty\ Value = 0.139f ;
    //  		uncertainty:Maximum\ Uncertainty\ Value = 1.001f ;
    //
    //  // group attributes:
    //  		:Bag\ Version = "1.1.0\000\000\000ISO19139/smXML/metadataE" ;
    //  } // group BAG_root
    //}
    //
    // NClibFile:
    // ncfile = netcdf H12007_1m_MLLW_1of6.bag {
    //
    //  group: BAG_root {
    //    types:
    //      compound _AnonymousCompound1 {
    //        uint row ;
    //        uint col ;
    //        float depth ;
    //        float uncertainty ;
    //        ubyte track_code ;
    //        short list_series ;
    //      }; // _AnonymousCompound1
    //    variables:
    //      float elevation(3027, 6981) ;
    //        :Maximum_Elevation_Value = -4.265297f ;
    //        :Minimum_Elevation_Value = -26.525713f ;
    //      char metadata(4953) ;
    //      _AnonymousCompound1 tracking_list(6) ;
    //        :Tracking_List_Length = 6 ;
    //      float uncertainty(3027, 6981) ;
    //        :Maximum_Uncertainty_Value = 1.001f ;
    //        :Minimum_Uncertainty_Value = 0.13900001f ;
    //
    //      // group attributes:
    //    :Bag_Version = "1.1.0" ;
    //  }
    //}
    //////////////////////////////////////////
    // org.opentest4j.AssertionFailedError: variable /BAG_root/metadata ==> expected:
    //
    // <class ArrayString shape=[4953] data='<','?','x','m','l',' ','v','e','r','s','i','o','n','=','"','1','.','0','"',' ','?','>','<','s','m','X','M', ...
    //> but was: <class ArrayUByte shape=[4953] data=60,63,120,109,108,32,118,101,114,115,105,111,110,61,34,49,46,48,34,32,63,62,60,115,109,88,77,76,58, ...
    // the HDF5 mdt= DatatypeMessage(address=1992, type=String, elemSize=1, endian=false, isShared=false)
    //          mds= DataspaceMessage(type=Simple, dims=[4953], isUnlimited=true)
    // looks like netchdf is changing that to char[4953], claiming string[1] is a mistake i guess.
    @Test
    fun testString1() {
        // compareH5andNclib(testData + "netchdf/austin/H12007_1m_MLLW_1of6.bag")
        compareDataWithClib(testData + "netchdf/austin/H12007_1m_MLLW_1of6.bag", "/BAG_root/metadata")
    }


    //     /** Compute the tile from an index, ie which tile does this point belong to? */
    //    fun tile(index: LongArray): LongArray {
    //        val useRank = min(rank, index.size) // eg varlen (datatype 9) has mismatch
    //        val tile = LongArray(useRank)
    //        for (i in 0 until useRank) {
    //            // 7/30/2016 jcaron. Apparently in some cases, at the end of the array, the index can be greater than the shape.
    //            // eg cdmUnitTest/formats/netcdf4/UpperDeschutes_t4p10_swemelt.nc
    //            // Presumably to have even chunks. Could try to calculate the last even chunk.
    //            // For now im removing this consistency check.
    //            // assert shape[i] >= pt[i] : String.format("shape[%s]=(%s) should not be less than pt[%s]=(%s)", i, shape[i], i, pt[i]);
    //            tile[i] = index[i] / chunk[i] // LOOK seems wrong, rounding down ??
    //        }
    //        return tile
    //    }
    @Test
    fun testNetchIterate() {
        val filename = problemDir + "UpperDeschutes_t4p10_swemelt.nc"
        val varname = "UpperDeschutes_t4p10_swemelt"
        //  *** double UpperDeschutes_t4p10_swemelt[8395, 781, 385] skip read ArrayData too many bytes= 2524250575
        compareNetchIterate(filename, varname)

        // readH5concurrent(filename, null)
    }

    // * IP8/1             usedBy=false pos=18664902/32 rgb=147,0,108,144,0,111,141,0,114,138,0,117,135,0,120,132,0,123,129,0,126,126,0,129,123,0,132,120,0,135,117,0,138,114,0,141,111,0,144,108,0,147,105,0,150,102,0,153,99,0,156,96,0,159,93,0,162,90,0,165,87,0,168,84,0,171,81,0,174,78,0,177,75,0,180,72,0,183,69,0,186,66,0,189,63,0,192,60,0,195,57,0,198,54,0,201,51,0,204,48,0,207,45,0,210,42,0,213,39,0,216,36,0,219,33,0,222,30,0,225,27,0,228,24,0,231,21,0,234,18,0,237,15,0,240,12,0,243,9,0,246,6,0,249,0,0,252,0,0,255,0,5,255,0,10,255,0,16,255,0,21,255,0,26,255,0,32,255,0,37,255,0,42,255,0,48,255,0,53,255,0,58,255,0,64,255,0,69,255,0,74,255,0,80,255,0,85,255,0,90,255,0,96,255,0,101,255,0,106,255,0,112,255,0,117,255,0,122,255,0,128,255,0,133,255,0,138,255,0,144,255,0,149,255,0,154,255,0,160,255,0,165,255,0,170,255,0,176,255,0,181,255,0,186,255,0,192,255,0,197,255,0,202,255,0,208,255,0,213,255,0,218,255,0,224,255,0,229,255,0,234,255,0,240,255,0,245,255,0,250,255,0,255,255,0,255,247,0,255,239,0,255,231,0,255,223,0,255,215,0,255,207,0,255,199,0,255,191,0,255,183,0,255,175,0,255,167,0,255,159,0,255,151,0,255,143,0,255,135,0,255,127,0,255,119,0,255,111,0,255,103,0,255,95,0,255,87,0,255,79,0,255,71,0,255,63,0,255,55,0,255,47,0,255,39,0,255,31,0,255,23,0,255,15,0,255,0,8,255,0,16,255,0,24,255,0,32,255,0,40,255,0,48,255,0,56,255,0,64,255,0,72,255,0,80,255,0,88,255,0,96,255,0,104,255,0,112,255,0,120,255,0,128,255,0,136,255,0,144,255,0,152,255,0,160,255,0,168,255,0,176,255,0,184,255,0,192,255,0,200,255,0,208,255,0,216,255,0,224,255,0,232,255,0,240,255,0,248,255,0,255,255,0,255,251,0,255,247,0,255,243,0,255,239,0,255,235,0,255,231,0,255,227,0,255,223,0,255,219,0,255,215,0,255,211,0,255,207,0,255,203,0,255,199,0,255,195,0,255,191,0,255,187,0,255,183,0,255,179,0,255,175,0,255,171,0,255,167,0,255,163,0,255,159,0,255,155,0,255,151,0,255,147,0,255,143,0,255,139,0,255,135,0,255,131,0,255,127,0,255,123,0,255,119,0,255,115,0,255,111,0,255,107,0,255,103,0,255,99,0,255,95,0,255,91,0,255,87,0,255,83,0,255,79,0,255,75,0,255,71,0,255,67,0,255,63,0,255,59,0,255,55,0,255,51,0,255,47,0,255,43,0,255,39,0,255,35,0,255,31,0,255,27,0,255,23,0,255,19,0,255,15,0,255,11,0,255,7,0,255,3,0,255,0,0,250,0,0,245,0,0,240,0,0,235,0,0,230,0,0,225,0,0,220,0,0,215,0,0,210,0,0,205,0,0,200,0,0,195,0,0,190,0,0,185,0,0,180,0,0,175,0,0,170,0,0,165,0,0,160,0,0,155,0,0,150,0,0,145,0,0,140,0,0,135,0,0,130,0,0,125,0,0,120,0,0,115,0,0,110,0,0,105,0,0,0,0,0,
    // * LUT/1             usedBy=false pos=18664902/32 nelems=null
    //  DIL/1             usedBy=true pos=18665670/31 for=1/201 text=palette
    @Test
    fun problemDIL() {
        val filename = testData + "hdf4/S2007329.L3m_DAY_CHLO_9"
        Hdf4File(filename).use { h4file ->
            println("--- ${h4file.type()} $filename ")
            assertTrue( 0 == h4file.header.showTags(true, true, false))
        }
    }

    @Test
    fun problemTruncated() {
        compareCdlWithClib(problemDir + "OMI-Aura_L2-OMTO3_2009m0829t1219-o27250_v003-2009m0829t175727.he5")
    }

    @Test
    fun problem() {
        val filename = problemDir + "RAD_NL25_PCP_NA_200804110600.h5"
        compareCdlWithClib(filename)
        compareDataWithClib(filename)
    }

    // tried to add duplicate dimension 'lv1'
    @Test
    fun problem2() {
        val filename = testData + "netchdf/gilmore/data.nc"
        compareCdlWithClib(filename)
        compareDataWithClib(filename)
    }

    // yikes, wtf? dont they know what arrays are? consider these damaged
    @Test
    fun testIASI() {
        val filename = testData + "cdmUnitTest/formats/hdf5/IASI/IASI_xxx_1C_M02_20070704193256Z_20070704211159Z_N_O_20070704211805Z.h5"
        compareCdlWithClib(filename)
        compareDataWithClib(filename)
    }


    // not picking up missing value ??
    //  *** FAIL comparing data for variable = ubyte Granule [Granule]
    //255 != 0 at idx = 1
    //255 != 0 at idx = 2
    //255 != 0 at idx = 3
    //255 != 0 at idx = 4
    //255 != 0 at idx = 5
    //255 != 0 at idx = 6
    // *** count values differ = 6 same = 1
    @Test
    fun superblockIsOffsetNPP() {
        compareDataWithClib(testData + "exclude/superblockIsOffsetNPP.h5")
    }

}