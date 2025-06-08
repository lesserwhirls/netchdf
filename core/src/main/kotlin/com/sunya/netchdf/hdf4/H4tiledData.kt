package com.sunya.netchdf.hdf4

import com.sunya.cdm.api.toIntArray
import com.sunya.cdm.api.toLongArray
import com.sunya.cdm.iosp.OpenFileState
import com.sunya.cdm.layout.IndexSpace
import com.sunya.cdm.layout.IndexND
import com.sunya.cdm.layout.Tiling
import com.sunya.cdm.util.IOcopyB
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.zip.InflaterInputStream

private const val defaultBufferSize = 50_000

// replace H4ChunkIterator, LayoutBB
internal class H4tiledData(val h4 : H4builder, varShape : LongArray, chunk : IntArray, val chunks: List<SpecialDataChunk>) {
    val tiling = Tiling(varShape, chunk.toLongArray())

    // optimize later
    fun findEntryContainingKey(want : IntArray) : SpecialDataChunk? {
        chunks.forEach { chunk ->
            if (chunk.origin.contentEquals(want)) return chunk
        }
        return null
    }

    fun findDataChunks(wantSpace : IndexSpace) : Iterable<H4CompressedDataChunk> {
        val chunks = mutableListOf<H4CompressedDataChunk>()

        val tileSection = tiling.section(wantSpace) // section in tiles that we want
        val tileOdometer = IndexND(tileSection, tiling.tileShape) // loop over tiles we want
        // println("tileSection = ${tileSection}")

        for (wantTile in tileOdometer) {
            val wantKey = tiling.index(wantTile).toIntArray() // convert to chunk origin
            val chunk = findEntryContainingKey(wantKey)
            val useEntry = if (chunk != null) H4CompressedDataChunk(h4, chunk.origin, chunk.data.compress)
                else H4CompressedDataChunk(h4, wantKey, null)
            chunks.add(useEntry)
        }
        return chunks
    }
}

internal class H4CompressedDataChunk(
    val h4 : H4builder,
    val offsets: IntArray,  // offset index of this chunk, reletive to entire array
    private val compress: SpecialComp?
) {
    fun isMissing() = (compress == null)

    private var bb: ByteArray? = null // the data is placed into here

    @Throws(IOException::class)
    fun getByteArray(): ByteArray {
        if (bb != null) return bb!!
        if (compress != null) {
            // read compressed data in
            val cdata = compress.getDataTag(h4)

            // compressed data stored in one place
            val input: InputStream = if (cdata.linked == null) {
                val state = OpenFileState(cdata.offset, true)
                val cbuffer = h4.raf.readByteArray(state, cdata.length)
                ByteArrayInputStream(cbuffer)
            } else { // or compressed data stored in linked storage
                makeSpecialLinkedInputStream(h4, cdata.linked!!)
            }

            // uncompress it
            bb = when (compress.compress_type) {
                TagEnum.COMP_CODE_DEFLATE -> {
                    // read the stream in and uncompress
                    val zin: InputStream = InflaterInputStream(input)
                    val out = ByteArrayOutputStream(compress.uncomp_length)
                    IOcopyB(zin, out, defaultBufferSize)
                    out.toByteArray()
                }
                TagEnum.COMP_CODE_NONE -> {
                    // just read the stream in
                    val out = ByteArrayOutputStream(compress.uncomp_length)
                    IOcopyB(input, out, defaultBufferSize)
                    out.toByteArray()
                }
                else -> {
                    throw IllegalStateException("unknown compression type =" + compress.compress_type)
                }
            }
            // println("uncompress offset ${cdata.offset} length ${cdata.length} uncomp_length=${compress.uncomp_length} outSize=${outSize}")
        }
        return bb!!
    }

    fun show(tiling : Tiling) =
        "chunkStart=${offsets.contentToString()}, tile= ${tiling.tile(offsets.toLongArray()).contentToString()}"

}