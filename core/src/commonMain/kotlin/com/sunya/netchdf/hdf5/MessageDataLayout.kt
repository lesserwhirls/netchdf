@file:OptIn(InternalLibraryApi::class)

package com.sunya.netchdf.hdf5

import com.sunya.cdm.iosp.OpenFileState
import com.sunya.cdm.util.InternalLibraryApi

// Message Type 8 "Data Layout" : regular (contiguous), chunked, or compact (stored with the message)
// The dimensions were specified in version 1 and 2. In version 3 and 4, dimensions are in the Dataspace message.
// When chunked, the last dimension is the chunk size. LOOK version 4 not done.

// The Data Layout message describes how the elements of a multi-dimensional array are stored in the HDF5 file.
// Four types of data layout are supported:
//  Contiguous: The array is stored in one contiguous area of the file. This layout requires that the size of the array
//     be constant: data manipulations such as chunking, compression, checksums, or encryption are not permitted.
//     The message stores the total storage size of the array. The offset of an element from the beginning of the
//     storage area is computed as in a C array.
//  Chunked: The array domain is regularly decomposed into chunks, and each chunk is allocated and stored separately.
//     This layout supports arbitrary element traversals, compression, encryption, and checksums (these features are
//     described in other messages). The message stores the size of a chunk instead of the size of the entire array;
//     the storage size of the entire array can be calculated by traversing the chunk index that stores the chunk addresses.
//  Compact: The array is stored in one contiguous block as part of this object header message.
//  Virtual: This is only supported for version 4 of the Data Layout message. The message stores information that is
//     used to locate the global heap collection containing the Virtual Dataset (VDS) mapping information. The mapping
//     associates the VDS to the source dataset elements that are stored across a collection of HDF5 files.


internal fun H5builder.readDataLayoutMessage(state : OpenFileState) : DataLayoutMessage {
    val tstate = state.copy()
    val version = raf.readByte(tstate).toInt()
    val layoutClass = if (version > 2) raf.readByte(tstate).toInt() else raf.readByte(tstate.incr(1)).toInt()

    if (version < 3) {
        val rawdata =
            structdsl("MessageLayout", raf, state) {
                fld("version", 1)
                fld("rank", 1)
                fld("layoutClass", 1)
                skip(5)
                if (layoutClass != 0) { // not compact
                    fld("dataAddress", sizeOffsets)
                }
                array("dims", 4, "rank")
                if (layoutClass == 2) { // chunked
                    fld("chunkedElementSize", 4)
                }
                if (layoutClass == 0) { // compact
                    fld("compactDataSize", 4)
                    array("compactData", 1, "compactDataSize")
                }
            }
        if (debugMessage) rawdata.show()

        return when (layoutClass) {
            0 -> DataLayoutCompact(rawdata.getIntArray("dims"), rawdata.getByteArray("compactData"))
            1 -> DataLayoutContiguous(rawdata.getIntArray("dims"), rawdata.getLong("dataAddress"))
            2 -> DataLayoutBTreeVer1(version, rawdata.getIntArray("dims"), rawdata.getLong("dataAddress"), rawdata.getInt("chunkedElementSize"))
            else -> throw RuntimeException()
        }

    } else if (version == 3) {
        val rawdata =
            structdsl("MessageLayout3", raf, state) {
                fld("version", 1)
                fld("layoutClass", 1)
                when (layoutClass) {
                    0 -> {
                        fld("compactDataSize", 2)
                        array("compactData", 1, "compactDataSize")
                    }

                    1 -> {
                        fld("dataAddress", sizeOffsets)
                        fld("dataSize", sizeLengths)
                    }

                    2 -> { // chunked
                        fld("rank", 1) // aka dimensionality
                        fld("btreeAddress", sizeOffsets)
                        array("dims", 4,"rank")
                        fld("chunkedElementSize", 4)
                    }

                    else -> throw RuntimeException()
                }
            }
        if (debugMessage) rawdata.show()

        return when (layoutClass) {
            0 -> DataLayoutCompact3(rawdata.getByteArray("compactData"))
            1 -> DataLayoutContiguous3(rawdata.getLong("dataAddress"), rawdata.getLong("dataSize"))
            2 -> DataLayoutBTreeVer1(version, rawdata.getIntArray("dims"), rawdata.getLong("btreeAddress"), rawdata.getInt("chunkedElementSize"))
            else -> throw RuntimeException()
        }
    } else if (version == 4) {

        if (layoutClass != 2) { // layoutClass = 0 and 1 are the same as version 3
            val rawdata =
                structdsl("MessageLayout4", raf, state) {
                    fld("version", 1)
                    fld("layoutClass", 1)
                    when (layoutClass) {
                        0 -> {
                            fld("compactDataSize", 2)
                            array("compactData", 1, "compactDataSize")
                        }

                        1 -> {
                            fld("dataAddress", sizeOffsets)
                            fld("dataSize", sizeLengths)
                        }

                        3 -> {
                            // This is the address of the global heap collection where the VDS mapping entries are stored.
                            fld("heapAddress", sizeOffsets)
                            fld("index", sizeLengths)
                        }
                    }
                }
            if (debugMessage) rawdata.show()

            return when (layoutClass) {
                0 -> DataLayoutCompact3(rawdata.getByteArray("compactData"))
                1 -> DataLayoutContiguous3(rawdata.getLong("dataAddress"), rawdata.getLong("dataSize"))
                3 -> DataLayoutVirtual4(rawdata.getLong("heapAddress"), rawdata.getInt("index"))
                else -> throw RuntimeException()
            }
        }

        // version 4, layoutClass = 2 is too complex for structdls
        if (layoutClass == 2) {
            // this structure is too complex for structdls
            val version = raf.readByte(state)
            val layoutClass = raf.readByte(state)
            val flags = raf.readByte(state)
            val rank = raf.readByte(state).toInt()
            val dimSizeLength = raf.readByte(state)
            val dims = IntArray(rank) { this.readVariableSizeDimension(state, dimSizeLength) }  // TODO is dimSizeLength correct ??
            val chunkIndexingType = raf.readByte(state).toInt()
            return when (chunkIndexingType) {
                1 -> { // VII.A single chunk index
                    val chunkSize = this.readLength(state)
                    val nextBytes = raf.readByteArray(state.copy(), 40)
                    println("SingleChunk ${this.raf.location()}")
                    println("  chunkSize $chunkSize nextBytes ${nextBytes.contentToString()}")
                    // https://github.com/HDFGroup/hdf5/issues/5610
                    // The second field should be "Filter mask" for the chunk, which indicates the filter to skip for the dataset chunk.
                    // Each filter has an index number in the pipeline; if that filter is skipped, the bit corresponding to its index is set.
                    val filterMask = raf.readInt(state)
                    /* repeat ( 32) { idx ->
                        val isSet = isBitSet(filterMask, idx)
                        println("   idx = $idx  isSet = $isSet")
                    } */
                    // Address of the single chunk. size specified in “Size of Lengths” field in the superblock.
                    // The address may be undefined if the chunk or index storage is not allocated yet.
                    val chunkAddress =  this.readLength(state)
                    println("  filterMask $filterMask chunkAddress $chunkAddress")

                    DataLayoutSingleChunk4(flags, dims, chunkSize, chunkAddress)
                }
                2 -> { // VII.B implicit index
                    // Address of the array of dataset chunks.
                    val address =  raf.readLong(state) // probably wrong
                    DataLayoutImplicit4(flags, dims, address)
                }
                3 -> { // VII.C fixed array index
                    // This field contains the number of bits needed to store the maximum number of elements in a data block page.
                    val pageBits = raf.readByte(state)

                    //  Address of the index. probably points to the "Fixed Array Header" structure in the appendix
                    val indexAddress =  raf.readLong(state)
                    println("   *** FixedArray dims= ${dims.contentToString()} pageBits=$pageBits indexAddress = $indexAddress")
                    val fixedArrayIndex = if (indexAddress > 0) FixedArrayIndex(this, indexAddress) else null
                    DataLayoutFixedArray4(flags, dims, pageBits, indexAddress, fixedArrayIndex)
                }
                4 -> { // VII.D extensible array index
                    val maxBits = raf.readByte(state)
                    val indexElements = raf.readByte(state)
                    val minPointers = raf.readByte(state)
                    val minElements = raf.readByte(state)
                    val pageBits = raf.readByte(state)
                    val indexAddress =  raf.readLong(state) // probably wrong
                    DataLayoutExtensibleArray4(flags, dims, maxBits, indexElements, minPointers, minElements, pageBits, indexAddress)
                }
                5 -> { // VII.E version 2 B-tree index
                    val nodeSize = raf.readInt(state)
                    val splitPercent = raf.readByte(state)
                    val mergePercent = raf.readByte(state)
                    val heapAddress =  raf.readLong(state) // probably wrong
                    println("   *** DataLayoutBtreeVer2 dims= ${dims.contentToString()} heapAddress = $heapAddress")
                    DataLayoutBtreeVer2(flags, dims, nodeSize, splitPercent, mergePercent, heapAddress)
                }
                else -> throw RuntimeException()
            }
            val address = raf.readLong(state) // TODO read address ??
        }
    }
    throw RuntimeException()
}

//////////////////////////////////////////////////////////////////////////////////////////
internal val dataLayoutNames = listOf(
        DataLayoutCompact::class.simpleName,
        DataLayoutContiguous::class.simpleName,
        DataLayoutBTreeVer1::class.simpleName,
        DataLayoutCompact3::class.simpleName,
        DataLayoutContiguous3::class.simpleName,
        DataLayoutVirtual4::class.simpleName,
        DataLayoutSingleChunk4::class.simpleName,
        DataLayoutImplicit4::class.simpleName,
        DataLayoutFixedArray4::class.simpleName,
        DataLayoutExtensibleArray4::class.simpleName,
        DataLayoutBtreeVer2::class.simpleName,
    )

// debugging
internal fun getDataLayoutCounts() : MutableMap<String, Int> {
    return dataLayoutNames.map { Pair(it!!, 0) }.toMap().toMutableMap()
}

////////////////////////////////////////////////////////////////////////////////////////

internal open class DataLayoutMessage() : MessageHeader(MessageType.Layout) {
    val isCompact = (this is DataLayoutCompact) || (this is DataLayoutCompact3)
    val isContiguous = (this is DataLayoutContiguous) || (this is DataLayoutContiguous3)
    override fun show() : String = "${this::class.simpleName}"
}

// 1 & 2
internal data class DataLayoutCompact(val dims : IntArray, val compactData: ByteArray) : DataLayoutMessage()

internal data class DataLayoutContiguous(val dims : IntArray, val dataAddress: Long) : DataLayoutMessage() {
    override fun show() : String = "${super.show()} dims=${dims.contentToString()} dataAddress=$dataAddress"
}
internal data class DataLayoutBTreeVer1(val version : Int, val chunkDims : IntArray, val btreeAddress: Long, val chunkedElementSize : Int)
    : DataLayoutMessage() {
    override fun show(): String = "${super.show()} dims=${chunkDims.contentToString()} btreeAddress=$btreeAddress chunkedElementSize=$chunkedElementSize"
}

// 3
internal data class DataLayoutCompact3(val compactData: ByteArray) : DataLayoutMessage()

internal data class DataLayoutContiguous3(val dataAddress: Long, val dataSize: Long) : DataLayoutMessage() {
    override fun show(): String = "${super.show()} dataAddress=$dataAddress dataSize=$dataSize"
}

// 4
internal data class DataLayoutSingleChunk4(val flags: Byte, val dims: IntArray, val chunkSize: Long, val heapAddress: Long) : DataLayoutMessage() {
    override fun show(): String = "${super.show()} flags=$flags dims=$dims chunkSize=$chunkSize heapAddress=$heapAddress"
}
internal data class DataLayoutImplicit4(val flags: Byte, val dims: IntArray, val address: Long) : DataLayoutMessage() {
    override fun show(): String = "${super.show()} flags=$flags dims=$dims address=$address"
}
internal data class DataLayoutFixedArray4(val flags: Byte, val dims: IntArray, val pageBits: Byte, val indexAddress: Long, val fixedArrayIndex: FixedArrayIndex?) : DataLayoutMessage() {
    override fun show(): String = "${super.show()} flags=$flags dims=$dims pageBits=$pageBits fixedArrayIndex=$fixedArrayIndex"
}
internal data class DataLayoutExtensibleArray4(val flags: Byte, val dims: IntArray, val maxBits: Byte, val indexElements: Byte,
        val minPointers: Byte, val minElements: Byte, val pageBits: Byte, val indexAddress: Long) : DataLayoutMessage() {
    override fun show(): String = "${super.show()} flags=$flags dims=$dims maxBits=$maxBits indexElements=$indexElements " +
            "minPointers=$minPointers minElements=$minPointers pageBits=$pageBits indexAddress=$indexAddress"
}
internal data class DataLayoutBtreeVer2(val flags: Byte, val dims: IntArray, val nodeSize: Int, val splitPercent: Byte, val mergePercent: Byte, val heapAddress: Long)
    : DataLayoutMessage() {
    override fun show(): String = "${super.show()} flags=$flags dims=$dims nodeSize=$nodeSize splitPercent=$splitPercent nodeSize=$nodeSize " +
            "mergePercent = $mergePercent heapAddress=$heapAddress"
}
internal data class DataLayoutVirtual4(val heapAddress: Long, val index: Int) : DataLayoutMessage() {
    override fun show(): String = "${super.show()} heapAddress=$heapAddress index=$index"
}

//////////////////////////////////////////////////////////////////////////
// TODO probably want to defer this until reading?
internal class FixedArrayIndex(val h5: H5builder, address: Long) {
    val elementType : Int
    val entrySize : Int
    val pageBits : Int // This field contains the number of bits needed to store the maximum number of elements in a data block page. bits ??

    var nonFilteredChunks = mutableListOf<Long>()
    var filteredChunks = mutableListOf<FilteredChunk>()

    init {
        val state = OpenFileState(h5.getFileOffset(address), false)
        val raf = h5.raf

        // header
        val magic = raf.readString(state, 4)
        check(magic == "FAHD") { "$magic should equal FAHD" }
        val version: Byte = raf.readByte(state)
        elementType = raf.readByte(state).toInt() // aka "Client Id"
        entrySize = raf.readByte(state).toInt()
        pageBits = raf.readByte(state).toInt() // presumably this replicates the MessageDataLayout
        val maxEntries = h5.readLength(state)
        val dataAddress = h5.readOffset(state)
        state.pos = dataAddress

        val magic2 = raf.readString(state, 4)
        check(magic2 == "FADB") { "$magic2 should equal FADB" }
        val versionAgain: Byte = raf.readByte(state)
        val clientId: Byte = raf.readByte(state) // presumably this replicates the elementType
        val headerAddress = h5.readOffset(state) // used for file integrity checking.

        for (i in 0 until maxEntries) {
            //if (paged) { // must be in the superblock ??
            //    readPagedBitmap()
            //} else {
            val state2 = state.copy()
            val nextBytes1 = raf.readByteArray(state2, 14)
            val nextBytes2 = raf.readByteArray(state2, 14)
            if (elementType == 0) {
                nonFilteredChunks.add(raf.readLong(state))
            } else {
                val address = h5.readOffset(state)
                val fileOffset = h5.getFileOffset(address) // TODO
                val chunkSize = raf.readShort(state).toInt() // TODO Chunk Size (variable size; at most 8 bytes) h5.readVariableLengthSize(state, pageBits)
                val filterMask = raf.readInt(state)  // TODO filter mask is 0, but chunkSize is 13. There is a filterPipeline message with shuffle, deflate.
                filteredChunks.add(FilteredChunk(address, chunkSize, filterMask))
            }
            //}
            // could do pos += entrySize
        }
        // do we need the checksum to keep our file pos ??
        // al checksum = raf.readLong(state)
    }

    override fun toString(): String {
        return "FixedArrayIndex(entrySize=$entrySize, nonFilteredChunks=${nonFilteredChunks.size}, filteredChunks=${filteredChunks.size})"
    }

}

internal data class FilteredChunk(val address: Long, val chunkSize : Int, val filterMask: Int)