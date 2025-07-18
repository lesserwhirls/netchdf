package com.sunya.netchdf.hdf5

import com.sunya.cdm.api.CompoundTypedef
import com.sunya.cdm.api.Datatype
import com.sunya.cdm.api.EnumTypedef
import com.sunya.cdm.api.convertEnums
import com.sunya.cdm.array.ArrayStructureData
import com.sunya.cdm.array.ArrayTyped
import com.sunya.netchdf.openNetchdfFile
import com.sunya.netchdf.testutil.readNetchdfData
import com.sunya.netchdf.testutil.testData
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals


class H5enumTest {

    companion object {
        fun files(): Sequence<String> {
            return sequenceOf (
                testData + "devcdm/hdf5/cenum.h5",
                testData + "devcdm/hdf5/enum.h5",
                testData + "devcdm/hdf5/enumcmpnd.h5",
                testData + "devcdm/netcdf4/test_enum_type.nc",
                testData + "devcdm/netcdf4/tst_enums.nc",
            )
        }
    }

    @Test
    fun testReadNetchdfData() {
        files().forEach { filename ->
            readNetchdfData(filename, null, null, true, true)
        }
    }
    
    @Test
    fun testEnumAttribute() {
        val filename = testData + "devcdm/netcdf4/tst_enums.nc"
        openNetchdfFile(filename).use { myfile ->
            println("--- ${myfile!!.type()} $filename ")
            println(myfile.cdl())

            val att = myfile.rootGroup().attributes.find{ it.name == "brady_attribute"}!!
            println("brady_attribute = $att")
            assertEquals(Datatype.Companion.ENUM1, att.datatype)
            assertContentEquals(listOf(0.toUByte(), 3.toUByte(), 8.toUByte()), att.values)
            assertEquals(listOf("Mike", "Marsha", "Alice"), att.convertEnums())

            println("cdl= ${myfile.cdl()}")
            assertContains(myfile.cdl(), "brady_attribute = Mike, Marsha, Alice")
        }
    }

    @Test
    fun testEnumVariable() {
        val filename = testData + "devcdm/hdf5/enum.h5"
        openNetchdfFile(filename).use { myfile ->
            println("--- ${myfile!!.type()} $filename ")
            println(myfile.cdl())
            val v = myfile.rootGroup().variables.find{ it.name == "EnumTest"}!!
            assertEquals(Datatype.Companion.ENUM4, v.datatype)
            val data = myfile.readArrayData(v)
            println("EnumTest data = $data")
            val expect = listOf(0,1,2,3,4,0,1,2,3,4)
            data.forEachIndexed { idx, it ->
                assertEquals(expect[idx], (it as UInt).toInt())
            }

            val expectNames = listOf("RED", "GREEN", "BLUE", "WHITE", "BLACK")
            val names = data.convertEnums().toList()
            names.forEachIndexed { idx, it ->
                assertEquals(expectNames[idx % 5], it)
            }
        }
    }

    @Test
    fun testEnumMember() {
        val filename = testData + "devcdm/hdf5/enumcmpnd.h5"
        openNetchdfFile(filename).use { myfile ->
            println("--- ${myfile!!.type()} $filename ")
            println(myfile.cdl())
            val v = myfile.rootGroup().variables.find{ it.name == "EnumCmpndTest"}!!
            assertEquals(Datatype.Companion.COMPOUND, v.datatype)
            val typedef = v.datatype.typedef as CompoundTypedef
            val member = typedef.members.find { it.name == "color_name"}!!

            val mtypedef = member.datatype.typedef as EnumTypedef

            val sdataArray = myfile.readArrayData(v)
            println("EnumCmpndTest data = $sdataArray")
            assertEquals(Datatype.Companion.COMPOUND, sdataArray.datatype)
            val dtypedef = v.datatype.typedef as CompoundTypedef
            assertEquals(typedef, dtypedef)

            val expectNames = listOf("RED", "GREEN", "BLUE", "WHITE", "BLACK")
            sdataArray.forEachIndexed { idx, it ->
                val sdata = it as ArrayStructureData.StructureData
                println("sdata = $sdata")
                val wtf : ArrayTyped<*> = member.values(sdata)
                println("value = $wtf")
                assertEquals(mtypedef.convertEnum(idx % 5), wtf.first())
                // assertEquals(expectNames[idx % 5], wtf.convertEnums().first())
            }
        }
    }

    // a compound with a member thats a type thats not a seperate typedef.
    // the obvious thing to do is to be able to add a typedef when processing the member.
    // or look for it when building H5group
    @Test
    fun compoundEnumTypedef() {
        val filename = testData + "devcdm/hdf5/enumcmpnd.h5"
        readNetchdfData(filename, null, null, true, false)
    }

}