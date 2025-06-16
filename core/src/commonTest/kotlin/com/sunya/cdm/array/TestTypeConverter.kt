package com.sunya.cdm.array

import kotlin.test.Test
import kotlin.test.assertEquals

class TestTypeConverter {

    @Test
    fun testMakeStringZ() {
        val ba = byteArrayOf(1, 4, 1, 0, 0, 0, 0, 0, 0, 0, 10, 68, 97, 116, 101, 83, 116, 114, 76, 101, 110)
        assertEquals("DateStrLen", makeStringZ(ba, 11, 10))
    }

    @Test
    fun testMakeStringZtrunc() {
        val ba = byteArrayOf(1, 4, 1, 0, 0, 0, 0, 0, 0, 0, 10, 68, 97, 116, 101, 83, 0, 114, 76, 101, 110)
        val s = makeStringZ(ba, 11, 10)
        assertEquals("DateS", makeStringZ(ba, 11, 10))
    }

    @Test
    fun testMakeStringOver() {
        val ba = byteArrayOf(1, 4, 1, 0, 0, 0, 0, 0, 0, 0, 10, 68, 97, 116, 101, 83, 116, 114, 76, 101, 110)
        assertEquals("DateStrLen", makeStringZ(ba, 11, 20))
    }
}