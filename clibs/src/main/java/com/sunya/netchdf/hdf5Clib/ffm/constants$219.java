// Generated by jextract

package com.sunya.netchdf.hdf5Clib.ffm;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import java.lang.foreign.*;
import static java.lang.foreign.ValueLayout.*;
final class constants$219 {

    // Suppresses default constructor, ensuring non-instantiability.
    private constants$219() {}
    static final MethodHandle const$0 = RuntimeHelper.downcallHandle(
        "H5Zget_filter_info",
        constants$22.const$0
    );
    static final StructLayout const$1 = MemoryLayout.structLayout(
        JAVA_INT.withName("id"),
        MemoryLayout.paddingLayout(4),
        RuntimeHelper.POINTER.withName("name"),
        RuntimeHelper.POINTER.withName("can_apply"),
        RuntimeHelper.POINTER.withName("set_local"),
        RuntimeHelper.POINTER.withName("filter")
    ).withName("H5Z_class1_t");
    static final VarHandle const$2 = constants$219.const$1.varHandle(MemoryLayout.PathElement.groupElement("id"));
    static final VarHandle const$3 = constants$219.const$1.varHandle(MemoryLayout.PathElement.groupElement("name"));
    static final VarHandle const$4 = constants$219.const$1.varHandle(MemoryLayout.PathElement.groupElement("can_apply"));
    static final VarHandle const$5 = constants$219.const$1.varHandle(MemoryLayout.PathElement.groupElement("set_local"));
}


