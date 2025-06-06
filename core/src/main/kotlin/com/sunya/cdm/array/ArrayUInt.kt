package com.sunya.cdm.array

import com.sunya.cdm.api.Datatype
import com.sunya.cdm.api.Section
import com.sunya.cdm.api.computeSize
import com.sunya.cdm.api.toIntArray
import java.nio.ByteBuffer
import java.nio.IntBuffer

class ArrayUInt(shape : IntArray, datatype : Datatype<UInt>, bb : ByteBuffer) : ArrayTyped<UInt>(bb, datatype, shape) {
    val values: IntBuffer = bb.asIntBuffer()

    constructor(shape : IntArray, bb : ByteBuffer) : this(shape, Datatype.UINT, bb)

    override fun iterator(): Iterator<UInt> = BufferIterator()
    private inner class BufferIterator : AbstractIterator<UInt>() {
        private var idx = 0
        override fun computeNext() = if (idx >= values.limit()) done() else setNext(values[idx++].toUInt())
    }

    override fun section(section : Section) : ArrayUInt {
        return ArrayUInt(section.shape.toIntArray(), sectionFrom(section))
    }

    companion object {
        fun fromArray(shape : IntArray, sa : IntArray) : ArrayUInt {
            val bb = ByteBuffer.allocate(4 * shape.computeSize())
            val ibb = bb.asIntBuffer()
            sa.forEach { ibb.put(it) }
            return ArrayUInt(shape, bb)
        }
    }
}