// Generated by jextract

package com.sunya.netchdf.mfhdfClib.ffm;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import java.lang.foreign.*;
import static java.lang.foreign.ValueLayout.*;
final class constants$5 {

    // Suppresses default constructor, ensuring non-instantiability.
    private constants$5() {}
    static final VarHandle const$0 = constants$4.const$5.varHandle(MemoryLayout.PathElement.groupElement("__ctype_b"));
    static final VarHandle const$1 = constants$4.const$5.varHandle(MemoryLayout.PathElement.groupElement("__ctype_tolower"));
    static final VarHandle const$2 = constants$4.const$5.varHandle(MemoryLayout.PathElement.groupElement("__ctype_toupper"));
    static final FunctionDescriptor const$3 = FunctionDescriptor.of(JAVA_INT,
        JAVA_INT,
        RuntimeHelper.POINTER
    );
    static final MethodHandle const$4 = RuntimeHelper.downcallHandle(
        "isalnum_l",
        constants$5.const$3
    );
    static final MethodHandle const$5 = RuntimeHelper.downcallHandle(
        "isalpha_l",
        constants$5.const$3
    );
}


