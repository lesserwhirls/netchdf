package com.sunya.netchdf.testfiles

import com.sunya.netchdf.testutils.testData
import com.sunya.netchdf.testutils.testFilesIn

class SuperblockFiles {

    companion object {
        fun files(subdir: String): Iterator<String> {
            val devcdm =
                testFilesIn(testData + "netcdf-c_hdf5_superblocks/netcdf-c-test-files/$subdir/")
                    .withRecursion()
                    .addNameFilter{ it.endsWith(".nc") }
                    .addNameFilter { name -> !name.endsWith("nc_test4__tst_xplatform2_3.nc") }
                    .addNameFilter { name -> !name.endsWith("nc_test4__tst_xplatform2_4.nc") }
                    .build()

            return devcdm.iterator()
        }
    }
}