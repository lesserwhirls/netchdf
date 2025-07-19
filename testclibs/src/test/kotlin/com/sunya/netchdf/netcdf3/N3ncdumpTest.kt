package com.sunya.netchdf.netcdf3

import com.sunya.netchdf.testutils.testData
import com.sunya.netchdf.openNetchdfFile
import com.sunya.netchdf.testutils.testFilesIn
import org.junit.jupiter.api.Disabled
import kotlin.test.*
import kotlin.test.assertEquals

import java.io.File

// Cannot run program "ncdump": error=2, No such file or directory
// doesnt work because of differences in the value printout.
// need to compare the parsed cdl, or maybe the xml?
@Disabled
class N3ncdumpTest {
    val ncdumpp = "/home/stormy/install/netcdf4/bin/ncdump"
    companion object {
        fun files(): Sequence<String> {
            return testFilesIn(testData + "devcdm/netcdf3").build()
        }
    }

    @Test
    fun problem() {
        compareN3header(testData + "devcdm/netcdf3/testWriteFill.nc")
    }

    // @Test
    fun compareN3header(filename : String) {
        println("=================")
        println(filename)
        val ncdumpOutput = ncdump(filename)
        println("expect = \"$ncdumpOutput\"")
        openNetchdfFile(filename).use { ncfile ->
            println("actual = \"${ncfile!!.cdl()}\"")
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