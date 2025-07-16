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

    override fun toString(): String {
        return "$name $kind"
    }

}

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
            Datatype5.Vlen -> buildVlenTypedef(this, member.name, member.mdt as DatatypeVlen)
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

private fun buildVlenTypedef(h5:H5builder, name : String, mess: DatatypeVlen): VlenTypedef {
    val base = mess.base
    val h5info = h5.makeH5TypeInfo(base)
    // class VlenTypedef(name : String, baseType : Datatype<*>) : Typedef(TypedefKind.Vlen, name, baseType) {
    return VlenTypedef(name, h5info.datatype())
}