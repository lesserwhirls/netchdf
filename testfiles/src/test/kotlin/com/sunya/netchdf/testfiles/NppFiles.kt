package com.sunya.netchdf.testfiles

import com.sunya.netchdf.testutils.testData
import com.sunya.netchdf.testutils.testFilesIn


class NppFiles {

    companion object {
        fun files(): Sequence<String> {
            val builder = testFilesIn(testData + "netchdf/npp")
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
            return builder.build()
        }
    }
}