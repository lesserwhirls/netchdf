// Generated by jextract

package com.sunya.netchdf.hdf5Clib.ffm;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import java.lang.foreign.*;
import static java.lang.foreign.ValueLayout.*;
final class constants$9 {

    // Suppresses default constructor, ensuring non-instantiability.
    private constants$9() {}
    static final VarHandle const$0 = constants$8.const$5.varHandle(MemoryLayout.PathElement.groupElement("__align"));
    static final UnionLayout const$1 = MemoryLayout.unionLayout(
        MemoryLayout.structLayout(
            JAVA_INT.withName("__readers"),
            JAVA_INT.withName("__writers"),
            JAVA_INT.withName("__wrphase_futex"),
            JAVA_INT.withName("__writers_futex"),
            JAVA_INT.withName("__pad3"),
            JAVA_INT.withName("__pad4"),
            JAVA_INT.withName("__cur_writer"),
            JAVA_INT.withName("__shared"),
            JAVA_BYTE.withName("__rwelision"),
            MemoryLayout.sequenceLayout(7, JAVA_BYTE).withName("__pad1"),
            JAVA_LONG.withName("__pad2"),
            JAVA_INT.withName("__flags"),
            MemoryLayout.paddingLayout(4)
        ).withName("__data"),
        MemoryLayout.sequenceLayout(56, JAVA_BYTE).withName("__size"),
        JAVA_LONG.withName("__align")
    ).withName("");
    static final VarHandle const$2 = constants$9.const$1.varHandle(MemoryLayout.PathElement.groupElement("__align"));
    static final UnionLayout const$3 = MemoryLayout.unionLayout(
        MemoryLayout.sequenceLayout(8, JAVA_BYTE).withName("__size"),
        JAVA_LONG.withName("__align")
    ).withName("");
    static final VarHandle const$4 = constants$9.const$3.varHandle(MemoryLayout.PathElement.groupElement("__align"));
    static final StructLayout const$5 = MemoryLayout.structLayout(
        JAVA_LONG.withName("quot"),
        JAVA_LONG.withName("rem")
    ).withName("");
}


