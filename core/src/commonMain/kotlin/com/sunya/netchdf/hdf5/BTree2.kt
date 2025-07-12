package com.sunya.netchdf.hdf5

import com.sunya.cdm.iosp.OpenFileIF
import com.sunya.cdm.iosp.OpenFileState
import com.sunya.cdm.layout.Tiling
import com.sunya.cdm.util.InternalLibraryApi
import kotlin.math.ceil
import kotlin.math.pow

@OptIn(InternalLibraryApi::class)
internal class BTree2(private val h5: H5builder, owner: String, address: Long, val ndimStorage: Int) : BTreeIF { // BTree2
    val btreeType: Int
    private val nodeSize: Int // size in bytes of btree nodes
    private val recordSize: Int // size in bytes of btree records
    private val owner: String
    private val raf: OpenFileIF
    val rootNodeAddress: Long
    val treeDepth : Short
    val numRecordsRootNode : Int
    val totalNumberOfRecordsInTree: Int

    init {
        raf = h5.raf
        this.owner = owner
        val state = OpenFileState(h5.getFileOffset(address), false)

        // header
        val magic = raf.readString(state, 4)
        check(magic == "BTHD") { "$magic should equal BTHD" }
        val version: Byte = raf.readByte(state)
        btreeType = raf.readByte(state).toInt()
        nodeSize = raf.readInt(state) // This is the size in bytes of all B-tree nodes.
        recordSize = raf.readShort(state).toUShort().toInt() // This field is the size in bytes of the B-tree record.
        treeDepth = raf.readShort(state)

        val splitPct = raf.readByte(state)
        val mergePct = raf.readByte(state)
        rootNodeAddress = h5.readOffset(state)
        numRecordsRootNode = raf.readShort(state).toUShort().toInt()
        totalNumberOfRecordsInTree = h5.readLength(state).toInt() // total in entire btree

        val checksum: Int = raf.readInt(state)
    }

    override fun rootNodeAddress() = rootNodeAddress

    override fun makeMissingDataChunkEntry(rootNode: BTreeNodeIF, wantKey: LongArray): DataChunkEntryIF {
        // val parent = rootNode as Node
        //val key = BTree1.DataChunkKey(-1, 0, wantKey)
        return MissingDataChunk() // DataChunkEntry1(0, parent, -1, key, -1L)
    }

    // read all the entries in; used for non-data btrees
    fun readEntries() : List<Btree2Entry> {
        /* TODO
        val entryList = mutableListOf<Btree2Entry>()

        if (rootNodeAddress > 0) {
            if (treeDepth > 0) {
                val node = InternalNode(rootNodeAddress, numRecordsRootNode)
                node.recurse(entryList)
            } else {
                val leaf = LeafNode(rootNodeAddress, numRecordsRootNode)
                leaf.addEntries(entryList)
            }
        }

        return entryList

         */
        return emptyList()
    }

    override fun readNode(address: Long, parent: BTreeNodeIF?): BTreeNodeIF {
        val state = OpenFileState(h5.getFileOffset(address), false)

        // header
        val magic = raf.readString(state, 4)
        if (magic == "BTIN") {
            return InternalNode(state, address, treeDepth.toInt()) as BTreeNodeIF
        } else if (magic == "BTLF") {
            return LeafNode(state, address, 0) as BTreeNodeIF // TODO
        } else {
            throw RuntimeException("$magic unknown tag")
        }

        //val nrecords = nodeSize / recordSize // ??
        // TODO dont know if its a leaf node or not
        //return LeafNode(address, nrecords.toShort()) as BTreeNodeIF
    }

    internal inner class InternalNode(state: OpenFileState, val address: Long, val depth: Int) : BTreeNodeIF {
        var entries: Array<Btree2Entry?>

        init {
            val version: Byte = raf.readByte(state)
            val nodeType = raf.readByte(state).toInt() // same as the B-tree type in the header
            check(nodeType == btreeType)

            // 		for (int i = 0; i < numberOfRecords; i++) {
            //			records.add(readRecord(type, createSubBuffer(bb, recordSize), datasetInfo));
            //		}
            entries = arrayOfNulls(numRecordsRootNode + 1) // did i mention theres actually n+1 children?
            repeat(numRecordsRootNode) {
                entries[it] = Btree2Entry()
                entries[it]!!.record = readRecord(state, btreeType)
            }
            entries[numRecordsRootNode] = Btree2Entry()

            // Records: The size of this field is determined by the number of records for this node and the record size (from the header).
            
            // maximum possible number of records able to be stored in the child node.
            val maxNumRecords = nodeSize / recordSize // TODO see long calculation description in Fields: Version 2 B-tree Internal Node
            // maximum possible number of records able to be stored in the child node and its descendants
            val maxNumRecordsPlusDesc = nodeSize / recordSize // TODO see long calculation description in Fields: Version 2 B-tree Internal Node

            repeat(numRecordsRootNode + 1) {
                val e = entries[it]
                e!!.childAddress = h5.readOffset(state) // Child Node Pointer
                val sizeOfNumberOfRecords = getSizeOfNumberOfRecords(nodeSize, depth, totalNumberOfRecordsInTree, recordSize, h5.sizeOffsets)
                val numberOfChildRecords: Int = h5.readVariableSizeUnsigned(state, sizeOfNumberOfRecords).toInt() // readBytesAsUnsignedInt(bb, sizeOfNumberOfRecords)
                val sizeNumberOfChildRecords = getSizeOfTotalNumberOfChildRecords(nodeSize, depth, recordSize)
                val totalNumberOfChildRecords = if (depth > 1) {
                    h5.readVariableSizeUnsigned(state, sizeNumberOfChildRecords)
                } else {
                    -1
                }
                e.nrecords = totalNumberOfChildRecords
            }

            // skip
            raf.readInt(state)
        }

        /* TODO
        fun recurse(entryList : MutableList<Btree2Entry>) {
            for (entry in entries) {
                if (depth > 1) {
                    val node = InternalNode(entry!!.childAddress, entry.nrecords.toShort(), recordSize, depth - 1)
                    node.recurse(entryList)
                } else {
                    val nrecs = entry!!.nrecords
                    val leaf = LeafNode(entry.childAddress, nrecs.toShort())
                    leaf.addEntries(entryList)
                }
                if (entry.record != null) { // last one is null
                    entryList.add(entry)
                }
            }
        }

         */

        override fun isLeaf() = false
        override fun nentries() = entries.size
        override fun dataChunkEntryAt(idx: Int): DataChunkEntryIF {
            throw RuntimeException("dataChunkEntryAt only for leaf nodes")
        }
    }

    // problem is we need nrecords. Thinking we can derive it from the recordSize?
    internal inner class LeafNode(state: OpenFileState, val address: Long, val nrecords: Int) : BTreeNodeIF {
        val entries = mutableListOf<Btree2Entry>()

        init {
            val version: Byte = raf.readByte(state)
            val nodeType = raf.readByte(state).toInt()
            check(nodeType == btreeType)

            for (i in 0 until nrecords) {
                val entry = Btree2Entry()
                entry.record = readRecord(state, btreeType)
                entries.add(entry)
            }

            // skip checksum i guess
            raf.readInt(state)
        }

        fun addEntries(list: MutableList<Btree2Entry>) {
            list.addAll(entries)
        }

        override fun isLeaf() = true
        override fun nentries() = entries.size
        override fun dataChunkEntryAt(idx: Int) = entries[idx]
    }

    // heroic jhdf
    private fun getSizeOfNumberOfRecords(
        nodeSize: Int,
        depth: Int,
        totalRecords: Int,
        recordSize: Int,
        sizeOfOffsets: Int
    ): Int {
        val NODE_OVERHEAD_BYTES = 10
        var size: Int = nodeSize - NODE_OVERHEAD_BYTES

        // If the child is not a leaf
        if (depth > 1) {
            // Need to subtract the pointers as well
            val pointerTripletBytes = bytesNeededToHoldNumber(totalRecords) * 2 + sizeOfOffsets
            size -= pointerTripletBytes

            return bytesNeededToHoldNumber(size / recordSize)
        } else {
            // Its a leaf
            return bytesNeededToHoldNumber(size / recordSize)
        }
    }

    // jhdf
    private fun bytesNeededToHoldNumber(number: Int): Int {
        return (Integer.numberOfTrailingZeros(Integer.highestOneBit(number)) + 8) / 8
    }

    /* private fun getSizeOfTotalNumberOfChildRecords(nodeSize: Int, depth: Int, recordSize: Int): Int {
        require (nodeSize % recordSize == 0)
        val recordsInLeafNode = (nodeSize / recordSize).toDouble()
        val totalRecords = recordsInLeafNode.pow(depth)
        val totalBits = log2(totalRecords)
        val totalBitsInt = totalBits.toInt()
        return (totalBitsInt + 8) / 8
    } */

    // no BigInteger, max depth 6
    private fun getSizeOfTotalNumberOfChildRecords(nodeSize: Int, depth: Int, recordSize: Int): Int {
        require(depth < 7 ) { "no BigInteger, max depth 6 "}
        val recordsInLeafNode = (nodeSize/ recordSize).toDouble()
        val totalRecords = recordsInLeafNode.pow(depth)
        val totalRecordsL = ceil(totalRecords).toLong()
        val alt = com.sunya.cdm.util.log2(totalRecordsL) + 1
        val alt1 =  (alt + 8) / 8
        return alt1
    }

    // jhdf
    //private fun getSizeOfTotalNumberOfChildRecordsOrg(nodeSize: Int, depth: Int, recordSize: Int): Int {
    //    val recordsInLeafNode = nodeSize / recordSize
    //    return (BigInteger.valueOf(recordsInLeafNode.toLong()).pow(depth).bitLength() + 8) / 8
    //}

    inner class Btree2Entry : DataChunkEntryIF {
        var childAddress: Long = 0
        var nrecords: Long = 0
        var totNrecords: Long = 0
        var record: Any? = null

        override fun childAddress() = childAddress  // how is this used ?

        override fun offsets(): LongArray {
            if (record is Record10) return (record as Record10).scaledOffset
            if (record is Record11) return (record as Record11).scaledOffset
            throw RuntimeException("record type must be 10 or 11")
        }

        override fun isMissing() = (childAddress > 0)

        override fun chunkSize(): Int {
            if (record is Record11) return (record as Record11).chunkSize.toInt()
            return recordSize.toInt()
        }

        override fun filterMask(): Int? {
            if (record is Record11) return (record as Record11).filterMask
            return null
        }

        override fun show(tiling: Tiling): String {
            return "Not yet implemented"
        }
    }

    fun readRecord(state: OpenFileState, type: Int): Any {
        return when (type) {
            1 -> Record1(state)
            2 -> Record2(state)
            3 -> Record3(state)
            4 -> Record4(state)
            5 -> Record5(state)
            6 -> Record6(state)
            7 -> Record70(state) // TODO wrong
            8 -> Record8(state)
            9 -> Record9(state)
            10 -> Record10(state, ndimStorage - 1) // TODO wrong, whats ndims?
            11 -> Record11(state, ndimStorage - 1) // TODO wrong, whats ndims?
            else -> throw IllegalStateException()
        }
    }

    // Type 1 Record Layout - Indirectly Accessed, Non-filtered, ‘Huge’ Fractal Heap Objects
    internal inner class Record1(state: OpenFileState) {
        val hugeObjectAddress = h5.readOffset(state)
        val hugeObjectLength = h5.readLength(state)
        val hugeObjectID = h5.readLength(state)
    }

    // Type 2 Record Layout - Indirectly Accessed, Filtered, ‘Huge’ Fractal Heap Objects
    internal inner class Record2(state: OpenFileState) {
        val hugeObjectAddress = h5.readOffset(state)
        val hugeObjectLength = h5.readLength(state)
        val filterMask = raf.readInt(state)
        val hugeObjectSize = h5.readLength(state)
        val hugeObjectID = h5.readLength(state)
    }

    // Type 3 Record Layout - Directly Accessed, Non-filtered, ‘Huge’ Fractal Heap Objects
    internal inner class Record3(state: OpenFileState) {
        val hugeObjectAddress = h5.readOffset(state)
        val hugeObjectLength = h5.readLength(state)
    }

    // Type 4 Record Layout - Directly Accessed, Filtered, ‘Huge’ Fractal Heap Objects
    internal inner class Record4(state: OpenFileState) {
        val hugeObjectAddress = h5.readOffset(state)
        val hugeObjectLength = h5.readLength(state)
        val filterMask = raf.readInt(state)
        val hugeObjectSize = h5.readLength(state)
    }

    // Type 5 Record Layout - Link Name for Indexed Group
    inner class Record5(state: OpenFileState) {
        val nameHash = raf.readInt(state)
        val heapId: ByteArray = raf.readByteArray(state, 7)
    }

    // Type 6 Record Layout - Creation Order for Indexed Group
    inner class Record6(state: OpenFileState) {
        val creationOrder = raf.readLong(state)
        val heapId: ByteArray = raf.readByteArray(state, 7)
    }

    // Type 7 Record Layout - Shared Object Header Messages (Sub-type 0 - Message in Heap)
    internal inner class Record70(state: OpenFileState) {
        val location = raf.readByte(state)
        val hash = raf.readInt(state)
        val refCount = raf.readInt(state)
        val id: ByteArray = raf.readByteArray(state, 8)
    }

    // Type 7 Record Layout - Shared Object Header Messages (Sub-type 1 - Message in Object Header)
    internal inner class Record71(state: OpenFileState) {
        val location = raf.readByte(state)
        val hash = raf.readInt(state)
        val skip = raf.readByte(state)
        val messtype = raf.readByte(state)
        val index = raf.readShort(state)
        val address = h5.readOffset(state)
    }

    // Type 8 Record Layout - Attribute Name for Indexed Attributes
    inner class Record8(state: OpenFileState) {
        val heapId: ByteArray = raf.readByteArray(state, 8)
        val flags = raf.readByte(state)
        val creationOrder = raf.readInt(state)
        val nameHash = raf.readInt(state)
    }

    // Type 9 Record Layout - Creation Order for Indexed Attributes
    inner class Record9(state: OpenFileState) {
        val heapId: ByteArray = raf.readByteArray(state, 8)
        val flags = raf.readByte(state)
        val creationOrder = raf.readInt(state)
    }

    // Type 10 Record Layout - Non-filtered Dataset Chunks
    inner class Record10(state: OpenFileState, rank : Int) {
        val address = h5.readOffset(state)

        // ooops forgot to put in the rank. silly goose
        // This field is the scaled offset of the chunk within the dataset. n is the number of dimensions for the dataset.

        val scaledOffset = LongArray(rank) { raf.readLong(state) }

        // Scaled offset is calculated by dividing the chunk dimension sizes into the chunk offsets.
        // so to get the chunk offset:
        // jhdf
        // 		int[] chunkOffset = new int[datasetInfo.getDatasetDimensions().length];
        //		for (int i = 0; i < chunkOffset.length; i++) {
        //			chunkOffset[i] = Utils.readBytesAsUnsignedInt(buffer, 8) * datasetInfo.getChunkDimensions()[i];
        //		}
    }

    // Type 11 Record Layout - Filtered Dataset Chunks
    inner class Record11(state: OpenFileState, rank : Int) {
        val address = h5.readOffset(state)

        // LOOK variable size based on what? "Chunk Size (variable size; at most 8 bytes)"
        // jhdf
        // 		final int chunkSizeBytes = buffer.limit()
        //			- 8 // size of offsets
        //			- 4 // filter mask
        //			- datasetInfo.getDatasetDimensions().length * 8; // dimension offsets
        val chunkSizeBytes = recordSize - 8 - 4 - rank * 8
        val chunkSize: Long = h5.readVariableSizeUnsigned(state, chunkSizeBytes)

        val filterMask = raf.readInt(state)

        // ooops forgot to put in the rank. silly goose
        // This field is the scaled offset of the chunk within the dataset. n is the number of dimensions for the dataset.
        val scaledOffset = LongArray(rank) { raf.readLong(state) }

        // Scaled offset is calculated by dividing the chunk dimension sizes into the chunk offsets.
        // so to get the chunk offset:
        // jhdf
        // 		int[] chunkOffset = new int[datasetInfo.getDatasetDimensions().length];
        //		for (int i = 0; i < chunkOffset.length; i++) {
        //			chunkOffset[i] = Utils.readBytesAsUnsignedInt(buffer, 8) * datasetInfo.getChunkDimensions()[i];
        //		}
    }

    class MissingDataChunk() : DataChunkEntryIF {
        override fun childAddress() = -1L
        override fun offsets() = longArrayOf()
        override fun isMissing() = true
        override fun chunkSize() = 0
        override fun filterMask() = 0

        override fun show(tiling : Tiling) : String = "missing"
    }

    companion object {
        internal fun findRecord1byId(entryList: List<Btree2Entry>, hugeObjectID: Int): Record1? {
            for (entry in entryList) {
                val record1 = entry.record as Record1?
                if (record1!!.hugeObjectID == hugeObjectID.toLong()) return record1
            }
            return null
        }
    }
}
