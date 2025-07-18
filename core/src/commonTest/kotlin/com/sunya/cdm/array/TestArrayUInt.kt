package com.sunya.cdm.array

import com.sunya.cdm.api.*
import com.sunya.cdm.layout.IndexND
import com.sunya.cdm.layout.IndexSpace
import com.sunya.netchdf.testutil.propTestSlowConfig
import com.sunya.netchdf.testutil.runTest
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll
import kotlin.test.*
import kotlin.math.max

@OptIn(ExperimentalUnsignedTypes::class)
class TestArrayUInt {

    @Test
    fun testArrayUInt() {
        val shape = intArrayOf(4,5,6)
        val size = shape.computeSize()
        val bb = UIntArray(size) { it.toUInt()  }

        val testArray = ArrayUInt(shape, bb)
        assertEquals(Datatype.UINT, testArray.datatype)
        assertEquals(size, testArray.nelems)

        testArray.forEachIndexed { idx, it ->
            assertEquals(idx.toUInt(), it)
        }
    }

    // fuzz test that section() works
    @Test
    fun testSectionFuzz() {
        runTest {
            checkAll(
                propTestSlowConfig,
                Arb.int(min = 1, max = 4),
                Arb.int(min = 6, max = 8),
                Arb.int(min = 1, max = 4),
            ) { dim0, dim1, dim2 ->
                val shape = intArrayOf(dim0, dim1, dim2)
                val size = shape.computeSize()
                val bb = UIntArray(size) { it.toUInt()  }

                val testArray = ArrayUInt(shape, bb)

                val sectionStart = intArrayOf(dim0/2, dim1/3, dim2/2)
                val sectionLength = intArrayOf(max(1, dim0/2), max(1,dim1/3), max(1,dim2/2))
                val section = Section(sectionStart, sectionLength, shape.toLongArray())
                val sectionArray = testArray.section(section)

                assertEquals(Datatype.UINT, sectionArray.datatype)
                assertEquals(sectionLength.computeSize(), sectionArray.nelems)
                assertEquals(sectionLength.computeSize(), sectionArray.values.size)

                val full = IndexND(IndexSpace(sectionStart.toLongArray(), sectionLength.toLongArray()), shape.toLongArray())
                val odo = IndexND(IndexSpace(sectionStart.toLongArray(), sectionLength.toLongArray()), shape.toLongArray())
                odo.forEachIndexed { idx, index ->
                    val have = sectionArray.values.get(idx)
                    val expect = testArray.values.get(full.element(index).toInt())
                    assertEquals(expect, have)
                }
            }
        }
    }

    @Test
    fun testFromArray() {
        val shape = intArrayOf(4,5,6)
        val size = shape.computeSize()
        val sarray = IntArray(size) { it - 21 }

        val testArray = ArrayUInt.fromIntArray(shape, sarray)
        assertEquals(Datatype.UINT, testArray.datatype)
        assertEquals(size, testArray.nelems)

        testArray.forEachIndexed { idx, it ->
            assertEquals(idx - 21, it.toInt())
        }
    }
}