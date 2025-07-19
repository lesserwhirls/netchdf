package com.sunya.netchdf.testfiles

import com.sunya.netchdf.testutils.testData
import com.sunya.netchdf.testutils.testFilesIn

class H4Files {

    companion object {
        fun files(): Iterator<String>  {
            val hdfeos2 =
                testFilesIn(testData + "devcdm/hdfeos2")
                    .withRecursion()
                    .build()

            val devcdm = testFilesIn(testData + "devcdm/hdf4")
                .withRecursion()
                .build()

            // remove files that core dump
            // /home/all/testdata/hdf4/eisalt/VHRR-KALPANA_20081216_070002.hdf  // CORE DUMP
            val hdf4NoCore =
                testFilesIn(testData + "hdf4")
                    .withRecursion()
                    //    .withPathFilter { p -> !(p.toString().contains("/eos/"))}
                    // .addNameFilter { name -> !name.endsWith("VHRR-KALPANA_20081216_070002.hdf") }
                    .build()

            return (devcdm + hdfeos2 + hdf4NoCore).iterator()
        }

        fun params2(): Sequence<String> {
            val hdf4 =
                testFilesIn(testData + "hdf4")
                    .withRecursion()
                    .addNameFilter { name -> !name.endsWith(".cdl") }
                    .addNameFilter { name -> !name.endsWith(".jpg") }
                    .addNameFilter { name -> !name.endsWith(".gif") }
                    .addNameFilter { name -> !name.endsWith(".ncml") }
                    .addNameFilter { name -> !name.endsWith(".png") }
                    .addNameFilter { name -> !name.endsWith(".pdf") }
                    .addNameFilter { name -> !name.endsWith(".tif") }
                    .addNameFilter { name -> !name.endsWith(".tiff") }
                    .addNameFilter { name -> !name.endsWith(".txt") }
                    .addNameFilter { name -> !name.endsWith(".xml") }
                    .build()

            val devcdm = testFilesIn(testData + "devcdm/hdf4")
                .withRecursion()
                .build()

            val hdfeos2 =
                testFilesIn(testData + "devcdm/hdfeos2")
                    .withRecursion()
                    .build()

            return devcdm + hdfeos2 + hdf4
        }
    }
}