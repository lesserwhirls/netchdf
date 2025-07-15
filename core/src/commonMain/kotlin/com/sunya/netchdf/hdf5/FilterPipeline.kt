package com.sunya.netchdf.hdf5

import com.sunya.cdm.iosp.decode

// should work even when filterType is unknown, as long as filter is registered with correct id.
enum class FilterType(val id: Int) {
    none(0), deflate(1), shuffle(2), fletcher32(3), szip(4), nbit(5), scaleoffset(6),
    bzip2(307),
    lzf(32000),
    lz4(32004),
    bitshuffle(32008),
    zstandard(32015),
    unknown(Int.MAX_VALUE);

    companion object {
        fun fromId(id: Int): FilterType {
            for (type in FilterType.entries) {
                if (type.id == id) {
                    return type
                }
            }
            return unknown
        }

        fun nameFromId(id: Int): String {
            for (type in entries) {
                if (type.id == id) {
                    return type.name
                }
            }
            return "UnknownFilter$id"
        }
    }
}

/** Apply filters, if any. */
internal class FilterPipeline(
    val varname : String,
    val mfp: FilterPipelineMessage?,
    val isBE: Boolean
) {

    init {
        if (mfp != null) {
            mfp.filters.forEach { filter ->
                if (filter.filterType == FilterType.lz4) {
                    println("GOT LZ4!")
                }
            }
        }
    }

    fun apply(encodedData: ByteArray, filterMask: Int): ByteArray {
        if (mfp == null) return encodedData
        var data = encodedData

        // apply filters backwards
        for (i in mfp.filters.indices.reversed()) {
            val filter = mfp.filters[i]
            if (isBitSet(filterMask, i)) {
                continue
            }

            val wtf = findFilter(filter)
            if (wtf == null) throw RuntimeException("Unimplemented filter type= ${filter.filterType} name = ${filter.name}")
            data = wtf.apply(data, filter.clientValues)
        }
        return data
    }
}

interface H5filterIF {
    fun id() : Int
    fun name() : String
    fun apply(encodedData: ByteArray, clientValues: IntArray): ByteArray
}

class DeflateFilter : H5filterIF {
    override fun id() = 1
    override fun name() = "deflate"
    override fun apply(encodedData: ByteArray, clientValues: IntArray): ByteArray {
        return decode(encodedData)
    }
}

class ShuffleFilter() : H5filterIF {
    override fun id() = 2
    override fun name() = "shuffle"
    override fun apply(encodedData: ByteArray, clientValues: IntArray): ByteArray {
        val n = clientValues[0]
        if (n <= 1) return encodedData
        val m = encodedData.size / n
        val count = IntArray(n)
        for (k in 0 until n) count[k] = k * m
        val result = ByteArray(encodedData.size)

        var pos = 0
        for (i in 0 until m) {
            for (j in 0 until n) {
                result[i * n + j] = encodedData[i + count[j]]
                pos++
            }
        }

        // jhdf
        if (pos < encodedData.size) {
            // In the overrun section no shuffle is done, just a straight copy
            // ByteArray.copyInto(destination: ByteArray, destinationOffset: Int = 0, startIndex: Int = 0, endIndex: Int = size)
            encodedData.copyInto(result, destinationOffset = pos, startIndex = pos)
        }
        return result
    }
}

class FletcherFilter : H5filterIF {
    override fun id() = 3
    override fun name() = "fletcher"
    override fun apply(encodedData: ByteArray, clientValues: IntArray): ByteArray {
        // just strip off the 4-byte fletcher32 checksum at the end
        // val result = ByteArray(org.size - 4)
        // System.arraycopy(org, 0, result, 0, result.size)
        // if (debug) println(" checkfletcher32 bytes in= " + org.size + " bytes out= " + result.size)
        return encodedData.copyOf(encodedData.size - 4)
    }
}

internal fun findFilter(filterMessage: FilterMessage) : H5filterIF? {
    return when (filterMessage.filterType.id) {
        1 -> DeflateFilter()
        2 -> ShuffleFilter()
        3 -> FletcherFilter()
        else -> {
            FilterRegistrar.resisteredFilters.find { it.id() == filterMessage.filterId }
        }
    }
}

object FilterRegistrar {
    val resisteredFilters = mutableListOf<H5filterIF>()

    fun registerFilter(f : H5filterIF) {
        resisteredFilters.add(f)
    }
}