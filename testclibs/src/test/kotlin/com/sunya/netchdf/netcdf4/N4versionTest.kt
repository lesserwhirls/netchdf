package com.sunya.netchdf.netcdf4

import com.sunya.netchdf.netcdfClib.NClibFile
import com.sunya.netchdf.testfiles.N4Files
import kotlin.test.Test
import kotlin.test.assertTrue

class N4versionTest {

    companion object {
        @JvmStatic
        fun files(): Iterator<String> {
            return N4Files.files()
        }
    }

    @Test
    fun errMemorySegment() {
        val filename = "/home/all/testdata/devcdm/netcdf4/attributeStruct.nc"
        NClibFile(filename).use { ncfile ->
            println("${ncfile.type()} $filename ")
            assertTrue((ncfile.type() == "netcdf4") or (ncfile.type() == "netcdf3"), "${ncfile.type()}")
        }
    }

    ///////////////////////////////////////////////////////

    @Test
    fun checkVersion() {
        files().forEach { filename ->
            NClibFile(filename).use { ncfile ->
                println("${ncfile.type()} $filename ")
                assertTrue((ncfile.type() == "netcdf3") or (ncfile.type() == "netcdf4"), "${ncfile.type()}")
            }
        }
    }

}