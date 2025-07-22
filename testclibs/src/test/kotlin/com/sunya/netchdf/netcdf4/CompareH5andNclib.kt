package com.sunya.netchdf.netcdf4

import com.sunya.netchdf.NetchdfFileFormat
import com.sunya.netchdf.compareDataWithClib
import com.sunya.netchdf.compareNetcdfData
import com.sunya.netchdf.compareNetchdfCdm
import com.sunya.netchdf.netcdfClib.NClibFile
import com.sunya.netchdf.openNetchdfFileWithFormat
import com.sunya.netchdf.testfiles.N4Files
import com.sunya.netchdf.testutils.testData
import kotlin.test.Test
import kotlin.use

class CompareH5andNclib {

    companion object {
        @JvmStatic
        fun files(): Iterator<String> {
            // 10 of 114 fail, because we compare with netcdf4 instead of hdf5 c library

            return N4Files.files()
        }
    }


    @Test
    fun problem() {
        compareH5andNclib(testData + "netchdf/tomas/S3A_OL_CCDB_CHAR_AllFiles.20101019121929_1.nc4")
        compareDataWithClib(testData + "netchdf/tomas/S3A_OL_CCDB_CHAR_AllFiles.20101019121929_1.nc4")
    }

    @Test
    fun compareH5andNclib() {
        files().forEach { filename ->
            compareH5andNclib(filename)
        }
    }
}

fun compareH5andNclib(filename: String, compareData : Boolean = false) {
    println("===================================================")
    openNetchdfFileWithFormat(filename, NetchdfFileFormat.HDF5).use { h5file ->
        println("Opened as ${h5file!!.javaClass.simpleName}")
        println("${h5file!!.type()} $filename ")
        println("\n${h5file.cdl()}")

        NClibFile(filename).use { nclibfile ->
            println("NClibFile = ${nclibfile.cdl()}")
            compareNetchdfCdm(nclibfile, h5file)
            compareNetcdfData(h5file, nclibfile, varname = null)
        }
    }
}