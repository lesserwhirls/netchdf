// Generated by jextract

package com.sunya.netchdf.mfhdfClib.ffm;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import java.lang.foreign.*;
import static java.lang.foreign.ValueLayout.*;
final class constants$51 {

    // Suppresses default constructor, ensuring non-instantiability.
    private constants$51() {}
    static final VarHandle const$0 = constants$50.const$5.varHandle(MemoryLayout.PathElement.groupElement("__align"));
    static final MethodHandle const$1 = RuntimeHelper.downcallHandle(
        "random",
        constants$38.const$3
    );
    static final FunctionDescriptor const$2 = FunctionDescriptor.ofVoid(
        JAVA_INT
    );
    static final MethodHandle const$3 = RuntimeHelper.downcallHandle(
        "srandom",
        constants$51.const$2
    );
    static final FunctionDescriptor const$4 = FunctionDescriptor.of(RuntimeHelper.POINTER,
        JAVA_INT,
        RuntimeHelper.POINTER,
        JAVA_LONG
    );
    static final MethodHandle const$5 = RuntimeHelper.downcallHandle(
        "initstate",
        constants$51.const$4
    );
}


