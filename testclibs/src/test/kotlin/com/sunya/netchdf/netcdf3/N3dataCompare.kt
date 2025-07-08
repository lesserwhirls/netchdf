package com.sunya.netchdf.netcdf3

import com.sunya.cdm.api.Datatype
import com.sunya.cdm.api.SectionPartial
import com.sunya.netchdf.compareDataWithClib
import com.sunya.netchdf.compareSelectedDataWithClib
import com.sunya.netchdf.hdf5.showDetail
import com.sunya.netchdf.testfiles.N3Files
import com.sunya.netchdf.testfiles.testData
import kotlin.test.*
import java.util.*

// Compare data reading for the same file with Netcdf3File and NetcdfClibFile
class N3dataCompare {

    companion object {
        fun files(): Sequence<String> {
            return N3Files.params()
        }
    }

/*
    @Test
    fun netcdf3() {
        val filename = testData + "cdmUnitTest/formats/netcdf3/awips.nc"
        if (showDetail) println("===============================================")
        if (showDetail) {
            Netcdf3File(filename).use { ncfile ->
                println("${ncfile.type()} $filename ")
                println("${ncfile.cdl()} ")
            }
        }
        readData(filename, "uw", SectionPartial.fromSpec("4, 39, 55, 74"))
        readData(filename, "uw", SectionPartial.fromSpec("0:4,13:26,18:37,25:49"))
        readData(filename, "vw", SectionPartial.fromSpec("4, 39, 55, 74"))
        readData(filename, "vw", SectionPartial.fromSpec("0:4,13:26,18:37,25:49"))
        readData(filename, "uw", SectionPartial.fromSpec(":,:,:,25"))
    }

 */

    @Test
    fun cdf5() {
        val filename = testData + "recent/cdf5/jays_DOMAIN000.nc"
        compareDataWithClib(filename, null)
    }

    // netcdf3 /home/all/testdata/netchdf/csiro/sixCells.nc 1.80 Mbytes
    // float lat[0:1] = 2 elems
    // this is the case where vsize=20, but calculated=8.
    // the file looks like it did use 20 bytes, but hard to know as there's only 2 records.
    // Im going to remove it from the test files (placed in exclude)
    @Test
    fun calcRecordSize() {
        compareDataWithClib(testData + "netchdf/csiro/sixCellsc.nc", "lat") // , "cellId")
    }

    @Test
    fun vsizeCdf1() {
        compareDataWithClib(testData + "devcdm/netcdf3/tst_small_classic.nc", null) // , "cellId")
    }

    @Test
    fun vsizeCdf2() {
        compareDataWithClib(testData + "devcdm/netcdf3/tst_small_64bit.nc", null) // , "cellId")
    }

    @Test
    fun problem() {
        compareDataWithClib(testData + "devcdm/netcdf3/tst_small_classic.nc", null)
    }

    @Test
    fun readN3dataCompareNC() {
        files().forEach { filename ->
            compareDataWithClib(filename, null)
        }
    }

    @Test
    fun readCharDataCompareNC() {
        files().forEach { filename ->
            compareSelectedDataWithClib(filename) { it.datatype == Datatype.CHAR || it.datatype == Datatype.STRING }
        }
    }

}