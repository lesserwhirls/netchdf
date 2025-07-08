package com.sunya.netchdf5.hdf5

import com.sunya.netchdf.*
import com.sunya.netchdf.testfiles.H5Files
import com.sunya.netchdf.testutil.readNetchdfData

import kotlin.test.*

// Sanity check read Hdf5File header, for non-netcdf4 files
class H5readTestJvm {

    companion object {
        fun files(): Sequence<String> {
            return H5Files.params()
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////
    // TODO this is a single test, not concurrent as it was with ParameterizedTest
    //    Anyway still dont have parallel tests

    @Test
    fun testOpenH5() {
        files().forEach { filename ->
            openH5(filename, null)
        }
    }

    @Test
    fun testReadNetchdfData() {
        files().forEach { filename ->
            readNetchdfData(filename)
        }
    }

    fun openH5(filename: String, varname : String? = null) {
        println("=================")
        println(filename)
        openNetchdfFileWithFormat(filename, NetchdfFileFormat.HDF5).use { h5file ->
            if (h5file == null) {
                println("Cant open $filename")
            } else {
                println(h5file.cdl())
                h5file.rootGroup().allVariables().forEach { println("  ${it.fullname()}") }

                if (varname != null) {
                    val h5var = h5file.rootGroup().allVariables().find { it.fullname() == varname }
                        ?: throw RuntimeException("cant find $varname")
                    val h5data = h5file.readArrayData(h5var)
                    println(" $varname = $h5data")
                }
            }
        }
    }

}