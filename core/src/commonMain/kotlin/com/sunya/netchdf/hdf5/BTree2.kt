package com.sunya.netchdf.hdf5

import com.sunya.cdm.iosp.OpenFileIF
import com.sunya.cdm.iosp.OpenFileState
import com.sunya.cdm.layout.Tiling
import com.sunya.cdm.util.InternalLibraryApi
import com.sunya.netchdf.hdf5.BTree1.Node

/**
 * Level 1A2
 * TODO ?? Used in readGroupNew( type 5 and 6), readAttributesFromInfoMessage(), FractalHeap, and DHeapId(type 1,2,3,4)
 */
@OptIn(InternalLibraryApi::class)
internal class BTree2(private val h5: H5builder, owner: String, address: Long, val ndimStorage: Int) : BTreeIF { // BTree2
    val btreeType: Int
    private val nodeSize: Int // size in bytes of btree nodes
    private val recordSize: Short// size in bytes of btree records
    private val owner: String
    private val raf: OpenFileIF
    val rootNodeAddress: Long
    val treeDepth : Short
    val numRecordsRootNode : Short
    val totalRecords: Long

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
        recordSize = raf.readShort(state) // This field is the size in bytes of the B-tree record.
        treeDepth = raf.readShort(state)

        val splitPct = raf.readByte(state)
        val mergePct = raf.readByte(state)
        rootNodeAddress = h5.readOffset(state)
        numRecordsRootNode = raf.readShort(state)

        totalRecords = h5.readLength(state) // total in entire btree
        val checksum: Int = raf.readInt(state)

        /*
        if (rootNodeAddress > 0) {
            // eager reading of all nodes TODO can cache in H5TiledData
            if (treeDepth > 0) {
                val node = InternalNode(rootNodeAddress, numRecordsRootNode, recordSize, treeDepth.toInt())
                node.recurse()
            } else {
                val leaf = LeafNode(rootNodeAddress, numRecordsRootNode)
                leaf.addEntries(entryList)
            }
        }

         */
    }

    override fun rootNodeAddress() = rootNodeAddress

    override fun readNode(address: Long, parent: BTreeNodeIF?): BTreeNodeIF {
        val nrecords = nodeSize / recordSize // ??
        return LeafNode(address, nrecords.toShort()) as BTreeNodeIF
    }

    override fun makeMissingDataChunkEntry(rootNode: BTreeNodeIF, wantKey: LongArray): DataChunkEntryIF {
        return BTree1.DataChunkEntry1(0, rootNode as Node, -1, BTree1.DataChunkKey(-1, 0, wantKey), -1L)
    }

    // read all the entries in; used for non-data btrees
    fun readEntries() : List<Btree2Entry> {
        val entryList = mutableListOf<Btree2Entry>()

        if (rootNodeAddress > 0) {
            if (treeDepth > 0) {
                val node = InternalNode(rootNodeAddress, numRecordsRootNode, recordSize, treeDepth.toInt())
                node.recurse(entryList)
            } else {
                val leaf = LeafNode(rootNodeAddress, numRecordsRootNode)
                leaf.addEntries(entryList)
            }
        }

        return entryList
    }

    internal inner class InternalNode(address: Long, nrecords: Short, recordSize: Short, val depth: Int) : BTreeNodeIF {
        var entries: Array<Btree2Entry?>

        init {
            val state = OpenFileState(h5.getFileOffset(address), false)

            // header
            val magic = raf.readString(state, 4)
            check(magic == "BTIN") { "$magic should equal BTIN" }
            val version: Byte = raf.readByte(state)
            val nodeType = raf.readByte(state).toInt() // same as the B-tree type in the header
            check(nodeType == btreeType)

            entries = arrayOfNulls(nrecords + 1) // did i mention theres actually n+1 children?
            for (i in 0 until nrecords) {
                entries[i] = Btree2Entry()
                entries[i]!!.record = readRecord(state, btreeType)
            }
            entries[nrecords.toInt()] = Btree2Entry()

            // Records: The size of this field is determined by the number of records for this node and the record size (from the header).
            
            // maximum possible number of records able to be stored in the child node.
            val maxNumRecords = nodeSize / recordSize // TODO see long calculation description in Fields: Version 2 B-tree Internal Node
            // maximum possible number of records able to be stored in the child node and its descendants
            val maxNumRecordsPlusDesc = nodeSize / recordSize // TODO see long calculation description in Fields: Version 2 B-tree Internal Node

            for (i in 0 until nrecords + 1) {
                val e = entries[i]
                e!!.childAddress = h5.readOffset(state) // Child Node Pointer
                e.nrecords = h5.readVariableSizeUnsigned(state, 1) // Number of Records for Child TODO maxNumRecords ??
                if (depth > 1) {
                    e.totNrecords = h5.readVariableSizeUnsigned(state, 2)
                } // readVariableSizeMax(maxNumRecordsPlusDesc); // TODO ??
            }

            // skip
            raf.readInt(state)
        }

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

        override fun isLeaf() = false
        override fun nentries() = entries.size
        override fun dataChunkEntryAt(idx: Int): DataChunkEntryIF {
            throw RuntimeException("dataChunkEntryAt only for leaf nodes")
        }
    }

    // problem is we need nrecords. Thinking we can derive it from the recordSize?
    internal inner class LeafNode(address: Long, nrecords: Short) : BTreeNodeIF {
        val entries = mutableListOf<Btree2Entry>()

        init {
            val state = OpenFileState(h5.getFileOffset(address), false)

            // header
            val magic = raf.readString(state, 4)
            check(magic == "BTLF") { "$magic should equal BTLF" }
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
        // The first scaled offset stored in the list is for the slowest changing dimension, and the last scaled offset
        // stored is for the fastest changing dimension. Scaled offset is calculated by dividing the chunk dimension sizes into the chunk offsets.
        val scaledOffset = LongArray(rank) { raf.readLong(state) }
    }

    // Type 11 Record Layout - Filtered Dataset Chunks
    inner class Record11(state: OpenFileState, rank : Int) {
        val address = h5.readOffset(state)
        val chunkSize = raf.readLong(state) // LOOK variable size based on what ?
        val filterMask = raf.readInt(state)
        // ooops forgot to put in the rank. silly goose
        // This field is the scaled offset of the chunk within the dataset. n is the number of dimensions for the dataset.
        // The first scaled offset stored in the list is for the slowest changing dimension, and the last scaled offset
        // stored is for the fastest changing dimension. Scaled offset is calculated by dividing the chunk dimension sizes into the chunk offsets.
        val scaledOffset = LongArray(rank) { raf.readLong(state) }
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
