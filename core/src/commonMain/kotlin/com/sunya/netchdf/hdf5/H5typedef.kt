@file:OptIn(InternalLibraryApi::class)

package com.sunya.netchdf.hdf5

import com.sunya.cdm.api.*
import com.sunya.cdm.array.StructureMember
import com.sunya.cdm.util.InternalLibraryApi

// convert a DataObject with an mdt to a H5typedef. if no name, its anonomous aka private
internal class H5typedef(val name: String?, val mdt: DatatypeMessage) {
    var enumMessage : DatatypeEnum? = null
    var vlenMessage : DatatypeVlen? = null
    var opaqueMessage : DatatypeOpaque? = null
    var compoundMessage : DatatypeCompound? = null

    val kind : TypedefKind
    val mdtAddress : Long
    val mdtHash : Int

    constructor(dataObject: DataObject) : this(dataObject.name, dataObject.mdt!!)

    init {
        mdtAddress = mdt.address
        mdtHash = mdt.hashCode()

        when (mdt.type) {
            Datatype5.Enumerated -> {
                this.enumMessage = (mdt) as DatatypeEnum
                kind = TypedefKind.Enum
            }
            Datatype5.Vlen -> {
                this.vlenMessage = (mdt) as DatatypeVlen
                kind = TypedefKind.Vlen
            }
            Datatype5.Opaque -> {
                this.opaqueMessage = (mdt) as DatatypeOpaque
                kind = TypedefKind.Opaque
            }
            Datatype5.Compound -> {
                this.compoundMessage = (mdt) as DatatypeCompound
                kind = TypedefKind.Compound
            }
            else -> {
                kind = TypedefKind.Unknown
            }
        }
        if (debugTypedefs) println("H5Typedef mdtAddress=$mdtAddress mdtHash=$mdtHash kind=$kind")
    }
}

/* so does it have a feckin name or not ?
internal fun H5builder.buildAndRegisterTypedef(groupb : Group.Builder, typedef5: H5typedef): H5TypeInfo {
    val typedef : Typedef? = when (typedef5.kind) {
        TypedefKind.Compound -> {
            val mess = typedef5.compoundMessage!!
            this.buildAndRegisterCompoundTypedef(groupb, typedef5.name!!, mess)
        }
        TypedefKind.Enum -> {
            val mess = typedef5.enumMessage!!
            EnumTypedef(typedef5.name!!, mess.datatype, mess.valuesMap)
        }
        TypedefKind.Opaque -> {
            val mess = typedef5.opaqueMessage!!
            OpaqueTypedef(typedef5.name!!, mess.elemSize)
        }
        TypedefKind.Vlen -> {
            val mess = typedef5.vlenMessage!!
            val h5type = makeH5TypeInfo(mess.base)
            VlenTypedef(typedef5.name!!, h5type.datatype())
        }
        else -> null
    }
    val typeinfo = makeH5TypeInfo(typedef5.mdt, typedef)
    return registerTypedef(typeinfo, groupb)
}

// allow it to recurse
private fun H5builder.buildAndRegisterCompoundTypedef(groupb : Group.Builder, name : String, mess: DatatypeCompound) : CompoundTypedef {
    // first look for embedded typedefs that need to be added
    mess.members.forEach { member ->
        val nestedTypedef = when (member.mdt.type) {
            Datatype5.Compound -> buildAndRegisterCompoundTypedef(groupb, member.name, member.mdt as DatatypeCompound)
            Datatype5.Enumerated -> buildEnumTypedef(member.name, member.mdt as DatatypeEnum)
            else -> null
        }
        if (nestedTypedef != null) {
            val ntypeinfo = makeH5TypeInfo(member.mdt, nestedTypedef)
            registerTypedef(ntypeinfo, groupb)
        }
    }

    // now build the typedef for the compound message
    val members = mess.members.map {
        val h5type = makeH5TypeInfo(it.mdt)
        val datatype = h5type.datatype()
        StructureMember(it.name, datatype, it.offset, it.dims, it.mdt.isBE)
    }
    return CompoundTypedef(name, members)
} */

// Convert H5typedef to Typedef
internal fun H5builder.buildTypedef(typedef5: H5typedef): Typedef? {
    return when (typedef5.kind) {
        TypedefKind.Compound -> {
            val mess = typedef5.compoundMessage!!
            this.buildCompoundTypedef(typedef5.name ?: "", mess)
        }
        TypedefKind.Enum -> {
            val mess = typedef5.enumMessage!!
            EnumTypedef(typedef5.name ?: "", mess.datatype, mess.valuesMap)
        }
        TypedefKind.Opaque -> {
            val mess = typedef5.opaqueMessage!!
            OpaqueTypedef(typedef5.name ?: "", mess.elemSize)
        }
        TypedefKind.Vlen -> {
            val mess = typedef5.vlenMessage!!
            val h5type = makeH5TypeInfo(mess.base)
            VlenTypedef(typedef5.name ?: "", h5type.datatype())
        }
        else -> null
    }
}

// allow recursion
private fun H5builder.buildCompoundTypedef(name : String, mess: DatatypeCompound) : CompoundTypedef {
    val members = mess.members.map { member ->
        val nestedTypedef = when (member.mdt.type) {
            Datatype5.Compound -> buildCompoundTypedef(member.name, member.mdt as DatatypeCompound)
            Datatype5.Enumerated -> buildEnumTypedef(member.name, member.mdt as DatatypeEnum)
            else -> null
        }
        val h5type = makeH5TypeInfo(member.mdt, nestedTypedef)
        StructureMember(member.name, h5type.datatype(), member.offset, member.dims, member.mdt.isBE)
    }
    return CompoundTypedef(name, members)
}

private fun buildEnumTypedef(name : String, mess: DatatypeEnum): EnumTypedef {
    return EnumTypedef(name, mess.datatype, mess.valuesMap)
}