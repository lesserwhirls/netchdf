package com.sunya.netchdf

import com.sunya.cdm.api.*
import com.sunya.netchdf.testfiles.*
import com.sunya.netchdf.testutil.Stats
import com.sunya.netchdf.testutil.compareNetchIterate
import com.sunya.netchdf.testutil.readNetchdfData
import com.sunya.netchdf.testutil.showNetchdfHeader
import kotlin.test.*

// Test files opened and read through openNetchdfFile().
class NetchdfTest {

    init {
        Stats.clear() // problem with concurrent tests
    }

    companion object {

        fun files(): Sequence<String> {
            return N3Files.params() + N4Files.params() + H5Files.params() + H4Files.params() +
                    NetchdfExtraFiles.params(false)
        }

        fun afterAll() {
            if (versions.size > 0) {
                val sversions = versions.entries.sortedBy { it.key }.toList()
                sversions.forEach{ (key, values) -> println("$key = ${values.size} files") }
                val total = sversions.map{ it.value.size }.sum()
                println("total # files = $total")
            }
            Stats.show()
        }

        private val versions = mutableMapOf<String, MutableList<String>>()

        var showDataRead = false
        var showData = false
        var showFailedData = false
        var showCdl = false
    }

    @Test
    fun missingChunks() {
        readNetchdfData(
            testData + "cdmUnitTest/formats/netcdf4/files/xma022032.nc",
            "/xma/dialoop_back"
        )
    }

    @Test
    fun hasMissing() {
        val filename =
            testData + "cdmUnitTest/formats/netcdf4/goes16/OR_ABI-L2-CMIPF-M6C13_G16_s20230451800207_e20230451809526_c20230451810015.nc"
        readNetchdfData(filename, "CMI", SectionPartial.fromSpec(":, :"))
        readNetchdfData(filename, "DQF", SectionPartial.fromSpec(":, :"))
    }

    @Test
    fun testNetchIterate() {
        //  *** double UpperDeschutes_t4p10_swemelt[8395, 781, 385] skip read ArrayData too many bytes= 2524250575
        //compareNetchIterate(testData + "cdmUnitTest/formats/netcdf4/UpperDeschutes_t4p10_swemelt.nc", "UpperDeschutes_t4p10_swemelt")
    }

    @Test
    fun testProblem() {
        readNetchdfData(testData + "hdf4/96108_08.hdf", "graphics")
    }

    @Test
    fun testSimple() {
        //  *** double UpperDeschutes_t4p10_swemelt[8395, 781, 385] skip read ArrayData too many bytes= 2524250575
        readNetchdfData(testData + "devcdm/netcdf3/simple_xy.nc", showData = true)
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    fun checkVersion() {
        files().forEach { filename ->
            openNetchdfFile(filename).use { ncfile ->
                if (ncfile == null) {
                    println("Not a netchdf file=$filename ")
                    return
                }
                println("${ncfile.type()} $filename ")
                val paths = versions.getOrPut(ncfile.type()) { mutableListOf() }
                paths.add(filename)
            }
        }
    }

    @Test
    fun testShowNetchdfHeader() {
        files().forEach { filename ->
            showNetchdfHeader(filename)
        }
    }

    @Test
    fun testReadNetchdfData() {
        files().forEach { filename ->
            readNetchdfData(filename)
        }
    }

    // TODO too slow
    // @Test
    fun testReadNetchIterate() {
        files().forEach { filename ->
            compareNetchIterate(filename)
        }
    }
}

/*

fun showNetchdfHeader(filename: String) {
    println(filename)
    openNetchdfFile(filename).use { myfile ->
        if (myfile == null) {
            println("*** not a netchdf file = $filename")
            return
        }
        println(myfile.cdl())
    }
}

fun readNetchdfData(filename: String, varname: String? = null, section: SectionPartial? = null, showCdl : Boolean = false, showData : Boolean = false) {
    // println("=============================================================")
    openNetchdfFile(filename).use { myfile ->
        if (myfile == null) {
            println("*** not a netchdf file = $filename")
            return
        }
        println("--- ${myfile.type()} $filename ")
        readMyData(myfile,varname, section, showCdl, showData)
    }
}

 */

//////////////////////////////////////////////////////////////////////////////////////////////////////
/* just read data from myfile

fun readMyData(myfile: Netchdf, varname: String? = null, section: SectionPartial? = null, showCdl : Boolean = false, showData : Boolean = false) {

    if (showCdl) {
        println(myfile.cdl())
    }
    // println(myfile.rootGroup().allVariables().map { it.fullname() })
    if (varname != null) {
        val myvar = myfile.rootGroup().allVariables().find { it.fullname() == varname }
        if (myvar == null) {
            println("cant find $varname")
            return
        }
        readOneVar(myvar, myfile, section, showData)
    } else {
        myfile.rootGroup().allVariables().forEach { it ->
            readOneVar(it, myfile, null, showData)
        }
    }
}

const val maxBytes = 10_000_000

fun readOneVar(myvar: Variable<*>, myfile: Netchdf, section: SectionPartial?, showData : Boolean = NetchdfTest.showData) {
    if (myvar.name.contains("mbReflectivity"))
        println()

    val sectionF = SectionPartial.fill(section, myvar.shape)
    val nbytes = sectionF.totalElements * myvar.datatype.size
    val myvarshape = myvar.shape.toIntArray()

    if (nbytes > maxBytes) {
        if (NetchdfTest.showDataRead) println(" * ${myvar.fullname()} read too big: ${nbytes} > $maxBytes")
    } else {
        val mydata = myfile.readArrayData(myvar, section)
        if (NetchdfTest.showDataRead) println(" ${myvar.datatype} ${myvar.fullname()}${myvar.shape.contentToString()} = " +
                    "${mydata.shape.contentToString()} ${mydata.shape.computeSize()} elems" )
        if (myvar.datatype == Datatype.CHAR) {
            testCharShape(myvarshape, mydata.shape)
        } else {
            assertTrue(myvarshape.equivalent(mydata.shape), "variable ${myvar.name}")
        }
        if (showData) println(mydata)
    }

    if (myvar.nelems > 8 && myvar.datatype != Datatype.CHAR) {
        readMiddleSection(myfile, myvar, myvar.shape)
    }
}

fun testCharShape(want: IntArray, got: IntArray) {
    val org = want.equivalent(got)
    val removeLast = removeLast(want)
    val removeLastOk = removeLast.equivalent(got)
    assertTrue(org or removeLastOk)
}

fun removeLast(org: IntArray): IntArray {
    if (org.size < 1) return org
    return IntArray(org.size - 1) { org[it] }
}

fun readMiddleSection(myfile: Netchdf, myvar: Variable<*>, shape: LongArray) {
    val orgSection = Section(shape)
    val middleRanges = orgSection.ranges.mapIndexed { idx, range ->
        if (range == null) throw RuntimeException("Range is null")
        val length = orgSection.shape[idx]
        if (length < 9) range
        else LongProgression.fromClosedRange(range.first + length / 3, range.last - length / 3, range.step)
    }
    val middleSection = Section(middleRanges, myvar.shape)
    val nbytes = middleSection.totalElements * myvar.datatype.size
    if (nbytes > maxBytes) {
        if (NetchdfTest.showDataRead) println("  * ${myvar.fullname()}[${middleSection}] read too big: ${nbytes} > $maxBytes")
        readMiddleSection(myfile, myvar, middleSection.shape)
        return
    }

    if (myvar.name == "temperature") {
        println()
    }

    val mydata = myfile.readArrayData(myvar, SectionPartial(middleSection.ranges))
    val middleShape = middleSection.shape.toIntArray()
    if (NetchdfTest.showDataRead) println("  ${myvar.fullname()}[$middleSection] = ${mydata.shape.contentToString()} ${mydata.shape.computeSize()} elems")
    if (myvar.datatype == Datatype.CHAR) {
        testCharShape(middleShape, mydata.shape)
    } else {
        assertTrue(middleShape.equivalent(mydata.shape), "variable ${myvar.name}")
    }
    if (NetchdfTest.showData) println(mydata)
}

//////////////////////////////////////////////////////////////////////////////////////
// compare reading data regular and through the chunkIterate API

fun compareNetchIterate(filename: String, varname : String? = null, compare : Boolean = true) {
    openNetchdfFile(filename).use { myfile ->
        if (myfile == null) {
            println("*** not a netchdf file = $filename")
            return
        }
        println("${myfile.type()} $filename ${"%.2f".format(myfile.size / 1000.0 / 1000.0)} Mbytes")
        var countChunks = 0
        if (varname != null) {
            val myvar = myfile.rootGroup().allVariables().find { it.fullname() == varname } ?: throw RuntimeException("cant find $varname")
            countChunks +=  compareOneVarIterate(myfile, myvar, compare)
        } else {
            myfile.rootGroup().allVariables().forEach { it ->
                countChunks += compareOneVarIterate(myfile, it, compare)
            }
        }
        if (countChunks > 0) {
            println("${myfile.type()} $filename ${"%.2f".format(myfile.size / 1000.0 / 1000.0)} Mbytes chunks = $countChunks")
        }
    }
}

// compare readArrayData with chunkIterator
fun compareOneVarIterate(myFile: Netchdf, myvar: Variable<*>, compare : Boolean = true) : Int {
    val filename = myFile.location().substringAfterLast('/')
    val varBytes = myvar.nelems
    if (varBytes >= maxBytes) {
        println(" *** ${myvar.nameAndShape()} skip reading ArrayData too many bytes= $varBytes max = $maxBytes")
        return 0
    }

    val sum1 = AtomicDouble(0.0)
    val sumArrayData = if (compare) {
        val time3 = measureNanoTime {
            val arrayData = myFile.readArrayData(myvar, null)
            sumValues(arrayData, sum1)
        }
        Stats.of("readArrayData", filename, "chunk").accum(time3, 1)
        sum1.get()
    } else 0.0

    val sum2 = AtomicDouble(0.0)
    var countChunks = 0
    val time1 = measureNanoTime {
        val chunkIter = myFile.chunkIterator(myvar)
        for (pair in chunkIter) {
            // println(" ${pair.section} = ${pair.array.shape.contentToString()}")
            sumValues(pair.array, sum2)
            countChunks++
        }
    }
    val sumChunkIterator = sum2.get()
    if (compare) Stats.of("chunkIterator", filename, "chunk").accum(time1, countChunks)

    if (compare && sumChunkIterator.isFinite() && sumArrayData.isFinite()) {
        // println("  sumChunkIterator = $sumChunkIterator for ${myvar.nameAndShape()}")
        assertTrue(nearlyEquals(sumArrayData, sumChunkIterator), "chunkIterator $sumChunkIterator != $sumArrayData sumArrayData")
    }
    return countChunks
}

//////////////////////////////////////////////////////////////////////////////////////
// compare reading data chunkIterate API with Netch and NC

fun compareIterateWithNC(myfile: Netchdf, ncfile: Netchdf, varname: String?, section: SectionPartial? = null) {
    if (varname != null) {
        val myvar = myfile.rootGroup().allVariables().find { it.fullname() == varname }
        if (myvar == null) {
            println(" *** cant find myvar $varname")
            return
        }
        val ncvar = ncfile.rootGroup().allVariables().find { it.fullname() == myvar.fullname() }
        if (ncvar == null) {
            throw RuntimeException(" *** cant find ncvar $varname")
        }
        compareOneVarIterate(myvar, myfile, ncvar, ncfile, section)
    } else {
        myfile.rootGroup().allVariables().forEach { myvar ->
            val ncvar = ncfile.rootGroup().allVariables().find { it.fullname() == myvar.fullname() }
            if (ncvar == null) {
                println(" *** cant find ${myvar.fullname()} in ncfile")
            } else {
                compareOneVarIterate(myvar, myfile, ncvar, ncfile, null)
            }
        }
    }
}

fun compareOneVarIterate(myvar: Variable<*>, myfile: Netchdf, ncvar : Variable<*>, ncfile: Netchdf, section: SectionPartial?) {
    val sum = AtomicDouble(0.0)
    var countChunks = 0
    val time1 = measureNanoTime {
        val chunkIter = myfile.chunkIterator(myvar)
        for (pair in chunkIter) {
            // println(" ${pair.section} = ${pair.array.shape.contentToString()}")
            sumValues(pair.array, sum)
            countChunks++
        }
    }
    Stats.of("netchdf", myfile.location(), "chunk").accum(time1, countChunks)
    val sum1 = sum.get()

    sum.set(0.0)
    countChunks = 0
    val time2 = measureNanoTime {
        val chunkIter = ncfile.chunkIterator(ncvar)
        for (pair in chunkIter) {
            // println(" ${pair.section} = ${pair.array.shape.contentToString()}")
            sumValues(pair.array, sum)
            countChunks++
        }
    }
    Stats.of("nclib", ncfile.location(), "chunk").accum(time2, countChunks)
    val sum2 = sum.get()

    if (sum1.isFinite() && sum2.isFinite()) {
        assertTrue(nearlyEquals(sum1, sum2), "$sum1 != $sum2 sum2")
        println("sum = $sum1")
    }
}

///////////////////////////////////////////////////////////
fun sumValues(array : ArrayTyped<*>, sum : AtomicDouble) {
    if (array is ArraySingle || array is ArrayEmpty) {
        return // test fillValue the same ??
    }
    // cant cast unsigned to Numbers
    val useArray = when (array.datatype) {
        Datatype.UBYTE -> ArrayByte(array.shape, (array as ArrayUByte).bb)
        Datatype.USHORT -> ArrayShort(array.shape, (array as ArrayUShort).bb)
        Datatype.UINT -> ArrayInt(array.shape, (array as ArrayUInt).bb)
        Datatype.ULONG -> ArrayLong(array.shape, (array as ArrayULong).bb)
        else -> array
    }

    if (useArray.datatype.isNumber) {
        for (value in useArray) {
            val number = (value as Number)
            val numberd: Double = number.toDouble()
            if (numberd.isFinite()) {
                sum.getAndAdd(numberd)
            }
        }
    }
}

*/