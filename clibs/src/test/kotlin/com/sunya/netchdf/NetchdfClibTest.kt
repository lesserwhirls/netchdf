package com.sunya.netchdf

import com.sunya.cdm.api.*
import com.sunya.cdm.array.*
import com.sunya.cdm.util.nearlyEquals
import com.sunya.netchdf.hdf4Clib.Hdf4ClibFile
import com.sunya.netchdf.hdf5Clib.Hdf5ClibFile
import com.sunya.netchdf.netcdfClib.NClibFile
import com.sunya.netchdf.testdata.*
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.system.measureNanoTime
import kotlin.test.assertEquals
import kotlin.test.assertTrue

// Compare Netchdf against NetcdfClibFile / Hdf5ClibFile / Hdf4ClibFile
class NetchdfClibTest {

    companion object {
        @JvmStatic
        fun params(): Stream<Arguments> {
            // return NppFiles.params()
            return Stream.of( N3Files.params(), N4Files.params(), H5Files.params(), H4Files.params(), NetchdfExtraFiles.params(true)).flatMap { i -> i }
        }

        @JvmStatic
        @BeforeAll
        fun beforeAll() {
            Stats.clear() // problem with concurrent tests
        }

        @JvmStatic
        @AfterAll
        fun afterAll() {
            if (versions.size > 0) {
                val sversions = versions.toSortedMap()
                sversions.keys.forEach{ println("$it = ${sversions[it]!!.size } files") }
                val total = sversions.keys.map{ sversions[it]!!.size }.sum()
                println("total # files = $total")
            }
            Stats.show()
        }

        private val versions = mutableMapOf<String, MutableList<String>>()

        var compareMiddleSection = false
        var showDataRead = false
        var showData = false
        var showFailedData = false
        var showCdl = false
    }

    @Test
    fun testOneCdl() {
        val filename = testData + "netchdf/tomas/S3A_OL_CCDB_CHAR_AllFiles.20101019121929_1.nc4"
        compareCdlWithClib(filename)
    }

    @Test
    fun testEnums() {
        compareCdlWithClib(testData + "devcdm/netcdf4/test_enum_type.nc")
        compareCdlWithClib(testData + "devcdm/netcdf4/tst_enums.nc")
        compareCdlWithClib(testData + "devcdm/hdf5/enumcmpnd.h5")
        compareCdlWithClib(testData + "devcdm/hdf5/enum.h5")
        compareCdlWithClib(testData + "devcdm/hdf5/cenum.h5")

        compareDataWithClib(testData + "devcdm/netcdf4/test_enum_type.nc")
        compareDataWithClib(testData + "devcdm/netcdf4/tst_enums.nc")
        compareDataWithClib(testData + "devcdm/hdf5/enumcmpnd.h5")
        compareDataWithClib(testData + "devcdm/hdf5/enum.h5")
        compareDataWithClib(testData + "devcdm/hdf5/cenum.h5")
    }

    // /home/all/testdata/devcdm/netcdf4/test_enum_type.nc

    @Test
    fun problemHdf4() {
        val filename = testData + "hdf4/nsidc/LAADS/MOD/MOD10A1.A2008001.h23v15.005.2008003161138.hdf"
        // compareN4withH5cdl(filename)
        // compareCdlWithClib(filename, true)
        compareDataWithClib(filename, "/MOD_Grid_Snow_500m/Data_Fields/Snow_Cover_Daily_Tile")
    }

    @Test
    fun problem2() {
        val filename = testData + "devcdm/netcdf4/IntTimSciSamp.nc"
        // compareN4withH5cdl(filename)
        compareCdlWithClib(filename)
        compareDataWithClib(filename)
    }

    // ncdump:
    // group: SomaticMutation {
    //  types:
    //    compound _AnonymousCompound12 {
    //      int ProbeSetName_\&size ;
    //      char ProbeSetName ;
    //      float QNSignal ;
    //      float MutScore ;
    //      int MutCall_\&size ;
    //      char MutCall ;
    //      float MutThreshPres ;
    //      float MutThreshPoss ;
    //    }; // _AnonymousCompound12
    //  dimensions:
    //  	phony_dim_17 = 70 ;
    //  variables:
    //  	_AnonymousCompound12 SomC
    //
    // netchdf cdl:
    //   group: SomaticMutation {
    //    types:
    //      compound SomCall {
    //        int ProbeSetName_&size ;
    //        string ProbeSetName ;
    //        float QNSignal ;
    //        float MutScore ;
    //        int MutCall_&size ;
    //        string MutCall ;
    //        float MutThreshPres ;
    //        float MutThreshPoss ;
    //      }; // SomCall
    //    variables:
    //      SomCall SomCall(70) ;
    //  }
    // TODO whats with the & ??
    @Test
    fun testReadArrayOfCompound() {
        val filename = testData + "cdmUnitTest/formats/hdf5/20130212_CN021_P3_222k_B02_WD7195FBPAT10231Nat_Nat_Std_CHTNWD_OP3_14.mip222k.oschp"
        println(filename)
        // val filename = testData + "cdmUnitTest/formats/hdf5/superblockIsOffsetNPP.h5"
        //val filename = testData + "cdmUnitTest/formats/hdf5/wrf/wrf_input_par.h5"
        // compareCdlWithClib(filename)

        val sectionp = SectionPartial.fromSpec("6:13")

        openNetchdfFile(filename).use { ncfile ->
            println("NetchdfFile ${ncfile!!.type()}\n${ncfile.cdl()}")
            val v = ncfile.rootGroup().allVariables().find { it.fullname() == "/SomaticMutation/SomCall" }
            val mydata = ncfile.readArrayData(v!!, null)
            val section = SectionPartial.fill(sectionp, mydata.shape.toLongArray())
            val mysdata = mydata.section(section)
            println("netch section $section data=$mysdata")

            Hdf5ClibFile(filename).use { hcfile ->
                println("Hdf5ClibFile ${hcfile!!.type()}\n${hcfile.cdl()}")
                val v = hcfile.rootGroup().allVariables().find { it.fullname() == "/SomaticMutation/SomCall" }
                val ncdata = hcfile.readArrayData(v!!, null)
                assertTrue (ncdata.equals(mydata))

                val section = SectionPartial.fill(sectionp, v.shape)
                val ncsdata = ncdata.section(section)
                println("H5C section $section data=$ncsdata")
                assertTrue (ncsdata.equals(mysdata))
            }
        }

        /* see if it can be read through N4C
        NClibFile(filename).use { ncfile ->
            println("${ncfile.type()} $filename ")
            openNetchdfFile(filename).use { netch ->
                compareNetcdfData(ncfile, netch!!, null, null)
            }
        }
        compareDataWithClib(filename) */
    }

    @Test
    fun missingChunks() {
        compareDataWithClib(
            testData + "cdmUnitTest/formats/netcdf4/files/xma022032.nc",
            "/xma/dialoop_back"
        )
    }

    @Test
    fun testCompareOpaqueData() {
        val filename = "/home/all/testdata/devcdm/hdf5/opaque.h5"
        compareDataWithClib(filename, "Opaque")
    }

    @Test
    fun testCharFillValue() {
        val filename = "/home/all/testdata/netchdf/martaan/SEVIR_OPER_R___MSGCPP__L2__20120119T121500_20120119T123000_0001.nc"
        readNetchdfData(filename)
    }

/*
hdf5      /home/all/testdata/devcdm/hdf5/bitop.h5 0.00 Mbytes
majnum = 1, minnum = 14, relnum = 6
ffiVersion = HDF5 library version: 1.14.6
isThreadsafe = 0 = false
 ubyte /typetests/bitfield_1[Section(ranges=[0..31 step 1], varShape=[32])] = 32 elems
 ushort /typetests/bitfield_2[Section(ranges=[0..15 step 1], varShape=[16])] = 16 elems
 *** FAIL cfile.readArrayData for variable = opaque /typetests/opaque_1 []
    */

    @Test
    fun testHdf4Attribute() {
        val filename = "/home/all/testdata/hdf4/eos/misr/MISR_AM1_GRP_TERR_GM_P040_AN"
        compareCdlWithClib(filename)
        compareDataWithClib(filename, )
    }

    @Test
    fun testFillValue() {
        val filename = "/home/all/testdata/hdf4/nsidc/LAADS/MOD/MOD07_L2.A2007001.0000.005.2007003012910.hdf"
        // compareCdlWithClib(filename, true)
        compareDataWithClib(filename, "/mod07/Data_Fields/Processing_Flag")
    }

    @Test
    fun testFailDataCompare3() {
        val filename = "/home/all/testdata/devcdm/hdfeos2/MISR_AM1_GP_GMP_P040_O003734_05.eos"
        compareCdlWithClib(filename, true)
        compareDataWithClib(filename, "/GeometricParameters/Data_Fields/SolarAzimuth")
    }

    @Test
    fun testFailDataCompare4() {
        val filename = "/home/all/testdata/devcdm/netcdf4/tst_opaque_data.nc4"
        compareCdlWithClib(filename, true)
        compareDataWithClib(filename)
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////

    @ParameterizedTest
    @MethodSource("params")
    fun checkVersion(filename: String) {
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

    @ParameterizedTest
    @MethodSource("params")
    fun testShowNetchdfHeader(filename: String) {
        showNetchdfHeader(filename)
    }


    @ParameterizedTest
    @MethodSource("params")
    fun testCdlWithClib(filename: String) {
        compareCdlWithClib(filename)
    }

    @ParameterizedTest
    @MethodSource("params")
    fun testReadNetchdfData(filename: String) {
        readNetchdfData(filename)
    }

    @ParameterizedTest
    @MethodSource("params")
    fun testCompareDataWithClib(filename: String) {
        compareDataWithClib(filename)
    }

    //@ParameterizedTest
    //@MethodSource("params")
    fun testIterateWithClib(filename: String) {
        compareIterateWithClib(filename)
    }

}

//////////////////////////////////////////////////////////////////////////////////////////////////////

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

fun showNcHeader(filename: String) {
    println(filename)
    NClibFile(filename).use { ncfile ->
        println(ncfile.cdl())
    }
}

fun readNetchdfData(filename: String, varname: String? = null, section: SectionPartial? = null, showCdl : Boolean = false) {
    // println("=============================================================")
    openNetchdfFile(filename).use { myfile ->
        if (myfile == null) {
            println("*** not a netchdf file = $filename")
            return
        }
        println("--- ${myfile.type()} $filename ")
        readMyData(myfile, varname, section, showCdl)
    }
}

fun readNcData(filename: String, varname: String? = null, section: SectionPartial? = null, showCdl : Boolean = false) {
    NClibFile(filename).use { ncfile ->
        readMyData(ncfile, varname, section, showCdl)
    }
}

fun compareCdlWithClib(filename: String, showCdl: Boolean = false) {
    println("=================")
    openNetchdfFile(filename, false).use { netchdf ->
        if (netchdf == null) {
            println("*** not a netchdf file = $filename")
            return
        }
        println("${netchdf.type()} $filename ")
        if (showCdl) println("\nnetchdf = ${netchdf.cdl()}")

        if (netchdf.type().contains("hdf4") || netchdf.type().contains("hdf-eos2")) {
            Hdf4ClibFile(filename).use { ncfile ->
                if (showCdl) println("\nHdf4ClibFile = ${ncfile.cdl()}")
                assertEquals(ncfile.cdl(), netchdf.cdl())
            }
        } else if (netchdf.type().contains("netcdf")) {
            NClibFile(filename).use { ncfile ->
                if (showCdl) println("\nNClibFile = ${ncfile.cdl()}")
                assertEquals(ncfile.cdl(), netchdf.cdl())
            }
        }  else if (netchdf.type().contains("hdf5") || netchdf.type().contains("hdf-eos5")) {
            Hdf5ClibFile(filename).use { ncfile ->
                if (showCdl) println("\nHdf5ClibFile = ${ncfile.cdl()}")
                assertEquals(ncfile.cdl(), netchdf.cdl())
            }
        } else {
            println("*** no c library to compare for $filename")
        }
    }
}

fun compareN4withH5cdl(filename: String) {
    println("=================")
    Hdf5ClibFile(filename).use { h5file ->
        if (h5file == null) {
            println("*** not an hdf5 file = $filename")
            return
        }
        println("${h5file.type()} $filename ")
        println("\nh5file = ${h5file.cdl()}")

        NClibFile(filename).use { ncfile ->
            assertEquals(h5file.cdl(), ncfile.cdl())
        }
    }
}

fun compareDataWithClib(filename: String, varname: String? = null, section: SectionPartial? = null) {
    println("=============================================================")
    openNetchdfFile(filename).use { netchdf ->
        if (netchdf == null) {
            println("*** not a netchdf file = $filename")
            return
        }
        println("${netchdf.type()} $filename ${"%.2f".format(netchdf.size / 1000.0 / 1000.0)} Mbytes")
        if (NetchdfClibTest.showCdl) println("\n${netchdf.cdl()}")

        if (netchdf.type().contains("hdf4")  || netchdf.type().contains("hdf-eos2")) {
            Hdf4ClibFile(filename).use { ncfile ->
                compareNetcdfData(netchdf, ncfile, varname, section)
            }
        } else if (netchdf.type().contains("netcdf")) {
            NClibFile(filename).use { ncfile ->
                compareNetcdfData(netchdf, ncfile, varname, section)
            }
        }  else if (netchdf.type().contains("hdf5") || netchdf.type().contains("hdf-eos5")) {
            Hdf5ClibFile(filename).use { ncfile ->
                compareNetcdfData(netchdf, ncfile, varname, section)
            }
        } else {
            println("*** no c library to compare for $filename")
        }
    }
}

fun compareIterateWithClib(filename: String, varname: String? = null, section: SectionPartial? = null) {
    println("=============================================================")
    openNetchdfFile(filename).use { netchdf ->
        if (netchdf == null) {
            println("*** not a netchdf file = $filename")
            return
        }
        println("${netchdf.type()} $filename ${"%.2f".format(netchdf.size / 1000.0 / 1000.0)} Mbytes")
        if (NetchdfClibTest.showCdl) println("\n${netchdf.cdl()}")

        if (netchdf.type().contains("hdf4")  || netchdf.type().contains("hdf-eos2")) {
            Hdf4ClibFile(filename).use { ncfile ->
                compareIterateNetchdf(netchdf, ncfile, varname, section) // LOOK should be compareIterateWithHC
            }
        } else if (netchdf.type().contains("netcdf")) {
            NClibFile(filename).use { ncfile ->
                compareIterateNetchdf(netchdf, ncfile, varname, section)
            }
        } else if (netchdf.type().contains("hdf5") || netchdf.type().contains("hdf-eos5")) {
            Hdf5ClibFile(filename).use { ncfile ->
                compareIterateNetchdf(netchdf, ncfile, varname, section)
            }
        } else {
            println("*** no c library to compare for $filename")
        }
    }
}

//////////////////////////////////////////////////////////////////////////////////////////////////////
// just read data from myfile

fun readMyData(myfile: Netchdf, varname: String? = null, section: SectionPartial? = null, showCdl : Boolean = false) {

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
        readOneVar(myvar, myfile, section)
    } else {
        myfile.rootGroup().allVariables().forEach { it ->
            readOneVar(it, myfile, null)
        }
    }
}

const val maxBytes = 10_000_000

fun readOneVar(myvar: Variable<*>, myfile: Netchdf, section: SectionPartial?) {
    val sectionF = SectionPartial.fill(section, myvar.shape)
    val nbytes = sectionF.totalElements * myvar.datatype.size
    val myvarshape = myvar.shape.toIntArray()

    if (nbytes > maxBytes) {
        if (NetchdfClibTest.showDataRead) println(" * ${myvar.fullname()} read too big: ${nbytes} > $maxBytes")
    } else {
        val mydata = myfile.readArrayData(myvar, section)
        if (NetchdfClibTest.showDataRead) println(" ${myvar.datatype} ${myvar.fullname()}${myvar.shape.contentToString()} has " +
                    "data shape = ${mydata.shape.contentToString()} ${mydata.shape.computeSize()} elems" )
        if (myvar.datatype == Datatype.CHAR) {
            testCharShape(myvarshape, mydata.shape)
        } else {
            assertTrue(myvarshape.equivalent(mydata.shape), "variable ${myvar.name}")
        }
        if (NetchdfClibTest.showData) println(mydata)
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
        val length = orgSection.shape[idx]
        if (length < 9) range
        else LongProgression.fromClosedRange(range.first + length / 3, range.last - length / 3, range.step)
    }
    val middleSection = Section(middleRanges, myvar.shape)
    val nbytes = middleSection.totalElements * myvar.datatype.size
    if (nbytes > maxBytes) {
        if (NetchdfClibTest.showDataRead) println("  * ${myvar.fullname()}[${middleSection}] read too big: ${nbytes} > $maxBytes")
        readMiddleSection(myfile, myvar, middleSection.shape)
        return
    }

    val mydata = myfile.readArrayData(myvar, SectionPartial(middleSection.ranges))
    val middleShape = middleSection.shape.toIntArray()
    if (NetchdfClibTest.showDataRead) println("  ${myvar.fullname()}[$middleSection] = ${mydata.shape.contentToString()} ${mydata.shape.computeSize()} elems")
    if (myvar.datatype == Datatype.CHAR) {
        testCharShape(middleShape, mydata.shape)
    } else {
        assertTrue(middleShape.equivalent(mydata.shape), "variable ${myvar.name}")
    }
    if (NetchdfClibTest.showData) println(mydata)
}

//////////////////////////////////////////////////////////////////////////////////////
// compare data from two Netchdf files

fun compareNetcdfData(myfile: Netchdf, cfile: Netchdf, varname: String?, section: SectionPartial? = null) {
    if (varname != null) {
        val myvar = myfile.rootGroup().allVariables().find { it.fullname() == varname }
        if (myvar == null) {
            throw RuntimeException(" *** cant find myvar $varname")
        }
        val cvar = cfile.rootGroup().allVariables().find { it.fullname() == myvar.fullname() }
        if (cvar == null) {
            throw RuntimeException(" *** cant find cvar $varname")
        }
        compareOneVar(myvar, myfile, cvar, cfile, section)
    } else {
        myfile.rootGroup().allVariables().forEach { myvar ->
            val cvar = cfile.rootGroup().allVariables().find { it.fullname() == myvar.fullname() }
            if (cvar == null) {
                println(" *** cant find ${myvar.fullname()} in cfile")
            } else {
                compareOneVar(myvar, myfile, cvar, cfile, null)
            }
        }

        cfile.rootGroup().allVariables().forEach { cvar ->
            val myvar = myfile.rootGroup().allVariables().find { it.fullname() == cvar.fullname() }
            if (myvar == null) {
                println(" *** cant find ${cvar.fullname()} in myfile")
            }
        }
    }
}


fun compareSelectedDataWithClib(filename: String, wanted : (Variable<*>) -> Boolean) {
    println("=============================================================")
    openNetchdfFile(filename).use { netchdf ->
        if (netchdf == null) {
            println("*** not a netchdf file = $filename")
            return
        }
        println("${netchdf.type()} $filename ${"%.2f".format(netchdf.size / 1000.0 / 1000.0)} Mbytes")
        if (NetchdfClibTest.showCdl) println("\n${netchdf.cdl()}")

        if (netchdf.type().contains("hdf4")  || netchdf.type().contains("hdf-eos2")) {
            Hdf4ClibFile(filename).use { ncfile ->
                compareSelectedData(netchdf, ncfile, wanted)
            }
        } else if (netchdf.type().contains("netcdf")) {
            NClibFile(filename).use { ncfile ->
                compareSelectedData(netchdf, ncfile, wanted)
            }
        }  else if (netchdf.type().contains("hdf5") || netchdf.type().contains("hdf-eos5")) {
            Hdf5ClibFile(filename).use { ncfile ->
                compareSelectedData(netchdf, ncfile, wanted)
            }
        } else {
            println("*** no c library to compare for $filename")
        }
    }
}

fun compareSelectedData(myfile: Netchdf, cfile: Netchdf, wanted : (Variable<*>) -> Boolean) {
    myfile.rootGroup().allVariables().filter { wanted(it) }. forEach { myvar ->
        val cvar = cfile.rootGroup().allVariables().find { it.fullname() == myvar.fullname() }
        if (cvar == null) {
            println(" *** cant find ${myvar.fullname()} in cfile")
        } else {
            println("   ${myvar.nameAndShape()}")
            compareOneVar(myvar, myfile, cvar, cfile, null)
        }
    }
}

fun compareOneVar(myvar: Variable<*>, myfile: Netchdf, cvar : Variable<*>, cfile: Netchdf, section: SectionPartial?) {
    val filledSection = SectionPartial.fill(section, myvar.shape)
    val nbytes = filledSection.totalElements * myvar.datatype.size

    if (nbytes > maxBytes) {
        println(" * ${myvar.fullname()} read too big = ${nbytes}")
    } else {
        val mydata = myfile.readArrayData(myvar, section)
        val ncdata = try {
            cfile.readArrayData(cvar, section)
        } catch (e : Exception) {
            println(" *** FAIL cfile.readArrayData for variable = ${cvar.datatype} ${cvar.fullname()} ${cvar.dimensions.map { it.name }}")
            throw e
        }
        val totalElems = mydata.shape.computeSize()
        println(" ${myvar.datatype} ${myvar.fullname()}[${filledSection}] = $totalElems elems" )

        //if (myvar.datatype == Datatype.CHAR) {
        //    compareCharData(myvar.fullname(), mydata, ncdata)
        //} else {
            if (!ncdata.equals(mydata)) {
                println(" *** FAIL comparing data for variable = ${cvar.datatype} ${cvar.fullname()} ${cvar.dimensions.map { it.name }}")
                if (NetchdfClibTest.showFailedData) {
                    println("\n mydata = $mydata")
                    println(" cdata = $ncdata")
                }
                val countDiffs = countArrayDiffs(ncdata, mydata, 10)
                println(" *** count values differ = $countDiffs same = ${totalElems - countDiffs}")
                assertEquals(0, countDiffs)
                // assertEquals(ncdata, mydata, "variable ${myvar.fullname()}")
                return
            } else {
                if (NetchdfClibTest.showData) {
                    print(" ${cvar.cdl()}, ")
                    print("\n mydata = $mydata")
                    print(" cdata = $ncdata")
                }
            }
        // }
    }
    if (NetchdfClibTest.compareMiddleSection && cvar.nelems > 8 && cvar.datatype != Datatype.CHAR) {
        compareMiddleSection(myfile, myvar, cfile, cvar, cvar.shape)
    }
}

fun compareMiddleSection(myfile: Netchdf, myvar: Variable<*>, cfile: Netchdf, cvar: Variable<*>, shape : LongArray) {
    val orgSection = Section(shape)
    val middleRanges = orgSection.ranges.mapIndexed { idx, range ->
        val length = orgSection.shape[idx]
        if (length < 9) range
        else LongProgression.fromClosedRange(range.first + length / 3, range.last - length / 3, range.step)
    }
    val middleSection = Section(middleRanges, myvar.shape)
    val nbytes = middleSection.totalElements * myvar.datatype.size
    if (nbytes > maxBytes) {
        if (NetchdfClibTest.showDataRead) println("  * ${myvar.fullname()}[${middleSection}] read too big: ${nbytes} > $maxBytes")
        readMiddleSection(myfile, myvar, middleSection.shape)
        return
    }

    val mydata = myfile.readArrayData(myvar, SectionPartial(middleSection.ranges))
    val ncdata = try {
        cfile.readArrayData(cvar, SectionPartial(middleSection.ranges))
    } catch (e: Exception) {
        println(" *** FAIL compareMiddleSection data for variable = ${cvar.datatype} ${cvar.fullname()} ${cvar.dimensions.map { it.name }}")
        throw e
    }
    println("  ${myvar.fullname()}[$middleSection] = ${mydata.shape.contentToString()} ${mydata.shape.computeSize()} elems")

    //if (myvar.datatype == Datatype.CHAR) {
    //    compareCharData(myvar.fullname(), mydata, ncdata)
    //} else {
        if (!ncdata.equals(mydata)) {
            println(" *** FAIL comparing middle section variable = ${cvar.nameAndShape()}")
            if (NetchdfClibTest.showFailedData) {
                println(" mydata = $mydata")
                println(" cdata = $ncdata")
            } else {
                println("\n countDifferences = ${countArrayDiffs(ncdata, mydata, 10)}")
            }
            assertTrue(false, "variable ${myvar.name}")
            return
        }
    //}
}
fun compareCharDataOld(name : String, mydata: ArrayTyped<*>, ncdata: ArrayTyped<*>) {
    if (!ArrayTyped.valuesEqual(ncdata, mydata)) {
        println("   *** FAIL comparing char variable = ${name}")
        print("   ncdata = $ncdata")
        print("   mydata = $mydata")
        assertTrue(false, "variable $name")
    }
}

//////////////////////////////////////////////////////////////////////////////////////////////////////
// just read data from myfile with iterator

fun readDataIterate(myfile: Netchdf, varname: String? = null, section: SectionPartial? = null, showCdl : Boolean = false) {

    if (showCdl) {
        println(myfile.cdl())
    }
    if (varname != null) {
        val myvar = myfile.rootGroup().allVariables().find { it.fullname() == varname }
        if (myvar == null) {
            println("cant find $varname")
            return
        }
        readOneVarIterate(myvar, myfile, section)
    } else {
        myfile.rootGroup().allVariables().forEach { it ->
            readOneVarIterate(it, myfile, null)
        }
    }
}

fun readOneVarIterate(myvar: Variable<*>, myfile: Netchdf, section: SectionPartial?) {
    val chunkIter = myfile.chunkIterator(myvar, section, maxBytes)
    val sum = AtomicDouble(0.0)
    for (pair in chunkIter) {
        sumValues(pair.array, sum)
    }
}

//////////////////////////////////////////////////////////////////////////////////////
// compare reading data chunkIterate API with two Netchdf

private const val debugIter = false

fun compareIterateNetchdf(myfile: Netchdf, cfile: Netchdf, varname: String?, section: SectionPartial? = null) {
    if (varname != null) {
        val myvar = myfile.rootGroup().allVariables().find { it.fullname() == varname }
        if (myvar == null) {
            println(" *** cant find myvar $varname")
            return
        }
        val cvar = cfile.rootGroup().allVariables().find { it.fullname() == myvar.fullname() }
        if (cvar == null) {
            throw RuntimeException(" *** cant find cvar $varname")
        }
        compareOneVarIterate(myvar, myfile, cvar, cfile, section)
    } else {
        myfile.rootGroup().allVariables().forEach { myvar ->
            val cvar = cfile.rootGroup().allVariables().find { it.fullname() == myvar.fullname() }
            if (cvar == null) {
                println(" *** cant find ${myvar.fullname()} in cfile")
            } else try {
                compareOneVarIterate(myvar, myfile, cvar, cfile, null)
            } catch (e :Throwable) {
                println(" *** FAILED ${myvar.fullname()} ${e.message}")
                throw e
            }
        }
    }
}

fun compareOneVarIterate(myvar: Variable<*>, myfile: Netchdf, cvar : Variable<*>, cfile: Netchdf, section: SectionPartial?) {
    val sum = AtomicDouble(0.0)
    sum.set(0.0)
    var countChunks = 0
    val time1 = measureNanoTime {
        val chunkIter = myfile.chunkIterator(myvar)
        for (pair in chunkIter) {
            if (debugIter) println(" compareOneVarIterate myvar=${myvar.name} ${pair.section} = ${pair.array.shape.contentToString()}")
            sumValues(pair.array, sum)
            countChunks++
        }
    }
    Stats.of("netchdf", myfile.location(), "chunk").accum(time1, countChunks)
    val sum1 = sum.get()

    sum.set(0.0)
    countChunks = 0
    val time2 = measureNanoTime {
        val chunkIter = cfile.chunkIterator(cvar)
        for (pair in chunkIter) {
            if (debugIter) println(" compareOneVarIterate cvar=${cvar.name} ${pair.section} = ${pair.array.shape.contentToString()}")
            sumValues(pair.array, sum)
            countChunks++
        }
    }
    Stats.of("nclib", cfile.location(), "chunk").accum(time2, countChunks)
    val sum2 = sum.get()

    if (sum1.isFinite() && sum2.isFinite()) {
        assertTrue(nearlyEquals(sum1, sum2), "$sum1 != $sum2 sum2")
        // println("sum = $sum1")
    }
}

///////////////////////////////////////////////////////////
/*
fun sumValues(array : ArrayTyped<*>) {
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
} */

fun countArrayDiffs(array1 : ArrayTyped<*>, array2 : ArrayTyped<*>, showDiff : Int = 0) : Int {
    val iter1 = array1.iterator()
    val iter2 = array2.iterator()
    var idx = 0
    var countDiff = 0
    while (iter1.hasNext() && iter2.hasNext()) {
        val v1 = iter1.next()
        val v2 = iter2.next()
        if (v1 != v2) {
            if (countDiff < showDiff) println("$v1 != $v2 at idx = $idx")
            countDiff++
        }
        idx++
    }
    return countDiff
}
