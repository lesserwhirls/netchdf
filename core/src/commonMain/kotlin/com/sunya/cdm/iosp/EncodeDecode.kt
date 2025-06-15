package com.sunya.cdm.iosp

import okio.Buffer
import okio.InflaterSource
import okio.deflate
import okio.inflate

internal fun encode(sb: ByteArray): ByteArray {
    val source = Buffer()
    source.write(sb)

    val sink = Buffer()
    val deflatorSink = sink.deflate()
    deflatorSink.write(source, source.size)
    deflatorSink.flush()
    deflatorSink.close()

    return sink.readByteArray()
}

internal fun decode(encoded: ByteArray): ByteArray {
    val source = Buffer()
    source.write(encoded)

    val inflaterSource: InflaterSource = source.inflate()

    val sink = Buffer()
    var totalBytes = 0L
    while (true) {
        val ret = inflaterSource.read(sink, Long.MAX_VALUE)
        if (ret < 0) break
        totalBytes += ret
    }

    return sink.readByteArray()
}