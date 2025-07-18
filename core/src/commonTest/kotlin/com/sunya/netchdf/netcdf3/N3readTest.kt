package com.sunya.netchdf.netcdf3

import com.sunya.cdm.api.Datatype
import com.sunya.netchdf.openNetchdfFile
import com.sunya.netchdf.testutil.testData
import kotlin.test.Test
import kotlin.test.assertEquals

class N3readTest {

    @Test
    fun simple() {
        val filename = testData + "netcdf3/simple_xy.nc"
        println(filename)
        openNetchdfFile(filename).use { myfile ->
            println(myfile!!.cdl())
        }
    }

    @Test
    fun testCharAttribute() {
        val filename = testData + "netcdf3/jays_DOMAIN000.nc"
        openNetchdfFile(filename).use { myfile ->
            println("--- ${myfile!!.type()} $filename ")
            println(myfile.cdl())

            val gatt = myfile.rootGroup().attributes.find { it.name == "texture_fudging" }!!
            println("texture_fudging = $gatt")
            assertEquals(Datatype.Companion.STRING, gatt.datatype)
            assertEquals(1, gatt.values.size)
            assertEquals("No", gatt.values[0])

            val crs = myfile.rootGroup().variables.find { it.name == "crs" }!!
            val att = crs.attributes.find { it.name == "grid_mapping_name" }!!
            assertEquals(Datatype.Companion.STRING, att.datatype)
            assertEquals(1, att.values.size)
            assertEquals("lambert_conformal_conic", att.values[0])
        }
    }
}