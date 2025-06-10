package com.sunya.cdm.iosp

interface ReaderIntoByteArray {
    fun readIntoByteArray(state : OpenFileState, dest : ByteArray, destPos : Int, nbytes : Int) : Int
}

interface ByteSource : Iterator<ByteArray> {
    fun totalSize(): Int
    override fun hasNext(): Boolean
    override fun next() : ByteArray
}
