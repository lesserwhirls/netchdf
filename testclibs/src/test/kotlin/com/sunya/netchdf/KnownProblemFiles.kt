@file:OptIn(InternalLibraryApi::class)

package com.sunya.netchdf

import com.sunya.cdm.util.InternalLibraryApi
import com.sunya.netchdf.hdf4.Hdf4File
import com.sunya.netchdf.hdf5Clib.compareDataWithHdf5Clib
import com.sunya.netchdf.netcdf4.compareH5andNclib
import com.sunya.netchdf.testutils.testData
import kotlin.test.*
import kotlin.test.assertTrue

val problemDir = testData + "/exclude/"

class KnownProblemFiles {

    // superblockIsOffsetNPP.h5 is opened as a Netcdf4 file, but same problem  with hdf5
    // not picking up missing value
    //  *** FAIL comparing data for variable = ubyte Granule [Granule]
    //255 != 0 at idx = 1
    //255 != 0 at idx = 2
    //255 != 0 at idx = 3
    //255 != 0 at idx = 4
    //255 != 0 at idx = 5
    //255 != 0 at idx = 6
    // *** count values differ = 6 same = 1
    @Test
    fun superblockIsOffsetNPP() {
        val filename = problemDir + "superblockIsOffsetNPP.h5"
        compareH5andNclib(filename, compareData = true)
        CompareCdmWithClib(filename, true)
        compareDataWithClib(filename, varname = "Granule")
    }

    @Test
    fun problemTruncated() {
        CompareCdmWithClib(problemDir + "OMI-Aura_L2-OMTO3_2009m0829t1219-o27250_v003-2009m0829t175727.he5")
    }

    // H5C cant find dataset reference for Attribute(orgName=PALETTE, datatype=reference, values=[-1])
    @Test
    fun attReference() {
        val filename = problemDir + "RAD_NL25_PCP_NA_200804110600.h5"
        CompareCdmWithClib(filename)
        compareDataWithClib(filename)
    }

    // tried to add duplicate dimension 'lv1'
    // ncdump gives "NetCDF: Invalid dimension ID or name". assumed damaged file
    @Test
    fun dupDimensions() {
        val filename = problemDir + "gilmore/data.nc"
        showNetchdfHeader(filename)
        CompareCdmWithClib(filename)
        compareDataWithClib(filename)
    }

}