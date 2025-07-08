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
                    .build()

            return devcdm.iterator()
        }
    }
}