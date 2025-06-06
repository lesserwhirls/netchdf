// Generated by jextract

package com.sunya.netchdf.hdf5Clib.ffm;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import java.lang.foreign.*;
import static java.lang.foreign.ValueLayout.*;
/**
 * {@snippet :
 * enum H5T_conv_ret_t (*H5T_conv_except_func_t)(enum H5T_conv_except_t except_type,long src_id,long dst_id,void* src_buf,void* dst_buf,void* user_data);
 * }
 */
public interface H5T_conv_except_func_t {

    int apply(int except_type, long src_id, long dst_id, java.lang.foreign.MemorySegment src_buf, java.lang.foreign.MemorySegment dst_buf, java.lang.foreign.MemorySegment user_data);
    static MemorySegment allocate(H5T_conv_except_func_t fi, Arena scope) {
        return RuntimeHelper.upcallStub(constants$24.const$4, fi, constants$24.const$3, scope);
    }
    static H5T_conv_except_func_t ofAddress(MemorySegment addr, Arena arena) {
        MemorySegment symbol = addr.reinterpret(arena, null);
        return (int _except_type, long _src_id, long _dst_id, java.lang.foreign.MemorySegment _src_buf, java.lang.foreign.MemorySegment _dst_buf, java.lang.foreign.MemorySegment _user_data) -> {
            try {
                return (int)constants$24.const$5.invokeExact(symbol, _except_type, _src_id, _dst_id, _src_buf, _dst_buf, _user_data);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


