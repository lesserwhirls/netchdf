// Generated by jextract

package com.sunya.netchdf.hdf5Clib.ffm;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import java.lang.foreign.*;
import static java.lang.foreign.ValueLayout.*;
final class constants$114 {

    // Suppresses default constructor, ensuring non-instantiability.
    private constants$114() {}
    static final MethodHandle const$0 = RuntimeHelper.downcallHandle(
        "fflush_unlocked",
        constants$15.const$2
    );
    static final MethodHandle const$1 = RuntimeHelper.downcallHandle(
        "fopen",
        constants$113.const$3
    );
    static final FunctionDescriptor const$2 = FunctionDescriptor.of(RuntimeHelper.POINTER,
        RuntimeHelper.POINTER,
        RuntimeHelper.POINTER,
        RuntimeHelper.POINTER
    );
    static final MethodHandle const$3 = RuntimeHelper.downcallHandle(
        "freopen",
        constants$114.const$2
    );
    static final FunctionDescriptor const$4 = FunctionDescriptor.of(RuntimeHelper.POINTER,
        JAVA_INT,
        RuntimeHelper.POINTER
    );
    static final MethodHandle const$5 = RuntimeHelper.downcallHandle(
        "fdopen",
        constants$114.const$4
    );
}


