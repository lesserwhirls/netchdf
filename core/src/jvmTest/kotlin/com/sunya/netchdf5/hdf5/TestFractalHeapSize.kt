package com.sunya.netchdf5.hdf5

import com.sunya.netchdf.testutil.runTest
import io.kotest.property.Arb
import io.kotest.property.PropTestConfig
import io.kotest.property.ShrinkingMode
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll
import java.math.BigInteger

import kotlin.test.*

val TWO = BigInteger.valueOf(2L)

// TODO
class TestFractalHeapSize {

    @Test
    fun testFractalHeapSize() {
        val nodeSize = 2048
        val depth = 4
        val recordSize = 35
        compare(nodeSize, depth, recordSize)
    }

    @Test
    fun problem() {
        val nodeSize = 8000
        val depth = 7
        val recordSize = 12
        compare(nodeSize, depth, recordSize)
    }

    @Test
    fun testFuzz() {
        val nodeSize = 2048
        val depth= 4
        val recordSize = 35
        compare(nodeSize, depth, recordSize)

        runTest {
            checkAll(
                PropTestConfig(maxFailure = 1, shrinkingMode = ShrinkingMode.Off, iterations = 100000),
                Arb.int(min = 512, max = 8000),
                Arb.int(min = 1, max = 6),
                Arb.int(min = 12, max = 4200),
            ) { nodeSize, depth, recordSize ->
                if (recordSize < nodeSize) {
                    compare(nodeSize, depth, recordSize)
                }
            }
        }
    }

    fun compare(nodeSize: Int, depth: Int, recordSize: Int, ) {
        /* println("$nodeSize, $depth, $recordSize")
        println(" getBig ${getBig(nodeSize, depth, recordSize)}")
        println(" getAlt ${getAlt(nodeSize, depth, recordSize)}")
        println(" getMine ${getMine(nodeSize, depth, recordSize)}") */

        val jhdf = jhdf(0, 0, 0, 0)
        // val mine = getMine(nodeSize, depth, recordSize)
        //val mine = mine(nodeSize, depth, recordSize)
        //assertEquals(jhdf, mine)
    }

    fun jhdf(blockIndex: Int, tableWidth: Int, startingBlockSize: Int, maxDirectBlockSize: Int): Int {
        val row = blockIndex / tableWidth // int division
        if (row < 2) {
            return startingBlockSize
        } else {
            val big = TWO.pow(row - 1)
            val size: Int = startingBlockSize * big.intValueExact()
            if (size < maxDirectBlockSize) {
                return size
            } else {
                return -1 // Indicates the block is an indirect block
            }
        }
    }

    /*
    fun mine(isOffsetLong: Boolean, maxHeapSize: Int): Int {
        // // keep track of how much room is taken out of block size, that is, how much is left for the object
        var extraBytes = 5
        extraBytes += if (isOffsetLong) 8 else 4
        var nbytes = maxHeapSize / 8
        if (maxHeapSize % 8 != 0) nbytes++

        // This is the offset of the block within the fractal heap's address space (in bytes).
        dblock.offset = h5.readVariableSizeUnsigned(state, nbytes)
        dblock.dataPos = startPos  // offsets are from the start of the block

        extraBytes += nbytes
        if ((flags.toInt() and 2) != 0) extraBytes += 4 // ?? size of checksum

        return extraBytes
    }

     */

}