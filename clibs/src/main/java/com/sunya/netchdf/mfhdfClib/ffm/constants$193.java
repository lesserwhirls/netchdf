// Generated by jextract

package com.sunya.netchdf.mfhdfClib.ffm;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import java.lang.foreign.*;
import static java.lang.foreign.ValueLayout.*;
final class constants$193 {

    // Suppresses default constructor, ensuring non-instantiability.
    private constants$193() {}
    static final MethodHandle const$0 = RuntimeHelper.downcallHandle(
        "Vgetattr",
        constants$132.const$2
    );
    static final MethodHandle const$1 = RuntimeHelper.downcallHandle(
        "Vgetattr2",
        constants$132.const$2
    );
    static final MethodHandle const$2 = RuntimeHelper.downcallHandle(
        "Vgetversion",
        constants$1.const$5
    );
    static final MethodHandle const$3 = RuntimeHelper.downcallHandle(
        "VSfindex",
        constants$26.const$5
    );
    static final FunctionDescriptor const$4 = FunctionDescriptor.of(JAVA_INT,
        JAVA_INT,
        JAVA_INT,
        RuntimeHelper.POINTER,
        JAVA_INT,
        JAVA_INT,
        RuntimeHelper.POINTER
    );
    static final MethodHandle const$5 = RuntimeHelper.downcallHandle(
        "VSsetattr",
        constants$193.const$4
    );
}


