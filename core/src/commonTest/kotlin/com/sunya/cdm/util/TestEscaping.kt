package com.sunya.cdm.util

import kotlin.test.Test
import kotlin.test.assertEquals

class TestEscaping {

    @Test
    fun testReplaceChar() {
        val x = "123#ret"
        val sb = StringBuilder(x)
        replace(sb, '#', "6")
        assertEquals("1236ret", sb.toString())
    }

    @Test
    fun testReplaceArray() {
        val x = "123#r9et"
        assertEquals("123sharprstuffet", replace(x, charArrayOf('#', '9'), arrayOf("sharp", "stuff")))
    }

    @Test
    fun testEscapeName() {
        val x = "/yr/mileage\\should be 42\bok"
        assertEquals("/yr/mileage\\\\should_be_42\\\\bok", escapeName(x))
    }

    @Test
    fun testCdl() {
        val x = "/yr/mileage\\should be 42\bok"
        assertEquals("/yr/mileage\\\\should be 42\\\\bok", escapeCdl(x))
    }
}