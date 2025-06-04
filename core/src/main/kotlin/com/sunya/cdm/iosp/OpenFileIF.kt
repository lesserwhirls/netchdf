package com.sunya.cdm.iosp

import java.nio.ByteBuffer
import java.nio.charset.Charset

interface OpenFileIF : ReaderIntoByteArray, AutoCloseable {
    override fun close()
    fun location(): String
    fun size(): Long

    override fun readIntoByteArray(state: OpenFileState, dest: ByteArray, destPos: Int, nbytes: Int): Int
    fun readByteArray(state: OpenFileState, nbytes: Int): ByteArray

    fun readIntoByteBuffer(state : OpenFileState, dst : ByteBuffer, dstPos : Int, nbytes : Int) : Int
    fun readByteBuffer(state: OpenFileState, nbytes: Int): ByteBuffer

    fun readByte(state: OpenFileState): Byte
    fun readShort(state: OpenFileState): Short
    fun readInt(state: OpenFileState): Int
    fun readLong(state: OpenFileState): Long
    fun readFloat(state: OpenFileState): Float
    fun readDouble(state: OpenFileState): Double

    fun readArrayByte(state: OpenFileState, nelems: Int): Array<Byte>
    fun readArrayUByte(state: OpenFileState, nelems: Int): Array<UByte>
    fun readArrayShort(state: OpenFileState, nelems: Int): Array<Short>
    fun readArrayUShort(state: OpenFileState, nelems: Int): Array<UShort>
    fun readArrayInt(state: OpenFileState, nelems: Int): Array<Int>
    fun readArrayUInt(state: OpenFileState, nelems: Int): Array<UInt>
    fun readArrayLong(state: OpenFileState, nelems: Int): Array<Long>
    fun readArrayULong(state: OpenFileState, nelems: Int): Array<ULong>
    fun readArrayFloat(state: OpenFileState, nelems: Int): Array<Float>
    fun readArrayDouble(state: OpenFileState, nelems: Int): Array<Double>
    fun readString(state: OpenFileState, nbytes: Int, charset: Charset = Charsets.UTF_8): String
}