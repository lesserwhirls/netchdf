package com.sunya.netchdf.hdf5

import com.sunya.cdm.api.*
import com.sunya.cdm.array.ArrayEmpty
import com.sunya.cdm.array.ArraySingle
import com.sunya.cdm.array.ArrayTyped
import com.sunya.cdm.array.TypedByteArray
import com.sunya.cdm.iosp.*

/**
 * @param strict true = make it agree with nclib if possible
 */
internal class Hdf5File(val filename : String, strict : Boolean = false) : Netchdf {
    private val raf : OpenFileIF = OkioFile(filename)
    val header : H5builder = H5builder(raf, strict)

    override fun close() {
        raf.close()
    }

    override fun rootGroup() = header.cdmRoot
    override fun location() = filename
    override fun cdl() = cdl(this)
    override fun type() = header.formatType()
    override val size : Long get() = raf.size()

    override fun <T> readArrayData(v2: Variable<T>, section: SectionPartial?): ArrayTyped<T> {
        if (v2.nelems == 0L) {
            return ArrayEmpty(v2.shape.toIntArray(), v2.datatype)
        }
        val wantSection = SectionPartial.fill(section, v2.shape)

        // promoted attributes
        if (v2.spObject is DataContainerAttribute) {
            return header.readRegularData(v2.spObject, v2.datatype, wantSection)
        }

        val vinfo = v2.spObject as DataContainerVariable
        if (vinfo.onlyFillValue) { // fill value only, no data
            val tba = TypedByteArray(v2.datatype, vinfo.fillValue, 0, isBE = vinfo.h5type.isBE)
            return ArraySingle(wantSection.shape.toIntArray(), v2.datatype, tba.get(0))
        }

        return try {
            if (vinfo.isChunked) {
                H5chunkReader(header).readChunkedData(v2, wantSection)
            } else if (vinfo.isCompact) {
                val alldata = header.readCompactData(v2, v2.shape.toIntArray())
                alldata.section(wantSection)
            } else {
                header.readRegularData(vinfo, v2.datatype, wantSection)
            }
        } catch (ex: Exception) {
            println("failed to read ${v2.name}, $ex")
            throw ex
        }
    }

    override fun <T> chunkIterator(v2: Variable<T>, section: SectionPartial?, maxElements : Int?) : Iterator<ArraySection<T>> {
        if (v2.nelems == 0L) {
            return listOf<ArraySection<T>>().iterator()
        }
        val wantSection = SectionPartial.fill(section, v2.shape)
        val vinfo = v2.spObject as DataContainerVariable

        if (vinfo.onlyFillValue) { // fill value only, no data
            val tba = TypedByteArray(v2.datatype, vinfo.fillValue, 0, isBE = vinfo.h5type.isBE)
            val single = ArraySection<T>( ArraySingle(wantSection.shape.toIntArray(), v2.datatype, tba.get(0)), wantSection)
            return listOf(single).iterator()
        }

        return if (vinfo.isChunked) {
            H5chunkIterator(header, v2, wantSection)
        } else {
            H5maxIterator(this, v2, wantSection, maxElements ?: 100_000)
        }
    }

}