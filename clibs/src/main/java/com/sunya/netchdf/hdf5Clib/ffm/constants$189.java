// Generated by jextract

package com.sunya.netchdf.hdf5Clib.ffm;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import java.lang.foreign.*;
import static java.lang.foreign.ValueLayout.*;
final class constants$189 {

    // Suppresses default constructor, ensuring non-instantiability.
    private constants$189() {}
    static final MethodHandle const$0 = RuntimeHelper.upcallHandle(H5FD_class_t.flush.class, "apply", constants$188.const$5);
    static final MethodHandle const$1 = RuntimeHelper.downcallHandle(
        constants$188.const$5
    );
    static final VarHandle const$2 = constants$178.const$0.varHandle(MemoryLayout.PathElement.groupElement("flush"));
    static final MethodHandle const$3 = RuntimeHelper.upcallHandle(H5FD_class_t.truncate.class, "apply", constants$188.const$5);
    static final VarHandle const$4 = constants$178.const$0.varHandle(MemoryLayout.PathElement.groupElement("truncate"));
    static final FunctionDescriptor const$5 = FunctionDescriptor.of(JAVA_INT,
        RuntimeHelper.POINTER,
        JAVA_BOOLEAN
    );
}


