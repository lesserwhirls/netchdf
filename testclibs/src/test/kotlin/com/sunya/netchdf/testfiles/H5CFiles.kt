package com.sunya.netchdf.testfiles

import com.sunya.netchdf.testutils.testData
import com.sunya.netchdf.testutils.testFilesIn

class H5CFiles {

    companion object {
        fun files(): Iterator<String> {

            val starting = sequenceOf(
                testData + "cdmUnitTest/formats/hdf5/grid_1_3d_xyz_aug.h5",
                testData + "cdmUnitTest/formats/hdf5/StringsWFilter.h5",
                testData + "cdmUnitTest/formats/hdf5/msg/test.h5",
                testData + "cdmUnitTest/formats/hdf5/extLink/extlink_source.h5 ",
            )

            val cdmUnitTest =
                testFilesIn(testData + "cdmUnitTest/formats/hdf5")
                    .withPathFilter { p -> !p.toString().contains("exclude") && !p.toString().contains("problem")
                            && !p.toString().contains("npoess")}
                    .addNameFilter { name -> !name.contains("OMI-Aura") }
                    .addNameFilter { name -> !name.contains("IASI") }
                    .addNameFilter { name -> !name.endsWith("groupHasCycle.h5") } // /home/all/testdata/cdmUnitTest/formats/hdf5/groupHasCycle.h5
                    .addNameFilter { name -> !name.endsWith("extlink_source.h5") }
                    .addNameFilter { name -> !name.endsWith(".xml") }
                    .withRecursion()
                    .build()

            val devcdm =
                testFilesIn(testData + "devcdm/hdf5")
                    .withRecursion()
                    .build()

            return (devcdm + cdmUnitTest).iterator()
        }
    }
}