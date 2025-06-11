package com.sunya.netchdf

import com.sunya.netchdf.testdata.NetchdfExtraFiles
import kotlin.test.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import com.sunya.netchdf.testdata.testData
import org.junit.jupiter.api.AfterAll
import java.util.stream.Stream

// Compare header using cdl(!strict) with Netchdf and NetcdfClibFile
// mostly fails in handling of types. nclib doesnt pass over all the types.
class NetchdfExtra {

    companion object {
        @JvmStatic
        fun params(): Stream<Arguments> {
            return NetchdfExtraFiles.params(false)
        }

        @JvmStatic
        @AfterAll
        fun afterAll() {
            if (versions.size > 0) {
                val sversions = versions.toSortedMap()
                sversions.keys.forEach{ println("$it = ${sversions[it]!!.size } files") }
                val total = sversions.keys.map{ sversions[it]!!.size }.sum()
                println("total # files = $total")
            }
            Stats.show()
        }

        private val versions = mutableMapOf<String, MutableList<String>>()

        const val topdir = "$testData/netchdf/"
    }

    // npp filers: superblock at file offset; reference data type
    @Test
    fun h5npp() {
        NetchdfTest.showData = false
        readNetchdfData(topdir + "npp/VCBHO_npp_d20030125_t084955_e085121_b00015_c20071213022754_den_OPS_SEG.h5")
        readNetchdfData(topdir + "npp/GATRO-SATMR_npp_d20020906_t0409572_e0410270_b19646_c20090720223122943227_devl_int.h5")
    }

    @Test
    fun problem() {
        val filename = topdir + "new/gdas.t12z.sfcf006.nc"
        readNetchdfData(filename, null)
    }


    ///////////////////////////////////////////////////////
    @ParameterizedTest
    @MethodSource("params")
    fun checkVersion(filename: String) {
        openNetchdfFile(filename).use { ncfile ->
            if (ncfile == null) {
                println("Not a netchdf file=$filename ")
                return
            }
            println("${ncfile.type()} $filename ")
            val paths = versions.getOrPut(ncfile.type()) { mutableListOf() }
            paths.add(filename)
        }
    }

    @ParameterizedTest
    @MethodSource("params")
    fun readNetchdfData(filename: String) {
        readNetchdfData(filename, null)
    }

}