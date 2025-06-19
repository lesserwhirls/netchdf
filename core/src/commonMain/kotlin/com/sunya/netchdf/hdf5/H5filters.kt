package com.sunya.netchdf.hdf5

import com.sunya.cdm.iosp.decode

/** Apply filters, if any. */
internal class H5filters(
    val varname : String,
    val mfp: FilterPipelineMessage?,
    val isBE: Boolean
) {

    fun apply(rawdata: ByteArray, filterMask : Int): ByteArray {
        if (mfp == null) return rawdata

        // LOOK can you hook the streams up rather than writing to bytearray at each step ??
        var data = rawdata
        // apply filters backwards
        for (i in mfp.filters.indices.reversed()) {
            val filter = mfp.filters[i]
            if (isBitSet(filterMask, i)) {
                continue
            }
            data = when (filter.filterType) {
                FilterType.deflate -> {
                    // val oldway = inflate(data)
                    decode(data)
                }

                FilterType.shuffle -> shuffle(data, filter.clientValues[0])

                FilterType.fletcher32 -> checkfletcher32(data)

                /* FilterType.zstandard -> {
                    val result = zstandard(data, expectedLengthBytes)
                    result.order(byteOrder)
                    return result // LOOK end of filters ??
                } */
                else -> throw RuntimeException("Unknown filter type=" + filter.filterType)
            }
        }
        return data
    }

    /*
     * decompress using Zstandard
     *
     * @param compressed compressed data
     * @return uncompressed data
     *
    private fun zstandard(compressed: ByteArray, expectedLengthBytes: Int): ByteBuffer {
        val input = ByteBuffer.wrap(compressed)
        val output = ByteBuffer.wrap(ByteArray(expectedLengthBytes))
        val decompressor = ZstdDecompressor()
        decompressor.decompress(input, output)
        output.flip()
        if (debug || debugFilter) {
            val compress = compressed.size.toFloat() / output.limit()
            System.out.printf(
                " zstandard bytes in= %d out= %d compress = %f.2%n", compressed.size, output.limit(),
                compress
            )
        }
        return output.slice()
    }

     */

/*
    private fun inflate(compressed: ByteArray): ByteArray {
        // run it through the Inflator
        val input = ByteArrayInputStream(compressed)
        val inflater = Inflater()
        val inflatestream = InflaterInputStream(input, inflater, inflateBufferSize)
        val len = min(8 * compressed.size, MAX_ARRAY_LEN)
        val out = ByteArrayOutputStream(len) // Fixes KXL-349288
        IOcopyB(inflatestream, out, inflateBufferSize)
        val uncomp = out.toByteArray()
        if (debug || debugFilter) println(" inflate bytes in= " + compressed.size + " bytes out= " + uncomp.size)
        return uncomp
    }

 */

    // just strip off the 4-byte fletcher32 checksum at the end
    private fun checkfletcher32(org: ByteArray): ByteArray {
        // val result = ByteArray(org.size - 4)
        // System.arraycopy(org, 0, result, 0, result.size)
        // if (debug) println(" checkfletcher32 bytes in= " + org.size + " bytes out= " + result.size)
        return org.copyOf(org.size - 4)
    }

    private fun shuffle(data: ByteArray, n: Int): ByteArray {
        if (debug) println(" shuffle bytes in= " + data.size + " n= " + n)
        require(data.size % n == 0)
        if (n <= 1) return data
        val m = data.size / n
        val count = IntArray(n)
        for (k in 0 until n) count[k] = k * m
        val result = ByteArray(data.size)
        for (i in 0 until m) {
            for (j in 0 until n) {
                result[i * n + j] = data[i + count[j]]
            }
        }
        return result
    }

    companion object {
        var debugFilter = false
        private const val MAX_ARRAY_LEN = Int.MAX_VALUE - 8
        private const val debug = false
    }
}