package com.sunya.cdm.layout

import com.sunya.cdm.api.Section
import com.sunya.cdm.api.computeSize
import com.sunya.cdm.util.InternalLibraryApi
import kotlin.math.min

/**
 * The case where you have a regular (non-chunked) layout, and you want to chunk it efficiently into
 * chunks of approx maxElems.
 * Used in Netchdf.chunkIterator() for non-chunked data.
 *
 * @param maxElems the approx size of the dataChunks to make
 * @param wantSection the requested section of data.
 */
@InternalLibraryApi
class MaxChunker(val maxElems: Int, val wantSection: Section) : AbstractIterator<IndexSpace>() {
    private val totalNelems = wantSection.totalElements
    private val rank = wantSection.rank
    private val strider = LongArray(rank)
    private val odo = IndexND(wantSection)

    init {
        var accumStride = 1L
        for (k in rank - 1 downTo 0) {
            strider[k] = accumStride
            accumStride *= wantSection.shape[k]
        }
    }

    //// iterator
    private var done: Long = 0 // done so far

    // start with the case where varshape == wantshape
    override fun computeNext() {
        if (done >= totalNelems) {
            return done()
        }

        val chunk = maxChunkShape(wantSection.shape, odo.current)
        val sectionSpace = IndexSpace(odo.current.copyOf(), chunk)
        setNext(sectionSpace)

        done += chunk.computeSize()
        odo.set(done)
    }

    fun maxChunkShape(shape: LongArray, current: LongArray): LongArray {
        // always use the full length of the innermost dimension
        val chunkShape = LongArray(rank) { idx ->
            if (idx == rank - 1) shape[idx] else {
                var size : Long = (maxElems / strider[idx])
                size = if (size == 0L) 1L else size
                min(size, shape[idx] - current[idx])
            }
        }
        return chunkShape
    }
}