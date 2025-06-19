package com.sunya.cdm.array

import com.sunya.cdm.api.Datatype
import com.sunya.cdm.api.computeSize
import com.sunya.cdm.util.makeValidCdmObjectName

// dim lengths here are ints; Hdf4,5 only supports ints.
/**
 * @param offset byte offset into the StructureData.ByteArray
 * @param isBE is BIG_ENDIAN
 */
class StructureMember<T>(orgName: String, val datatype : Datatype<T>, val offset: Int, val shape : IntArray, val isBE : Boolean) {
    val name = makeValidCdmObjectName(orgName)
    val nelems = shape.computeSize()

    /**
     * Get the value of this member from the given StructureData.
     * return T for nelems = 1, ArrayTyped<T> for nelems > 1
     */
    fun value(sdata: ArrayStructureData.StructureData): Any {
        val offset = sdata.offset + this.offset

        if (nelems > 1) {
            val tba = TypedByteArray(this.datatype, sdata.ba, offset, this.isBE)
            return tba.convertToArrayTyped(shape)
        }

        return when (datatype) {
            Datatype.BYTE -> sdata.ba.get(offset)
            Datatype.SHORT -> convertToShort(sdata.ba, offset, this.isBE)
            Datatype.INT -> convertToInt(sdata.ba, offset, this.isBE)
            Datatype.LONG -> convertToLong(sdata.ba, offset, this.isBE)
            Datatype.UBYTE, Datatype.CHAR, Datatype.ENUM1 -> sdata.ba.get(offset).toUByte()
            Datatype.USHORT, Datatype.ENUM2 -> convertToShort(sdata.ba, offset, this.isBE).toUShort()
            Datatype.UINT, Datatype.ENUM4 -> convertToInt(sdata.ba, offset, this.isBE).toUInt()
            Datatype.ULONG, Datatype.ENUM8 -> convertToLong(sdata.ba, offset, this.isBE).toULong()
            Datatype.FLOAT -> convertToFloat(sdata.ba, offset, this.isBE)
            Datatype.DOUBLE -> convertToDouble(sdata.ba, offset, this.isBE)
            Datatype.STRING -> {
                if (datatype.isVlenString) {
                    val ret = sdata.getFromHeap(offset)
                    if (ret == null)  {
                        sdata.getFromHeap(offset)
                        "unknown"
                    } else if (ret is List<*>) {
                       ret[0] as Any
                    } else {
                        ret
                    }
                } else {
                    makeStringZ(sdata.ba, offset, nelems) // nelems ??
                }
            }
            Datatype.VLEN -> {
                val ret = sdata.getFromHeap(offset)
                if (ret != null) (ret as ArrayVlen<*>) else {
                    throw RuntimeException("cant find ArrayVlen on heap at $offset")
                }
            }
            else -> makeStringZ(sdata.ba, offset, nelems) // // wtf?
        }
    }

    /**
     * Get the values of this member from the given StructureData as an ArrayTyped.
     * return T for nelems = 1, ArrayTyped<T> for nelems > 1
     */
    fun values(sdata: ArrayStructureData.StructureData): ArrayTyped<*> {
        val offset = sdata.offset + this.offset

        // class TypedByteArray<T>(val datatype: Datatype<T>, val ba: ByteArray, val offset: Int, val isBE: Boolean) {
        val tba = TypedByteArray(this.datatype, sdata.ba, offset, this.isBE)
        return tba.convertToArrayTyped(shape)

    }

    override fun toString(): String {
        return "\nStructureMember(name='$name', datatype=$datatype, offset=$offset, dims=${shape.contentToString()}, nelems=$nelems)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is StructureMember<*>) return false

        if (name != other.name) return false
        if (datatype != other.datatype) return false
        if (!shape.contentEquals(other.shape)) return false
        return nelems == other.nelems
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + datatype.hashCode()
        result = 31 * result + shape.contentHashCode()
        result = 31 * result + nelems
        return result
    }
}