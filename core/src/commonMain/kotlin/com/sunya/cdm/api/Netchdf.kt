package com.sunya.cdm.api

import com.sunya.cdm.array.ArrayTyped
import com.sunya.cdm.iosp.ReadChunkConcurrent
import com.sunya.cdm.util.CdmFullNames

interface Netchdf : AutoCloseable {
    fun location() : String
    fun type() : String
    val size : Long get() = 0L
    fun rootGroup() : Group
    fun cdl() : String

    fun findVariable(fullName: String): Variable<*>? {
        return CdmFullNames(rootGroup()).findVariable(fullName)
    }

    fun readArrayDataAll(v2: Variable<*>) : ArrayTyped<*> {
        return  readArrayData(v2, null)
    }

    fun readArrayDataIF(v2: Variable<*>, section: SectionPartial? = null) : ArrayTyped<*> {
        return  readArrayData(v2, section)
    }

    // TODO I think the output type is not always the input type
    fun <T> readArrayData(v2: Variable<T>, section: SectionPartial? = null) : ArrayTyped<T>

    fun <T> chunkIterator(v2: Variable<T>, section: SectionPartial? = null, maxElements : Int? = null) : Iterator<ArraySection<T>>
}

// the section describes the array chunk reletive to the variable's shape.
data class ArraySection<T>(val array : ArrayTyped<T>, val section : Section)

// Experimental: read concurrent chunks of data, call back with them in ArraySection, order is arbitrary.
fun <T> Netchdf.chunkConcurrent(v2: Variable<T>, section: SectionPartial? = null, maxElements : Int? = null, nthreads: Int = 20, lamda : (ArraySection<T>) -> Unit) {
    val reader = ReadChunkConcurrent()
    val chunkIter = this.chunkIterator( v2, section, maxElements)
    reader.readChunks(nthreads, chunkIter, lamda)
}