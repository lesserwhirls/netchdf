package com.sunya.netchdfc

import com.sunya.cdm.api.*
import com.sunya.cdm.array.ArrayInt

fun version() : String {
    return "netchdf version 0.4.0"
}

fun openNetchdfFile(filename : String) : Netchdf? {
    return com.sunya.netchdf.openNetchdfFile(filename)
}

fun Netchdf.readArrayInt(v2: Variable<*>) : ArrayIntSection {
    require(v2.datatype == Datatype.INT)
    val arrayInt = this.readArrayData(v2) as ArrayInt
    return ArrayIntSection(v2.name, v2.shape, arrayInt.values, arrayInt.nelems, arrayInt.shape)
}

fun Netchdf.readArrayIntSection(v2: Variable<*>, start: IntArray, length: IntArray) : ArrayIntSection {
    require(v2.datatype == Datatype.INT)
    val arrayInt = this.readArrayData(v2, Section(start, length, v2.shape).toSectionPartial()) as ArrayInt
    return ArrayIntSection(v2.name, v2.shape, arrayInt.values, arrayInt.nelems, arrayInt.shape)
}

class ArrayIntSection(val varName: String, val varShape: LongArray, val array: IntArray, val nelems: Int, val shape: IntArray)

//  from Section all we need is get_shape and get_varShape
//               struct {
//                libnetchdf_KType* (*_type)(void);
//                libnetchdf_kref_com_sunya_cdm_api_Section (*Section)(libnetchdf_kref_kotlin_LongArray varShape);
//                libnetchdf_kref_com_sunya_cdm_api_Section (*Section_)(libnetchdf_kref_kotlin_IntArray start, libnetchdf_kref_kotlin_IntArray len, libnetchdf_kref_kotlin_LongArray varShape);
//                libnetchdf_kref_com_sunya_cdm_api_Section (*Section__)(libnetchdf_kref_kotlin_collections_List ranges, libnetchdf_kref_kotlin_LongArray varShape);
//                libnetchdf_kref_kotlin_collections_List (*get_ranges)(libnetchdf_kref_com_sunya_cdm_api_Section thiz);
//                libnetchdf_KInt (*get_rank)(libnetchdf_kref_com_sunya_cdm_api_Section thiz);
//                libnetchdf_kref_kotlin_LongArray (*get_shape)(libnetchdf_kref_com_sunya_cdm_api_Section thiz);
//                libnetchdf_KLong (*get_totalElements)(libnetchdf_kref_com_sunya_cdm_api_Section thiz);
//                libnetchdf_kref_kotlin_LongArray (*get_varShape)(libnetchdf_kref_com_sunya_cdm_api_Section thiz);
//                libnetchdf_kref_kotlin_collections_List (*component1)(libnetchdf_kref_com_sunya_cdm_api_Section thiz);
//                libnetchdf_kref_kotlin_LongArray (*component2)(libnetchdf_kref_com_sunya_cdm_api_Section thiz);
//                libnetchdf_kref_com_sunya_cdm_api_Section (*copy)(libnetchdf_kref_com_sunya_cdm_api_Section thiz, libnetchdf_kref_kotlin_collections_List ranges, libnetchdf_kref_kotlin_LongArray varShape);
//                libnetchdf_KBoolean (*equals)(libnetchdf_kref_com_sunya_cdm_api_Section thiz, libnetchdf_kref_kotlin_Any other);
//                libnetchdf_KInt (*hashCode)(libnetchdf_kref_com_sunya_cdm_api_Section thiz);
//                const char* (*toString)(libnetchdf_kref_com_sunya_cdm_api_Section thiz);
//              } Section;

internal fun Section.toSectionPartial() : SectionPartial {
    return SectionPartial(this.ranges)
}