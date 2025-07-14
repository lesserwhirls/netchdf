package com.sunya.netchdf.hdf5

fun isBitSet(num: Int, bitno: Int): Boolean {
    return ((num ushr bitno) and 1) != 0
}

fun makeIntFromLEBytes(bb: ByteArray, start: Int, n: Int): Int {
    var result = 0
    for (i in start + n - 1 downTo start) {
        result = result shl 8
        val b = bb[i].toInt()
        result += if ((b < 0)) b + 256 else b
    }
    return result
}

fun makeLongFromLEBytes(bb: ByteArray, start: Int, n: Int): Long {
    var result = 0L
    for (i in start + n - 1 downTo start) {
        result = result shl 8
        val b = bb[i].toInt()
        result += if ((b < 0)) b + 256 else b
    }
    return result
}

fun makeIntFromBEBytes(bb: ByteArray, start: Int, n: Int): Int {
    var result = 0
    for (i in start until start + n) {
        result = result shl 8
        val b = bb[i].toInt()
        result += if ((b < 0)) b + 256 else b
    }
    return result
}

fun makeLongFromBEBytes(bb: ByteArray, start: Int, n: Int): Long {
    var result = 0L
    for (i in start until start + n) {
        result = result shl 8
        val b = bb[i].toInt()
        result += if ((b < 0)) b + 256 else b
    }
    return result
}