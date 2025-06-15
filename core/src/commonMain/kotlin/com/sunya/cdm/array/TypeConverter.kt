package com.sunya.cdm.array

import com.fleeksoft.charset.Charset
import com.fleeksoft.charset.Charsets
import com.fleeksoft.charset.decodeToString
import com.fleeksoft.charset.toByteArray
import com.sunya.cdm.api.Datatype

fun convertToShort(ba: ByteArray, offset: Int, isBE: Boolean): Short {
    val ch1: Int = ba[offset].toInt() and 0xff
    val ch2: Int = ba[offset+1].toInt() and 0xff

    return if (isBE) {
        ((ch1 shl 8) + (ch2)).toShort()
    } else {
        ((ch2 shl 8) + (ch1)).toShort()
    }
}

fun convertFromShort(value: Short, isBE: Boolean): ByteArray {
    val ival = value.toInt()
    val b1 = (ival and 0xff).toByte()
    val b2 = ((ival ushr 8) and 0xff).toByte()

    return if (isBE) {
        byteArrayOf(b2, b1)
    } else {
        byteArrayOf(b1, b2)
    }
}

// public final int readInt() throws IOException {
//    int ch1 = this.read();
//    int ch2 = this.read();
//    int ch3 = this.read();
//    int ch4 = this.read();
//    if ((ch1 | ch2 | ch3 | ch4) < 0) {
//      throw new EOFException();
//    }
//
//    if (byteOrder == ByteOrder.BIG_ENDIAN) {
//      return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4));
//    } else {
//      return ((ch4 << 24) + (ch3 << 16) + (ch2 << 8) + (ch1));
//    }
//  }

fun convertToInt(ba: ByteArray, offset: Int, isBE: Boolean): Int {
    val ch1 = ba[offset].toInt() and 0xff
    val ch2 = ba[offset+1].toInt() and 0xff
    val ch3 = ba[offset+2].toInt() and 0xff
    val ch4 = ba[offset+3].toInt() and 0xff

    return if (isBE) {
        ((ch1 shl 24) + (ch2 shl 16) + (ch3 shl 8) + (ch4))
    } else {
        ((ch4 shl 24) + (ch3 shl 16) + (ch2 shl 8) + (ch1))
    }
}

fun convertFromInt(ival: Int, isBE: Boolean): ByteArray {
    val b1 = (ival and 0xff).toByte()
    val b2 = ((ival ushr 8) and 0xff).toByte()
    val b3 = ((ival ushr 16) and 0xff).toByte()
    val b4 = ((ival ushr 24) and 0xff).toByte()

    return if (isBE) {
        byteArrayOf(b4, b3, b2, b1)
    } else {
        byteArrayOf(b1, b2, b3, b4)
    }
}

//   public final long readLong() throws IOException {
//    if (byteOrder == ByteOrder.BIG_ENDIAN) {
//      return ((long) (readInt()) << 32) + (readInt() & 0xFFFFFFFFL); // tested ok
//    } else {
//      return ((readInt() & 0xFFFFFFFFL) + ((long) readInt() << 32)); // not tested yet ??
//    }

fun convertToLong(ba: ByteArray, offset: Int, isBE: Boolean): Long {
    val val1 = convertToInt(ba, offset, isBE).toLong()
    val val2 = convertToInt(ba, offset + 4, isBE).toLong()
    return if (isBE) {
        ((val1 shl 32) + (val2 and 0xFFFFFFFFL))
    } else {
        ((val1 and 0xFFFFFFFFL) + (val2 shl 32))
    }
}

fun convertFromLong(lval: Long, isBE: Boolean): ByteArray {
    val b1 = (lval and 0xff).toByte()
    val b2 = ((lval ushr 8) and 0xff).toByte()
    val b3 = ((lval ushr 16) and 0xff).toByte()
    val b4 = ((lval ushr 24) and 0xff).toByte()
    val b5 = ((lval ushr 32) and 0xff).toByte()
    val b6 = ((lval ushr 40) and 0xff).toByte()
    val b7 = ((lval ushr 48) and 0xff).toByte()
    val b8 = ((lval ushr 56) and 0xff).toByte()

    return if (isBE) {
        byteArrayOf(b8, b7, b6, b5, b4, b3, b2, b1)
    } else {
        byteArrayOf(b1, b2, b3, b4, b5, b6, b7, b8)
    }
}

fun convertToFloat(ba: ByteArray, offset: Int, isBE: Boolean): Float {
    return Float.fromBits(convertToInt(ba, offset, isBE))
}

fun convertFromFloat(fval: Float, isBE: Boolean): ByteArray {
    val ival = fval.toBits()
    return convertFromInt(ival, isBE)
}

fun convertToDouble(ba: ByteArray, offset: Int, isBE: Boolean): Double {
    return Double.fromBits(convertToLong(ba, offset, isBE))
}

fun convertFromDouble(dval: Double, isBE: Boolean): ByteArray {
    val lval = dval.toBits()
    return convertFromLong(lval, isBE)
}

/** read a String from ByteArray, starting from offset, up to maxBytes, terminate at a zero byte. */
fun makeStringZ(ba : ByteArray, start : Int = 0, maxBytes : Int = ba.size, charset : Charset = Charsets.UTF8): String {
    var count = 0
    while (start+count < ba.size && count < maxBytes && ba[start+count] != 0.toByte()) count++
    // fun ByteArray.decodeToString(charset: Charset, off: Int = 0, len: Int = this.size): String {
    return ba.decodeToString(charset, start, count) // String(ba, start, count, charset)
}

fun makeString(ba: ByteArray) = ba.decodeToString(charset = Charsets.UTF8)

// needed when setting fillValue from Attribute value
fun convertToBytes(datatype : Datatype<*>, value: Any?, isBE: Boolean, charset : Charset = Charsets.UTF8): ByteArray {
    if ( value == null) return ByteArray(datatype.size)
    return when (value) {
        is Byte -> byteArrayOf(value)
        // is Char -> byteArrayOf(value.toByte()) // avoid CHAR altogether
        is UByte -> byteArrayOf(value.toByte())
        is Short -> convertFromShort(value, isBE)
        is UShort -> convertFromShort(value.toShort(), isBE)
        is Int -> convertFromInt(value, isBE)
        is UInt -> convertFromInt(value.toInt(), isBE)
        is Long -> convertFromLong(value, isBE)
        is ULong -> convertFromLong(value.toLong(), isBE)
        is Float -> convertFromFloat(value, isBE)
        is Double -> convertFromDouble(value, isBE)
        is String -> value.toByteArray(charset)
        else -> throw RuntimeException("Unsupported type ${value::class}")
    }
}

fun convertFromBytes(datatype : Datatype<*>, ba: ByteArray, isBE: Boolean, charset : Charset = Charsets.UTF8): Any {
    return when (datatype) {
        Datatype.BYTE -> ba[0]
        Datatype.CHAR, Datatype.UBYTE, Datatype.ENUM1 -> ba[0].toUByte()
        Datatype.SHORT -> convertToShort(ba, 0, isBE)
        Datatype.USHORT, Datatype.ENUM2  -> convertToShort(ba, 0, isBE).toUShort()
        Datatype.INT -> convertToInt(ba, 0, isBE)
        Datatype.UINT, Datatype.ENUM4 -> convertToInt(ba, 0, isBE).toUInt()
        Datatype.LONG -> convertToLong(ba, 0, isBE)
        Datatype.ULONG -> convertToLong(ba, 0, isBE).toULong()
        Datatype.DOUBLE -> convertToDouble(ba, 0, isBE)
        Datatype.FLOAT -> convertToFloat(ba, 0, isBE)
        Datatype.STRING -> ba.decodeToString(charset)
        else -> throw IllegalArgumentException("datatype ${datatype}")
    }
}
