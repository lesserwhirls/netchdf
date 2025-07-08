package com.sunya.netchdf.testfiles

import com.sunya.netchdf.testutils.testFilesIn

class JhdfFiles {
    companion object {
        fun files(): Iterator<String> {
            val jhdf =
                testFilesIn("/home/stormy/dev/github/netcdf/jhdf/jhdf/src/test/resources/hdf5")
                    .build()
            return jhdf.iterator()
        }
    }
}