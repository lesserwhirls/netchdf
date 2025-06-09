package com.sunya.cdm.iosp

import com.sunya.cdm.util.IOcopyB
import okio.*
import okio.Path.Companion.toPath
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.zip.Inflater
import java.util.zip.InflaterInputStream
import kotlin.math.min
import kotlin.test.Test
import kotlin.test.assertEquals

class TestOkio {

    @Test
    fun testPeek() {
        val buffer = Buffer()
        buffer.writeUtf8("abcdefghi")
        assertEquals("abc", buffer.readUtf8(3)) // returns "abc", buffer contains "defghi"
        val peek = buffer.peek()
        assertEquals("def", peek.readUtf8(3)) // returns "def", buffer contains "defghi"
        assertEquals("ghi", peek.readUtf8(3)) // returns "ghi", buffer contains "defghi"
        assertEquals("def", buffer.readUtf8(3)) // returns "def", buffer contains "ghi"
    }

    @Test
    fun testZipFile() {
        val filename = "/home/stormy/Downloads/p256.zip"
        val path = filename.toPath()
        val fileSystem = FileSystem.SYSTEM
        val zipFile = fileSystem.openZip(path)

        println("zipFile: $filename")
        val paths = zipFile.listRecursively("/".toPath()).toList()
        paths.forEach {
            println("  $it")
        }

        // ZipFileSystem
        //         val inflaterSource = InflaterSource(
        //          FixedLengthSource(source, entry.compressedSize, truncate = true),
        //          Inflater(true),
        //        )
        //        FixedLengthSource(inflaterSource, entry.size, truncate = false)

        val bs = zipFile.source(paths[1]).buffer()
        while (true) {
            val line = bs.readUtf8Line() ?: break
            println(line)
        }

        zipFile.close()
    }

    @Test
    fun testEncodeDecode() {
        val s = "secretDecoderMessage."
        val sb = s.encodeToByteArray()

        val n = 1000 // make sure its > 8192
        val bigs = ByteArray(n * sb.size) { sb[it % sb.size] }
        val encoded = encode(bigs)

        val encodedJ = encodeJ(bigs)
        println("encoded = ${encoded.size} encodedJ = ${encodedJ.size} ")
        encodedJ.forEachIndexed { idx, b ->
            if (b != encoded[idx]) println("idx=$idx, encJ = $b enc = ${encoded[idx]}")
        }
        println("encoded last = ${encoded.last()} ")


        println("    org size = ${bigs.size}")
        println("encoded size = ${encoded.size}")

        val decoded = decode(encodedJ)
        println("decodedJ size = ${decoded.size}")
        // println(String(decoded))
        assertEquals(n * sb.size, decoded.size)
    }

    fun encode(sb: ByteArray): ByteArray {
        val source = Buffer()
        source.write(sb)

        val sink = Buffer()
        val deflatorSink = sink.deflate()
        deflatorSink.write(source, source.size)
        deflatorSink.flush()
        deflatorSink.close()

        return sink.readByteArray()
    }


    val clevel = 5
    fun encodeJ(sb: ByteArray): ByteArray {
        val out = ByteArrayOutputStream(sb.size)
        java.util.zip.DeflaterOutputStream(out, java.util.zip.Deflater()).use { dos ->
            dos.write(sb)
        }
        println("    org size = ${sb.size}")
        println("encodedJ size = ${out.size()}")
        return out.toByteArray()
    }

    fun decode(encoded: ByteArray): ByteArray {
        val source = Buffer()
        source.write(encoded)

        //Returns an InflaterSource that DEFLATE-decompresses this Source while reading.
        val inflaterSource: InflaterSource = source.inflate()

        val sink = Buffer()
        var totalBytes = 0L
        while (true) {
            val ret = inflaterSource.read(sink, Long.MAX_VALUE)
            println("ret value = $ret")
            if (ret < 0) break
            totalBytes += ret
        }
        println("total bytes = $totalBytes")

        return sink.readByteArray()
    }

    val inflateBufferSize = 80_000
    val MAX_ARRAY_LEN = Int.MAX_VALUE - 8

    private fun decodeJ(compressed: ByteArray): ByteArray {
        // run it through the Inflator
        val input = ByteArrayInputStream(compressed)
        val inflater = Inflater()
        val inflatestream = InflaterInputStream(input, inflater, inflateBufferSize)
        val len = min(8 * compressed.size, MAX_ARRAY_LEN)
        val out = ByteArrayOutputStream(len) // Fixes KXL-349288
        IOcopyB(inflatestream, out, inflateBufferSize)
        return out.toByteArray()
    }
}