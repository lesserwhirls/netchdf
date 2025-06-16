package com.sunya.netchdf.hdf4

import com.sunya.cdm.api.Datatype
import com.sunya.netchdf.netcdf4.Netcdf4
import kotlin.test.*

class TestFillValues {

    @Test
    fun testNcDefaultFillValue() {
        assertEquals(Netcdf4.NC_FILL_BYTE, getNcDefaultFillValue(Datatype.BYTE))
        assertEquals(Netcdf4.NC_FILL_UBYTE, getNcDefaultFillValue(Datatype.UBYTE))
        assertEquals(Netcdf4.NC_FILL_UBYTE, getNcDefaultFillValue(Datatype.CHAR))
        assertEquals(Netcdf4.NC_FILL_SHORT, getNcDefaultFillValue(Datatype.SHORT))
        assertEquals(Netcdf4.NC_FILL_USHORT, getNcDefaultFillValue(Datatype.USHORT))
        assertEquals(Netcdf4.NC_FILL_INT, getNcDefaultFillValue(Datatype.INT))
        assertEquals(Netcdf4.NC_FILL_UINT, getNcDefaultFillValue(Datatype.UINT))
        assertEquals(Netcdf4.NC_FILL_FLOAT, getNcDefaultFillValue(Datatype.FLOAT))
        assertEquals(Netcdf4.NC_FILL_DOUBLE, getNcDefaultFillValue(Datatype.DOUBLE))
        assertEquals(Netcdf4.NC_FILL_INT64, getNcDefaultFillValue(Datatype.LONG))
        assertEquals(Netcdf4.NC_FILL_UINT64, getNcDefaultFillValue(Datatype.ULONG))
        assertEquals("", getNcDefaultFillValue(Datatype.STRING))
        assertEquals(0, getNcDefaultFillValue(Datatype.OPAQUE))
    }

    // #define FILL_BYTE    ((char)-127)        /* Largest Negative value */
    //#define FILL_CHAR    ((char)0)
    //#define FILL_SHORT    ((short)-32767)
    //#define FILL_LONG    ((long)-2147483647)

    @Test
    fun testSdDefaultFillValue() {
        assertEquals((-127).toByte(), getNcDefaultFillValue(Datatype.BYTE))
        assertEquals(32769.toShort(), getNcDefaultFillValue(Datatype.SHORT))
        assertEquals(32769.toUShort(), (getNcDefaultFillValue(Datatype.SHORT) as Short).toUShort())
        assertEquals((-2147483647), getNcDefaultFillValue(Datatype.INT))
    }

}