package com.sunya.netchdf.netcdf3

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.util.*
import java.util.stream.Stream
import kotlin.test.assertEquals
import kotlin.test.assertTrue

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import com.sunya.netchdf.testdata.testData
import com.sunya.netchdf.testdata.testFilesIn
import java.io.File

// Cannot run program "ncdump": error=2, No such file or directory
// doesnt work because of differences in the value printout.
// need to compare the parsed cdl, or maybe the xml?
@Disabled
class N3ncdumpTest {
    val ncdumpp = "/home/stormy/install/netcdf4/bin/ncdump"
    companion object {
        @JvmStatic
        fun params(): Stream<Arguments> {
            val stream3 =
                testFilesIn(testData + "devcdm/netcdf3")
                    .build()
            return stream3
        }
    }

    @Test
    fun problem() {
        compareN3header(testData + "devcdm/netcdf3/testWriteFill.nc")
    }

    @ParameterizedTest
    @MethodSource("params")
    fun compareN3header(filename : String) {
        println("=================")
        println(filename)
        val ncdumpOutput = ncdump(filename)
        println("expect = \"$ncdumpOutput\"")
        Netcdf3File(filename).use { ncfile ->
            println("actual = \"${ncfile.cdl()}\"")
            assertEquals(normalize(ncdumpOutput), normalize(ncfile.cdl()))
        }
    }

    fun ncdump(filename : String) : String {
        val file = File("temp")
        ProcessBuilder(ncdumpp, "-h", filename)
            .redirectOutput(ProcessBuilder.Redirect.to(file))
            .start()
            .waitFor()

        return file.readText(Charsets.UTF_8)
    }

    fun normalize(org : String) : String {
        val org2 = org.trimIndent()
        return buildString {
            for (line in org2.lines()) {
                append(line.trim())
                append("\n")
            }
        }
    }

}