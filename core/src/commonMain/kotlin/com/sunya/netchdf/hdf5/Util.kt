package com.sunya.netchdf.hdf5

fun isBitSet(num: Int, bitno: Int): Boolean {
    return ((num ushr bitno) and 1) != 0
}

fun makeIntFromBytes(bb: ByteArray, start: Int, n: Int): Int {
    var result = 0
    for (i in start + n - 1 downTo start) {
        result = result shl 8
        val b = bb[i].toInt()
        result += if ((b < 0)) b + 256 else b
    }
    return result
}