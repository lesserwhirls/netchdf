package com.sunya.netchdf.testdata

class NetchdfExtraFiles {

    companion object {
        fun params(excludeClibFails: Boolean): Sequence<String> {
            val builder = testFilesIn(testData + "netchdf")
                .withRecursion()
                .withPathFilter { p ->
                    !(p.toString().contains("exclude") or
                            p.toString().contains("gilmore/data.nc") or
                            p.toString().contains("austin/H12007_1m_MLLW_1of6.bag") or
                            p.toString().contains("SATMS_justdims_npp_d20120619_t1121416_e1122133_b03335_c20120619200237705890_noaa_ops.h5")
                    )
                }
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

            /*
            /home/all/testdata/netchdf/esben/level2_MSG2_8bit_VISIR_STD_20091005_0700.H5
            /home/all/testdata/netchdf/rink/I3A_VHR_22NOV2007_0902_L1B_STD.h5
            /home/all/testdata/netchdf/austin/H12007_1m_MLLW_1of6.bag
            /home/all/testdata/netchdf/tomas/S3A_OL_CCDB_CHAR_AllFiles.20101019121929_1.nc4
             */
            if (excludeClibFails) {
                builder.addNameFilter { name -> !name.lowercase().contains("_npp_") }          // disagree with C library
                    // .addNameFilter { name -> !name.endsWith("level2_MSG2_8bit_VISIR_STD_20091005_0700.H5") } // ditto
                    // .addNameFilter { name -> !name.endsWith("I3A_VHR_22NOV2007_0902_L1B_STD.h5") }          // ditto
                    .addNameFilter { name -> !name.endsWith("H12007_1m_MLLW_1of6.bag") }                    // ditto
                    // .addNameFilter { name -> !name.endsWith("S3A_OL_CCDB_CHAR_AllFiles.20101019121929_1.nc4") } // ditto
            }

            return builder.build()
        }
    }
}