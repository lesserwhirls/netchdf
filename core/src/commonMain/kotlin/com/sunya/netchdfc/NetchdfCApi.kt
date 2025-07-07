package com.sunya.netchdfc

import com.sunya.cdm.api.*
import com.sunya.cdm.array.ArrayInt

/*
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.IntVar
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.pin
import kotlin.experimental.ExperimentalNativeApi
import kotlin.native.CName

@OptIn(ExperimentalNativeApi::class, ExperimentalForeignApi::class)
@CName("getPinnedIntArrayPointer")
fun getPinnedIntArrayPointer(array: IntArray): CPointer<IntVar> {
    return array.pin().addressOf(0)
}

// for testing
@OptIn(ExperimentalForeignApi::class)
fun testCArray(): CPointer<IntVar> {
    return getPinnedIntArrayPointer(intArrayOf(1,2,3,4,5))
}
*/

fun version() : String {
    return "netchdf version 0.4.0"
}

fun openNetchdfFile(filename : String) : Netchdf? {
    return com.sunya.netchdf.openNetchdfFile(filename)
}

fun Netchdf.readArrayInt(v2: Variable<*>) : ArrayIntSection {
    require(v2.datatype == Datatype.INT)
    val arrayInt = this.readArrayData(v2) as ArrayInt
    return ArrayIntSection(v2.name, v2.shape, v2.rank, arrayInt.values, arrayInt.nelems, arrayInt.shape)
}

fun Netchdf.readArrayIntSection(v2: Variable<*>, start: IntArray, length: IntArray) : ArrayIntSection {
    require(v2.datatype == Datatype.INT)
    val arrayInt = this.readArrayData(v2, Section(start, length, v2.shape).toSectionPartial()) as ArrayInt
    return ArrayIntSection(v2.name, v2.shape, v2.rank, arrayInt.values, arrayInt.nelems, arrayInt.shape)
}

class ArrayIntSection(val varName: String, val varShape: LongArray, val rank: Int, val array: IntArray, val nelems: Int, val shape: IntArray)

internal fun Section.toSectionPartial() : SectionPartial {
    return SectionPartial(this.ranges)
}
