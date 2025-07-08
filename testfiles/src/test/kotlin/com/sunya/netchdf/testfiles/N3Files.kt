package com.sunya.netchdf.testfiles

import com.sunya.netchdf.testutils.testData
import com.sunya.netchdf.testutils.testFilesIn

class N3Files {

    companion object {
        fun files(): Iterator<String> {
            val stream3 =
                testFilesIn(testData + "devcdm/netcdf3")
                    .build()

            val moar3 =
                testFilesIn(testData + "cdmUnitTest/formats/netcdf3")
                    .withPathFilter { p -> !p.toString().contains("exclude") }
                    .addNameFilter { name -> !name.endsWith("perverse.nc") } // too slow
                    .withRecursion()
                    .build()

            val cdf5 = sequenceOf(testData + "recent/cdf5/jays_DOMAIN000.nc")

            return (stream3 + moar3 + cdf5).iterator()
        }
    }
}