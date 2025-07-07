package com.sunya.netchdf.hdf5

import com.sunya.cdm.layout.Tiling

interface BTreeIF {
    fun rootNodeAddress() : Long
    fun readNode(address: Long, parent: BTreeNodeIF?) : BTreeNodeIF
    fun makeMissingDataChunkEntry(rootNode : BTreeNodeIF, wantKey : LongArray) : DataChunkEntryIF
}

interface BTreeNodeIF {
    fun isLeaf(): Boolean
    fun nentries(): Int
    fun dataChunkEntryAt(idx: Int) : DataChunkEntryIF // only if isLeaf
}

interface DataChunkEntryIF {
    fun childAddress(): Long
    fun offsets(): LongArray
    fun isMissing(): Boolean
    fun chunkSize(): Int
    fun filterMask(): Int?

    fun show(tiling : Tiling): String
}