package com.sunya.netchdf.hdf4

import com.sunya.cdm.iosp.OpenFileState
import com.sunya.cdm.iosp.ReaderIntoByteArray
import okio.Buffer
import okio.BufferedSource
import okio.Source
import okio.buffer

/**
 * TODO may not be viable?
 * Similar to a DataInputStream that keeps track of position.
 * The position must always increase, no going backwards.
 * Note cant handle byte order yet - assume big endian(?).
 */
class PositioningDataInputSource(val source: Source) : ReaderIntoByteArray {
    private val delegate : BufferedSource = source.buffer()
    private var cpos: Long = 0

    override fun readIntoByteArray(state : OpenFileState, dest : ByteArray, destPos : Int, nbytes : Int) : Int {
        seek(state.pos)
        val sink = Buffer()
        val got = delegate.read(sink, nbytes.toLong())
        cpos += got

        for (it in 0 until got.toInt()) {
            dest[destPos + it] = sink.readByte()
        }
        return got.toInt()
     }

    private fun seek(pos: Long) {
        require(pos >= cpos) { "Cannot go backwards; current=$cpos request=$pos" }
        delegate.skip( pos - cpos)
        cpos = pos
    }
}
