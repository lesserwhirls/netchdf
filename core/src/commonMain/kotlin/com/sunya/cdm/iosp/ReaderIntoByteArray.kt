package com.sunya.cdm.iosp

internal interface ReaderIntoByteArray {
    fun readIntoByteArray(state : OpenFileState, dest : ByteArray, destPos : Int, nbytes : Int) : Int
}

internal interface ByteSource : Iterator<ByteArray> {
    fun totalSize(): Int
    override fun hasNext(): Boolean
    override fun next() : ByteArray
}
