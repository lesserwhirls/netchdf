package com.sunya.cdm.array

import com.sunya.cdm.api.Datatype
import org.junit.jupiter.api.Test
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.test.assertEquals

class TestTypeConverter {

    @Test
    fun testConvertLong() {
        val ba = byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0, -96, 3, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, -128, 1, 0, 0, 0, 0, 0, 0, 96, 0, 0, 0, 0, 0, 0, 0)
        val actual = convertToLong(ba, 8, false)
        println("actual = $actual")
        assertEquals(928L, actual)
    }

    @Test
    fun testBBLong() {
        val ba = byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0, -96, 3, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, -128, 1, 0, 0, 0, 0, 0, 0, 96, 0, 0, 0, 0, 0, 0, 0)
        val bb = ByteBuffer.wrap(ba)
        bb.order(ByteOrder.LITTLE_ENDIAN)

        val actual = convertLongBB(bb, 1)
        println("convertLong = $actual")
        assertEquals(928L, actual)
    }

    @Test
    fun testConverter() { // TODO
        repeat(2) {
            val isBE =  (it == 0)
            convertRoundTrip(Datatype.BYTE, 43.toByte(), isBE)
            convertRoundTrip(Datatype.UBYTE, (-43).toUByte(), isBE)

            convertRoundTrip(Datatype.SHORT, 22243.toShort(), isBE)
            convertRoundTrip(Datatype.USHORT, (-43).toUShort(), isBE)

            convertRoundTrip(Datatype.INT, 22243333, isBE)
            convertRoundTrip(Datatype.UINT, 123456.toUInt(), isBE)

            convertRoundTrip(Datatype.LONG, 222494543333L, isBE)
            convertRoundTrip(Datatype.ULONG, 222494543333L.toULong(), isBE)

            convertRoundTrip(Datatype.FLOAT, -999.9f, isBE)
            convertRoundTrip(Datatype.DOUBLE, 1231.98273, isBE)

            convertRoundTrip(Datatype.STRING, "you know what!", isBE)
        }
    }
}

fun convertLongBB(ba: ByteBuffer, elem: Int): Long {
    val baLong = ba.asLongBuffer()
    return baLong.get(elem)
}

fun convertRoundTrip(datatype: Datatype<*>, value : Any, isBE: Boolean) {
    val ba = convertToBytes(datatype, value, isBE)
    val roundtrip = convertFromBytes(datatype, ba, isBE)
    assertEquals(value, roundtrip, "datatype '$datatype' value $value isBE $isBE")
}

