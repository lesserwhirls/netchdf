package com.sunya.cdm.array

import com.sunya.cdm.api.Datatype
import com.sunya.cdm.api.computeSize

// replacement for ByteBuffer.
// could use IntArray, DoubleArray etc instead
@OptIn(ExperimentalUnsignedTypes::class)
class TypedByteArray<T>(val datatype: Datatype<T>, val ba: ByteArray, val offset: Int, val isBE: Boolean) {

    fun get(idx: Int): Any {
        return when (datatype) {
            //Datatype.BYTE -> ArrayByte(shape, bb)
            // Datatype.STRING, Datatype.CHAR, Datatype.UBYTE, Datatype.ENUM1 -> ArrayUByte(shape, datatype as Datatype<UByte>, bb)

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
            //    Datatype.REFERENCE, Datatype.LONG -> ArrayLong(shape, bb)
            //     Datatype.OPAQUE -> ArrayOpaque(shape, bb, h5type.elemSize)
            else -> throw IllegalStateException("unimplemented type= $datatype")
        }
    }

    fun convertToArrayTyped(shape: IntArray): ArrayTyped<T> {
        val nelems = shape.computeSize()
        val result = when (datatype) {
            Datatype.BYTE -> ArrayByte(shape, ba)
            Datatype.UBYTE -> ArrayUByte(shape, UByteArray(nelems) { ba.get(it).toUByte() })
            Datatype.CHAR -> ArrayUByte(shape, Datatype.CHAR, UByteArray(nelems) { ba.get(it).toUByte() })
            Datatype.STRING -> ArrayUByte(shape, UByteArray(nelems) { ba.get(it).toUByte() }).makeStringsFromBytes()
            Datatype.DOUBLE -> ArrayDouble(shape, DoubleArray(nelems) { this.get(it) as Double })
            Datatype.FLOAT -> ArrayFloat(shape, FloatArray(nelems) { this.get(it) as Float })
            Datatype.INT -> ArrayInt(shape, IntArray(nelems) { this.get(it) as Int } )
            Datatype.UINT -> ArrayUInt(shape, UIntArray(nelems) { (this.get(it) as Int).toUInt() })
            Datatype.LONG -> ArrayLong(shape, LongArray(nelems) { this.get(it) as Long })
            Datatype.ULONG -> ArrayULong(shape, ULongArray(nelems) { (this.get(it) as Long).toULong() })
            Datatype.SHORT -> ArrayShort(shape, ShortArray(nelems) { this.get(it) as Short })
            Datatype.USHORT -> ArrayUShort(shape, UShortArray(nelems) { (this.get(it) as Short).toUShort() })
            // Datatype.OPAQUE -> ArrayOpaque(shape, ba)
            else -> throw IllegalArgumentException("datatype ${datatype}")
        }
        return result as ArrayTyped<T>
    }
}

fun <T> emptyTypeByteArray(datatype: Datatype<T>) = TypedByteArray(datatype, ByteArray(0), 0, true)

/*
fun ShortArray.toArrayUShort(shape : IntArray) : ArrayUShort {
    val bb = ByteBuffer.allocate(2 * shape.computeSize())
    val sbb = bb.asShortBuffer()
    this.forEach { sbb.put(it) }
    val ba =  TypedByteArray(Datatype.USHORT, bb)
    return ArrayUShort(shape, ba)
}

fun IntArray.toArrayUInt(shape : IntArray) : ArrayUInt {
    val bb = ByteBuffer.allocate(4 * shape.computeSize())
    val ibb = bb.asIntBuffer()
    this.forEach { ibb.put(it) }
    val ba =  TypedByteArray(Datatype.UINT, bb)
    return ArrayUInt(shape, ba)
}
*/
