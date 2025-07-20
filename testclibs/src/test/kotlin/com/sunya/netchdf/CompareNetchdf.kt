package com.sunya.netchdf

import com.sunya.cdm.api.Attribute
import com.sunya.cdm.api.CompoundTypedef
import com.sunya.cdm.api.Datatype
import com.sunya.cdm.api.EnumTypedef
import com.sunya.cdm.api.Group
import com.sunya.cdm.api.Netchdf
import com.sunya.cdm.api.OpaqueTypedef
import com.sunya.cdm.api.Typedef
import com.sunya.cdm.api.Variable
import com.sunya.cdm.api.VlenTypedef
import com.sunya.cdm.util.Indent
import com.sunya.netchdf.hdf4Clib.Hdf4ClibFile
import com.sunya.netchdf.hdf5Clib.Hdf5ClibFile
import com.sunya.netchdf.netcdfClib.NClibFile
import org.junit.jupiter.api.assertNotNull
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail
import kotlin.use

class CompareNetchdf(filename: String, showCdl: Boolean = false, val showCompare: Boolean = false) {
    init {
        println("=============================================================")
        openNetchdfFile(filename).use { netchdf ->
            if (netchdf == null) {
                println("*** not a netchdf file = $filename")
            } else {
                println("${netchdf.type()} $filename ${"%.2f".format(netchdf.size / 1000.0 / 1000.0)} Mbytes")
                if (showCdl) println("\nnetchdf ${netchdf.cdl()}")

                if (netchdf.type().contains("hdf4") || netchdf.type().contains("hdf-eos2")) {
                    Hdf4ClibFile(filename).use { ncfile ->
                        if (showCdl) println("\nHdf4ClibFile ${ncfile.cdl()}")
                        compareNetchdf(netchdf, ncfile, Indent(2, startingLevel=0))
                    }
                } else if (netchdf.type().contains("netcdf")) {
                    NClibFile(filename).use { ncfile ->
                        if (showCdl) println("\nNClibFile ${ncfile.cdl()}")
                        compareNetchdf(netchdf, ncfile, Indent(2, startingLevel=0))
                    }
                } else if (netchdf.type().contains("hdf5") || netchdf.type().contains("hdf-eos5")) {
                    Hdf5ClibFile(filename).use { ncfile ->
                        if (showCdl) println("\nHdf5ClibFile ${ncfile.cdl()}")
                        compareNetchdf(netchdf, ncfile, Indent(2, startingLevel=0))
                    }
                } else {
                    println("*** no c library to compare for $filename")
                }
            }
        }
    }

    fun compareNetchdf(myfile: Netchdf, cfile: Netchdf, indent: Indent) {
        compareGroup(myfile.rootGroup(), cfile.rootGroup(), indent)
    }

    fun compareGroup(group: Group, cgroup: Group, indent: Indent) {
        if (showCompare) {
            println("${indent}group  $group")
            println("${indent}cgroup $cgroup")
        }

        group.attributes.forEach { att ->
            val catt = cgroup.findAttribute(att.name)!!
            compareAttribute(att, catt, indent.incr())
        }

        group.variables.forEach { v ->
            val cv = cgroup.variables.find { it.name == v.name }!!
            compareVariable(v, cv, indent.incr())
        }

        group.groups.forEach { nested ->
            val cnested = cgroup.findNestedGroupByShortName(nested.name)!!
            compareGroup(nested, cnested, indent.incr())
        }
    }

    fun compareAttribute(att: Attribute<*>, catt: Attribute<*>, indent: Indent) {
        if (showCompare) {
            println("${indent}att  $att")
            println("${indent}catt $catt")
        }
        assertEquals(att.name, catt.name)
        if (att.datatype == Datatype.OPAQUE) {
            att.values.forEachIndexed { idx, it ->
                val bval = it as ByteArray
                val cval = catt.values[idx] as ByteArray
                assertTrue(bval.contentEquals(cval))
            }
        } else {
            assertEquals(att.values, catt.values)
        }
    }

    fun compareVariable(v: Variable<*>, cv: Variable<*>, indent: Indent) {
        if (showCompare) {
            println("${indent}v  ${v.nameAndShape()}")
            println("${indent}cv ${cv.nameAndShape()}")
        }
        assertEquals(v.name, cv.name)
        assertEquals(v.dimensions, cv.dimensions)

        v.attributes.forEach { att ->
            val catt = cv.findAttribute(att.name)!!
            compareAttribute(att, catt, indent.incr())
        }

        compareDatatype(v.datatype, cv.datatype, indent.incr())
    }

    fun compareDatatype(datatype: Datatype<*>, cdatatype: Datatype<*>, indent: Indent) {
        if (showCompare) {
            println("${indent}datatype  ${datatype}")
            println("${indent}cdatatype ${cdatatype}")
        }
        assertEquals (datatype.cdlName, cdatatype.cdlName)
        compareTypedef(datatype.typedef, cdatatype.typedef, indent)
    }

    fun compareTypedef(t: Typedef?, ct: Typedef?, indent: Indent) {
        if ((t == null) && (ct == null)) return
        if ((t == null) && (ct != null)) {
            fail("typedef missing; ctypedef = ${ct}")
        }
        if ((t != null) && (ct == null)) {
            fail("ctypedef missing; typedef = ${t}")
        }
        assertNotNull(t)
        assertNotNull(ct)

        if (showCompare) {
            println("${t.cdl(indent)}")
            println("${ct.cdl(indent)}")
        }

        assertEquals(t.kind, ct.kind)
        assertEquals(t.baseType, ct.baseType)

        if (t is OpaqueTypedef) {
            assertTrue(ct is OpaqueTypedef)
            assertEquals(t.elemSize, ct.elemSize)

        } else if (t is VlenTypedef) {
            assertTrue(ct is VlenTypedef)

        } else if (t is EnumTypedef) {
            assertTrue(ct is EnumTypedef)
            assertEquals(t.valueMap, ct.valueMap)

        } else if (t is CompoundTypedef) {
            assertTrue(ct is CompoundTypedef)
            assertEquals(t.members, ct.members)
        }
    }
}