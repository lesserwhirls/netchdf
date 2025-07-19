package com.sunya.netchdf.testfiles

import com.sunya.netchdf.testutils.testFilesIn

const val jhdfTestDir = "../core/src/commonTest/data/jhdf/"

class JhdfFiles {
    companion object {
        fun files(): Iterator<String> {
            val jhdf =
                testFilesIn(jhdfTestDir)
                    .addNameFilter { name -> !name.contains("float_special") }
                    .addNameFilter { name -> !name.endsWith("isssue-523.hdf5") }
                    .addNameFilter { name -> !name.endsWith("test_compressed_chunked_datasets_earliest.hdf5") }
                    .addNameFilter { name -> !name.endsWith("test_compressed_chunked_datasets_latest.hdf5") }
                    .addNameFilter { name -> !name.endsWith("test_chunked_datasets_earliest.hdf5") }
                    .addNameFilter { name -> !name.endsWith("test_chunked_datasets_latest.hdf5") }
                    .addNameFilter { name -> !name.endsWith("external_link.hdf5") }
                    .build()
            return jhdf.iterator()
        }
    }
}