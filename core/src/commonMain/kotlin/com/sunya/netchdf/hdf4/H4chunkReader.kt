@file:OptIn(InternalLibraryApi::class)

package com.sunya.netchdf.hdf4

import com.sunya.cdm.api.*
import com.sunya.cdm.array.*
import com.sunya.cdm.layout.Chunker
import com.sunya.cdm.layout.IndexSpace
import com.sunya.cdm.util.InternalLibraryApi

internal class H4chunkReader(val h4 : H4builder) {

    private val debugChunkingDetail = false
    private val debugChunking = false
    private val debugMissing = false

    internal fun <T> readChunkedData(v2: Variable<T>, wantSection : Section) : ArrayTyped<T> {
        val vinfo = v2.spObject as Vinfo
        val elemSize = vinfo.elemSize

        val wantSpace = IndexSpace(wantSection)
        val sizeBytes = wantSpace.totalElements * elemSize
        if (sizeBytes <= 0 || sizeBytes >= Int.MAX_VALUE) {
            throw RuntimeException("Illegal nbytes to read = $sizeBytes")
        }
        val ba = ByteArray(sizeBytes.toInt())

        val tiledData = H4tiledData(h4, v2.shape, vinfo.chunkLengths, vinfo.chunks!!)
        if (debugChunking) println(" ${tiledData.tiling}")

        var count = 0
        var transferChunks = 0
        for (dataChunk: H4CompressedDataChunk in tiledData.findDataChunks(wantSpace)) { // : Iterable<BTree1New.DataChunkEntry>
            val dataSection = IndexSpace(v2.rank, dataChunk.offsets.toLongArray(), vinfo.chunkLengths.toLongArray())
            val chunker = Chunker(dataSection, wantSpace) // each dataChunk has its own Chunker iteration
            if (dataChunk.isMissing()) {
                if (debugMissing) println(" ${dataChunk.show(tiledData.tiling)}")
                val fillValue = vinfo.fillValue ?: ByteArray(elemSize)
                chunker.transferMissing(fillValue, elemSize, ba)
            } else {
                if (debugChunkingDetail and (count < 1)) println(" ${dataChunk.show(tiledData.tiling)}")
                val filteredData = dataChunk.getByteArray() // filter already applied
                chunker.transferBA(filteredData, 0, elemSize, ba, 0)
                transferChunks += chunker.transferChunks
            }
            count++
        }

        val shape = wantSpace.shape.toIntArray()
        val tba = TypedByteArray(v2.datatype, ba, 0, isBE = vinfo.isBE)
        return tba.convertToArrayTyped(shape)
    }

}