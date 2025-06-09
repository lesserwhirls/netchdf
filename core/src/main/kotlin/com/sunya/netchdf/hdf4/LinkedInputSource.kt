package com.sunya.netchdf.hdf4

import com.sunya.cdm.iosp.OpenFileState
import okio.Buffer
import okio.Source
import okio.Timeout

/** Make a linked list of data segments look like a Source. */
internal class LinkedInputSource(val h4 : H4builder,
                                 val nsegs: Int,
                                 val segPosA: LongArray,
                                 val segSizeA: IntArray) : Source {

    constructor(h4 : H4builder, vinfo: Vinfo) : this(h4, vinfo.segSize.size, vinfo.segPos, vinfo.segSize)

    private val state = OpenFileState(0, true)
    private var segno = -1
    private var segpos = 0
    private var segSize = 0
    private var buffer = ByteArray(0)
    private var exhausted = false

    // var size = 0L // TODO

    //     override fun read(): Int {
    //        if (segpos == segSize) {
    //            val ok = readSegment()
    //            if (!ok) return -1
    //        }
    //        val b = buffer[segpos].toInt() and 0xff
    //        segpos++
    //        return b
    //    }

    override fun read(sink: Buffer, byteCount: Long): Long {
        if (exhausted) return -1

        require(byteCount >= 0L) { "byteCount < 0: $byteCount" }
        val asked = byteCount.toInt()
        var gave = 0
        val ba = ByteArray(asked)
        for (i in 0 until asked) {
            val ib = read()
            if (ib == -9999) break
            ba[i] = ib.toByte()
            gave++
        }
        val source = Buffer()
        source.write(ba)
        sink.write(source, gave.toLong())
        return gave.toLong()
    }

    private fun read(): Int {
        if (segpos == segSize) {
            val ok = readSegment()
            if (!ok) return -9999
        }
        val b = buffer[segpos].toInt() and 0xff
        segpos++
        return b
    }

    // return false when all done
    private fun readSegment(): Boolean {
        segno++
        if (segno == nsegs) {
            exhausted = true
            return false
        }
        segSize = segSizeA[segno]
        while (segSize == 0) { // for some reason may have a 0 length segment
            segno++
            if (segno == nsegs) return false
            segSize = segSizeA[segno]
        }

        state.pos = segPosA[segno]
        buffer = h4.raf.readByteArray(state, segSize)
        segpos = 0
        return true
    }

    override fun timeout() = Timeout.NONE

    override fun close() {
        // NOP
    }
}

// just read all the bytes in from SpecialLinked
internal fun readSpecialLinkedInputSource(h4: H4builder, linked: SpecialLinked): ByteArray {
    val linkedBlocks = linked.getLinkedDataBlocks(h4)
    val nsegs = linkedBlocks.size
    val segPos = LongArray(nsegs) { idx -> linkedBlocks[idx].offset }
    val segSize = IntArray(nsegs) { idx -> linkedBlocks[idx].length }
    val totalSize = segSize.sum()
    val inputSource = LinkedInputSource(h4, nsegs, segPos, segSize)
    val sink = Buffer()
    val nbytesRead = inputSource.read(sink, totalSize.toLong())

    val result = ByteArray(nbytesRead.toInt())
    for (it in 0 until nbytesRead.toInt()) {
        result[it] = sink.readByte()
    }
    return result
}