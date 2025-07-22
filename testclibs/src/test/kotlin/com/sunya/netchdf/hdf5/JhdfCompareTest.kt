package com.sunya.netchdf.hdf5

import com.sunya.netchdf.CompareCdmWithClib
import com.sunya.netchdf.compareDataWithClib
import com.sunya.netchdf.testfiles.JhdfFiles
import kotlin.test.Test

class JhdfCompareTest {
    companion object {
        @JvmStatic
        fun files(): Iterator<String> {
            return JhdfFiles.files()
        }
    }

    /* init {
        FilterRegistrar.registerFilter(Lz4Filter())
        FilterRegistrar.registerFilter(LzfFilter())
        FilterRegistrar.registerFilter(BitShuffleFilter())
    } */

    // H5Aread VlenString return -1
    //java.io.IOException: H5Aread VlenString return -1
    //	at com.sunya.netchdf.hdf5Clib.H5CbuilderKt.checkErr(H5Cbuilder.kt:720)
    //	at com.sunya.netchdf.hdf5Clib.H5Cbuilder.readVlenStrings$testclibs(H5Cbuilder.kt:645)
    @Test
    fun problem() {
        val filename = "../core/src/commonTest/data/jhdf/globalheaps_test.hdf5"
        CompareCdmWithClib(filename, showCdl = true, showCompare = true)
        compareDataWithClib(filename)
    }

    //  lots of link messages we dont handle
    @Test
    fun problem2() {
        val filename = "../core/src/commonTest/data/jhdf/test_file.hdf5"
        CompareCdmWithClib(filename, showCdl = true, showCompare = true)
        compareDataWithClib(filename)
    }

    @Test
    fun compareNetchdf() {
        files().forEach { filename ->
            println(filename)
            try {
                CompareCdmWithClib(filename, false, false)
            } catch (e: Throwable) {
                CompareCdmWithClib(filename, true, true)
                e.printStackTrace()
            }
        }
    }

    @Test
    fun compareDataWithClib() {
        files().forEach { filename ->
            println(filename)
            try {
                compareDataWithClib(filename)
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }
}