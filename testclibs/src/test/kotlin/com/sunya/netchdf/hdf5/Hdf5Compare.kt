package com.sunya.netchdf.hdf5

import com.sunya.cdm.api.Datatype
import com.sunya.netchdf.*
import com.sunya.netchdf.netcdfClib.NClibFile
import com.sunya.netchdf.testfiles.N4Files
import com.sunya.netchdf.testutils.testData
import kotlin.test.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

// Compare header with Hdf5File and NetcdfClibFile
// some fail when they are not actually netcdf4 files
class Hdf5Compare {

    companion object {
        @JvmStatic
        fun files(): Iterator<String> {
            // 10 of 114 fail, because we compare with netcdf4 instead of hdf5 c library

            //val hdfeos5 =
             //   testFilesIn(testData + "devcdm/hdfeos5")
             //       .withRecursion()
              //      .build()

            return N4Files.files()
           //  return Stream.of( N4Files.params(),  H5Files.params()).flatMap { i -> i };
        }
    }

    @Test
    fun testNewLibrary() {
        val filename = testData + "netchdf/haberman/iso.h5"
        CompareNetchdf(filename, showCdl = true)
        compareDataWithClib(filename)
    }

    @Test
    fun problemChars() {
        val filename = testData + "cdmUnitTest/formats/netcdf4/files/c0_4.nc4"
        CompareNetchdf(filename)
        compareDataWithClib(filename)
    }

    @Test
    fun problemLibraryVersion() {
        val filename = testData + "devcdm/netcdf4/tst_solar_cmp.nc"
        CompareNetchdf(filename, showCdl = true)
        compareDataWithClib(filename)
    }

    @Test
    fun ok() {
        compareH5andNclib(testData + "netchdf/tomas/S3A_OL_CCDB_CHAR_AllFiles.20101019121929_1.nc4")
        compareDataWithClib(testData + "netchdf/tomas/S3A_OL_CCDB_CHAR_AllFiles.20101019121929_1.nc4")
    }

    @Test
    fun problem() {
        val filename = testData + "cdmUnitTest/formats/netcdf4/files/xma022032.nc"
        CompareNetchdf(filename)
        compareDataWithClib(filename)
    }

    ////////////////////////////////////////////////////////////////////

    @Test
    fun checkVersion() {
        files().forEach { filename ->
            openNetchdfFileWithFormat(filename, NetchdfFileFormat.HDF5).use { ncfile ->
                println("${ncfile!!.type()} $filename ")
                assertTrue(
                    ncfile.type().contains("hdf5") || ncfile.type().contains("hdf-eos5")
                            || (ncfile.type().contains("netcdf4"))
                )
            }
        }
    }

    @Test
    fun testCdlWithClib() {
        files().forEach { filename ->
            CompareNetchdf(filename)
        }
    }

    @Test
    fun testCompareDataWithClib() {
        files().forEach { filename ->
            compareDataWithClib(filename)
        }
    }

    @Test
    fun compareH5andNclib() {
        files().forEach { filename ->
            compareH5andNclib(filename)
        }
    }

    fun compareH5andNclib(filename: String) {
        println("===================================================")
        openNetchdfFileWithFormat(filename, NetchdfFileFormat.HDF5).use { h5file ->
            println("${h5file!!.type()} $filename ")
            println("\n${h5file.cdl()}")

            NClibFile(filename).use { nclibfile ->
                println("ncfile = ${nclibfile.cdl()}")
                compareCdlWithoutFileType(nclibfile.cdl(), h5file.cdl())
            }
        }
    }

    @Test
    fun readCharDataCompareNC() {
        files().forEach { filename ->
            compareSelectedDataWithClib(filename) { it.datatype == Datatype.CHAR }
        }
    }

}

fun compareCdlWithoutFileType(cdl1: String, cdl2: String) {
    val pos1 = cdl1.indexOf(' ')
    val pos2 = cdl2.indexOf(' ')
    assertEquals(cdl1.substring(pos1), cdl2.substring(pos2))
}