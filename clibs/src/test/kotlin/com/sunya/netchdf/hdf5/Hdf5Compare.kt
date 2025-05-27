package com.sunya.netchdf.hdf5

import com.sunya.cdm.api.Datatype
import com.sunya.netchdf.compareCdlWithClib
import com.sunya.netchdf.compareDataWithClib
import com.sunya.netchdf.compareSelectedDataWithClib
import com.sunya.netchdf.hdf5Clib.Hdf5ClibFile
import com.sunya.netchdf.netcdfClib.NClibFile
import com.sunya.netchdf.readNetchdfData
import com.sunya.netchdf.testdata.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.*
import java.util.stream.Stream
import kotlin.test.assertEquals
import kotlin.test.assertTrue

// Compare header with Hdf5File and NetcdfClibFile
// some fail when they are not actually netcdf4 files
class Hdf5Compare {

    companion object {
        @JvmStatic
        fun params(): Stream<Arguments> {
            // 10 of 114 fail, because we compare with netcdf4 instead of hdf5 c library

            val hdfeos5 =
                testFilesIn(testData + "devcdm/hdfeos5")
                    .withRecursion()
                    .build()

            return Stream.of( N4Files.params()).flatMap { i -> i };
           //  return Stream.of( N4Files.params(),  H5Files.params()).flatMap { i -> i };
        }
    }


    @Test
    fun testNewLibrary() {
        val filename = testData + "netchdf/haberman/iso.h5"
        compareCdlWithClib(filename, showCdl = true)
        compareDataWithClib(filename)
    }

    // We use HDFEOS_INFORMATION to modify the structure, Nclib does not.
    @Test
    fun testEos() {
        compareH5andNclib(testData + "cdmUnitTest/formats/hdf5/aura/MLS-Aura_L2GP-BrO_v01-52-c01_2007d029.he5")
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
        compareH5andNclib(testData + "netchdf/esben/level2_MSG2_8bit_VISIR_STD_20091005_0700.H5")
        compareDataWithClib(testData + "netchdf/esben/level2_MSG2_8bit_VISIR_STD_20091005_0700.H5")
    }

    @Test
    fun problemChars() {
        val filename = testData + "cdmUnitTest/formats/netcdf4/files/c0_4.nc4"
        compareCdlWithClib(filename)
        compareDataWithClib(filename)
    }

    @Test
    fun problemLibraryVersion() {
        val filename = testData + "devcdm/netcdf4/tst_solar_cmp.nc"
        compareCdlWithClib(filename, showCdl = true)
        compareDataWithClib(filename)
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
    // HDF5File:
    // netcdf H12007_1m_MLLW_1of6.bag {
    //
    //  group: BAG_root {
    //    types:
    //      compound tracking_list {
    //        uint row ;
    //        uint col ;
    //        float depth ;
    //        float uncertainty ;
    //        ubyte track_code ;
    //        short list_series ;
    //      }; // tracking_list
    //    variables:
    //      float elevation(3027, 6981) ;
    //        :Maximum_Elevation_Value = -4.265297f ;
    //        :Minimum_Elevation_Value = -26.525713f ;
    //      char metadata(4953) ;
    //      tracking_list tracking_list(6) ;
    //        :Tracking_List_Length = 6 ;
    //      float uncertainty(3027, 6981) ;
    //        :Maximum_Uncertainty_Value = 1.001f ;
    //        :Minimum_Uncertainty_Value = 0.13900001f ;
    //
    //      // group attributes:
    //    :Bag_Version = "1.1.0" ;
    //  }
    //}
    @Test
    fun problem3() {
        compareH5andNclib(testData + "netchdf/austin/H12007_1m_MLLW_1of6.bag")
        compareDataWithClib(testData + "netchdf/austin/H12007_1m_MLLW_1of6.bag")
    }

    @Test
    fun ok() {
        compareH5andNclib(testData + "netchdf/tomas/S3A_OL_CCDB_CHAR_AllFiles.20101019121929_1.nc4")
        compareDataWithClib(testData + "netchdf/tomas/S3A_OL_CCDB_CHAR_AllFiles.20101019121929_1.nc4")
    }

    @ParameterizedTest
    @MethodSource("params")
    fun checkVersion(filename: String) {
        Hdf5File(filename).use { ncfile ->
            println("${ncfile.type()} $filename ")
            assertTrue(ncfile.type().contains("hdf5") || ncfile.type().contains("hdf-eos5")
                    || (ncfile.type().contains("netcdf4")))
        }
    }

    @ParameterizedTest
    @MethodSource("params")
    fun testCdlWithClib(filename: String) {
        compareCdlWithClib(filename)
    }

    @ParameterizedTest
    @MethodSource("params")
    fun testCompareDataWithClib(filename: String) {
        compareDataWithClib(filename)
    }

    @ParameterizedTest
    @MethodSource("params")
    fun compareH5andNclib(filename: String) {
        println("=================")
        Hdf5File(filename, true).use { h5file ->
            println("${h5file.type()} $filename ")
            println("\n${h5file.cdl()}")

            NClibFile(filename).use { nclibfile ->
                println("ncfile = ${nclibfile.cdl()}")
                assertEquals(nclibfile.cdl(), h5file.cdl())
            }
        }
    }

    @ParameterizedTest
    @MethodSource("params")
    fun readCharDataCompareNC(filename : String) {
        compareSelectedDataWithClib(filename) { it.datatype == Datatype.CHAR }
    }

}