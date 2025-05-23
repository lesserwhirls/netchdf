package com.sunya.cdm.api

import com.sunya.cdm.util.Indent
import com.sunya.cdm.util.escapeCdl
import com.sunya.cdm.util.escapeName
import java.nio.ByteBuffer
import java.util.*


// TODO dont show attributes ??
const val strict = false

fun cdl(netcdf : Netchdf) : String {
    val filename = netcdf.location().substringAfterLast('/')
    return buildString{
        append("netcdf $filename {\n")
        append(netcdf.rootGroup().cdl(true, Indent(2, 1)))
        append("}")
    }
}

fun Group.cdl(isRoot : Boolean, indent : Indent = Indent(2)) : String {
    return buildString{
        if (typedefs.isNotEmpty()) {
            append("${indent}types:\n")
            typedefs.sortedBy { it.name }.forEach { append("${it.cdl(indent.incr())}\n") }
        }
        if (dimensions.isNotEmpty()) {
            append("${indent}dimensions:\n")
            dimensions.sortedBy { it.name }.forEach { append("${it.cdl(indent.incr())}\n") }
        }
        if (variables.isNotEmpty()) {
            append("${indent}variables:\n")
            variables.sortedBy { it.name }.map { append(it.cdl(indent.incr())) }
        }
        if (attributes.isNotEmpty()) {
            val nindent = if (isRoot) indent else indent.incr()
            val text = if (isRoot) "global" else "group"
            append("\n${nindent}// $text attributes:\n")
            attributes.sortedBy { it.name }.forEach { append("${it.cdl("", indent)}\n") }
        }
        if (groups.isNotEmpty()) {
            groups.sortedBy { it.name }.forEach {
                append("\n${indent}group: ${it.name} {\n")
                append(it.cdl(false, indent.incr()))
                append("${indent}}\n")
            }
        }
    }
}

fun Dimension.cdl(indent : Indent = Indent(2)) : String {
    return if (!isShared) "${indent}$length"
           else "${indent}$name = $length ;"
}

fun Variable<*>.cdl(indent : Indent = Indent(2)) : String {
    val typedef = datatype.typedef
    val typename = typedef?.name ?: datatype.cdlName
    return buildString {
        append("${indent}${typename} ${escapeName(name)}")
        if (dimensions.isNotEmpty()) {
            append("(")
            dimensions.forEachIndexed { idx, it ->
                if (idx > 0) append(", ")
                if (!it.isShared) append("${it.length}") else append(it.name)
            }
            append(")")
        }
        append(" ;")
        if (attributes.isNotEmpty()) {
            append("\n")
            attributes.sortedBy { it.name }.forEach { append("${it.cdl(escapeName(name), indent.incr())}\n") }
        } else {
            append("\n")
        }
    }
}

fun Attribute<*>.cdl(varname: String, indent : Indent = Indent(2)) : String {
    val typedef = datatype.typedef
    val typename = typedef?.name ?: "" // datatype.cdlName
    val valueDatatype = typedef?.baseType ?: datatype
    return buildString {
        if (strict) append("${indent}${typename} $varname:$name = ")
        else append("${indent}:$name = ")
        if (values.isEmpty()) {
            append("NIL")
        }
        if (datatype == Datatype.OPAQUE) {
            append((values[0] as ByteBuffer).toHex())
        } else if (datatype.isEnum) {
            val converted = this@cdl.convertEnums()
            converted.forEachIndexed { idx, it ->
                if (idx != 0) append(", ")
                append(it)
            }
        } else if (datatype == Datatype.VLEN) {
            values.forEachIndexed { idx, it ->
                if (idx != 0) append(", ")
                if (it is Array<*>) append(it.contentToString()) else append(it.toString())
            }
        } else {
            values.forEachIndexed { idx, it ->
                if (idx != 0) append(", ")
                when (valueDatatype) {
                    Datatype.STRING, Datatype.REFERENCE -> append("\"${escapeCdl(it as String)}\"")
                    Datatype.FLOAT -> append("${it}f")
                    Datatype.SHORT -> append("${it}s")
                    Datatype.BYTE -> append("${it}b")
                    Datatype.VLEN -> {
                        if (it is Array<*>) append(it.contentToString()) else append("$it")
                    }
                    else -> append("$it")
                }
            }
        }
        append(" ;")
    }
}

internal fun ByteBuffer.toHex() : String {
    return "0X" + HexFormat.of().withUpperCase().formatHex(this.array())
}