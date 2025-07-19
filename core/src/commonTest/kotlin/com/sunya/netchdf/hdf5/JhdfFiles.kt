package com.sunya.netchdf.hdf5

import com.sunya.netchdf.testutil.testData
import com.sunya.netchdf.testutil.testFilesIn

class JhdfFiles {
    companion object {
        fun files(): Iterator<String> {
            val jhdf =
                testFilesIn("$testData/jhdf")
                    .addNameFilter { name -> !name.contains("float_special") }
                    .addNameFilter { name -> !name.endsWith("isssue-523.hdf5") }
                    .addNameFilter { name -> !name.endsWith("bitshuffle_datasets.hdf5") }
                    .addNameFilter { name -> !name.endsWith("lz4_datasets.hdf5") }
                    .addNameFilter { name -> !name.endsWith("test_compressed_chunked_datasets_earliest.hdf5") }
                    .addNameFilter { name -> !name.endsWith("test_compressed_chunked_datasets_latest.hdf5") }
                    .build()
            return jhdf.iterator()
        }
    }
}