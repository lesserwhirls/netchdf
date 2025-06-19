package com.sunya.netchdf.testfiles

class SuperblockFiles {

    companion object {
        fun files(subdir: String): Sequence<String> {
            val devcdm =
                testFilesIn(testData + "netcdf-c_hdf5_superblocks/netcdf-c-test-files/$subdir/")
                    .withRecursion()
                    .addNameFilter{ it.endsWith(".nc") }
                    .build()

            return devcdm
        }
    }
}