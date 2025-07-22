package com.sunya.netchdf.hdf5

import com.sunya.cdm.api.Datatype
import com.sunya.netchdf.*
import com.sunya.netchdf.netcdfClib.NClibFile
import com.sunya.netchdf.testfiles.H5Files
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

            return H5Files.files()
        }
    }

    @Test
    fun testNewLibrary() {
        val filename = testData + "netchdf/haberman/iso.h5"
        CompareCdmWithClib(filename, showCdl = true)
        compareDataWithClib(filename)
    }

    @Test
    fun problemChars() {
        val filename = testData + "cdmUnitTest/formats/netcdf4/files/c0_4.nc4"
        CompareCdmWithClib(filename)
        compareDataWithClib(filename, showCdl = true, varname = "c213")
    }

    @Test
    fun problemLibraryVersion() {
        val filename = testData + "devcdm/netcdf4/tst_solar_cmp.nc"
        CompareCdmWithClib(filename, showCdl = true)
        compareDataWithClib(filename)
    }

    @Test
    fun problem() {
        val filename = testData + "devcdm/netcdf4/IntTimSciSamp.nc"
        CompareCdmWithClib(filename, showCdl = true)
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
    fun compareNetchdf() {
        files().forEach { filename ->
            CompareCdmWithClib(filename)
        }
    }

    @Test
    fun testCompareDataWithClib() {
        files().forEach { filename ->
            compareDataWithClib(filename)
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