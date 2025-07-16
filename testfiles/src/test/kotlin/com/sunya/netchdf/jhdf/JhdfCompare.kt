package com.sunya.netchdf.jhdf


import com.sunya.cdm.array.ArrayByte
import com.sunya.cdm.array.ArrayDouble
import com.sunya.cdm.array.ArrayFloat
import com.sunya.cdm.array.ArrayInt
import com.sunya.cdm.array.ArrayLong
import com.sunya.cdm.array.ArrayShort
import com.sunya.cdm.array.ArrayString
import com.sunya.cdm.array.ArrayStructureData
import com.sunya.cdm.array.ArrayTyped
import com.sunya.cdm.array.ArrayUByte
import com.sunya.cdm.util.Indent
import com.sunya.netchdf.hdf5.BitShuffleFilter
import com.sunya.netchdf.hdf5.Datatype5
import com.sunya.netchdf.hdf5.FilterRegistrar
import com.sunya.netchdf.hdf5.Lz4Filter
import com.sunya.netchdf.hdf5.LzfFilter
import com.sunya.netchdf.openNetchdfFile
import com.sunya.netchdf.testfiles.JhdfFiles
import com.sunya.netchdf.testutils.readNetchdfData
import io.jhdf.CommittedDatatype
import io.jhdf.HdfFile
import io.jhdf.api.Attribute
import io.jhdf.api.Dataset
import io.jhdf.api.Group
import io.jhdf.`object`.datatype.DataType
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.use

class JhdfCompare {
    companion object {
        @JvmStatic
        fun files(): Iterator<String> {
            return JhdfFiles.Companion.files()
        }
    }

    init {
        FilterRegistrar.registerFilter(Lz4Filter())
        FilterRegistrar.registerFilter(LzfFilter())
        FilterRegistrar.registerFilter(BitShuffleFilter())
    }

    @Test
    fun compareDataWithJhdf() {
        files().forEach { filename ->
            println("=============================")
            compareDataWithJhdf(filename)
        }
    }

    @Test
    fun compareOneWithJhdf() {
        val filename = "/home/stormy/dev/github/netcdf/jhdf/jhdf/src/test/resources/hdf5/compound_datasets_earliest.hdf5"
        compareDataWithJhdf(filename, varname="chunked_compound", showData = true)
    }

    fun compareDataWithJhdf(filename: String, varname: String? = null, showData: Boolean = false) {
        println(filename)

        openNetchdfFile(filename).use { myfile ->
            if (myfile == null) {
                println("*** not a netchdf file = $filename")
                return
            }

            val path = Paths.get(filename)
            HdfFile(path).use { hdf ->
                myfile.rootGroup().allVariables().forEach { myvar ->
                    if (varname == null || varname == myvar.name)
                        try {
                            val mydata = myfile.readArrayData(myvar)
                            println("  mydata = ${myvar.datatype} ${myvar.name}${myvar.shape.contentToString()}")
                            if (showData) println("[${mydata.showValues()}]")

                            val jhdfDataset = hdf.getDatasetByPath(myvar.fullname())
                            val jhdfData = jhdfDataset.getDataFlat();
                            println("  yrdata = ${jhdfDataset.dataType.show()} ${jhdfDataset.name}${jhdfDataset.dimensions.contentToString()}")
                            if (showData) println("[${showJhdfData(jhdfData)}]")

                            if (mydata is ArrayStructureData)
                                compareStructureData(jhdfData as Map<*, *>, mydata)
                            else
                                compareArrayData(jhdfData, mydata)
                            println()
                        } catch (e: Throwable) {
                            println("*** Exception ${e.message} for $myvar")
                            e.printStackTrace()
                        }
                }
            }
        }
    }

    fun compareArrayData(data: Any, mydata: ArrayTyped<*>) {
        when (data) {
            is BooleanArray -> {
                // why does jhdf convert H5T_STD_B8LE to BooleanArray ??
                when (mydata) {
                    is ArrayUByte ->  {
                        mydata.forEachIndexed { index, ubyte -> assertTrue( (ubyte != 0.toUByte()) == data[index]) }
                    }
                    else -> println("*** ${data.javaClass.simpleName} not compared to BooleanArray")
                }
            }
            is ByteArray ->  assertTrue(data.contentEquals ((mydata as ArrayByte).values))
            is ShortArray -> assertTrue(data.contentEquals ((mydata as ArrayShort).values))
            is IntArray -> assertTrue(data.contentEquals ((mydata as ArrayInt).values))
            is LongArray -> assertTrue(data.contentEquals ((mydata as ArrayLong).values))
            is FloatArray -> assertTrue(data.contentEquals ((mydata as ArrayFloat).values))
            is DoubleArray -> assertTrue(data.contentEquals ((mydata as ArrayDouble).values))
            is Array<*> -> {
                when (mydata) {
                    is ArrayString ->  {
                        mydata.forEachIndexed { index, ss -> assertTrue( ss == data[index]) }
                    }
                    else -> println("*** ${data.javaClass.simpleName} not compared to Array<*>")
                }
            }
            else -> println("*** ${data.javaClass.simpleName} not compared")
        }
    }


    fun compareStructureData(dataMap: Map<*, *>, mydata: ArrayStructureData) {
        val members = mydata.members
        dataMap.forEach { key, arrayjhdf ->
            val membername = key as String
            val member = members.find { it.name == membername }!!

            when (arrayjhdf) {
                is BooleanArray -> arrayjhdf.forEachIndexed { idx, datahdf -> compareData( datahdf, member.value(mydata.get(idx)))}
                is ByteArray ->  arrayjhdf.forEachIndexed { idx, datahdf -> compareData( datahdf, member.value(mydata.get(idx)))}
                is ShortArray -> arrayjhdf.forEachIndexed { idx, datahdf -> compareData( datahdf, member.value(mydata.get(idx)))}
                is IntArray -> arrayjhdf.forEachIndexed { idx, datahdf -> compareData( datahdf, member.value(mydata.get(idx)))}
                is LongArray -> arrayjhdf.forEachIndexed { idx, datahdf -> compareData( datahdf, member.value(mydata.get(idx)))}
                is FloatArray -> arrayjhdf.forEachIndexed { idx, datahdf -> compareData( datahdf, member.value(mydata.get(idx)))}
                is DoubleArray -> arrayjhdf.forEachIndexed { idx, datahdf -> compareData( datahdf, member.value(mydata.get(idx)))}
                is Array<*> -> arrayjhdf.forEachIndexed { idx, datahdf -> compareData( datahdf!!, member.value(mydata.get(idx)))}
                else -> println("*** ${arrayjhdf!!.javaClass.simpleName} not compared")
            }
        }
    }

    fun compareData(datahdf: Any, mydata: Any) {
        when (datahdf) {
            is Boolean ->  assertEquals(datahdf, (mydata != 0.toUByte()) )
            is Byte ->  assertEquals(datahdf, mydata as Byte )
            is Short ->  assertEquals(datahdf, mydata as Short )
            is Int ->  assertEquals(datahdf, mydata as Int )
            is Long ->  assertEquals(datahdf, mydata as Long )
            is Float ->  assertEquals(datahdf, mydata as Float )
            is Double ->  assertEquals(datahdf, mydata as Double )
            is String ->  {
                if (mydata is String)
                    assertEquals(datahdf, mydata)
                else if (mydata is ArrayString)
                    assertEquals(datahdf, mydata.values[0])
                else
                    println("*** ${datahdf.javaClass.simpleName} not compared as String")
            }
            is Array<*> -> {
                val mydata2 = mydata as ArrayString
                datahdf.forEachIndexed { idx, datahdf2 ->
                    val mydata2v = mydata2.values.get(idx)
                    compareData( datahdf2!!,  mydata2v)
                }
            }
            else -> println("*** ${datahdf.javaClass.simpleName} not compared")
        }
    }

    @Test
    fun showJhdfData() {
        val filename = "/home/stormy/dev/github/netcdf/jhdf/jhdf/src/test/resources/hdf5/compound_datasets_earliest.hdf5"
        showJhdfData(filename, datasetName="chunked_compound")
    }

    fun showJhdfData(filename: String, datasetName:String?) {
        println(filename)
        val path = Paths.get(filename)
        HdfFile(path).use { hdf ->
            hdf.showData("/", datasetName)
        }
    }

    fun Group.showData(parent : String, datasetName: String? = null) {
        println(" group: ${this.name} {")

        this.children.forEach { name, child ->
            if (child is Group) {
                child.showData(parent + "/" + name)
            } else if (child is Dataset) {
                if (datasetName == null || datasetName == child.name) {
                     child.showData(parent + "/" + name)
                }
            }
        }
    }

    fun Dataset.showData(path: String) {
        print("   dataset: ${this.dataType.show()} ${this.name}${this.dimensions.contentToString()} Dataset.getJavaType() = ${this.getJavaType().simpleName};")

        val lookup = hdfFile.getDatasetByPath(path)
        assertEquals(this, lookup)

        // data will be a java array of the dimensions of the HDF5 dataset
        val data = this.getDataFlat();
        println(" data javaClass = ${data.javaClass.simpleName}")

        if (data is Map<*,*>)
            println(showJhdfCompoundData(data))
        else
            println("     data = ${showJhdfData(data)}")
    }

    fun showJhdfCompoundData(dataMap: Map<*, *>)  = buildString {
        dataMap.forEach { key, arrayjhdf ->
            append(key as String)
            append(": ")
            when (arrayjhdf) {
                is BooleanArray -> arrayjhdf.forEachIndexed { idx, datahdf -> append(" ${showJhdfData(datahdf)}") }
                is ByteArray ->  arrayjhdf.forEachIndexed { idx, datahdf -> append(" ${showJhdfData( datahdf)}")  }
                is ShortArray -> arrayjhdf.forEachIndexed { idx, datahdf -> append(" ${showJhdfData( datahdf)}")  }
                is IntArray -> arrayjhdf.forEachIndexed { idx, datahdf -> append(" ${showJhdfData( datahdf)}")  }
                is LongArray -> arrayjhdf.forEachIndexed { idx, datahdf -> append(" ${showJhdfData( datahdf)}")  }
                is FloatArray -> arrayjhdf.forEachIndexed { idx, datahdf -> append(" ${showJhdfData( datahdf)}")  }
                is DoubleArray -> arrayjhdf.forEachIndexed { idx, datahdf -> append(" ${showJhdfData( datahdf)}")  }
                is Array<*> -> arrayjhdf.forEachIndexed { idx, datahdf -> append(" ${showJhdfData( datahdf!!)}")  }
                else -> append("*** ${arrayjhdf!!.javaClass.simpleName} not compared")
            }
            appendLine()
        }
    }

    fun showJhdfData(data: Any): String {
        return when ( data) {
            is BooleanArray -> data.contentToString()
            is ByteArray -> data.contentToString()
            is ShortArray -> data.contentToString()
            is IntArray -> data.contentToString()
            is LongArray -> data.contentToString()
            is FloatArray -> data.contentToString()
            is DoubleArray -> data.contentToString()
            is Array<*> -> data.contentToString()
            else -> data.toString()
        }
    }

    @Test
    fun showJhdfCdl() {
        val filename = "/home/stormy/dev/github/netcdf/jhdf/jhdf/src/test/resources/hdf5/bitfield_datasets.hdf5"
        println(filename)
        showJhdfCdl(filename, null)
    }

    fun showJhdfCdl(filename: String, dataset:String?) {
        val path = Paths.get(filename)
        val indent = Indent(2)
        HdfFile(path).use { hdf ->
            hdf.show(indent)
        }
    }

    fun Group.show(indent: Indent) {
        println("$indent group: ${this.name} {")

        this.children.forEach { name, child ->
            if (child is Group) {
                child.show(indent.incr())
            } else if (child is Dataset) {
                child.show(indent.incr())
            } else if (child is CommittedDatatype) {
                val dataType = child.getDataType()
                dataType.show(indent.incr(), child.name)
            }
        }

        // if (this.children.size > 0) println()
        this.attributes.forEach { (key, value) -> value.show(indent.incr())}
        println("$indent }")
    }

    fun Dataset.show(indent: Indent) {
        println("$indent variable: ${this.dataType.show()} ${this.name}${this.dimensions.contentToString()}")
        this.attributes.forEach { (key, value) -> value.show(indent.incr())}
    }

    fun Attribute.show(indent: Indent) {
        println("$indent attribute: ${this.name} = ${this.data} (${this.dataType.show()})")
     }

    fun DataType.show(indent: Indent, name: String) {
        val h5Datatype = Datatype5.of(this.dataClass)
        println("$indent type ${name} ${h5Datatype}")
    }

    fun DataType.show(): String {
        val h5Datatype = Datatype5.of(this.dataClass)
        return h5Datatype.toString()
    }

    /////////////////////////////////////
    @ParameterizedTest
    @MethodSource("files")
    fun testReadN3data(filename: String) {
        println(filename)
        readNetchdfData(filename, null, null, true, false)
    }

}