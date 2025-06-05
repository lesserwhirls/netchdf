package com.sunya.cdm.iosp

import java.io.EOFException
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

/** An abstraction over a Java FileChannel. */
data class OpenFile(val location : String) : OpenFileIF {
    private var allowTruncation = true
    val raf : com.sunya.io.RandomAccessFile = com.sunya.io.RandomAccessFile(location, "r")
    private val fileChannel : FileChannel = raf.fileChannel
    val size : Long

    init {
        raf.order(ByteOrder.LITTLE_ENDIAN)
        size = raf.length()
    }


    override fun location() = location
    override fun size() = size

    override fun close() {
        raf.close()
    }

    private fun readIntoByteBufferDirect(state : OpenFileState, dst : ByteBuffer, dstPos : Int, nbytes : Int) : Int {
        if (nbytes < 4000) {
            return readIntoByteBuffer(state, dst, dstPos, nbytes)
        }

        if (state.pos >= fileChannel.size()) {
            throw EOFException("Tried to read past EOF ${fileChannel.size()} at pos ${state.pos} location $location")
        }
        // this is what fileChannel.read uses to read into dst; so limit and pos are getting munged
        dst.limit(dstPos + nbytes)
        dst.position(dstPos)
        try {
            val nread = fileChannel.read(dst, state.pos)
            if (nread != nbytes) {
                throw EOFException("nread != nwanted at pos ${state.pos} location $location EOF=${fileChannel.size()}")
            }
            state.pos += nread
            return nread
        } catch ( ioe : IOException) {
            println("Got error on $location")
            ioe.printStackTrace()
            throw ioe
        }
    }

    override fun readIntoByteArray(state : OpenFileState, dest : ByteArray, destPos : Int, nbytes : Int) : Int {
        return readIntoByteBufferDirect(state, ByteBuffer.wrap(dest), destPos, nbytes)
    }

    private fun readIntoByteBuffer(state : OpenFileState, dst : ByteBuffer, dstPos : Int, nbytes : Int) : Int {
        if (state.pos >= size) {
            if (allowTruncation) return 0
            throw EOFException("Tried to read past EOF $size at pos ${state.pos} location $location")
        }
        val bb = readByteArray(state, nbytes)
        var pos = dstPos
        for (element in bb) {
            dst.put(pos++, element)
        }
        return bb.size
    }

    private fun readByteBuffer(state : OpenFileState, nbytes : Int): ByteBuffer {
        val dst = readByteArray(state, nbytes)
        val bb = ByteBuffer.wrap(dst)
        bb.order(if (state.isBE) ByteOrder.BIG_ENDIAN else ByteOrder.LITTLE_ENDIAN)
        return bb
    }

    private fun readBytes(state : OpenFileState, dst : ByteArray) : Int {
        raf.seek(state.pos)
        raf.order(if (state.isBE) ByteOrder.BIG_ENDIAN else ByteOrder.LITTLE_ENDIAN)
        val nread = raf.read(dst)
        if (nread != dst.size && !allowTruncation) {
            throw EOFException("Only read $nread bytes of wanted ${dst.size} bytes; starting at pos ${state.pos} EOF=${size}")
        }
        state.pos += nread
        return nread
    }

    override fun readByteArray(state : OpenFileState, nbytes : Int) : ByteArray {
        val dst = ByteArray(nbytes)
        readBytes(state, dst)
        return dst
    }

    override fun readByte(state : OpenFileState): Byte {
        raf.seek(state.pos)
        raf.order(if (state.isBE) ByteOrder.BIG_ENDIAN else ByteOrder.LITTLE_ENDIAN)
        state.pos++
        return raf.readByte()
    }

    override fun readDouble(state : OpenFileState): Double {
        raf.seek(state.pos)
        raf.order(if (state.isBE) ByteOrder.BIG_ENDIAN else ByteOrder.LITTLE_ENDIAN)
        state.pos += 8
        return raf.readDouble()
    }

    override fun readFloat(state : OpenFileState): Float {
        raf.seek(state.pos)
        raf.order(if (state.isBE) ByteOrder.BIG_ENDIAN else ByteOrder.LITTLE_ENDIAN)
        state.pos += 4
        return raf.readFloat()
    }

    override fun readInt(state : OpenFileState): Int {
        raf.seek(state.pos)
        raf.order(if (state.isBE) ByteOrder.BIG_ENDIAN else ByteOrder.LITTLE_ENDIAN)
        state.pos += 4
        return raf.readInt()
    }

    override fun readLong(state : OpenFileState): Long {
        raf.seek(state.pos)
        raf.order(if (state.isBE) ByteOrder.BIG_ENDIAN else ByteOrder.LITTLE_ENDIAN)
        state.pos += 8
        return raf.readLong()
    }

    override fun readShort(state : OpenFileState): Short {
        raf.seek(state.pos)
        raf.order(if (state.isBE) ByteOrder.BIG_ENDIAN else ByteOrder.LITTLE_ENDIAN)
        state.pos += 2
        return raf.readShort()
    }

    fun readString(state : OpenFileState, nbytes : Int): String {
        return readString(state, nbytes, Charsets.UTF_8)
    }

    override fun readString(state : OpenFileState, nbytes : Int, charset : Charset): String {
        val dst = ByteArray(nbytes)
        readIntoByteArray(state, dst, 0, dst.size)
        return makeStringZ(dst, 0, charset)
    }

    override fun readArrayOfByte(state : OpenFileState, nelems : Int): Array<Byte> {
        val dst = readByteBuffer(state, nelems)
        return Array(nelems) { dst[it] }
    }

    override fun readArrayOfUByte(state : OpenFileState, nelems : Int): Array<UByte> {
        val dst = readByteBuffer(state, nelems)
        return Array(nelems) { dst[it].toUByte() }
    }

    override fun readArrayOfShort(state : OpenFileState, nelems : Int): Array<Short> {
        val dst = readByteBuffer(state, 2 * nelems).asShortBuffer()
        return Array(nelems) { dst[it] }
    }

    override fun readArrayOfUShort(state : OpenFileState, nelems : Int): Array<UShort> {
        val dst = readByteBuffer(state, 2 * nelems).asShortBuffer()
        return Array(nelems) { dst[it].toUShort() }
    }

    override fun readArrayOfInt(state : OpenFileState, nelems : Int): Array<Int> {
        val dst = readByteBuffer(state, 4 * nelems).asIntBuffer()
        return Array(nelems) { dst[it] }
    }

    override fun readArrayOfUInt(state : OpenFileState, nelems : Int): Array<UInt> {
        val dst = readByteBuffer(state, 4 * nelems).asIntBuffer()
        return Array(nelems) { dst[it].toUInt() }
    }

    override fun readArrayOfLong(state : OpenFileState, nelems : Int): Array<Long> {
        val dst = readByteBuffer(state, 8 * nelems).asLongBuffer()
        return Array(nelems) { dst[it] }
    }

    override fun readArrayOfULong(state : OpenFileState, nelems : Int): Array<ULong> {
        val dst = readByteBuffer(state, 8 * nelems).asLongBuffer()
        return Array(nelems) { dst[it].toULong() }
    }

    override fun readArrayOfFloat(state : OpenFileState, nelems : Int): Array<Float> {
        val dst = readByteBuffer(state, 4 * nelems).asFloatBuffer()
        return Array(nelems) { dst[it] }
    }

    override fun readArrayOfDouble(state : OpenFileState, nelems : Int): Array<Double> {
        val dst = readByteBuffer(state, 8 * nelems).asDoubleBuffer()
        return Array(nelems) { dst[it] }
    }
}