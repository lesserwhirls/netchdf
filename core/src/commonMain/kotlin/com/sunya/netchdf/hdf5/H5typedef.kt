@file:OptIn(InternalLibraryApi::class)

package com.sunya.netchdf.hdf5

import com.sunya.cdm.api.*
import com.sunya.cdm.array.StructureMember
import com.sunya.cdm.util.InternalLibraryApi

// convert a DatatypeMessage to an H5typedef. if no name, its anonomous, aka private
internal class H5typedef(nameIn: String?, val mdt: DatatypeMessage) {
    var enumMessage : DatatypeEnum? = null
    var vlenMessage : DatatypeVlen? = null
    var opaqueMessage : DatatypeOpaque? = null
    var compoundMessage : DatatypeCompound? = null

    val name : String
    val kind : TypedefKind
    val mdtAddress : Long
    val mdtHash : Int

    init {
        name = nameIn ?: getNextAnonTypeName()

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

    companion object {
        private var nextAnon = 1

        fun getNextAnonTypeName(): String {
            val next = "_AnonymousType${nextAnon}"
            nextAnon++
            return next
        }
    }
}

class TypedefManager(val h5: H5builder) {
    val typedefGroups = mutableMapOf<Typedef, MutableList<Group.Builder>>()
    val privateTypedefGroups = mutableMapOf<Typedef, MutableList<Group.Builder>>()
    val addressToTypedef = mutableMapOf<Long, Typedef>() // key = mdt address
    val hashToTypedef = mutableMapOf<Int, Typedef>() // key = mdt hash

    fun findTypedef(mdtAddress : Long, mdtHash : Int) : Typedef? {
        return addressToTypedef[mdtAddress] ?: hashToTypedef[mdtHash]
    }

    //     val mdtAddress = mdt.address // used to look up typedefs
    //    val mdtHash = mdt.hashCode() // used to look up typedefs

    internal fun registerTypedef(typeInfo : H5TypeInfo, gb : Group.Builder) : H5TypeInfo {
        val existing = findTypedef(typeInfo.mdtAddress, typeInfo.mdtHash)
        if (existing == null) {
            addTypedef(typeInfo)
            val groups = typedefGroups.getOrPut(typeInfo.typedef!!) { mutableListOf() }
            groups.add(gb)
        } else {
            val groups = typedefGroups.getOrPut(existing) { mutableListOf() }
            groups.add(gb)
        }
        return typeInfo
    }

    private fun addTypedef(typeInfo : H5TypeInfo) : Boolean {
        val typedef = typeInfo.typedef!!
        if (hashToTypedef[typeInfo.mdtHash] != null) {
            if (debugTypedefs) println("already have typedef ${typedef.name}@${typeInfo.mdtAddress} hash=${typeInfo.mdtHash}")
            return false
        }
        addressToTypedef[typeInfo.mdtAddress] = typedef
        if (debugTypedefs) println("add typdef ${typedef.name}@${typeInfo.mdtAddress} hash=${typeInfo.mdtHash}")

        // use object identity instead of a shared object. seems like a bug in netcdf4 to me.
        hashToTypedef[typeInfo.mdtHash] = typedef
        return true
    }

    ////////////////////////////////////////////////////////////////////////////////////

    // TODO needed?
    internal fun registerPrivateTypedef(typedef: Typedef, gb: Group.Builder) {
        val groups = privateTypedefGroups.getOrPut(typedef) { mutableListOf() }
        if (!groups.contains(gb)) groups.add(gb)

        // look for nested typedefs
        if (typedef.kind == TypedefKind.Compound) {
            val ctdef = typedef as CompoundTypedef
            ctdef.members.forEach { member ->
                if (member.datatype.typedef != null) {
                    registerPrivateTypedef(member.datatype.typedef, gb)
                }
            }
        }
    }
}

// Convert H5typedef to Typedef
internal fun H5builder.buildTypedef(typedef5: H5typedef, gb: Group.Builder): Typedef? {
    return when (typedef5.kind) {
        TypedefKind.Compound -> {
            val mess = typedef5.compoundMessage!!
            this.buildCompoundTypedef(typedef5.name, mess, gb)
        }
        TypedefKind.Enum -> {
            val mess = typedef5.enumMessage!!
            this.buildEnumTypedef(typedef5.name, mess, gb)
        }
        TypedefKind.Opaque -> {
            val mess = typedef5.opaqueMessage!!
            this.buildOpaqueTypedef(typedef5.name, mess, gb)
        }
        TypedefKind.Vlen -> {
            val mess = typedef5.vlenMessage!!
            buildVlenTypedef(typedef5.name, mess, gb)
        }
        else -> null
    }
}

// allow recursion
private fun H5builder.buildCompoundTypedef(nameIn : String?, mess: DatatypeCompound, gb: Group.Builder) : CompoundTypedef {
    val typedef =  typedefManager.findTypedef(mess.address, mess.hashCode())
    if (typedef is CompoundTypedef) return typedef

    val name = nameIn ?: H5typedef.getNextAnonTypeName()

    val members = mess.members.map { member ->
        val nestedTypedef = when (member.mdt.type) {
            Datatype5.Compound -> {
                val nestedMdt = member.mdt as DatatypeCompound
                buildCompoundTypedef(if (nestedMdt.isShared) member.name else null, nestedMdt, gb)
            }
            Datatype5.Enumerated -> buildEnumTypedef(member.name, member.mdt as DatatypeEnum, gb)
            Datatype5.Vlen -> buildVlenTypedef(member.name, member.mdt as DatatypeVlen, gb)
            else -> null
        }
        val h5type = makeH5TypeInfo(member.mdt, nestedTypedef)
        StructureMember(member.name, h5type.datatype(), member.offset, member.dims, member.mdt.isBE)
    }
    val compoundTypedef =  CompoundTypedef(name, members)
    typedefManager.registerTypedef(makeH5TypeInfo(mess, compoundTypedef), gb)
    return compoundTypedef
}

private fun H5builder.buildEnumTypedef(name : String, mess: DatatypeEnum, gb: Group.Builder): EnumTypedef {
    val typedef =  typedefManager.findTypedef(mess.address, mess.hashCode())
    if (typedef is EnumTypedef) return typedef

    val enumTypedef = EnumTypedef(name, mess.basetype, mess.valuesMap)
    typedefManager.registerTypedef(makeH5TypeInfo(mess, enumTypedef), gb)
    return enumTypedef
}

private fun H5builder.buildVlenTypedef(name : String, mess: DatatypeVlen, gb: Group.Builder): VlenTypedef {
    val typedef =  typedefManager.findTypedef(mess.address, mess.hashCode())
    if (typedef is VlenTypedef) return typedef

    val h5info = this.makeH5TypeInfo(mess.base)
    val datatype = h5info.datatype()
    val altname = if (mess.isShared) name else "${datatype.cdlName}(*)" // dont use the typedef name or member name
    return VlenTypedef(altname, h5info.datatype())
}

private fun H5builder.buildOpaqueTypedef(name : String, mess: DatatypeOpaque, gb: Group.Builder): OpaqueTypedef {
    val typedef =  typedefManager.findTypedef(mess.address, mess.hashCode())
    if (typedef is OpaqueTypedef) return typedef

    //val h5info = this.makeH5TypeInfo(mess.base)
    //return VlenTypedef(name, h5info.datatype())
    return OpaqueTypedef(name, mess.elemSize)
}