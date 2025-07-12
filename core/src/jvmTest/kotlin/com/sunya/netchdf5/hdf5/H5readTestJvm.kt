package com.sunya.netchdf5.hdf5

import com.sunya.cdm.util.log2
import com.sunya.netchdf.*
import com.sunya.netchdf.testfiles.H5Files
import com.sunya.netchdf.testutil.readNetchdfData
import com.sunya.netchdf.testutil.runTest
import io.kotest.property.Arb
import io.kotest.property.PropTestConfig
import io.kotest.property.ShrinkingMode
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll
import java.math.BigInteger
import kotlin.math.ceil
import kotlin.math.log2
import kotlin.math.pow

import kotlin.test.*

// Sanity check read Hdf5File header, for non-netcdf4 files
class H5readTestJvm {

    companion object {
        fun files(): Sequence<String> {
            return H5Files.params()
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////
    // TODO this is a single test, not concurrent as it was with ParameterizedTest
    //    Anyway still dont have parallel tests

    @Test
    fun testOpenH5() {
        files().forEach { filename ->
            openH5(filename, null)
        }
    }

    @Test
    fun testReadNetchdfData() {
        files().forEach { filename ->
            readNetchdfData(filename)
        }
    }

    fun openH5(filename: String, varname : String? = null) {
        println("=================")
        println(filename)
        openNetchdfFileWithFormat(filename, NetchdfFileFormat.HDF5).use { h5file ->
            if (h5file == null) {
                println("Cant open $filename")
            } else {
                println(h5file.cdl())
                h5file.rootGroup().allVariables().forEach { println("  ${it.fullname()}") }

                if (varname != null) {
                    val h5var = h5file.rootGroup().allVariables().find { it.fullname() == varname }
                        ?: throw RuntimeException("cant find $varname")
                    val h5data = h5file.readArrayData(h5var)
                    println(" $varname = $h5data")
                }
            }
        }
    }

    @Test
    fun getSizeOfTotalNumberOfChildRecords() {
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

    fun compare(nodeSize: Int, depth: Int, recordSize: Int) {
        /* println("$nodeSize, $depth, $recordSize")
        println(" getBig ${getBig(nodeSize, depth, recordSize)}")
        println(" getAlt ${getAlt(nodeSize, depth, recordSize)}")
        println(" getMine ${getMine(nodeSize, depth, recordSize)}") */

        val big = getBig(nodeSize, depth, recordSize)
        // val mine = getMine(nodeSize, depth, recordSize)
        val alt = getAlt(nodeSize, depth, recordSize)
        assertEquals(big, alt)
    }

    private fun getAlt(nodeSize: Int, depth: Int, recordSize: Int): Int {
        // val recordsInLeafNode = (nodeSize / recordSize).toDouble()
        val recordsInLeafNode = (nodeSize/ recordSize).toDouble()
        val totalRecords = recordsInLeafNode.pow(depth) // 11316496
        val totalRecordsL = ceil(totalRecords).toLong()
        val alt = log2(totalRecordsL) + 1
        val alt1 =  (alt + 8) / 8
        return alt1
    }

    private fun getMine(nodeSize: Int, depth: Int, recordSize: Int): Int {
        val recordsInLeafNode = (nodeSize.toDouble() / recordSize)
        val totalRecords = recordsInLeafNode.pow(depth) // 11316496
        val totalBits = log2(totalRecords)
        val totalBitsCiel = ceil(totalBits)
        val totalBitsCielInt = totalBitsCiel.toInt()
        return (totalBitsCielInt + 8) / 8
    }

    // jhdf
    fun getBig(nodeSize: Int, depth: Int, recordSize: Int): Int {
       val recordsInLeafNode = nodeSize / recordSize
       val big = BigInteger.valueOf(recordsInLeafNode.toLong())
       val big2 = big.pow(depth)
       val big3 = big2.bitLength()
       return (big3 + 8) / 8
    }

}