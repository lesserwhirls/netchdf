package com.sunya.netchdf.hdf5

import com.sunya.cdm.api.Netchdf
import com.sunya.cdm.api.Variable
import com.sunya.cdm.api.chunkConcurrent
import com.sunya.cdm.array.ArrayTyped
import com.sunya.netchdf.testfiles.H5Files
import com.sunya.netchdf.testfiles.testData
import com.sunya.netchdf.testutil.*

import kotlin.test.*

// Sanity check read Hdf5File header, for non-netcdf4 files
class H5readTest {

    init {
        Stats.clear() // problem with concurrent tests
    }

    companion object {
        fun files(): Sequence<String> {
            return H5Files.params()
        }

        fun afterAll() {
            Stats.show()
        }
    }

    @Test
    fun hasLinkName() {
        openH5(testData + "cdmUnitTest/formats/hdf5/aura/MLS-Aura_L2GP-BrO_v01-52-c01_2007d029.he5")
    }

    @Test
    fun opaqueAttribute() {
        openH5(testData + "devcdm/netcdf4/tst_opaque_data.nc4")
    }

    @Test
    fun groupHasCycle() {
        openH5(testData + "cdmUnitTest/formats/hdf5/groupHasCycle.h5")
    }

    @Test
    fun timeIterateConcurrent() {
        // readH5(testData + "devcdm/hdf5/zip.h5", "/Data/Compressed_Data")
        readH5concurrent(testData + "cdmUnitTest/formats/hdf5/StringsWFilter.h5", "/observation/matrix/data")
    }

    @Test
    fun timeIterateProblem() {
        compareNetchIterate(testData + "cdmUnitTest/formats/hdf5/xmdf/mesh_datasets.h5", "/2DMeshModule/mesh/Datasets/velocity_(64)/Mins")
    }

    @Test
    fun testEos() {
        openH5(testData + "cdmUnitTest/formats/hdf5/aura/MLS-Aura_L2GP-BrO_v01-52-c01_2007d029.he5")
    }

    @Test
    fun testNpp() {
        openH5(testData + "netchdf/npp/GATRO-SATMR_npp_d20020906_t0409572_e0410270_b19646_c20090720223122943227_devl_int.h5")
    }

    // ~/dev/github/netcdf/netchdf:$ h5dump /home/all/testdata/devcdm/netcdf4/tst_solar_cmp.nc
    //HDF5 "/home/all/testdata/devcdm/netcdf4/tst_solar_cmp.nc" {
    //GROUP "/" {
    //   ATTRIBUTE "my_favorite_wind_speeds" {
    //      DATATYPE  "/wind_vector"
    //      DATASPACE  SIMPLE { ( 3 ) / ( 3 ) }
    //      DATA {
    //      (0): {
    //            13.3,
    //            12.2
    //         },
    //      (1): {
    //            13.3,
    //            12.2
    //         },
    //      (2): {
    //            13.3,
    //            12.2
    //         }
    //      }
    //   }
    //   DATATYPE "wind_vector" H5T_COMPOUND {
    //      H5T_IEEE_F32LE "u";
    //      H5T_IEEE_F32LE "v";
    //   }
    //}
    //}
    @Test
    fun testIsNetcdf() { // why is this not isNetcdf? Because theres nothing it in to show that it is.
        val filename = testData + "devcdm/netcdf4/tst_solar_cmp.nc"
        Hdf5File(filename).use { h5file ->
            println(h5file.type())
            println(h5file.cdl())
        }
    }

    @Test
    fun testReference() {
        openH5(testData + "cdmUnitTest/formats/hdf5/msg/test.h5")
    }

    ///////////////////////////////////////////////////////////////////////////////////

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

    //@Test
    fun testReadIterate() {
        files().forEach { filename ->
            compareNetchIterate(filename, null)
        }
    }

    //@Test
    fun testReadConcurrent() {
        files().forEach { filename ->
            readH5concurrent(filename, null)
        }
    }

    /////////////////////////////////////////////////////////

    fun openH5(filename: String, varname : String? = null) {
        println("=================")
        println(filename)
        Hdf5File(filename).use { h5file ->
            println(h5file.cdl())
            h5file.rootGroup().allVariables().forEach { println("  ${it.fullname()}") }

            if (varname != null) {
                val h5var = h5file.rootGroup().allVariables().find { it.fullname() == varname } ?: throw RuntimeException("cant find $varname")
                val h5data = h5file.readArrayData(h5var)
                println(" $varname = $h5data")
            }
        }
    }

    fun readH5concurrent(filename: String, varname : String? = null) {
        Hdf5File(filename).use { myfile ->
            println("${myfile.type()} $filename ${myfile.size / 1000.0 / 1000.0} Mbytes")
            var countChunks = 0
            if (varname != null) {
                val myvar = myfile.rootGroup().allVariables().find { it.fullname() == varname } ?: throw RuntimeException("cant find $varname")
                countChunks +=  testOneVarConcurrent(myfile, myvar)
            } else {
                myfile.rootGroup().allVariables().forEach { it ->
                    if (it.datatype.isNumber) {
                        countChunks += testOneVarConcurrent(myfile, it)
                    }
                }
            }
            if (countChunks > 0) {
                println("${myfile.type()} $filename ${myfile.size / 1000.0 / 1000.0} Mbytes chunks = $countChunks")
            }
        }
    }

    fun testOneVarConcurrent(myFile: Netchdf, myvar: Variable<*>) : Int {
        val filename = myFile.location().substringAfterLast('/')
        sum = AtomicDouble(0.0)
        var countChunks = 0
        val time1 = measureNanoTime {
            val chunkIter = myFile.chunkIterator(myvar)
            for (pair in chunkIter) {
                // println(" ${pair.section} = ${pair.array.shape.contentToString()}")
                    sumValues(pair.array)
                countChunks++
            }
        }
        val sum1 = sum.get()
        Stats.of("serialSum", filename, "chunk").accum(time1, countChunks)

        sum.set(0.0)
        val time2 = measureNanoTime {
            myFile.chunkConcurrent(myvar, null) { sumValues(it.array) }
        }
        val sum2 = sum.get()
        Stats.of("concurrentSum", filename, "chunk").accum(time2, countChunks)

        sum.set(0.0)
        val time3 = measureNanoTime {
            val arrayData = myFile.readArrayData(myvar, null)
                sumValues(arrayData)
        }
        val sum3 = sum.get()
        Stats.of("regularSum", filename, "chunk").accum(time3, countChunks)

        /* if (sum1.isFinite() && sum2.isFinite() && sum3.isFinite()) {
            assertTrue(nearlyEquals(sum1, sum2), "$sum1 != $sum2 sum2")
            assertTrue(nearlyEquals(sum1, sum3), "$sum1 != $sum3 sum3")
        }

         */
        return countChunks
    }

    var sum = AtomicDouble(0.0)
    fun sumValues(array : ArrayTyped<*>) {
        if (!array.datatype.isNumber or true) return
        for (value in array) {
            val number = (value as Number)
            val numberd : Double = number.toDouble()
            if (numberd.isFinite()) {
                sum.getAndAdd(numberd)
            }
        }
    }

}