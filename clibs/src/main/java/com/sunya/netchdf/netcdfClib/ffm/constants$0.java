// Generated by jextract

package com.sunya.netchdf.netcdfClib.ffm;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import java.lang.foreign.*;
import static java.lang.foreign.ValueLayout.*;
final class constants$0 {

    // Suppresses default constructor, ensuring non-instantiability.
    private constants$0() {}
    static final StructLayout const$0 = MemoryLayout.structLayout(
        JAVA_LONG.withName("__clang_max_align_nonce1"),
        MemoryLayout.paddingLayout(8),
        MemoryLayout.paddingLayout(16).withName("__clang_max_align_nonce2")
    ).withName("");
    static final VarHandle const$1 = constants$0.const$0.varHandle(MemoryLayout.PathElement.groupElement("__clang_max_align_nonce1"));
    static final FunctionDescriptor const$2 = FunctionDescriptor.of(RuntimeHelper.POINTER);
    static final MethodHandle const$3 = RuntimeHelper.downcallHandle(
        "__errno_location",
        constants$0.const$2
    );
    static final MethodHandle const$4 = RuntimeHelper.downcallHandle(
        "nc_inq_libvers",
        constants$0.const$2
    );
    static final FunctionDescriptor const$5 = FunctionDescriptor.of(RuntimeHelper.POINTER,
        JAVA_INT
    );
    static final MethodHandle const$6 = RuntimeHelper.downcallHandle(
        "nc_strerror",
        constants$0.const$5
    );
}


