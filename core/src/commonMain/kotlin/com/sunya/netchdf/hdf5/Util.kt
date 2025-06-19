package com.sunya.netchdf.hdf5

fun isBitSet(num: Int, bitno: Int): Boolean {
    return ((num ushr bitno) and 1) != 0
}