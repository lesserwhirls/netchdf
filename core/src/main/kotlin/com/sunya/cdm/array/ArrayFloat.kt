package com.sunya.cdm.array

import com.sunya.cdm.api.Datatype
import com.sunya.cdm.api.Section
import com.sunya.cdm.api.toIntArray
import java.nio.ByteBuffer
import java.nio.FloatBuffer

class ArrayFloat(shape : IntArray, bb : ByteBuffer) : ArrayTyped<Float>(bb, Datatype.FLOAT, shape) {
    val values: FloatBuffer = bb.asFloatBuffer()

    override fun iterator(): Iterator<Float> = BufferIterator()
    private inner class BufferIterator : AbstractIterator<Float>() {
        private var idx = 0
        override fun computeNext() = if (idx >= values.limit()) done() else setNext(values[idx++])
    }

    override fun section(section : Section) : ArrayFloat {
        return ArrayFloat(section.shape.toIntArray(), sectionFrom(section))
    }

}