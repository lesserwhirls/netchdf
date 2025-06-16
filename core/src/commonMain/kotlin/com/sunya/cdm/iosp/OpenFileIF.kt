package com.sunya.cdm.iosp

import com.fleeksoft.charset.Charset
import com.fleeksoft.charset.Charsets
import com.sunya.cdm.util.InternalLibraryApi

@OptIn(InternalLibraryApi::class)
interface OpenFileIF : ReaderIntoByteArray, AutoCloseable {
    override fun close()
    fun location(): String
    fun size(): Long

    override fun readIntoByteArray(state: OpenFileState, dest: ByteArray, destPos: Int, nbytes: Int): Int
    fun readByteArray(state: OpenFileState, nbytes: Int): ByteArray

    /* fun readIntoByteBuffer(state : OpenFileState, dst : ByteBuffer, dstPos : Int, nbytes : Int) : Int
    fun readByteBuffer(state: OpenFileState, nbytes: Int): ByteBuffer */

    fun readByte(state: OpenFileState): Byte
    fun readShort(state: OpenFileState): Short
    fun readInt(state: OpenFileState): Int
    fun readLong(state: OpenFileState): Long
    fun readFloat(state: OpenFileState): Float
    fun readDouble(state: OpenFileState): Double
    fun readString(state: OpenFileState, nbytes: Int, charset: Charset = Charsets.UTF8): String

    // generally, this is for reading Attribute values
    fun readArrayOfByte(state: OpenFileState, nelems: Int): Array<Byte>
    fun readArrayOfUByte(state: OpenFileState, nelems: Int): Array<UByte>
    fun readArrayOfShort(state: OpenFileState, nelems: Int): Array<Short>
    fun readArrayOfUShort(state: OpenFileState, nelems: Int): Array<UShort>
    fun readArrayOfInt(state: OpenFileState, nelems: Int): Array<Int>
    fun readArrayOfUInt(state: OpenFileState, nelems: Int): Array<UInt>
    fun readArrayOfLong(state: OpenFileState, nelems: Int): Array<Long>
    fun readArrayOfULong(state: OpenFileState, nelems: Int): Array<ULong>
    fun readArrayOfFloat(state: OpenFileState, nelems: Int): Array<Float>
    fun readArrayOfDouble(state: OpenFileState, nelems: Int): Array<Double>

    companion object {
        val nativeByteOrder  = false // TODO fix this
    }
}