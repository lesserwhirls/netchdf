package com.sunya.netchdf.jhdf


import com.sunya.cdm.array.ArrayByte
import com.sunya.cdm.array.ArrayDouble
import com.sunya.cdm.array.ArrayFloat
import com.sunya.cdm.array.ArrayInt
import com.sunya.cdm.array.ArrayLong
import com.sunya.cdm.array.ArrayShort
import com.sunya.cdm.array.ArrayString
import com.sunya.cdm.array.ArrayTyped
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

    fun compareDataWithJhdf(filename: String) {
        println(filename)

        try {
            openNetchdfFile(filename).use { myfile ->
                if (myfile == null) {
                    println("*** not a netchdf file = $filename")
                    return
                }

                val path = Paths.get(filename)
                HdfFile(path).use { hdf ->

                    myfile.rootGroup().allVariables().forEach { myvar ->
                        val mydata = myfile.readArrayData(myvar)
                        println("  mydata = ${myvar.datatype} ${myvar.name}${myvar.shape.contentToString()}")
                                // + "[${mydata.showValues()}]")

                        val jhdfDataset = hdf.getDatasetByPath(myvar.fullname())
                        val jhdfData = jhdfDataset.getDataFlat();
                        println("  yrdata = ${jhdfDataset.dataType.show()} ${jhdfDataset.name}${jhdfDataset.dimensions.contentToString()}")
                               //  "${showJhdfData(jhdfData)}")
                        compareBinaryData(jhdfData, mydata)
                        println()
                    }

                }
            }
        } catch (e: Throwable) {
            println("*** Exception ${e.message}")
        }
    }

    fun compareBinaryData(data: Any, mydata: ArrayTyped<*>) {
        when (data) {
            // is BooleanArray -> assertEquals(data, mydata as
            is ByteArray ->  assertTrue(data.contentEquals ((mydata as ArrayByte).values))
            is ShortArray -> assertTrue(data.contentEquals ((mydata as ArrayShort).values))
            is IntArray -> assertTrue(data.contentEquals ((mydata as ArrayInt).values))
            is LongArray -> assertTrue(data.contentEquals ((mydata as ArrayLong).values))
            is FloatArray -> assertTrue(data.contentEquals ((mydata as ArrayFloat).values))
            is DoubleArray -> assertTrue(data.contentEquals ((mydata as ArrayDouble).values))
            // is Array<*> -> assertEquals(data, (mydata as ArrayString.values)
            else -> println("*** ${data.javaClass.simpleName} not compared")
        }
    }

    @Test
    fun showJhdfData() {
        val filename = "/home/stormy/dev/github/netcdf/jhdf/jhdf/src/test/resources/hdf5/bitfield_datasets.hdf5"
        println(filename)
        showJhdfData(filename, null)
    }

    fun showJhdfData(filename: String, dataset:String?) {
        val path = Paths.get(filename)

        HdfFile(path).use { hdf ->
            hdf.showData("/")
        }
    }

    fun Group.showData(parent : String) {
        println(" group: ${this.name} {")

        this.children.forEach { name, child ->
            if (child is Group) {
                child.showData(parent + "/" + name)
            } else if (child is Dataset) {
                child.showData(parent + "/" + name)
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

        println("     data = ${showJhdfData(data)}")
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