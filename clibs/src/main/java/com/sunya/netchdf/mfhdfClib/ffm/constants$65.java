// Generated by jextract

package com.sunya.netchdf.mfhdfClib.ffm;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import java.lang.foreign.*;
import static java.lang.foreign.ValueLayout.*;
final class constants$65 {

    // Suppresses default constructor, ensuring non-instantiability.
    private constants$65() {}
    static final MethodHandle const$0 = RuntimeHelper.downcallHandle(
        "system",
        constants$17.const$4
    );
    static final MethodHandle const$1 = RuntimeHelper.downcallHandle(
        "realpath",
        constants$21.const$1
    );
    static final MethodHandle const$2 = RuntimeHelper.upcallHandle(__compar_fn_t.class, "apply", constants$19.const$5);
    static final MethodHandle const$3 = RuntimeHelper.downcallHandle(
        constants$19.const$5
    );
    static final FunctionDescriptor const$4 = FunctionDescriptor.of(RuntimeHelper.POINTER,
        RuntimeHelper.POINTER,
        RuntimeHelper.POINTER,
        JAVA_LONG,
        JAVA_LONG,
        RuntimeHelper.POINTER
    );
    static final MethodHandle const$5 = RuntimeHelper.downcallHandle(
        "bsearch",
        constants$65.const$4
    );
}


