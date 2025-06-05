package com.sunya.cdm.array

import com.fleeksoft.charset.Charset

/** Converts bytes to a dataType */

fun convertShort(ba: ByteArray, offset: Int, isBE: Boolean): Short {
    val ch1: Int = ba[offset].toInt() and 0xff
    val ch2: Int = ba[offset+1].toInt() and 0xff

    return if (isBE) {
        ((ch1 shl 8) + (ch2)).toShort()
    } else {
        ((ch2 shl 8) + (ch1)).toShort()
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

fun convertInt(ba: ByteArray, offset: Int, isBE: Boolean): Int {
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

//   public final long readLong() throws IOException {
//    if (byteOrder == ByteOrder.BIG_ENDIAN) {
//      return ((long) (readInt()) << 32) + (readInt() & 0xFFFFFFFFL); // tested ok
//    } else {
//      return ((readInt() & 0xFFFFFFFFL) + ((long) readInt() << 32)); // not tested yet ??
//    }

fun convertLong(ba: ByteArray, offset: Int, isBE: Boolean): Long {
    val val1 = convertInt(ba, offset, isBE).toLong()
    val val2 = convertInt(ba, offset + 4, isBE).toLong()
    return if (isBE) {
        ((val1 shl 32) + (val2 and 0xFFFFFFFFL))
    } else {
        ((val1 and 0xFFFFFFFFL) + (val2 shl 32))
    }
}

fun convertFloat(ba: ByteArray, offset: Int, isBE: Boolean): Float {
    return Float.fromBits(convertInt(ba, offset, isBE))
}

fun convertDouble(ba: ByteArray, offset: Int, isBE: Boolean): Double {
    return Double.fromBits(convertLong(ba, offset, isBE))
}

/** read a String from ByteArray, starting from offset, up to maxBytes, terminate at a zero byte. */
fun makeStringZ(ba : ByteArray, start : Int, maxBytes : Int = ba.size, charset : Charset = Charsets.UTF_8): String {
    var count = 0
    while (start+count < maxBytes && ba[start+count] != 0.toByte()) count++
    return String(ba, start, count, charset)
}