package com.sunya.cdm.array

import com.sunya.cdm.api.Datatype
import com.sunya.cdm.api.computeSize

// replacement for ByteBuffer.
/*
    val tba = TypedByteArray(v2.datatype, ba, 0, isBE = isBE)
    val result = tba.convertToArrayTyped(shape)
 */

@OptIn(ExperimentalUnsignedTypes::class)
class TypedByteArray<T>(val datatype: Datatype<T>, val ba: ByteArray, val offset: Int, val isBE: Boolean) {

    fun get(idx: Int): Any {
        return when (datatype) {

            Datatype.BYTE -> {
                ba[offset + idx]
            }

            Datatype.CHAR, Datatype.UBYTE, Datatype.ENUM1 -> {
                ba[offset + idx].toUByte()
            }

            Datatype.SHORT -> {
                convertToShort(ba, offset + 2 * idx, isBE)
            }

            Datatype.USHORT, Datatype.ENUM2 -> {
                convertToShort(ba, offset + 2 * idx, isBE).toUShort()
            }

            Datatype.INT -> {
                convertToInt(ba, offset + 4 * idx, isBE)
            }

            Datatype.UINT, Datatype.ENUM4 -> {
                convertToInt(ba, offset + 4 * idx, isBE).toUInt()
            }

            Datatype.LONG -> {
                convertToLong(ba, offset + 8 * idx, isBE)
            }

            Datatype.ULONG, Datatype.ENUM8 -> {
                convertToLong(ba, offset + 8 * idx, isBE).toULong()
            }

            Datatype.FLOAT -> {
                convertToFloat(ba, offset + 4 * idx, isBE)
            }

            Datatype.DOUBLE -> {
                convertToDouble(ba, offset + 8 * idx, isBE)
            }

            Datatype.REFERENCE -> { // TODO
                convertToLong(ba, offset + 8 * idx, isBE)
            }
            else -> throw IllegalStateException("unimplemented type= $datatype")
        }
    }

    fun getAsInt(idx: Int): Int {
        val valueAny = this.get(idx)
        return when (datatype) {
            Datatype.UBYTE, Datatype.ENUM1 -> (valueAny as UByte).toInt()
            Datatype.BYTE -> (valueAny as Byte).toInt()
            Datatype.USHORT, Datatype.ENUM2 -> (valueAny as UShort).toInt()
            Datatype.SHORT -> (valueAny as Short).toInt()
            Datatype.UINT, Datatype.ENUM4 -> (valueAny as UInt).toInt()
            Datatype.INT -> valueAny as Int
            Datatype.ULONG, Datatype.ENUM8 -> (valueAny as ULong).toInt()
            Datatype.LONG -> (valueAny as Long).toInt()
            else -> throw IllegalStateException("getAsInt unimplemented type= $datatype")
        }
    }

    fun convertToArrayTyped(shape: IntArray, elemSize : Int? = null): ArrayTyped<T> {
        val nelems = shape.computeSize()
        val result = when (datatype) {
            Datatype.BYTE -> ArrayByte(shape, ByteArray(nelems) { this.get(it) as Byte } )
            Datatype.CHAR, Datatype.UBYTE, Datatype.ENUM1 -> ArrayUByte(shape, datatype, UByteArray(nelems) { this.get(it) as UByte })
            Datatype.SHORT -> ArrayShort(shape, ShortArray(nelems) { this.get(it) as Short })
            Datatype.USHORT, Datatype.ENUM2  -> ArrayUShort(shape, datatype, UShortArray(nelems) { this.get(it) as UShort })
            Datatype.INT -> ArrayInt(shape, IntArray(nelems) { this.get(it) as Int } )
            Datatype.UINT, Datatype.ENUM4 -> ArrayUInt(shape, datatype, UIntArray(nelems) { this.get(it) as UInt })
            Datatype.LONG -> ArrayLong(shape, LongArray(nelems) { this.get(it) as Long })
            Datatype.ULONG, Datatype.ENUM8 -> ArrayULong(shape, datatype, ULongArray(nelems) { this.get(it) as ULong })
            Datatype.DOUBLE -> ArrayDouble(shape, DoubleArray(nelems) { this.get(it) as Double })
            Datatype.FLOAT -> ArrayFloat(shape, FloatArray(nelems) { this.get(it) as Float })
            Datatype.STRING -> { // TODO not dealing with vlen string; cant read out of ArrayStructureData heap
                val useShape = if (elemSize == null) shape else (shape.toList() + listOf(elemSize)).toIntArray()
                ArrayUByte.fromByteArray(useShape, ba).makeStringsFromBytes()
            }
            Datatype.REFERENCE -> ArrayLong(shape, LongArray(nelems) { this.get(it) as Long }) // TODO
            else -> throw IllegalArgumentException("convertToArrayTyped cant handle datatype ${datatype}")
        }
        return result as ArrayTyped<T>
    }
}

fun <T> emptyTypeByteArray(datatype: Datatype<T>) = TypedByteArray(datatype, ByteArray(0), 0, true)