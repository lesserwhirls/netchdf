package com.sunya.netchdf.testfiles

import com.sunya.netchdf.testutils.testData
import com.sunya.netchdf.testutils.testFilesIn

class N4Files {

    companion object {
        fun files(): Iterator<String> {
            val stream4 =
                testFilesIn(testData + "devcdm/netcdf4")
                    .addNameFilter { name -> !name.endsWith("tst_grps.nc4") } // nested group typedefs
                    .addNameFilter { name -> !name.endsWith("UpperDeschutes_t4p10_swemelt.nc") } // see KnownProblems
                    .build()

            val moar4 =
                testFilesIn(testData + "cdmUnitTest/formats/netcdf4")
                    .addNameFilter { name -> !name.endsWith("compound-attribute-test.nc") } // bug in clib
                    .withRecursion()
                    .build()

            return (stream4 + moar4).iterator()
        }
    }
}