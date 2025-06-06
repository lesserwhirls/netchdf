package com.sunya.cdm.array

import com.sunya.cdm.api.Datatype
import com.sunya.cdm.api.Section
import com.sunya.cdm.api.toIntArray
import java.nio.ByteBuffer
import java.nio.LongBuffer

class ArrayLong(shape : IntArray, bb : ByteBuffer) : ArrayTyped<Long>(bb, Datatype.LONG, shape) {
    val values: LongBuffer = bb.asLongBuffer()

    override fun iterator(): Iterator<Long> = BufferIterator()
    private inner class BufferIterator : AbstractIterator<Long>() {
        private var idx = 0
        override fun computeNext() = if (idx >= values.limit()) done() else setNext(values[idx++])
    }

    override fun section(section : Section) : ArrayLong {
        return ArrayLong(section.shape.toIntArray(), sectionFrom(section))
    }
}