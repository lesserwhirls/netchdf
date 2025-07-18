package com.sunya.cdm.layout

import kotlin.test.*
import kotlin.test.assertEquals

private const val show = false
/** Test [com.sunya.cdm.layout.Chunker]  */
class TestChunkerIntersection {

    @Test
    fun intersectLeft() {
        // A rectangular subsection of indices, going from start to start + shape, relative to varShape
        //  class IndexSpace(startIn : LongArray, shapeIn : LongArray) {
        val varShape = IndexSpace(longArrayOf(0,0), longArrayOf(9, 9))

        // may be an improper subset
        val dataSection = IndexSpace(longArrayOf(0,0), longArrayOf(10,10))

        val chunker = Chunker(dataSection, varShape)

        val data = ByteArray(100) { it.toByte() }
        val result = ByteArray(81)
        chunker.transferBA(data, 0, 1, result, 0)

        println(result.contentToString())

        var count = 0
        val expected = ByteArray(81)
        repeat(9) { tens ->
            repeat(9) { ones ->
                expected[count++] = (10 * tens + ones).toByte()
            }
        }

        assertEquals(expected.contentToString(), result.contentToString())
    }

    // @Test
    fun intersectRight() {
        // A rectangular subsection of indices, going from start to start + shape, relative to varShape
        //  class IndexSpace(startIn : LongArray, shapeIn : LongArray) {
        val varShape = IndexSpace(longArrayOf(0, 0), longArrayOf(10, 10))

        // may be an improper subset
        val dataSection = IndexSpace(longArrayOf(1, 1), longArrayOf(9, 9))

        val chunker = Chunker(dataSection, varShape)

        val data = ByteArray(81) { it.toByte() }
        val result = ByteArray(100)
        chunker.transferBA(data, 0, 1, result, 0)

        println(result.contentToString())

        var count = 0
        val expected = ByteArray(100)
        repeat(10) { tens ->
            repeat(10) { ones ->
                expected[count++] = if (ones == 0 || tens == 0) 0 else (10 * tens + ones).toByte()
            }
        }

        assertEquals(expected.contentToString(), result.contentToString())
    }
}