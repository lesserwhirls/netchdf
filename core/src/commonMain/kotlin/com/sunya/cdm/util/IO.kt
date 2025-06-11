package com.sunya.cdm.util

import com.fleeksoft.io.InputStream
import com.fleeksoft.io.OutputStream
import okio.*

fun IOcopy(input: Source, output: Sink, nbytes: Long): Long {
    var bufferedSource : BufferedSource = input.buffer()
    var totalRead = 0L
    var leftToRead = nbytes
    while (leftToRead > 0) {
        val okioBuffer = Buffer()
        val nread = bufferedSource.read(okioBuffer, leftToRead)
        output.write(okioBuffer, nread)
        totalRead += nread
        leftToRead -= nread
    }
    return totalRead
}

fun IOcopyB(input: InputStream, out: OutputStream, bufferSize: Int): Long {
    var totalBytesRead: Long = 0
    val buffer = ByteArray(bufferSize)
    while (true) {
        val n = input.read(buffer)
        if (n == -1) break
        out.write(buffer, 0, n)
        totalBytesRead += n.toLong()
    }
    out.flush()
    return totalBytesRead
}

/*
fun Long.reverseByteOrder(): Long {
    return ByteBuffer.allocate(Long.SIZE_BYTES)
        .order(ByteOrder.LITTLE_ENDIAN)
        .putLong(this)
        .rewind()
        .order(ByteOrder.BIG_ENDIAN)
        .long
}

 */