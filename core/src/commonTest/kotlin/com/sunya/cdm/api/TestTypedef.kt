package com.sunya.cdm.api

import com.sunya.cdm.array.*
import kotlin.test.*

class TestTypedef {

    @Test
    fun testVlen() {
        val tvlen = VlenTypedef("viva", Datatype.UINT)
        assertEquals(TypedefKind.Vlen, tvlen.kind)
        assertEquals("viva", tvlen.name)
        assertEquals(Datatype.UINT, tvlen.baseType)
        assertEquals("  uint(*) viva ;", tvlen.cdl())
        assertEquals(tvlen.toString(), tvlen.cdl())

        assertEquals(tvlen, VlenTypedef("viva", Datatype.UINT))
        assertEquals(tvlen.hashCode(), VlenTypedef("viva", Datatype.UINT).hashCode())
        assertNotEquals(tvlen, VlenTypedef("viva", Datatype.INT))
    }

    @Test
    fun testOpaque() {
        val tope = OpaqueTypedef("viva", 42)
        assertEquals(TypedefKind.Opaque, tope.kind)
        assertEquals("viva", tope.name)
        assertEquals(Datatype.OPAQUE, tope.baseType)
        assertEquals("  opaque(42) viva ;", tope.cdl())

        assertNotEquals(tope, OpaqueTypedef("viva", 43))
        assertNotEquals(tope, OpaqueTypedef("vivacious", 42))
        assertEquals(tope, OpaqueTypedef("viva", 42))
        assertEquals(tope.hashCode(), OpaqueTypedef("viva", 42).hashCode())
    }

    @Test
    fun testEnum() {
        val map = mapOf(1 to "name1", 2 to "name2", 3 to "name3")
        val tenum = EnumTypedef("low", Datatype.ENUM1, map)
        assertEquals(TypedefKind.Enum, tenum.kind)
        assertEquals("low", tenum.name)
        assertEquals(Datatype.ENUM1, tenum.baseType)
        assertEquals("  ubyte enum low {1 = name1, 2 = name2, 3 = name3};", tenum.cdl())

        assertEquals(tenum,  EnumTypedef("low", Datatype.ENUM1, map))
        assertEquals(tenum.hashCode(),  EnumTypedef("low", Datatype.ENUM1, map).hashCode())
        assertNotEquals(tenum,  EnumTypedef("high", Datatype.INT, map))

        val map2 = mapOf(1 to "name1", 2 to "name2", 3 to "name3")
        assertEquals(tenum,  EnumTypedef("low", Datatype.ENUM1, map2))

        val map3 = mapOf(1 to "name1", 2 to "name2", 3 to "namaste")
        assertNotEquals(tenum,  EnumTypedef("low", Datatype.ENUM1, map3))

        val enumVals = ArrayUByte.fromByteArray(intArrayOf(3), byteArrayOf(1,1,0))
        val enumNames = tenum.convertEnumArray(enumVals)
        val expected = ArrayString(intArrayOf(3), listOf("name1", "name1", "Unknown enum number=0"))
        assertEquals(expected, enumNames)

        val tenum2 = EnumTypedef("low", Datatype.ENUM2, map)
        assertEquals("  ushort enum low {1 = name1, 2 = name2, 3 = name3};", tenum2.cdl())
        val enumVals2 = ArrayUShort.fromShortArray(intArrayOf(3), shortArrayOf(1,1,0))
        val enumNames2 = tenum2.convertEnumArray(enumVals2)
        val expected2 = ArrayString(intArrayOf(3), listOf("name1", "name1", "Unknown enum number=0"))
        assertEquals(expected2, enumNames2)

        val tenum4 = EnumTypedef("low", Datatype.ENUM4, map)
        assertEquals("  uint enum low {1 = name1, 2 = name2, 3 = name3};", tenum4.cdl())
        val enumVals4 = ArrayUInt.fromIntArray(intArrayOf(3), intArrayOf(1,1,0))
        val enumNames4 = tenum4.convertEnumArray(enumVals4)
        val expected4 = ArrayString(intArrayOf(3), listOf("name1", "name1", "Unknown enum number=0"))
        assertEquals(expected4, enumNames4)

        val tenum8 = EnumTypedef("low", Datatype.ENUM8, map)
        assertEquals("  uint64 enum low {1 = name1, 2 = name2, 3 = name3};", tenum8.cdl())
        val enumVals8 = ArrayULong.fromLongArray(intArrayOf(3), longArrayOf(1,1,0))
        val enumNames8 = tenum8.convertEnumArray(enumVals8)
        val expected8 = ArrayString(intArrayOf(3), listOf("name1", "name1", "Unknown enum number=0"))
        assertEquals(expected8, enumNames8)
    }

    @Test
    fun testCompound() {
        val members = listOf(
            StructureMember("member1", Datatype.INT, 0, intArrayOf(1,2,3), false),
            StructureMember("member2", Datatype.STRING, 43, intArrayOf(), false),
            StructureMember("member3", Datatype.ENUM4, 32, intArrayOf(), false),
        )
        val tcomp = CompoundTypedef("fracture", members)
        assertEquals(TypedefKind.Compound, tcomp.kind)
        assertEquals("fracture", tcomp.name)
        assertEquals(Datatype.COMPOUND, tcomp.baseType)
        assertEquals("""  compound fracture {
    int member1(1,2,3) ;
    string member2 ;
    uint enum member3 ;
  }; // fracture""",
            tcomp.cdl())

        assertEquals(tcomp,  CompoundTypedef("fracture", members))
        assertEquals(tcomp.hashCode(),  CompoundTypedef("fracture", members).hashCode())
        assertNotEquals(tcomp,  CompoundTypedef("flicker", members))
        assertNotEquals(tcomp,  CompoundTypedef("flicker", listOf()))
    }
}