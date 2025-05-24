package com.sunya.netchdf.netcdf4

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import com.sunya.netchdf.netcdfClib.NClibFile
import com.sunya.netchdf.testdata.N4Files
import java.util.stream.Stream
import kotlin.test.Test
import kotlin.test.assertTrue

class N4versionTest {

    companion object {
        @JvmStatic
        fun params(): Stream<Arguments> {
            return N4Files.params()
        }
    }

    @ParameterizedTest
    @MethodSource("params")
    fun checkVersion(filename: String) {
        NClibFile(filename).use { ncfile ->
            println("${ncfile.type()} $filename ")
            assertTrue((ncfile.type() == "NC_FORMAT_NETCDF4") or (ncfile.type() == "NC_FORMAT_NETCDF4_CLASSIC"))
        }
    }

    @Test
    fun errMemorySegment() {
        val filename = "/home/all/testdata/devcdm/netcdf4/attributeStruct.nc"
        NClibFile(filename).use { ncfile ->
            println("${ncfile.type()} $filename ")
            assertTrue((ncfile.type() == "NC_FORMAT_NETCDF4") or (ncfile.type() == "NC_FORMAT_NETCDF4_CLASSIC"))
        }
    }

}