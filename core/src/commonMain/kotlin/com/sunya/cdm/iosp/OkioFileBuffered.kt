package com.sunya.cdm.iosp

import com.fleeksoft.charset.Charset
import com.sunya.cdm.array.makeStringZ
import com.sunya.cdm.util.InternalLibraryApi
import okio.Buffer
import okio.buffer


@InternalLibraryApi
class OkioFileBuffered(val openFile : OkioFile, var fileOffset: Long) : OpenFileIF {
    var bufferedSource = openFile.raf.source(fileOffset).buffer()

    override fun location() = openFile.location()

    override fun size() = openFile.size()

    override fun close() {
        bufferedSource.close()
    }

    override fun readIntoByteArray(state : OpenFileState, dest : ByteArray, destPos : Int, nbytes : Int) : Int {
        if (state.pos > fileOffset) {
            bufferedSource.skip(state.pos - fileOffset)
            fileOffset = state.pos
        } else if (state.pos < fileOffset) {
            throw IllegalStateException("Asked to read before current position in file: ${state.pos} < ${fileOffset}")
        }
        var totalRead = 0
        var leftToRead = nbytes
        while (leftToRead > 0) {
            val nread = bufferedSource.read(dest, destPos + totalRead, leftToRead)
            totalRead += nread
            leftToRead -= nread
        }
        fileOffset += totalRead
        state.pos = fileOffset
        return totalRead
    }

    private fun readBytes(state : OpenFileState, dst : ByteArray) : Int {
        return readIntoByteArray(state, dst, 0, dst.size)
    }

    /////////////////////////////////////////////////////////////////////////////

    /* override fun readIntoByteBuffer(state : OpenFileState, dst : ByteBuffer, dstPos : Int, nbytes : Int) : Int {
        if (state.pos >= size()) {
            if (openFile.allowTruncation) return 0
            throw RuntimeException("Tried to read past EOF ${size()} at pos ${state.pos} location ${location()}")
        }
        val bb = readByteArray(state, nbytes)
        // copy to ByteBuffer TODO optimize
        var pos = dstPos
        for (element in bb) {
            dst.put(pos++, element)
        }
        return bb.size
    }

    override fun readByteBuffer(state : OpenFileState, nbytes : Int): ByteBuffer {
        val dst = readByteArray(state, nbytes)
        val bb = ByteBuffer.wrap(dst)
        bb.order(state.byteOrder)
        return bb
    } */

    override fun readByteArray(state : OpenFileState, nbytes : Int): ByteArray {
        val dst = ByteArray(nbytes)
        readBytes(state, dst)
        return dst
    }

    ///////////////////////////////////////////////////////////////////////////
    // TODO put common in one place

    override fun readByte(state : OpenFileState): Byte {
        return readByteArray(state, 1)[0]
    }

    override fun readArrayOfByte(state : OpenFileState, nelems : Int): Array<Byte> {
        val dst = readByteArray(state, nelems)
        return Array(nelems) { dst[it] }
    }

    override fun readArrayOfUByte(state : OpenFileState, nelems : Int): Array<UByte> {
        val dst = readByteArray(state, nelems)
        return Array(nelems) { dst[it].toUByte() }
    }

    override fun readShort(state : OpenFileState): Short {
        val ba = readByteArray(state, 2)
        val buffer = Buffer().write(ba) // Could return as a buffer ??
        return if (state.isBE) buffer.readShort() else buffer.readShortLe()
    }

    override fun readArrayOfShort(state : OpenFileState, nelems : Int): Array<Short> {
        val ba = readByteArray(state, 2 * nelems)
        val buffer = Buffer().write(ba) // Could return as a buffer ??
        return if (state.isBE)
            Array(ba.size / 2) {  buffer.readShort() }
        else
            Array(ba.size / 2) {  buffer.readShortLe() }
    }

    override fun readArrayOfUShort(state : OpenFileState, nelems : Int): Array<UShort> {
        val ba = readByteArray(state, 2 * nelems)
        val buffer = Buffer().write(ba) // Could return as a buffer ??
        return if (state.isBE)
            Array(ba.size / 2) {  buffer.readShort().toUShort() }
        else
            Array(ba.size / 2) {  buffer.readShortLe().toUShort() }
    }

    override fun readInt(state : OpenFileState): Int {
        val ba = readByteArray(state, 4)
        val buffer = Buffer().write(ba) // Could return as a buffer ??
        return if (state.isBE) buffer.readInt() else buffer.readIntLe()
    }

    override fun readArrayOfInt(state : OpenFileState, nelems : Int): Array<Int> { // vs Array<Int>
        val ba = readByteArray(state, 4 * nelems)
        val buffer = Buffer().write(ba) // Could return as a buffer ??
        return if (state.isBE)
            Array(ba.size / 4) {  buffer.readInt() }
        else
            Array(ba.size / 4) {  buffer.readIntLe() }
    }

    override fun readArrayOfUInt(state : OpenFileState, nelems : Int): Array<UInt> { // vs Array<Int>
        val ba = readByteArray(state, 4 * nelems)
        val buffer = Buffer().write(ba) // Could return as a buffer ??
        return if (state.isBE)
            Array(ba.size / 4) {  buffer.readInt().toUInt() }
        else
            Array(ba.size / 4) {  buffer.readIntLe().toUInt() }
    }

    override fun readLong(state : OpenFileState): Long {
        val ba = readByteArray(state, 8)
        val buffer = Buffer().write(ba) // Could return as a buffer ??
        return if (state.isBE) buffer.readLong() else buffer.readLongLe()
    }

    override fun readArrayOfLong(state : OpenFileState, nelems : Int): Array<Long> {
        val ba = readByteArray(state, 8 * nelems)
        val buffer = Buffer().write(ba) // Could return as a buffer ??
        return if (state.isBE)
            Array(ba.size / 8) {  buffer.readLong() }
        else
            Array(ba.size / 8) {  buffer.readLongLe() }
    }

    override fun readArrayOfULong(state : OpenFileState, nelems : Int): Array<ULong> {
        val ba = readByteArray(state, 8 * nelems)
        val buffer = Buffer().write(ba) // Could return as a buffer ??
        return if (state.isBE)
            Array(ba.size / 8) {  buffer.readLong().toULong() }
        else
            Array(ba.size / 8) {  buffer.readLongLe().toULong() }
    }

    override fun readFloat(state : OpenFileState): Float {
        val ival = readInt(state)
        return Float.fromBits(ival)
    }

    override fun readArrayOfFloat(state : OpenFileState, nelems : Int): Array<Float>  {
        val ba = readByteArray(state, 4 * nelems)
        val buffer = Buffer().write(ba) // Could return as a buffer ??
        return if (state.isBE)
            Array(ba.size / 4) {  Float.fromBits(buffer.readInt()) }
        else
            Array(ba.size / 4) {  Float.fromBits(buffer.readIntLe()) }
    }

    override fun readDouble(state : OpenFileState): Double {
        val lval = readLong(state)
        return Double.fromBits(lval)
    }

    override fun readArrayOfDouble(state : OpenFileState, nelems : Int): Array<Double> {
        val ba = readByteArray(state, 8 * nelems)
        val buffer = Buffer().write(ba) // Could return as a buffer ??
        return if (state.isBE)
            Array(ba.size / 8) {  Double.fromBits(buffer.readLong()) }
        else
            Array(ba.size / 8) {  Double.fromBits(buffer.readLongLe()) }
    }

    override fun readString(state : OpenFileState, nbytes : Int, charset : Charset): String {
        val dst = ByteArray(nbytes)
        readIntoByteArray(state, dst, 0, dst.size)
        return makeStringZ(dst, charset = charset)
    }
}
