package com.sunya.netchdf

import com.sunya.netchdf.testfiles.JhdfFiles
import com.sunya.netchdf.testutils.readNetchdfData
import com.sunya.netchdf.testutils.testData
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.Test

class JhdfReadTest {

    companion object {
        @JvmStatic
        fun files(): Iterator<String> {
            return JhdfFiles.files()
        }
    }

    @Test
    fun problem() {
        val filename = "/home/stormy/dev/github/netcdf/jhdf/jhdf/src/test/resources/hdf5/test_scalar_empty_datasets_earliest.hdf5"
        println(filename)
        readNetchdfData(filename, null, null, true, false)
    }

    @ParameterizedTest
    @MethodSource("files")
    fun testReadN3data(filename: String) {
        println(filename)
        readNetchdfData(filename, null, null, true, false)
    }

}