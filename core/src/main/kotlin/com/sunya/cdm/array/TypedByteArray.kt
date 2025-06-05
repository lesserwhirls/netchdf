package com.sunya.cdm.array

import com.sunya.cdm.api.Datatype
import com.sunya.cdm.api.computeSize

// replacement for ByteBuffer.
// could use IntArray, DoubleArray etc instead
@OptIn(ExperimentalUnsignedTypes::class)
class TypedByteArray<T>(val datatype: Datatype<T>, val ba: ByteArray, val offset: Int, val isBE: Boolean) {

    fun get(idx: Int): Any {
        return when (datatype) {

            Datatype.USHORT, Datatype.ENUM2, Datatype.SHORT -> {
                convertShort(ba, offset + 2 * idx, isBE)
            }

            Datatype.UINT, Datatype.ENUM4, Datatype.INT -> {
                convertInt(ba, offset + 4 * idx, isBE)
            }

            Datatype.ULONG, Datatype.LONG -> {
                convertLong(ba, offset + 8 * idx, isBE)
            }

            Datatype.FLOAT -> {
                convertFloat(ba, offset + 4 * idx, isBE)
            }

            Datatype.DOUBLE -> {
                convertDouble(ba, offset + 8 * idx, isBE)
            }

            Datatype.REFERENCE -> { // TODO
                convertLong(ba, offset + 8 * idx, isBE)
            }
            else -> throw IllegalStateException("unimplemented type= $datatype")
        }
    }

    fun convertToArrayTyped(shape: IntArray): ArrayTyped<T> {
        val nelems = shape.computeSize()
        val result = when (datatype) {
            Datatype.BYTE -> ArrayByte(shape, ba)
            Datatype.CHAR, Datatype.UBYTE, Datatype.ENUM1 -> ArrayUByte(shape, datatype, UByteArray(nelems) { ba.get(it).toUByte() })
            Datatype.SHORT -> ArrayShort(shape, ShortArray(nelems) { this.get(it) as Short })
            Datatype.USHORT, Datatype.ENUM2  -> ArrayUShort(shape, datatype, UShortArray(nelems) { (this.get(it) as Short).toUShort() })
            Datatype.INT -> ArrayInt(shape, IntArray(nelems) { this.get(it) as Int } )
            Datatype.UINT, Datatype.ENUM4 -> ArrayUInt(shape, datatype, UIntArray(nelems) { (this.get(it) as Int).toUInt() })
            Datatype.LONG -> ArrayLong(shape, LongArray(nelems) { this.get(it) as Long })
            Datatype.ULONG -> ArrayULong(shape, ULongArray(nelems) { (this.get(it) as Long).toULong() })
            Datatype.DOUBLE -> ArrayDouble(shape, DoubleArray(nelems) { this.get(it) as Double })
            Datatype.FLOAT -> ArrayFloat(shape, FloatArray(nelems) { this.get(it) as Float })
            Datatype.STRING -> ArrayUByte(shape, UByteArray(nelems) { ba.get(it).toUByte() }).makeStringsFromBytes()
            Datatype.REFERENCE -> ArrayLong(shape, LongArray(nelems) { this.get(it) as Long }) // TODO
            else -> throw IllegalArgumentException("datatype ${datatype}")
        }
        return result as ArrayTyped<T>
    }
}

fun <T> emptyTypeByteArray(datatype: Datatype<T>) = TypedByteArray(datatype, ByteArray(0), 0, true)

// TODO. needed by fillValue setting by Attribute value
fun convertToBytes(value: Any?): ByteArray {
    return ByteArray(0)
}