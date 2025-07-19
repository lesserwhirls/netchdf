package com.sunya.cdm.iosp

import com.sunya.netchdf.testutil.testData
import okio.*
import okio.Path.Companion.toPath
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

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
        val filename = testData + "p256.zip"
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
        println("encode, decode")
        testEncodeDecode({ sb -> encode(sb) }, { sb -> decode(sb) })
        println()
    }

    fun testEncodeDecode(encoder: (sb: ByteArray) -> ByteArray, decoder: (sb: ByteArray) -> ByteArray) {
        val s = "secretDecoderMessage."
        val sb = s.encodeToByteArray()

        val n = 1000 // make sure its > 8192
        val bigs = ByteArray(n * sb.size) { sb[it % sb.size] }
        val encoded = encoder(bigs)

        println("     org size = ${bigs.size}")
        println(" encoded size = ${encoded.size}")

        val decoded = decoder(encoded)
        println(" decoded size = ${decoded.size}")
        assertEquals(n * sb.size, decoded.size)
        assertTrue(bigs.contentEquals(decoded))
    }

    @Test
    fun testEncodeDecodeForSO() {
        val s = "secretDecoderMessage."
        val sb = s.encodeToByteArray()

        val n = 1000 // make sure its > 8192
        val bigs = ByteArray(n * sb.size) { sb[it % sb.size] }
        val encoded = encode(bigs)

        println("    org size = ${bigs.size}")
        println("encoded size = ${encoded.size}")

        val decoded = decode(encoded)
        println("decoded size = ${decoded.size}")

        assertEquals(n * sb.size, decoded.size)
        assertTrue(bigs.contentEquals(decoded))
    }

}