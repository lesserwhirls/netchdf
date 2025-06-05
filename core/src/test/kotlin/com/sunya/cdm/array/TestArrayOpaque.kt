package com.sunya.cdm.array

import com.sunya.cdm.api.*
import org.junit.jupiter.api.Test
import kotlin.test.*

class TestArrayOpaque {

    @Test
    fun TestArrayOpaque() {
        val osize = 10
        val shape = intArrayOf(4,5)
        val size = shape.computeSize()
        val values = mutableListOf<ByteArray>()
        var count = 0
        repeat(size) {
            values.add( ByteArray(osize) { (count + it).toByte() } )
            count++
        }

        val testArray = ArrayOpaque(shape, values, osize)
        assertEquals(Datatype.OPAQUE, testArray.datatype)
        assertEquals(size, testArray.nelems)
        assertEquals(osize, testArray.size)
        println(testArray)
        assertTrue(testArray.toString().startsWith("class ArrayOpaque shape=[4, 5] data='[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]','[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]"))

        testArray.forEachIndexed { idx, nbb ->
            assertEquals(osize, nbb.size)
            repeat(osize) { pos ->
                assertEquals( (idx + pos).toByte(), nbb.get(pos),  "idx=$idx, pos=$pos")
            }
        }
    }

    @Test
    fun testSection() {
        val osize = 10
        val shape = intArrayOf(5)
        val size = shape.computeSize()
        val values = mutableListOf<ByteArray>()
        var count = 0
        repeat(size) {
            values.add( ByteArray(osize) { (count + it).toByte() } )
            count++
        }
        val testArray = ArrayOpaque(shape, values, osize)

        val sectionStart = intArrayOf(1)
        val sectionLength = intArrayOf(2)
        val section = Section(sectionStart, sectionLength, shape.toLongArray())
        val sectionArray = testArray.section(section)

        assertEquals(Datatype.OPAQUE, sectionArray.datatype)
        assertEquals(sectionLength.computeSize(), sectionArray.nelems)
        assertEquals(sectionLength.computeSize(), sectionArray.values.size)

        /* TODO
        val full = IndexND(IndexSpace(sectionStart.toLongArray(), sectionLength.toLongArray()), shape.toLongArray())
        val odo = IndexND(IndexSpace(sectionStart.toLongArray(), sectionLength.toLongArray()), shape.toLongArray())
        odo.forEachIndexed { idx, index ->
            println("$idx, ${index.contentToString()} ${full.element(index)}")
            val have = sectionArray.getElement(idx)
            val expect = testArray.getElement(full.element(index).toInt())
            assertTrue(expect.contentEquals(have))
        }

         */
    }
}