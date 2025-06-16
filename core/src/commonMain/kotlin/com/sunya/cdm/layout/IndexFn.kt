package com.sunya.cdm.layout

import com.sunya.cdm.api.computeSize

internal class IndexFn(val shape : IntArray) {
    val nelems = shape.computeSize()
    init {
        require(shape.size == 2) // ??
    }

    fun flip(orgBuffer : ByteArray, elemSize : Int) : ByteArray {
        val flipBuffer = ByteArray(orgBuffer.size)

        repeat (nelems) { elem ->
            val row = elem / shape[1]
            val col = elem % shape[1]
            val dstElem = col * shape[0] + row
            repeat (elemSize) { flipBuffer[dstElem * elemSize + it] = orgBuffer.get(elem * elemSize + it) }
        }

        return flipBuffer
    }

    fun flippedShape() = shape.flip()
}

internal fun IntArray.flip() : IntArray {
    val rank = this.size
    return IntArray(rank) { this[rank - it - 1] }
}

