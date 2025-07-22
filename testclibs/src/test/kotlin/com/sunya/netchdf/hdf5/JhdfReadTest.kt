package com.sunya.netchdf.hdf5

import com.sunya.netchdf.CompareCdmWithClib
import com.sunya.netchdf.compareDataWithClib
import com.sunya.netchdf.testfiles.JhdfFiles
import kotlin.test.Test

class JhdfReadTest {
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

    @Test
    fun problem() {
        CompareCdmWithClib("../core/src/commonTest/data/jhdf/compound_datasets_earliest.hdf5", showCdl = true, showCompare = true)
        // CompareNetchdf("../core/src/commonTest/data/jhdf/compound_datasets_earliest.hdf5", showCdl= true)
    }

    @Test
    fun problem2() {
        CompareCdmWithClib("../core/src/commonTest/data/jhdf/compound_datasets_latest.hdf5", showCdl= true, showCompare = true)
    }

    @Test
    fun failure() {
        CompareCdmWithClib("../core/src/commonTest/data/jhdf/globalheaps_test.hdf5", showCdl= true, showCompare = true)
    }

    @Test
    fun compareNetchdf() {
        files().forEach { filename ->
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
            try {
                compareDataWithClib(filename)
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }
}