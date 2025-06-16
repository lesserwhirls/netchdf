package com.sunya.netchdf.netcdf3

import com.sunya.netchdf.netcdfClib.NClibFile
import com.sunya.netchdf.openNetchdfFile
import com.sunya.netchdf.testfiles.N3Files
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

// Compare header using cdl(strict) with Netcdf3File and NetcdfClibFile
class N3headerCompare {

    companion object {
        fun files(): Sequence<String> {
            return N3Files.params()
        }
    }

    @Test
    fun checkVersion() {
        files().forEach { filename ->
            openNetchdfFile(filename).use { ncfile ->
                println("${ncfile!!.type()} $filename ")
                assertTrue((ncfile.type().contains("netcdf3")))
            }
        }
    }

    @Test
    fun readN3header() {
        files().forEach { filename ->

            println(filename)
            openNetchdfFile(filename).use { n3file ->
                NClibFile(filename).use { ncfile ->
                    //println("actual = $root")
                    //println("expect = $expect")
                    assertEquals(ncfile.cdl(), n3file!!.cdl())
                    // println(rootClib.cdlString())
                }
            }
        }
    }

}