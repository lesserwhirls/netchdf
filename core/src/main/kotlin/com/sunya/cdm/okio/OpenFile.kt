package com.sunya.cdm.okio

import com.sunya.cdm.iosp.makeStringZ
import com.sunya.cdm.iosp.OpenFileIF
import com.sunya.cdm.iosp.OpenFileState
import okio.Buffer
import okio.FileSystem
import okio.Path.Companion.toPath

import java.nio.ByteBuffer
import java.nio.charset.Charset

/**
 * from okio.FileHandle://
 * An open file for reading and writing; using either streaming and random access.
 *
 * Use read] and write] to perform one-off random-access reads and writes. Use source], sink],
 * and appendingSink] for streaming reads and writes.
 *
 * File handles must be closed when they are no longer needed. It is an error to read, write, or
 * create streams after a file handle is closed. The operating system resources held by a file
 * handle will be released once the file handle **and** all of its streams are closed.
 *
 * Although this class offers both reading and writing APIs, file handle instances may be
 * read-only or write-only. For example, a handle to a file on a read-only file system will throw an
 * exception if a write is attempted.
 *
 * File handles may be used by multiple threads concurrently. But the individual sources and sinks
 * produced by a file handle are not safe for concurrent use.
 */
open class OpenFile(val location : String) : OpenFileIF, AutoCloseable {
    var allowTruncation = true
    val raf : okio.FileHandle
    val size : Long

    init {
        val path = location.toPath()
        raf = FileSystem.SYSTEM.openReadOnly(path)
        size = raf.size()
    }

    override fun location() = location
    override fun size() = size
    override fun close() {
        raf.close()
    }

    override fun readIntoByteArray(state : OpenFileState, dest : ByteArray, destPos : Int, nbytes : Int) : Int {
        var totalRead = 0
        var leftToRead = nbytes
        while (leftToRead > 0) {
            val nread = raf.read(state.pos, dest, destPos + totalRead, leftToRead)
            totalRead += nread
            leftToRead -= nread
        }
        state.incr(totalRead.toLong())
        return totalRead
    }

    /////////////////////////////////////////////////////////////////////////////

    private fun readBytes(state : OpenFileState, dst : ByteArray) : Int {
        return readIntoByteArray(state, dst, 0, dst.size)
    }

    override fun readByteBuffer(state : OpenFileState, nbytes : Int): ByteBuffer {
        val dst = readByteArray(state, nbytes)
        val bb = ByteBuffer.wrap(dst)
        bb.order(state.byteOrder)
        return bb
    }

    override fun readIntoByteBuffer(state : OpenFileState, dst : ByteBuffer, dstPos : Int, nbytes : Int) : Int {
        if (state.pos >= size) {
            if (allowTruncation) return 0
            throw RuntimeException("Tried to read past EOF $size at pos ${state.pos} location $location")
        }
        val bb = readByteArray(state, nbytes)
        var pos = dstPos
        for (element in bb) {
            dst.put(pos++, element)
        }
        return bb.size
    }

    override fun readByteArray(state : OpenFileState, nbytes : Int): ByteArray {
        val dst = ByteArray(nbytes)
        readBytes(state, dst)
        return dst
    }

    ///////////////////////////////////////////////////////////////////////////

    override fun readByte(state : OpenFileState): Byte {
        return readByteArray(state, 1)[0]
    }

    override fun readArrayByte(state : OpenFileState, nelems : Int): Array<Byte> {
        val dst = readByteArray(state, nelems)
        return Array(nelems) { dst[it] }
    }

    override fun readArrayUByte(state : OpenFileState, nelems : Int): Array<UByte> {
        val dst = readByteArray(state, nelems)
        return Array(nelems) { dst[it].toUByte() }
    }

    override fun readShort(state : OpenFileState): Short {
        val ba = readByteArray(state, 2)
        val buffer = Buffer().write(ba) // Could return as a buffer ??
        return if (state.isLE) buffer.readShortLe() else buffer.readShort()
    }

    override fun readArrayShort(state : OpenFileState, nelems : Int): Array<Short> {
        val ba = readByteArray(state, 2 * nelems)
        val buffer = Buffer().write(ba) // Could return as a buffer ??
        return if (state.isLE)
            Array(ba.size / 2) {  buffer.readShortLe() }
        else
            Array(ba.size / 2) {  buffer.readShort() }
    }

    override fun readArrayUShort(state : OpenFileState, nelems : Int): Array<UShort> {
        val ba = readByteArray(state, 2 * nelems)
        val buffer = Buffer().write(ba) // Could return as a buffer ??
        return if (state.isLE)
            Array(ba.size / 2) {  buffer.readShortLe().toUShort() }
        else
            Array(ba.size / 2) {  buffer.readShort().toUShort() }
    }

    override fun readInt(state : OpenFileState): Int {
        val ba = readByteArray(state, 4)
        val buffer = Buffer().write(ba) // Could return as a buffer ??
        return if (state.isLE) buffer.readIntLe() else buffer.readInt()
    }

    override fun readArrayInt(state : OpenFileState, nelems : Int): Array<Int> { // vs Array<Int>
        val ba = readByteArray(state, 4 * nelems)
        val buffer = Buffer().write(ba) // Could return as a buffer ??
        return if (state.isLE)
            Array(ba.size / 4) {  buffer.readIntLe() }
        else
            Array(ba.size / 4) {  buffer.readInt() }
    }

    override fun readArrayUInt(state : OpenFileState, nelems : Int): Array<UInt> { // vs Array<Int>
        val ba = readByteArray(state, 4 * nelems)
        val buffer = Buffer().write(ba) // Could return as a buffer ??
        return if (state.isLE)
            Array(ba.size / 4) {  buffer.readIntLe().toUInt() }
        else
            Array(ba.size / 4) {  buffer.readInt().toUInt() }
    }

    override fun readLong(state : OpenFileState): Long {
        val ba = readByteArray(state, 8)
        val buffer = Buffer().write(ba) // Could return as a buffer ??
        return if (state.isLE) buffer.readLongLe() else buffer.readLong()
    }

    override fun readArrayLong(state : OpenFileState, nelems : Int): Array<Long> {
        val ba = readByteArray(state, 8 * nelems)
        val buffer = Buffer().write(ba) // Could return as a buffer ??
        return if (state.isLE)
            Array(ba.size / 8) {  buffer.readLongLe() }
        else
            Array(ba.size / 8) {  buffer.readLong() }
    }

    override fun readArrayULong(state : OpenFileState, nelems : Int): Array<ULong> {
        val ba = readByteArray(state, 8 * nelems)
        val buffer = Buffer().write(ba) // Could return as a buffer ??
        return if (state.isLE)
            Array(ba.size / 8) {  buffer.readLongLe().toULong() }
        else
            Array(ba.size / 8) {  buffer.readLong().toULong() }
    }

    override fun readFloat(state : OpenFileState): Float {
        val ival = readInt(state)
        return Float.fromBits(ival)
    }

    override fun readArrayFloat(state : OpenFileState, nelems : Int): Array<Float>  {
        val ba = readByteArray(state, 4 * nelems)
        val buffer = Buffer().write(ba) // Could return as a buffer ??
        return if (state.isLE)
            Array(ba.size / 4) {  Float.fromBits(buffer.readIntLe()) }
        else
            Array(ba.size / 4) {  Float.fromBits(buffer.readInt()) }
    }

    override fun readDouble(state : OpenFileState): Double {
        val lval = readLong(state)
        return Double.fromBits(lval)
    }

    override fun readArrayDouble(state : OpenFileState, nelems : Int): Array<Double> {
        val ba = readByteArray(state, 8 * nelems)
        val buffer = Buffer().write(ba) // Could return as a buffer ??
        return if (state.isLE)
            Array(ba.size / 8) {  Double.fromBits(buffer.readLongLe()) }
        else
            Array(ba.size / 8) {  Double.fromBits(buffer.readLong()) }
    }

    override fun readString(state : OpenFileState, nbytes : Int, charset : Charset): String {
        val dst = ByteArray(nbytes)
        readIntoByteArray(state, dst, 0, dst.size)
        return makeStringZ(dst, 0, charset)
    }
}
