// Generated by jextract

package com.sunya.netchdf.hdf5Clib.ffm;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import java.lang.foreign.*;
import static java.lang.foreign.ValueLayout.*;
/**
 * {@snippet :
 * int (*H5A_operator2_t)(long location_id,char* attr_name,struct * ainfo,void* op_data);
 * }
 */
public interface H5A_operator2_t {

    int apply(long location_id, java.lang.foreign.MemorySegment attr_name, java.lang.foreign.MemorySegment ainfo, java.lang.foreign.MemorySegment op_data);
    static MemorySegment allocate(H5A_operator2_t fi, Arena scope) {
        return RuntimeHelper.upcallStub(constants$81.const$0, fi, constants$61.const$5, scope);
    }
    static H5A_operator2_t ofAddress(MemorySegment addr, Arena arena) {
        MemorySegment symbol = addr.reinterpret(arena, null);
        return (long _location_id, java.lang.foreign.MemorySegment _attr_name, java.lang.foreign.MemorySegment _ainfo, java.lang.foreign.MemorySegment _op_data) -> {
            try {
                return (int)constants$62.const$1.invokeExact(symbol, _location_id, _attr_name, _ainfo, _op_data);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


