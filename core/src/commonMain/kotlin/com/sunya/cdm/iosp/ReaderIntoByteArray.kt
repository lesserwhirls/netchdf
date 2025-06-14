package com.sunya.cdm.iosp

import com.sunya.cdm.util.InternalLibraryApi

@InternalLibraryApi
interface ReaderIntoByteArray {
    fun readIntoByteArray(state : OpenFileState, dest : ByteArray, destPos : Int, nbytes : Int) : Int
}

internal interface ByteSource : Iterator<ByteArray> {
    fun totalSize(): Int
    override fun hasNext(): Boolean
    override fun next() : ByteArray
}
