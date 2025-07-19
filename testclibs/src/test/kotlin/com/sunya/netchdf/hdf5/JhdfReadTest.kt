package com.sunya.netchdf.hdf5

import com.sunya.netchdf.compareCdlWithClib
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
        compareCdlWithClib("../core/src/commonTest/data/jhdf/compound_datasets_earliest.hdf5", showCdl= true)
    }

    @Test
    fun problem2() {
        compareCdlWithClib("../core/src/commonTest/data/jhdf/compound_datasets_latest.hdf5", showCdl= true)
    }

    @Test
    fun failure() {
        compareCdlWithClib("../core/src/commonTest/data/jhdf/test_compound_scalar_attribute.hdf5", showCdl= true)
    }

    @Test
    fun compareCdlWithClib() {
        files().forEach { filename ->
            try {
                compareCdlWithClib(filename)
            } catch (e: Throwable) {
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