package com.sunya.netchdf.netcdf4

import com.sunya.netchdf.compareCdlWithClib
import com.sunya.netchdf.compareDataWithClib
import com.sunya.netchdf.testdata.testData
import org.junit.jupiter.api.Test

class KnownProblemFiles {

    // LOOK theory is that a HDF5_DIMENSION_LIST that is a vlen of reference is a netcdf4 file
    // but its not, we dont care, but using Nclib fails to detect missing data for Variable Granule.
    @Test
    fun testConfuseHdf5WithNetcdf4() {
        val filename = "/home/all/testdata/netchdf/knox/SATMS_justdims_npp_d20120619_t1121416_e1122133_b03335_c20120619200237705890_noaa_ops.h5"
        // compareCdlWithClib(filename, true)
        compareDataWithClib(filename, "Granule")
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
    // We seem to bee supressing user types that are not used.
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

}