// Generated by jextract

package com.sunya.netchdf.mfhdfClib.ffm;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import java.lang.foreign.*;
import static java.lang.foreign.ValueLayout.*;
final class constants$75 {

    // Suppresses default constructor, ensuring non-instantiability.
    private constants$75() {}
    static final MethodHandle const$0 = RuntimeHelper.downcallHandle(
        "strstr",
        constants$21.const$1
    );
    static final MethodHandle const$1 = RuntimeHelper.downcallHandle(
        "strtok",
        constants$21.const$1
    );
    static final MethodHandle const$2 = RuntimeHelper.downcallHandle(
        "__strtok_r",
        constants$22.const$0
    );
    static final MethodHandle const$3 = RuntimeHelper.downcallHandle(
        "strtok_r",
        constants$22.const$0
    );
    static final MethodHandle const$4 = RuntimeHelper.downcallHandle(
        "strcasestr",
        constants$21.const$1
    );
    static final FunctionDescriptor const$5 = FunctionDescriptor.of(RuntimeHelper.POINTER,
        RuntimeHelper.POINTER,
        JAVA_LONG,
        RuntimeHelper.POINTER,
        JAVA_LONG
    );
    static final MethodHandle const$6 = RuntimeHelper.downcallHandle(
        "memmem",
        constants$75.const$5
    );
}


