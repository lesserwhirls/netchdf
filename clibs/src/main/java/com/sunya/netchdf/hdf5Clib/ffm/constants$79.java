// Generated by jextract

package com.sunya.netchdf.hdf5Clib.ffm;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import java.lang.foreign.*;
import static java.lang.foreign.ValueLayout.*;
final class constants$79 {

    // Suppresses default constructor, ensuring non-instantiability.
    private constants$79() {}
    static final MethodHandle const$0 = RuntimeHelper.downcallHandle(
        "H5Ovisit1",
        constants$66.const$2
    );
    static final MethodHandle const$1 = RuntimeHelper.downcallHandle(
        "H5Ovisit_by_name1",
        constants$66.const$4
    );
    static final StructLayout const$2 = MemoryLayout.structLayout(
        JAVA_LONG.withName("size"),
        JAVA_LONG.withName("free"),
        JAVA_INT.withName("nmesgs"),
        JAVA_INT.withName("nchunks")
    ).withName("H5O_stat_t");
    static final VarHandle const$3 = constants$79.const$2.varHandle(MemoryLayout.PathElement.groupElement("size"));
    static final VarHandle const$4 = constants$79.const$2.varHandle(MemoryLayout.PathElement.groupElement("free"));
    static final VarHandle const$5 = constants$79.const$2.varHandle(MemoryLayout.PathElement.groupElement("nmesgs"));
}


