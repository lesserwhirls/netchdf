// Generated by jextract

package com.sunya.netchdf.hdf5Clib.ffm;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import java.lang.foreign.*;
import static java.lang.foreign.ValueLayout.*;
final class constants$109 {

    // Suppresses default constructor, ensuring non-instantiability.
    private constants$109() {}
    static final FunctionDescriptor const$0 = FunctionDescriptor.of(JAVA_LONG,
        RuntimeHelper.POINTER,
        RuntimeHelper.POINTER,
        JAVA_LONG
    );
    static final MethodHandle const$1 = RuntimeHelper.upcallHandle(cookie_read_function_t.class, "apply", constants$109.const$0);
    static final MethodHandle const$2 = RuntimeHelper.downcallHandle(
        constants$109.const$0
    );
    static final MethodHandle const$3 = RuntimeHelper.upcallHandle(cookie_write_function_t.class, "apply", constants$109.const$0);
    static final FunctionDescriptor const$4 = FunctionDescriptor.of(JAVA_INT,
        RuntimeHelper.POINTER,
        RuntimeHelper.POINTER,
        JAVA_INT
    );
    static final MethodHandle const$5 = RuntimeHelper.upcallHandle(cookie_seek_function_t.class, "apply", constants$109.const$4);
}


