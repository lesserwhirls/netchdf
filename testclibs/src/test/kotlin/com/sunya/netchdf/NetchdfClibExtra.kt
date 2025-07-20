package com.sunya.netchdf

import com.sunya.netchdf.testfiles.NetchdfExtraFiles
import com.sunya.netchdf.testutils.testData
import com.sunya.netchdf.testutils.Stats
import kotlin.test.*

// Compare header using cdl(!strict) with Netchdf and NetcdfClibFile
// mostly fails in handling of types. nclib doesnt pass over all the types.
class NetchdfClibExtra {

    companion object {
        @JvmStatic
        fun files(): Iterator<String> {
            return NetchdfExtraFiles.files(false)
        }

        fun afterAll() {
            if (versions.size > 0) {
                versions.keys.forEach{ println("$it = ${versions[it]!!.size } files") }
            }
            Stats.show()
        }

        private val versions = mutableMapOf<String, MutableList<String>>()
    }

    /*
    I wonder if npp has a group cycle?
    nc_inq_natts return -107 = NetCDF: Can't open HDF5 attribute g4.grpid= 65540
    then on close:
    HDF5: infinite loop closing library
      L,D_top,G_top,T_top,F,P,P,FD,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E
     */
    @Test
    fun problemNPP() {
        val filename = testData + "netchdf/npp/VCBHO_npp_d20030125_t084955_e085121_b00015_c20071213022754_den_OPS_SEG.h5"
        CompareNetchdf(filename)
        readNetchdfData(filename, null)
        compareDataWithClib(filename)
    }

    @Test
    fun unsolved2() {
        val filename = testData + "netchdf/tomas/S3A_OL_CCDB_CHAR_AllFiles.20101019121929_1.nc4"
        // showMyHeader(filename)
        showNcHeader(filename)
        // showMyData(filename)
        CompareNetchdf(filename)
        //readDataCompareNC(filename)
    }

    @Test
    fun problem() {
        val filename = testData + "netchdf/bird/watlev_NOAA.F.C_IKE_VIMS_3D_WITHWAVE.nc"
        CompareNetchdf(filename)
        readNetchdfData(filename, null)
        compareDataWithClib(filename)
    }


    ///////////////////////////////////////////////////////
    @Test
    fun checkVersion() {
        files().forEach { filename ->
            openNetchdfFile(filename).use { ncfile ->
                if (ncfile == null) {
                    println("Not a netchdf file=$filename ")
                    return
                }
                println("${ncfile.type()} $filename ")
                val paths = versions.getOrPut(ncfile.type()) { mutableListOf() }
                paths.add(filename)
            }
        }
    }

    @Test
    fun testCompareCdlWithClib() {
        files().forEach { filename ->
            CompareNetchdf(filename, showCdl = true)
        }
    }

    @Test
    fun readNetchdfData() {
        files().forEach { filename ->
            readNetchdfData(filename, null)
        }
    }

    @Test
    fun testCompareDataWithClib() {
        files().forEach { filename ->
            compareDataWithClib(filename)
        }
    }

}