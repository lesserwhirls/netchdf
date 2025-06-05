package com.sunya.cdm.array

import org.junit.jupiter.api.Test
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.test.assertEquals

class TestTypeConverter {

    @Test
    fun testConvertLong() {
        val ba = byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0, -96, 3, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, -128, 1, 0, 0, 0, 0, 0, 0, 96, 0, 0, 0, 0, 0, 0, 0)
        val actual = convertLong(ba, 8, false)
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
}


fun convertLongBB(ba: ByteBuffer, elem: Int): Long {
    val baLong = ba.asLongBuffer()
    return baLong.get(elem)
}