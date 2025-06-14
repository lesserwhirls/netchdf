#ifndef KONAN_LIBNETCHDF_H
#define KONAN_LIBNETCHDF_H
#ifdef __cplusplus
extern "C" {
#endif
#ifdef __cplusplus
typedef bool            libnetchdf_KBoolean;
#else
typedef _Bool           libnetchdf_KBoolean;
#endif
typedef unsigned short     libnetchdf_KChar;
typedef signed char        libnetchdf_KByte;
typedef short              libnetchdf_KShort;
typedef int                libnetchdf_KInt;
typedef long long          libnetchdf_KLong;
typedef unsigned char      libnetchdf_KUByte;
typedef unsigned short     libnetchdf_KUShort;
typedef unsigned int       libnetchdf_KUInt;
typedef unsigned long long libnetchdf_KULong;
typedef float              libnetchdf_KFloat;
typedef double             libnetchdf_KDouble;
typedef float __attribute__ ((__vector_size__ (16))) libnetchdf_KVector128;
typedef void*              libnetchdf_KNativePtr;
struct libnetchdf_KType;
typedef struct libnetchdf_KType libnetchdf_KType;

typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_kotlin_Byte;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_kotlin_Short;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_kotlin_Int;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_kotlin_Long;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_kotlin_Float;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_kotlin_Double;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_kotlin_Char;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_kotlin_Boolean;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_kotlin_Unit;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_kotlin_UByte;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_kotlin_UShort;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_kotlin_UInt;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_kotlin_ULong;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_com_sunya_cdm_api_Netchdf;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_kotlin_ByteArray;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_kotlin_IntArray;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_kotlin_Pair;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_kotlin_LongArray;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_com_sunya_cdm_api_Attribute;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_kotlin_collections_List;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_com_sunya_cdm_array_ArrayTyped;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_com_sunya_cdm_array_ArrayString;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_com_sunya_cdm_api_Dimension;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_kotlin_Any;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_com_sunya_cdm_api_Group;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_com_sunya_cdm_api_Typedef;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_com_sunya_cdm_api_Variable;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_com_sunya_cdm_api_InvalidRangeException;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_com_sunya_cdm_api_Section;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_com_sunya_cdm_api_SectionPartial;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_com_sunya_cdm_api_SectionPartial_Companion;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_com_sunya_cdm_api_TypedefKind;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_com_sunya_cdm_api_TypedefKind_Compound;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_com_sunya_cdm_api_TypedefKind_Enum;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_com_sunya_cdm_api_TypedefKind_Opaque;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_com_sunya_cdm_api_TypedefKind_Vlen;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_com_sunya_cdm_api_TypedefKind_Unknown;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_com_sunya_cdm_api_Datatype;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_com_sunya_cdm_util_Indent;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_com_sunya_cdm_api_CompoundTypedef;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_com_sunya_cdm_api_EnumTypedef;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_kotlin_collections_Map;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_com_sunya_cdm_api_OpaqueTypedef;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_com_sunya_cdm_api_VlenTypedef;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_com_fleeksoft_charset_Charset;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_com_sunya_cdm_array_ArrayByte;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_kotlin_collections_Iterator;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_com_sunya_cdm_array_ArrayDouble;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_kotlin_DoubleArray;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_com_sunya_cdm_array_ArrayFloat;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_kotlin_FloatArray;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_com_sunya_cdm_array_ArrayInt;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_com_sunya_cdm_array_ArrayLong;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_com_sunya_cdm_array_ArrayOpaque;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_com_sunya_cdm_array_ArrayOpaque_Companion;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_com_sunya_cdm_array_ArrayShort;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_kotlin_ShortArray;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_kotlin_Array;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_com_sunya_cdm_array_ArrayStructureData;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_com_sunya_cdm_array_ArrayStructureData_StructureData;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_com_sunya_cdm_array_ArrayUByte;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_com_sunya_cdm_array_ArrayUByte_Companion;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_com_sunya_cdm_array_ArrayUInt;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_com_sunya_cdm_array_ArrayUInt_Companion;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_com_sunya_cdm_array_ArrayULong;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_com_sunya_cdm_array_ArrayULong_Companion;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_com_sunya_cdm_array_ArrayUShort;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_com_sunya_cdm_array_ArrayUShort_Companion;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_com_sunya_netchdf_NetchdfFileFormat;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_com_sunya_netchdf_netcdf4_NUG;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_com_sunya_netchdf_NetchdfFileFormat_INVALID;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_com_sunya_netchdf_NetchdfFileFormat_NC_FORMAT_CLASSIC;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_com_sunya_netchdf_NetchdfFileFormat_NC_FORMAT_64BIT_OFFSET;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_com_sunya_netchdf_NetchdfFileFormat_NC_FORMAT_NETCDF4;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_com_sunya_netchdf_NetchdfFileFormat_NC_FORMAT_NETCDF4_CLASSIC;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_com_sunya_netchdf_NetchdfFileFormat_NC_FORMAT_64BIT_DATA;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_com_sunya_netchdf_NetchdfFileFormat_HDF5;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_com_sunya_netchdf_NetchdfFileFormat_HDF4;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_com_sunya_netchdf_NetchdfFileFormat_Companion;
typedef struct {
  libnetchdf_KNativePtr pinned;
} libnetchdf_kref_com_sunya_netchdfc_ArrayIntSection;


typedef struct {
  /* Service functions. */
  void (*DisposeStablePointer)(libnetchdf_KNativePtr ptr);
  void (*DisposeString)(const char* string);
  libnetchdf_KBoolean (*IsInstance)(libnetchdf_KNativePtr ref, const libnetchdf_KType* type);
  libnetchdf_kref_kotlin_Byte (*createNullableByte)(libnetchdf_KByte);
  libnetchdf_KByte (*getNonNullValueOfByte)(libnetchdf_kref_kotlin_Byte);
  libnetchdf_kref_kotlin_Short (*createNullableShort)(libnetchdf_KShort);
  libnetchdf_KShort (*getNonNullValueOfShort)(libnetchdf_kref_kotlin_Short);
  libnetchdf_kref_kotlin_Int (*createNullableInt)(libnetchdf_KInt);
  libnetchdf_KInt (*getNonNullValueOfInt)(libnetchdf_kref_kotlin_Int);
  libnetchdf_kref_kotlin_Long (*createNullableLong)(libnetchdf_KLong);
  libnetchdf_KLong (*getNonNullValueOfLong)(libnetchdf_kref_kotlin_Long);
  libnetchdf_kref_kotlin_Float (*createNullableFloat)(libnetchdf_KFloat);
  libnetchdf_KFloat (*getNonNullValueOfFloat)(libnetchdf_kref_kotlin_Float);
  libnetchdf_kref_kotlin_Double (*createNullableDouble)(libnetchdf_KDouble);
  libnetchdf_KDouble (*getNonNullValueOfDouble)(libnetchdf_kref_kotlin_Double);
  libnetchdf_kref_kotlin_Char (*createNullableChar)(libnetchdf_KChar);
  libnetchdf_KChar (*getNonNullValueOfChar)(libnetchdf_kref_kotlin_Char);
  libnetchdf_kref_kotlin_Boolean (*createNullableBoolean)(libnetchdf_KBoolean);
  libnetchdf_KBoolean (*getNonNullValueOfBoolean)(libnetchdf_kref_kotlin_Boolean);
  libnetchdf_kref_kotlin_Unit (*createNullableUnit)(void);
  libnetchdf_kref_kotlin_UByte (*createNullableUByte)(libnetchdf_KUByte);
  libnetchdf_KUByte (*getNonNullValueOfUByte)(libnetchdf_kref_kotlin_UByte);
  libnetchdf_kref_kotlin_UShort (*createNullableUShort)(libnetchdf_KUShort);
  libnetchdf_KUShort (*getNonNullValueOfUShort)(libnetchdf_kref_kotlin_UShort);
  libnetchdf_kref_kotlin_UInt (*createNullableUInt)(libnetchdf_KUInt);
  libnetchdf_KUInt (*getNonNullValueOfUInt)(libnetchdf_kref_kotlin_UInt);
  libnetchdf_kref_kotlin_ULong (*createNullableULong)(libnetchdf_KULong);
  libnetchdf_KULong (*getNonNullValueOfULong)(libnetchdf_kref_kotlin_ULong);

  /* User functions. */
  struct {
    struct {
      struct {
        struct {
          struct {
            struct {
              struct {
                libnetchdf_KType* (*_type)(void);
                libnetchdf_kref_com_sunya_cdm_api_Dimension (*Dimension)(const char* name, libnetchdf_KInt len);
                libnetchdf_kref_com_sunya_cdm_api_Dimension (*Dimension_)(libnetchdf_KInt len);
                libnetchdf_kref_com_sunya_cdm_api_Dimension (*Dimension__)(libnetchdf_KLong len);
                libnetchdf_kref_com_sunya_cdm_api_Dimension (*Dimension___)(const char* orgName, libnetchdf_KLong length, libnetchdf_KBoolean isShared);
                libnetchdf_KBoolean (*get_isShared)(libnetchdf_kref_com_sunya_cdm_api_Dimension thiz);
                libnetchdf_KLong (*get_length)(libnetchdf_kref_com_sunya_cdm_api_Dimension thiz);
                const char* (*get_name)(libnetchdf_kref_com_sunya_cdm_api_Dimension thiz);
                const char* (*get_orgName)(libnetchdf_kref_com_sunya_cdm_api_Dimension thiz);
                const char* (*component1)(libnetchdf_kref_com_sunya_cdm_api_Dimension thiz);
                libnetchdf_KLong (*component2)(libnetchdf_kref_com_sunya_cdm_api_Dimension thiz);
                libnetchdf_KBoolean (*component3)(libnetchdf_kref_com_sunya_cdm_api_Dimension thiz);
                libnetchdf_kref_com_sunya_cdm_api_Dimension (*copy)(libnetchdf_kref_com_sunya_cdm_api_Dimension thiz, const char* orgName, libnetchdf_KLong length, libnetchdf_KBoolean isShared);
                libnetchdf_KBoolean (*equals)(libnetchdf_kref_com_sunya_cdm_api_Dimension thiz, libnetchdf_kref_kotlin_Any other);
                libnetchdf_KInt (*hashCode)(libnetchdf_kref_com_sunya_cdm_api_Dimension thiz);
                const char* (*toString)(libnetchdf_kref_com_sunya_cdm_api_Dimension thiz);
              } Dimension;
              struct {
                libnetchdf_KType* (*_type)(void);
                libnetchdf_kref_kotlin_collections_List (*get_attributes)(libnetchdf_kref_com_sunya_cdm_api_Group thiz);
                libnetchdf_kref_kotlin_collections_List (*get_dimensions)(libnetchdf_kref_com_sunya_cdm_api_Group thiz);
                libnetchdf_kref_kotlin_collections_List (*get_groups)(libnetchdf_kref_com_sunya_cdm_api_Group thiz);
                const char* (*get_name)(libnetchdf_kref_com_sunya_cdm_api_Group thiz);
                libnetchdf_kref_com_sunya_cdm_api_Group (*get_parent)(libnetchdf_kref_com_sunya_cdm_api_Group thiz);
                libnetchdf_kref_kotlin_collections_List (*get_typedefs)(libnetchdf_kref_com_sunya_cdm_api_Group thiz);
                libnetchdf_kref_kotlin_collections_List (*get_variables)(libnetchdf_kref_com_sunya_cdm_api_Group thiz);
                libnetchdf_kref_kotlin_collections_List (*allVariables)(libnetchdf_kref_com_sunya_cdm_api_Group thiz);
                libnetchdf_KBoolean (*equals)(libnetchdf_kref_com_sunya_cdm_api_Group thiz, libnetchdf_kref_kotlin_Any other);
                libnetchdf_kref_com_sunya_cdm_api_Attribute (*findAttribute)(libnetchdf_kref_com_sunya_cdm_api_Group thiz, const char* attName);
                libnetchdf_kref_com_sunya_cdm_api_Dimension (*findDimension)(libnetchdf_kref_com_sunya_cdm_api_Group thiz, const char* dimName);
                libnetchdf_kref_com_sunya_cdm_api_Group (*findNestedGroupByShortName)(libnetchdf_kref_com_sunya_cdm_api_Group thiz, const char* shortName);
                libnetchdf_kref_com_sunya_cdm_api_Typedef (*findTypedef)(libnetchdf_kref_com_sunya_cdm_api_Group thiz, const char* typedefName);
                libnetchdf_kref_com_sunya_cdm_api_Variable (*findVariableByAttribute)(libnetchdf_kref_com_sunya_cdm_api_Group thiz, const char* attName, const char* attValue);
                const char* (*fullname)(libnetchdf_kref_com_sunya_cdm_api_Group thiz);
                libnetchdf_KInt (*hashCode)(libnetchdf_kref_com_sunya_cdm_api_Group thiz);
                const char* (*toString)(libnetchdf_kref_com_sunya_cdm_api_Group thiz);
              } Group;
              struct {
                libnetchdf_KType* (*_type)(void);
                libnetchdf_kref_com_sunya_cdm_api_InvalidRangeException (*InvalidRangeException)(const char* s);
              } InvalidRangeException;
              struct {
                libnetchdf_KType* (*_type)(void);
                libnetchdf_KLong (*get_size)(libnetchdf_kref_com_sunya_cdm_api_Netchdf thiz);
                const char* (*cdl)(libnetchdf_kref_com_sunya_cdm_api_Netchdf thiz);
                libnetchdf_kref_com_sunya_cdm_api_Variable (*findVariable)(libnetchdf_kref_com_sunya_cdm_api_Netchdf thiz, const char* fullName);
                const char* (*location)(libnetchdf_kref_com_sunya_cdm_api_Netchdf thiz);
                libnetchdf_kref_com_sunya_cdm_api_Group (*rootGroup)(libnetchdf_kref_com_sunya_cdm_api_Netchdf thiz);
                const char* (*type)(libnetchdf_kref_com_sunya_cdm_api_Netchdf thiz);
              } Netchdf;
              struct {
                libnetchdf_KType* (*_type)(void);
                libnetchdf_kref_com_sunya_cdm_api_Section (*Section)(libnetchdf_kref_kotlin_LongArray varShape);
                libnetchdf_kref_com_sunya_cdm_api_Section (*Section_)(libnetchdf_kref_kotlin_IntArray start, libnetchdf_kref_kotlin_IntArray len, libnetchdf_kref_kotlin_LongArray varShape);
                libnetchdf_kref_com_sunya_cdm_api_Section (*Section__)(libnetchdf_kref_kotlin_collections_List ranges, libnetchdf_kref_kotlin_LongArray varShape);
                libnetchdf_kref_kotlin_collections_List (*get_ranges)(libnetchdf_kref_com_sunya_cdm_api_Section thiz);
                libnetchdf_KInt (*get_rank)(libnetchdf_kref_com_sunya_cdm_api_Section thiz);
                libnetchdf_kref_kotlin_LongArray (*get_shape)(libnetchdf_kref_com_sunya_cdm_api_Section thiz);
                libnetchdf_KLong (*get_totalElements)(libnetchdf_kref_com_sunya_cdm_api_Section thiz);
                libnetchdf_kref_kotlin_LongArray (*get_varShape)(libnetchdf_kref_com_sunya_cdm_api_Section thiz);
                libnetchdf_kref_kotlin_collections_List (*component1)(libnetchdf_kref_com_sunya_cdm_api_Section thiz);
                libnetchdf_kref_kotlin_LongArray (*component2)(libnetchdf_kref_com_sunya_cdm_api_Section thiz);
                libnetchdf_kref_com_sunya_cdm_api_Section (*copy)(libnetchdf_kref_com_sunya_cdm_api_Section thiz, libnetchdf_kref_kotlin_collections_List ranges, libnetchdf_kref_kotlin_LongArray varShape);
                libnetchdf_KBoolean (*equals)(libnetchdf_kref_com_sunya_cdm_api_Section thiz, libnetchdf_kref_kotlin_Any other);
                libnetchdf_KInt (*hashCode)(libnetchdf_kref_com_sunya_cdm_api_Section thiz);
                const char* (*toString)(libnetchdf_kref_com_sunya_cdm_api_Section thiz);
              } Section;
              struct {
                struct {
                  libnetchdf_KType* (*_type)(void);
                  libnetchdf_kref_com_sunya_cdm_api_SectionPartial_Companion (*_instance)();
                  libnetchdf_kref_com_sunya_cdm_api_Section (*get_SCALAR)(libnetchdf_kref_com_sunya_cdm_api_SectionPartial_Companion thiz);
                  libnetchdf_kref_com_sunya_cdm_api_Section (*fill)(libnetchdf_kref_com_sunya_cdm_api_SectionPartial_Companion thiz, libnetchdf_kref_com_sunya_cdm_api_SectionPartial s, libnetchdf_kref_kotlin_LongArray varShape);
                  libnetchdf_kref_com_sunya_cdm_api_SectionPartial (*fromSpec)(libnetchdf_kref_com_sunya_cdm_api_SectionPartial_Companion thiz, const char* sectionSpec);
                } Companion;
                libnetchdf_KType* (*_type)(void);
                libnetchdf_kref_com_sunya_cdm_api_SectionPartial (*SectionPartial)(libnetchdf_kref_kotlin_collections_List ranges);
                libnetchdf_kref_kotlin_collections_List (*get_ranges)(libnetchdf_kref_com_sunya_cdm_api_SectionPartial thiz);
                libnetchdf_kref_kotlin_collections_List (*component1)(libnetchdf_kref_com_sunya_cdm_api_SectionPartial thiz);
                libnetchdf_kref_com_sunya_cdm_api_SectionPartial (*copy)(libnetchdf_kref_com_sunya_cdm_api_SectionPartial thiz, libnetchdf_kref_kotlin_collections_List ranges);
                libnetchdf_KBoolean (*equals)(libnetchdf_kref_com_sunya_cdm_api_SectionPartial thiz, libnetchdf_kref_kotlin_Any other);
                libnetchdf_KInt (*hashCode)(libnetchdf_kref_com_sunya_cdm_api_SectionPartial thiz);
                const char* (*toString)(libnetchdf_kref_com_sunya_cdm_api_SectionPartial thiz);
              } SectionPartial;
              struct {
                struct {
                  libnetchdf_kref_com_sunya_cdm_api_TypedefKind (*get)(); /* enum entry for Compound. */
                } Compound;
                struct {
                  libnetchdf_kref_com_sunya_cdm_api_TypedefKind (*get)(); /* enum entry for Enum. */
                } Enum;
                struct {
                  libnetchdf_kref_com_sunya_cdm_api_TypedefKind (*get)(); /* enum entry for Opaque. */
                } Opaque;
                struct {
                  libnetchdf_kref_com_sunya_cdm_api_TypedefKind (*get)(); /* enum entry for Vlen. */
                } Vlen;
                struct {
                  libnetchdf_kref_com_sunya_cdm_api_TypedefKind (*get)(); /* enum entry for Unknown. */
                } Unknown;
                libnetchdf_KType* (*_type)(void);
              } TypedefKind;
              struct {
                libnetchdf_KType* (*_type)(void);
                libnetchdf_kref_com_sunya_cdm_api_Typedef (*Typedef)(libnetchdf_kref_com_sunya_cdm_api_TypedefKind kind, const char* orgName, libnetchdf_kref_com_sunya_cdm_api_Datatype baseType);
                libnetchdf_kref_com_sunya_cdm_api_Datatype (*get_baseType)(libnetchdf_kref_com_sunya_cdm_api_Typedef thiz);
                libnetchdf_kref_com_sunya_cdm_api_TypedefKind (*get_kind)(libnetchdf_kref_com_sunya_cdm_api_Typedef thiz);
                const char* (*get_name)(libnetchdf_kref_com_sunya_cdm_api_Typedef thiz);
                const char* (*cdl)(libnetchdf_kref_com_sunya_cdm_api_Typedef thiz, libnetchdf_kref_com_sunya_cdm_util_Indent indent);
                libnetchdf_KBoolean (*equals)(libnetchdf_kref_com_sunya_cdm_api_Typedef thiz, libnetchdf_kref_kotlin_Any other);
                libnetchdf_KInt (*hashCode)(libnetchdf_kref_com_sunya_cdm_api_Typedef thiz);
                const char* (*toString)(libnetchdf_kref_com_sunya_cdm_api_Typedef thiz);
              } Typedef;
              struct {
                libnetchdf_KType* (*_type)(void);
                libnetchdf_kref_com_sunya_cdm_api_CompoundTypedef (*CompoundTypedef)(const char* name, libnetchdf_kref_kotlin_collections_List members);
                libnetchdf_kref_kotlin_collections_List (*get_members)(libnetchdf_kref_com_sunya_cdm_api_CompoundTypedef thiz);
                const char* (*cdl)(libnetchdf_kref_com_sunya_cdm_api_CompoundTypedef thiz, libnetchdf_kref_com_sunya_cdm_util_Indent indent);
                libnetchdf_KBoolean (*equals)(libnetchdf_kref_com_sunya_cdm_api_CompoundTypedef thiz, libnetchdf_kref_kotlin_Any other);
                libnetchdf_KInt (*hashCode)(libnetchdf_kref_com_sunya_cdm_api_CompoundTypedef thiz);
              } CompoundTypedef;
              struct {
                libnetchdf_KType* (*_type)(void);
                libnetchdf_kref_com_sunya_cdm_api_EnumTypedef (*EnumTypedef)(const char* name, libnetchdf_kref_com_sunya_cdm_api_Datatype baseType, libnetchdf_kref_kotlin_collections_Map valueMap);
                libnetchdf_kref_kotlin_collections_Map (*get_valueMap)(libnetchdf_kref_com_sunya_cdm_api_EnumTypedef thiz);
                const char* (*cdl)(libnetchdf_kref_com_sunya_cdm_api_EnumTypedef thiz, libnetchdf_kref_com_sunya_cdm_util_Indent indent);
                libnetchdf_kref_com_sunya_cdm_array_ArrayString (*convertEnums)(libnetchdf_kref_com_sunya_cdm_api_EnumTypedef thiz, libnetchdf_kref_kotlin_Any enums);
                libnetchdf_KBoolean (*equals)(libnetchdf_kref_com_sunya_cdm_api_EnumTypedef thiz, libnetchdf_kref_kotlin_Any other);
                libnetchdf_KInt (*hashCode)(libnetchdf_kref_com_sunya_cdm_api_EnumTypedef thiz);
              } EnumTypedef;
              struct {
                libnetchdf_KType* (*_type)(void);
                libnetchdf_kref_com_sunya_cdm_api_OpaqueTypedef (*OpaqueTypedef)(const char* name, libnetchdf_KInt elemSize);
                libnetchdf_KInt (*get_elemSize)(libnetchdf_kref_com_sunya_cdm_api_OpaqueTypedef thiz);
                const char* (*cdl)(libnetchdf_kref_com_sunya_cdm_api_OpaqueTypedef thiz, libnetchdf_kref_com_sunya_cdm_util_Indent indent);
                libnetchdf_KBoolean (*equals)(libnetchdf_kref_com_sunya_cdm_api_OpaqueTypedef thiz, libnetchdf_kref_kotlin_Any other);
                libnetchdf_KInt (*hashCode)(libnetchdf_kref_com_sunya_cdm_api_OpaqueTypedef thiz);
              } OpaqueTypedef;
              struct {
                libnetchdf_KType* (*_type)(void);
                libnetchdf_kref_com_sunya_cdm_api_VlenTypedef (*VlenTypedef)(const char* name, libnetchdf_kref_com_sunya_cdm_api_Datatype baseType);
                const char* (*cdl)(libnetchdf_kref_com_sunya_cdm_api_VlenTypedef thiz, libnetchdf_kref_com_sunya_cdm_util_Indent indent);
                libnetchdf_KBoolean (*equals)(libnetchdf_kref_com_sunya_cdm_api_VlenTypedef thiz, libnetchdf_kref_kotlin_Any other);
              } VlenTypedef;
              libnetchdf_KBoolean (*get_strict)();
              const char* (*cdl)(libnetchdf_kref_com_sunya_cdm_api_Netchdf netcdf);
              const char* (*toHex)(libnetchdf_kref_kotlin_ByteArray thiz);
              libnetchdf_kref_kotlin_Pair (*breakoutInner)(libnetchdf_kref_kotlin_IntArray thiz);
              libnetchdf_KInt (*computeSize)(libnetchdf_kref_kotlin_IntArray thiz);
              libnetchdf_KLong (*computeSize_)(libnetchdf_kref_kotlin_LongArray thiz);
              libnetchdf_KBoolean (*equivalent)(libnetchdf_kref_kotlin_IntArray thiz, libnetchdf_kref_kotlin_IntArray other);
              libnetchdf_KBoolean (*equivalent_)(libnetchdf_kref_kotlin_LongArray thiz, libnetchdf_kref_kotlin_LongArray other);
              libnetchdf_KBoolean (*isScalar)(libnetchdf_kref_kotlin_LongArray thiz);
              libnetchdf_kref_kotlin_IntArray (*toIntArray)(libnetchdf_kref_kotlin_LongArray thiz);
              libnetchdf_kref_kotlin_LongArray (*toLongArray)(libnetchdf_kref_kotlin_IntArray thiz);
              libnetchdf_kref_kotlin_collections_List (*convertEnums)(libnetchdf_kref_com_sunya_cdm_api_Attribute thiz);
              libnetchdf_kref_com_sunya_cdm_array_ArrayString (*convertEnums_)(libnetchdf_kref_com_sunya_cdm_array_ArrayTyped thiz);
            } api;
            struct {
              struct {
                libnetchdf_KType* (*_type)(void);
                libnetchdf_kref_com_sunya_cdm_array_ArrayByte (*ArrayByte)(libnetchdf_kref_kotlin_IntArray shape, libnetchdf_kref_kotlin_ByteArray values);
                libnetchdf_kref_kotlin_ByteArray (*get_values)(libnetchdf_kref_com_sunya_cdm_array_ArrayByte thiz);
                libnetchdf_kref_kotlin_collections_Iterator (*iterator)(libnetchdf_kref_com_sunya_cdm_array_ArrayByte thiz);
                libnetchdf_kref_com_sunya_cdm_array_ArrayByte (*section)(libnetchdf_kref_com_sunya_cdm_array_ArrayByte thiz, libnetchdf_kref_com_sunya_cdm_api_Section section);
              } ArrayByte;
              struct {
                libnetchdf_KType* (*_type)(void);
                libnetchdf_kref_com_sunya_cdm_array_ArrayDouble (*ArrayDouble)(libnetchdf_kref_kotlin_IntArray shape, libnetchdf_kref_kotlin_DoubleArray values);
                libnetchdf_kref_kotlin_DoubleArray (*get_values)(libnetchdf_kref_com_sunya_cdm_array_ArrayDouble thiz);
                libnetchdf_kref_kotlin_collections_Iterator (*iterator)(libnetchdf_kref_com_sunya_cdm_array_ArrayDouble thiz);
                libnetchdf_kref_com_sunya_cdm_array_ArrayDouble (*section)(libnetchdf_kref_com_sunya_cdm_array_ArrayDouble thiz, libnetchdf_kref_com_sunya_cdm_api_Section section);
              } ArrayDouble;
              struct {
                libnetchdf_KType* (*_type)(void);
                libnetchdf_kref_com_sunya_cdm_array_ArrayFloat (*ArrayFloat)(libnetchdf_kref_kotlin_IntArray shape, libnetchdf_kref_kotlin_FloatArray values);
                libnetchdf_kref_kotlin_FloatArray (*get_values)(libnetchdf_kref_com_sunya_cdm_array_ArrayFloat thiz);
                libnetchdf_kref_kotlin_collections_Iterator (*iterator)(libnetchdf_kref_com_sunya_cdm_array_ArrayFloat thiz);
                libnetchdf_kref_com_sunya_cdm_array_ArrayFloat (*section)(libnetchdf_kref_com_sunya_cdm_array_ArrayFloat thiz, libnetchdf_kref_com_sunya_cdm_api_Section section);
              } ArrayFloat;
              struct {
                libnetchdf_KType* (*_type)(void);
                libnetchdf_kref_com_sunya_cdm_array_ArrayInt (*ArrayInt)(libnetchdf_kref_kotlin_IntArray shape, libnetchdf_kref_kotlin_IntArray values);
                libnetchdf_kref_kotlin_IntArray (*get_values)(libnetchdf_kref_com_sunya_cdm_array_ArrayInt thiz);
                libnetchdf_kref_kotlin_collections_Iterator (*iterator)(libnetchdf_kref_com_sunya_cdm_array_ArrayInt thiz);
                libnetchdf_kref_com_sunya_cdm_array_ArrayInt (*section)(libnetchdf_kref_com_sunya_cdm_array_ArrayInt thiz, libnetchdf_kref_com_sunya_cdm_api_Section section);
              } ArrayInt;
              struct {
                libnetchdf_KType* (*_type)(void);
                libnetchdf_kref_com_sunya_cdm_array_ArrayLong (*ArrayLong)(libnetchdf_kref_kotlin_IntArray shape, libnetchdf_kref_kotlin_LongArray values);
                libnetchdf_kref_kotlin_LongArray (*get_values)(libnetchdf_kref_com_sunya_cdm_array_ArrayLong thiz);
                libnetchdf_kref_kotlin_collections_Iterator (*iterator)(libnetchdf_kref_com_sunya_cdm_array_ArrayLong thiz);
                libnetchdf_kref_com_sunya_cdm_array_ArrayLong (*section)(libnetchdf_kref_com_sunya_cdm_array_ArrayLong thiz, libnetchdf_kref_com_sunya_cdm_api_Section section);
              } ArrayLong;
              struct {
                struct {
                  libnetchdf_KType* (*_type)(void);
                  libnetchdf_kref_com_sunya_cdm_array_ArrayOpaque_Companion (*_instance)();
                } Companion;
                libnetchdf_KType* (*_type)(void);
                libnetchdf_kref_com_sunya_cdm_array_ArrayOpaque (*ArrayOpaque)(libnetchdf_kref_kotlin_IntArray shape, libnetchdf_kref_kotlin_collections_List values, libnetchdf_KInt size);
                libnetchdf_KInt (*get_size)(libnetchdf_kref_com_sunya_cdm_array_ArrayOpaque thiz);
                libnetchdf_kref_kotlin_collections_List (*get_values)(libnetchdf_kref_com_sunya_cdm_array_ArrayOpaque thiz);
                libnetchdf_kref_kotlin_collections_Iterator (*iterator)(libnetchdf_kref_com_sunya_cdm_array_ArrayOpaque thiz);
                libnetchdf_kref_com_sunya_cdm_array_ArrayOpaque (*section)(libnetchdf_kref_com_sunya_cdm_array_ArrayOpaque thiz, libnetchdf_kref_com_sunya_cdm_api_Section section);
                const char* (*showValues)(libnetchdf_kref_com_sunya_cdm_array_ArrayOpaque thiz);
              } ArrayOpaque;
              struct {
                libnetchdf_KType* (*_type)(void);
                libnetchdf_kref_com_sunya_cdm_array_ArrayShort (*ArrayShort)(libnetchdf_kref_kotlin_IntArray shape, libnetchdf_kref_kotlin_ShortArray values);
                libnetchdf_kref_kotlin_ShortArray (*get_values)(libnetchdf_kref_com_sunya_cdm_array_ArrayShort thiz);
                libnetchdf_kref_kotlin_collections_Iterator (*iterator)(libnetchdf_kref_com_sunya_cdm_array_ArrayShort thiz);
                libnetchdf_kref_com_sunya_cdm_array_ArrayShort (*section)(libnetchdf_kref_com_sunya_cdm_array_ArrayShort thiz, libnetchdf_kref_com_sunya_cdm_api_Section section);
              } ArrayShort;
              struct {
                libnetchdf_KType* (*_type)(void);
                libnetchdf_kref_com_sunya_cdm_array_ArrayString (*ArrayString)(libnetchdf_kref_kotlin_IntArray shape, libnetchdf_kref_kotlin_Array valueArray);
                libnetchdf_kref_com_sunya_cdm_array_ArrayString (*ArrayString_)(libnetchdf_kref_kotlin_IntArray shape, libnetchdf_kref_kotlin_collections_List values);
                libnetchdf_kref_kotlin_collections_List (*get_values)(libnetchdf_kref_com_sunya_cdm_array_ArrayString thiz);
                libnetchdf_kref_kotlin_collections_Iterator (*iterator)(libnetchdf_kref_com_sunya_cdm_array_ArrayString thiz);
                libnetchdf_kref_com_sunya_cdm_array_ArrayString (*section)(libnetchdf_kref_com_sunya_cdm_array_ArrayString thiz, libnetchdf_kref_com_sunya_cdm_api_Section section);
                const char* (*showValues)(libnetchdf_kref_com_sunya_cdm_array_ArrayString thiz);
              } ArrayString;
              struct {
                struct {
                  libnetchdf_KType* (*_type)(void);
                  libnetchdf_kref_com_sunya_cdm_array_ArrayStructureData_StructureData (*StructureData)(libnetchdf_kref_com_sunya_cdm_array_ArrayStructureData thiz, libnetchdf_kref_kotlin_ByteArray ba, libnetchdf_KInt offset, libnetchdf_kref_kotlin_collections_List members);
                  libnetchdf_kref_kotlin_ByteArray (*get_ba)(libnetchdf_kref_com_sunya_cdm_array_ArrayStructureData_StructureData thiz);
                  libnetchdf_kref_kotlin_collections_List (*get_members)(libnetchdf_kref_com_sunya_cdm_array_ArrayStructureData_StructureData thiz);
                  libnetchdf_KInt (*get_offset)(libnetchdf_kref_com_sunya_cdm_array_ArrayStructureData_StructureData thiz);
                  libnetchdf_KBoolean (*equals)(libnetchdf_kref_com_sunya_cdm_array_ArrayStructureData_StructureData thiz, libnetchdf_kref_kotlin_Any other);
                  libnetchdf_KInt (*hashCode)(libnetchdf_kref_com_sunya_cdm_array_ArrayStructureData_StructureData thiz);
                  const char* (*memberValues)(libnetchdf_kref_com_sunya_cdm_array_ArrayStructureData_StructureData thiz);
                  const char* (*toString)(libnetchdf_kref_com_sunya_cdm_array_ArrayStructureData_StructureData thiz);
                } StructureData;
                libnetchdf_KType* (*_type)(void);
                libnetchdf_kref_com_sunya_cdm_array_ArrayStructureData (*ArrayStructureData)(libnetchdf_kref_kotlin_IntArray shape, libnetchdf_kref_kotlin_ByteArray ba, libnetchdf_KBoolean isBE, libnetchdf_KInt recsize, libnetchdf_kref_kotlin_collections_List members);
                libnetchdf_kref_kotlin_ByteArray (*get_ba)(libnetchdf_kref_com_sunya_cdm_array_ArrayStructureData thiz);
                libnetchdf_KBoolean (*get_isBE)(libnetchdf_kref_com_sunya_cdm_array_ArrayStructureData thiz);
                libnetchdf_kref_kotlin_collections_List (*get_members)(libnetchdf_kref_com_sunya_cdm_array_ArrayStructureData thiz);
                libnetchdf_KInt (*get_recsize)(libnetchdf_kref_com_sunya_cdm_array_ArrayStructureData thiz);
                libnetchdf_kref_com_sunya_cdm_array_ArrayStructureData_StructureData (*get)(libnetchdf_kref_com_sunya_cdm_array_ArrayStructureData thiz, libnetchdf_KInt idx);
                libnetchdf_kref_kotlin_collections_Iterator (*iterator)(libnetchdf_kref_com_sunya_cdm_array_ArrayStructureData thiz);
                libnetchdf_kref_com_sunya_cdm_array_ArrayStructureData (*section)(libnetchdf_kref_com_sunya_cdm_array_ArrayStructureData thiz, libnetchdf_kref_com_sunya_cdm_api_Section section);
                const char* (*toString)(libnetchdf_kref_com_sunya_cdm_array_ArrayStructureData thiz);
              } ArrayStructureData;
              struct {
                struct {
                  libnetchdf_KType* (*_type)(void);
                  libnetchdf_kref_com_sunya_cdm_array_ArrayUByte_Companion (*_instance)();
                } Companion;
                libnetchdf_KType* (*_type)(void);
                libnetchdf_kref_com_sunya_cdm_array_ArrayUByte (*ArrayUByte)(libnetchdf_kref_kotlin_IntArray shape, libnetchdf_kref_kotlin_ByteArray values);
                libnetchdf_kref_com_sunya_cdm_array_ArrayUByte (*ArrayUByte_)(libnetchdf_kref_kotlin_IntArray shape, libnetchdf_kref_com_sunya_cdm_api_Datatype datatype, libnetchdf_kref_kotlin_ByteArray values);
                libnetchdf_kref_kotlin_ByteArray (*get_values)(libnetchdf_kref_com_sunya_cdm_array_ArrayUByte thiz);
                libnetchdf_kref_kotlin_collections_Iterator (*iterator)(libnetchdf_kref_com_sunya_cdm_array_ArrayUByte thiz);
                libnetchdf_kref_com_sunya_cdm_array_ArrayUByte (*section)(libnetchdf_kref_com_sunya_cdm_array_ArrayUByte thiz, libnetchdf_kref_com_sunya_cdm_api_Section section);
              } ArrayUByte;
              struct {
                struct {
                  libnetchdf_KType* (*_type)(void);
                  libnetchdf_kref_com_sunya_cdm_array_ArrayUInt_Companion (*_instance)();
                } Companion;
                libnetchdf_KType* (*_type)(void);
                libnetchdf_kref_com_sunya_cdm_array_ArrayUInt (*ArrayUInt)(libnetchdf_kref_kotlin_IntArray shape, libnetchdf_kref_kotlin_IntArray values);
                libnetchdf_kref_com_sunya_cdm_array_ArrayUInt (*ArrayUInt_)(libnetchdf_kref_kotlin_IntArray shape, libnetchdf_kref_com_sunya_cdm_api_Datatype datatype, libnetchdf_kref_kotlin_IntArray values);
                libnetchdf_kref_kotlin_IntArray (*get_values)(libnetchdf_kref_com_sunya_cdm_array_ArrayUInt thiz);
                libnetchdf_kref_kotlin_collections_Iterator (*iterator)(libnetchdf_kref_com_sunya_cdm_array_ArrayUInt thiz);
                libnetchdf_kref_com_sunya_cdm_array_ArrayUInt (*section)(libnetchdf_kref_com_sunya_cdm_array_ArrayUInt thiz, libnetchdf_kref_com_sunya_cdm_api_Section section);
              } ArrayUInt;
              struct {
                struct {
                  libnetchdf_KType* (*_type)(void);
                  libnetchdf_kref_com_sunya_cdm_array_ArrayULong_Companion (*_instance)();
                } Companion;
                libnetchdf_KType* (*_type)(void);
                libnetchdf_kref_com_sunya_cdm_array_ArrayULong (*ArrayULong)(libnetchdf_kref_kotlin_IntArray shape, libnetchdf_kref_kotlin_LongArray values);
                libnetchdf_kref_kotlin_LongArray (*get_values)(libnetchdf_kref_com_sunya_cdm_array_ArrayULong thiz);
                libnetchdf_kref_kotlin_collections_Iterator (*iterator)(libnetchdf_kref_com_sunya_cdm_array_ArrayULong thiz);
                libnetchdf_kref_com_sunya_cdm_array_ArrayULong (*section)(libnetchdf_kref_com_sunya_cdm_array_ArrayULong thiz, libnetchdf_kref_com_sunya_cdm_api_Section section);
              } ArrayULong;
              struct {
                struct {
                  libnetchdf_KType* (*_type)(void);
                  libnetchdf_kref_com_sunya_cdm_array_ArrayUShort_Companion (*_instance)();
                } Companion;
                libnetchdf_KType* (*_type)(void);
                libnetchdf_kref_com_sunya_cdm_array_ArrayUShort (*ArrayUShort)(libnetchdf_kref_kotlin_IntArray shape, libnetchdf_kref_kotlin_ShortArray values);
                libnetchdf_kref_com_sunya_cdm_array_ArrayUShort (*ArrayUShort_)(libnetchdf_kref_kotlin_IntArray shape, libnetchdf_kref_com_sunya_cdm_api_Datatype datatype, libnetchdf_kref_kotlin_ShortArray values);
                libnetchdf_kref_kotlin_ShortArray (*get_values)(libnetchdf_kref_com_sunya_cdm_array_ArrayUShort thiz);
                libnetchdf_kref_kotlin_collections_Iterator (*iterator)(libnetchdf_kref_com_sunya_cdm_array_ArrayUShort thiz);
                libnetchdf_kref_com_sunya_cdm_array_ArrayUShort (*section)(libnetchdf_kref_com_sunya_cdm_array_ArrayUShort thiz, libnetchdf_kref_com_sunya_cdm_api_Section section);
              } ArrayUShort;
              libnetchdf_kref_kotlin_Any (*convertFromBytes)(libnetchdf_kref_com_sunya_cdm_api_Datatype datatype, libnetchdf_kref_kotlin_ByteArray ba, libnetchdf_KBoolean isBE, libnetchdf_kref_com_fleeksoft_charset_Charset charset);
              libnetchdf_kref_kotlin_ByteArray (*convertFromDouble)(libnetchdf_KDouble dval, libnetchdf_KBoolean isBE);
              libnetchdf_kref_kotlin_ByteArray (*convertFromFloat)(libnetchdf_KFloat fval, libnetchdf_KBoolean isBE);
              libnetchdf_kref_kotlin_ByteArray (*convertFromInt)(libnetchdf_KInt ival, libnetchdf_KBoolean isBE);
              libnetchdf_kref_kotlin_ByteArray (*convertFromLong)(libnetchdf_KLong lval, libnetchdf_KBoolean isBE);
              libnetchdf_kref_kotlin_ByteArray (*convertFromShort)(libnetchdf_KShort value, libnetchdf_KBoolean isBE);
              libnetchdf_kref_kotlin_ByteArray (*convertToBytes)(libnetchdf_kref_com_sunya_cdm_api_Datatype datatype, libnetchdf_kref_kotlin_Any value, libnetchdf_KBoolean isBE, libnetchdf_kref_com_fleeksoft_charset_Charset charset);
              libnetchdf_KDouble (*convertToDouble)(libnetchdf_kref_kotlin_ByteArray ba, libnetchdf_KInt offset, libnetchdf_KBoolean isBE);
              libnetchdf_KFloat (*convertToFloat)(libnetchdf_kref_kotlin_ByteArray ba, libnetchdf_KInt offset, libnetchdf_KBoolean isBE);
              libnetchdf_KInt (*convertToInt)(libnetchdf_kref_kotlin_ByteArray ba, libnetchdf_KInt offset, libnetchdf_KBoolean isBE);
              libnetchdf_KLong (*convertToLong)(libnetchdf_kref_kotlin_ByteArray ba, libnetchdf_KInt offset, libnetchdf_KBoolean isBE);
              libnetchdf_KShort (*convertToShort)(libnetchdf_kref_kotlin_ByteArray ba, libnetchdf_KInt offset, libnetchdf_KBoolean isBE);
              const char* (*makeString)(libnetchdf_kref_kotlin_ByteArray ba);
              const char* (*makeStringZ)(libnetchdf_kref_kotlin_ByteArray ba, libnetchdf_KInt start, libnetchdf_KInt maxBytes, libnetchdf_kref_com_fleeksoft_charset_Charset charset);
            } array;
            struct {
              struct {
                libnetchdf_KType* (*_type)(void);
                libnetchdf_kref_com_sunya_cdm_util_Indent (*Indent)(libnetchdf_KInt nspaces);
                libnetchdf_kref_com_sunya_cdm_util_Indent (*Indent_)(libnetchdf_KInt nspaces, libnetchdf_KInt startingLevel);
                libnetchdf_kref_com_sunya_cdm_util_Indent (*Indent__)(libnetchdf_KInt nspaces, libnetchdf_KInt level, libnetchdf_KInt ntabs);
                libnetchdf_KInt (*get_level)(libnetchdf_kref_com_sunya_cdm_util_Indent thiz);
                void (*set_level)(libnetchdf_kref_com_sunya_cdm_util_Indent thiz, libnetchdf_KInt set);
                libnetchdf_KInt (*get_nspaces)(libnetchdf_kref_com_sunya_cdm_util_Indent thiz);
                libnetchdf_KInt (*get_ntabs)(libnetchdf_kref_com_sunya_cdm_util_Indent thiz);
                libnetchdf_KInt (*component1)(libnetchdf_kref_com_sunya_cdm_util_Indent thiz);
                libnetchdf_KInt (*component2)(libnetchdf_kref_com_sunya_cdm_util_Indent thiz);
                libnetchdf_KInt (*component3)(libnetchdf_kref_com_sunya_cdm_util_Indent thiz);
                libnetchdf_kref_com_sunya_cdm_util_Indent (*copy)(libnetchdf_kref_com_sunya_cdm_util_Indent thiz, libnetchdf_KInt nspaces, libnetchdf_KInt level, libnetchdf_KInt ntabs);
                libnetchdf_kref_com_sunya_cdm_util_Indent (*decr)(libnetchdf_kref_com_sunya_cdm_util_Indent thiz);
                libnetchdf_kref_com_sunya_cdm_util_Indent (*decrTab)(libnetchdf_kref_com_sunya_cdm_util_Indent thiz, libnetchdf_KInt amount);
                libnetchdf_KBoolean (*equals)(libnetchdf_kref_com_sunya_cdm_util_Indent thiz, libnetchdf_kref_kotlin_Any other);
                libnetchdf_KInt (*hashCode)(libnetchdf_kref_com_sunya_cdm_util_Indent thiz);
                libnetchdf_kref_com_sunya_cdm_util_Indent (*incr)(libnetchdf_kref_com_sunya_cdm_util_Indent thiz);
                libnetchdf_kref_com_sunya_cdm_util_Indent (*incrTab)(libnetchdf_kref_com_sunya_cdm_util_Indent thiz, libnetchdf_KInt amount);
                const char* (*toString)(libnetchdf_kref_com_sunya_cdm_util_Indent thiz);
              } Indent;
              libnetchdf_kref_com_sunya_cdm_api_Group (*findNestedGroupByRelativeName)(libnetchdf_kref_com_sunya_cdm_api_Group thiz, libnetchdf_kref_kotlin_collections_List relativeNames);
              const char* (*get_reservedFullName)();
              libnetchdf_KDouble (*get_defaultDiffFloat)();
              libnetchdf_KDouble (*get_defaultMaxRelativeDiffDouble)();
              libnetchdf_KFloat (*get_defaultMaxRelativeDiffFloat)();
              libnetchdf_KFloat (*absoluteDifference)(libnetchdf_KFloat a, libnetchdf_KFloat b);
              libnetchdf_KDouble (*absoluteDifference_)(libnetchdf_KDouble a, libnetchdf_KDouble b);
            } util;
          } cdm;
          struct {
            struct {
              struct {
                libnetchdf_KType* (*_type)(void);
                libnetchdf_kref_com_sunya_netchdf_netcdf4_NUG (*_instance)();
                const char* (*get_ABBREV)(libnetchdf_kref_com_sunya_netchdf_netcdf4_NUG thiz);
                const char* (*get_ADD_OFFSET)(libnetchdf_kref_com_sunya_netchdf_netcdf4_NUG thiz);
                const char* (*get_CONVENTIONS)(libnetchdf_kref_com_sunya_netchdf_netcdf4_NUG thiz);
                const char* (*get_COORDINATES)(libnetchdf_kref_com_sunya_netchdf_netcdf4_NUG thiz);
                const char* (*get_DESCRIPTION)(libnetchdf_kref_com_sunya_netchdf_netcdf4_NUG thiz);
                const char* (*get_FILL_VALUE)(libnetchdf_kref_com_sunya_netchdf_netcdf4_NUG thiz);
                const char* (*get_HISTORY)(libnetchdf_kref_com_sunya_netchdf_netcdf4_NUG thiz);
                const char* (*get_LONG_NAME)(libnetchdf_kref_com_sunya_netchdf_netcdf4_NUG thiz);
                const char* (*get_MISSING_VALUE)(libnetchdf_kref_com_sunya_netchdf_netcdf4_NUG thiz);
                const char* (*get_NO_FILL)(libnetchdf_kref_com_sunya_netchdf_netcdf4_NUG thiz);
                const char* (*get_SCALE_FACTOR)(libnetchdf_kref_com_sunya_netchdf_netcdf4_NUG thiz);
                const char* (*get_TITLE)(libnetchdf_kref_com_sunya_netchdf_netcdf4_NUG thiz);
                const char* (*get_UDUNITS)(libnetchdf_kref_com_sunya_netchdf_netcdf4_NUG thiz);
                const char* (*get_UNITS)(libnetchdf_kref_com_sunya_netchdf_netcdf4_NUG thiz);
                const char* (*get_UNSIGNED)(libnetchdf_kref_com_sunya_netchdf_netcdf4_NUG thiz);
                const char* (*get_VALID_MAX)(libnetchdf_kref_com_sunya_netchdf_netcdf4_NUG thiz);
                const char* (*get_VALID_MIN)(libnetchdf_kref_com_sunya_netchdf_netcdf4_NUG thiz);
                const char* (*get_VALID_RANGE)(libnetchdf_kref_com_sunya_netchdf_netcdf4_NUG thiz);
              } NUG;
            } netcdf4;
            struct {
              struct {
                libnetchdf_kref_com_sunya_netchdf_NetchdfFileFormat (*get)(); /* enum entry for INVALID. */
              } INVALID;
              struct {
                libnetchdf_kref_com_sunya_netchdf_NetchdfFileFormat (*get)(); /* enum entry for NC_FORMAT_CLASSIC. */
              } NC_FORMAT_CLASSIC;
              struct {
                libnetchdf_kref_com_sunya_netchdf_NetchdfFileFormat (*get)(); /* enum entry for NC_FORMAT_64BIT_OFFSET. */
              } NC_FORMAT_64BIT_OFFSET;
              struct {
                libnetchdf_kref_com_sunya_netchdf_NetchdfFileFormat (*get)(); /* enum entry for NC_FORMAT_NETCDF4. */
              } NC_FORMAT_NETCDF4;
              struct {
                libnetchdf_kref_com_sunya_netchdf_NetchdfFileFormat (*get)(); /* enum entry for NC_FORMAT_NETCDF4_CLASSIC. */
              } NC_FORMAT_NETCDF4_CLASSIC;
              struct {
                libnetchdf_kref_com_sunya_netchdf_NetchdfFileFormat (*get)(); /* enum entry for NC_FORMAT_64BIT_DATA. */
              } NC_FORMAT_64BIT_DATA;
              struct {
                libnetchdf_kref_com_sunya_netchdf_NetchdfFileFormat (*get)(); /* enum entry for HDF5. */
              } HDF5;
              struct {
                libnetchdf_kref_com_sunya_netchdf_NetchdfFileFormat (*get)(); /* enum entry for HDF4. */
              } HDF4;
              struct {
                libnetchdf_KType* (*_type)(void);
                libnetchdf_kref_com_sunya_netchdf_NetchdfFileFormat_Companion (*_instance)();
                libnetchdf_kref_kotlin_ByteArray (*get_H5HEAD)(libnetchdf_kref_com_sunya_netchdf_NetchdfFileFormat_Companion thiz);
                libnetchdf_KInt (*get_MAGIC_NUMBER_LEN)(libnetchdf_kref_com_sunya_netchdf_NetchdfFileFormat_Companion thiz);
                libnetchdf_KLong (*get_MAXHEADERPOS)(libnetchdf_kref_com_sunya_netchdf_NetchdfFileFormat_Companion thiz);
                libnetchdf_kref_com_sunya_netchdf_NetchdfFileFormat (*netcdfFormat)(libnetchdf_kref_com_sunya_netchdf_NetchdfFileFormat_Companion thiz, libnetchdf_KInt format);
                const char* (*netcdfFormatExtended)(libnetchdf_kref_com_sunya_netchdf_NetchdfFileFormat_Companion thiz, libnetchdf_KInt formatx);
                const char* (*netcdfMode)(libnetchdf_kref_com_sunya_netchdf_NetchdfFileFormat_Companion thiz, libnetchdf_KInt mode);
              } Companion;
              libnetchdf_KType* (*_type)(void);
              libnetchdf_KBoolean (*get_isClassicModel)(libnetchdf_kref_com_sunya_netchdf_NetchdfFileFormat thiz);
              libnetchdf_KBoolean (*get_isExtendedModel)(libnetchdf_kref_com_sunya_netchdf_NetchdfFileFormat thiz);
              libnetchdf_KBoolean (*get_isLargeFile)(libnetchdf_kref_com_sunya_netchdf_NetchdfFileFormat thiz);
              libnetchdf_KBoolean (*get_isNetdf3format)(libnetchdf_kref_com_sunya_netchdf_NetchdfFileFormat thiz);
              libnetchdf_KBoolean (*get_isNetdf4format)(libnetchdf_kref_com_sunya_netchdf_NetchdfFileFormat thiz);
              const char* (*formatName)(libnetchdf_kref_com_sunya_netchdf_NetchdfFileFormat thiz);
              libnetchdf_KInt (*version)(libnetchdf_kref_com_sunya_netchdf_NetchdfFileFormat thiz);
            } NetchdfFileFormat;
            libnetchdf_kref_com_sunya_cdm_api_Netchdf (*openNetchdfFile)(const char* filename, libnetchdf_KBoolean strict);
            libnetchdf_kref_com_sunya_cdm_api_Netchdf (*openNetchdfFile_)(const char* filename, libnetchdf_kref_com_sunya_netchdf_NetchdfFileFormat format);
          } netchdf;
          struct {
            struct {
              libnetchdf_KType* (*_type)(void);
              libnetchdf_kref_com_sunya_netchdfc_ArrayIntSection (*ArrayIntSection)(const char* varName, libnetchdf_kref_kotlin_LongArray varShape, libnetchdf_kref_kotlin_IntArray array, libnetchdf_KInt nelems, libnetchdf_kref_kotlin_IntArray shape);
              libnetchdf_kref_kotlin_IntArray (*get_array)(libnetchdf_kref_com_sunya_netchdfc_ArrayIntSection thiz);
              libnetchdf_KInt (*get_nelems)(libnetchdf_kref_com_sunya_netchdfc_ArrayIntSection thiz);
              libnetchdf_kref_kotlin_IntArray (*get_shape)(libnetchdf_kref_com_sunya_netchdfc_ArrayIntSection thiz);
              const char* (*get_varName)(libnetchdf_kref_com_sunya_netchdfc_ArrayIntSection thiz);
              libnetchdf_kref_kotlin_LongArray (*get_varShape)(libnetchdf_kref_com_sunya_netchdfc_ArrayIntSection thiz);
            } ArrayIntSection;
            libnetchdf_kref_com_sunya_cdm_api_Netchdf (*openNetchdfFile)(const char* filename);
            const char* (*version)();
            libnetchdf_kref_com_sunya_netchdfc_ArrayIntSection (*readArrayInt)(libnetchdf_kref_com_sunya_cdm_api_Netchdf thiz, libnetchdf_kref_com_sunya_cdm_api_Variable v2);
            libnetchdf_kref_com_sunya_netchdfc_ArrayIntSection (*readArrayIntSection)(libnetchdf_kref_com_sunya_cdm_api_Netchdf thiz, libnetchdf_kref_com_sunya_cdm_api_Variable v2, libnetchdf_kref_kotlin_IntArray start, libnetchdf_kref_kotlin_IntArray length);
          } netchdfc;
        } sunya;
      } com;
    } root;
  } kotlin;
} libnetchdf_ExportedSymbols;
extern libnetchdf_ExportedSymbols* libnetchdf_symbols(void);
#ifdef __cplusplus
}  /* extern "C" */
#endif
#endif  /* KONAN_LIBNETCHDF_H */
