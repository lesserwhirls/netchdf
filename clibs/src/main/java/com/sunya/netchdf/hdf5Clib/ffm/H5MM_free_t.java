// Generated by jextract

package com.sunya.netchdf.hdf5Clib.ffm;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import java.lang.foreign.*;
import static java.lang.foreign.ValueLayout.*;
/**
 * {@snippet :
 * void (*H5MM_free_t)(void* mem,void* free_info);
 * }
 */
public interface H5MM_free_t {

    void apply(java.lang.foreign.MemorySegment mem, java.lang.foreign.MemorySegment free_info);
    static MemorySegment allocate(H5MM_free_t fi, Arena scope) {
        return RuntimeHelper.upcallStub(constants$207.const$2, fi, constants$115.const$5, scope);
    }
    static H5MM_free_t ofAddress(MemorySegment addr, Arena arena) {
        MemorySegment symbol = addr.reinterpret(arena, null);
        return (java.lang.foreign.MemorySegment _mem, java.lang.foreign.MemorySegment _free_info) -> {
            try {
                constants$207.const$3.invokeExact(symbol, _mem, _free_info);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


