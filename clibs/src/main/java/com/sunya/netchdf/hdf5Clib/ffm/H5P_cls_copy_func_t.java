// Generated by jextract

package com.sunya.netchdf.hdf5Clib.ffm;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import java.lang.foreign.*;
import static java.lang.foreign.ValueLayout.*;
/**
 * {@snippet :
 * int (*H5P_cls_copy_func_t)(long new_prop_id,long old_prop_id,void* copy_data);
 * }
 */
public interface H5P_cls_copy_func_t {

    int apply(long new_prop_id, long old_prop_id, java.lang.foreign.MemorySegment copy_data);
    static MemorySegment allocate(H5P_cls_copy_func_t fi, Arena scope) {
        return RuntimeHelper.upcallStub(constants$220.const$2, fi, constants$85.const$4, scope);
    }
    static H5P_cls_copy_func_t ofAddress(MemorySegment addr, Arena arena) {
        MemorySegment symbol = addr.reinterpret(arena, null);
        return (long _new_prop_id, long _old_prop_id, java.lang.foreign.MemorySegment _copy_data) -> {
            try {
                return (int)constants$220.const$3.invokeExact(symbol, _new_prop_id, _old_prop_id, _copy_data);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


