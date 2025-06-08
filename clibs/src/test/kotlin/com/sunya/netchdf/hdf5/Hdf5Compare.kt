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
        println("===================================================")
        Hdf5File(filename, true).use { h5file ->
            println("${h5file.type()} $filename ")
            println("\n${h5file.cdl()}")

            NClibFile(filename).use { nclibfile ->
                println("ncfile = ${nclibfile.cdl()}")
                compareCdlWithoutFileType(nclibfile.cdl(), h5file.cdl())
            }
        }
    }

    @ParameterizedTest
    @MethodSource("params")
    fun readCharDataCompareNC(filename : String) {
        compareSelectedDataWithClib(filename) { it.datatype == Datatype.CHAR }
    }

}

fun compareCdlWithoutFileType(cdl1: String, cdl2: String) {
    val pos1 = cdl1.indexOf(' ')
    val pos2 = cdl2.indexOf(' ')
    assertEquals(cdl1.substring(pos1), cdl2.substring(pos2))
}