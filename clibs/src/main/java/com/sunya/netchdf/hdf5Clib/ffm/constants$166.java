// Generated by jextract

package com.sunya.netchdf.hdf5Clib.ffm;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import java.lang.foreign.*;
import static java.lang.foreign.ValueLayout.*;
final class constants$166 {

    // Suppresses default constructor, ensuring non-instantiability.
    private constants$166() {}
    static final MethodHandle const$0 = RuntimeHelper.downcallHandle(
        "H5Eprint1",
        constants$15.const$2
    );
    static final MethodHandle const$1 = RuntimeHelper.downcallHandle(
        "H5Eset_auto1",
        constants$112.const$0
    );
    static final MethodHandle const$2 = RuntimeHelper.downcallHandle(
        "H5Ewalk1",
        constants$119.const$0
    );
    static final MethodHandle const$3 = RuntimeHelper.downcallHandle(
        "H5Eget_major",
        constants$45.const$1
    );
    static final MethodHandle const$4 = RuntimeHelper.downcallHandle(
        "H5Eget_minor",
        constants$45.const$1
    );
    static final StructLayout const$5 = MemoryLayout.structLayout(
        MemoryLayout.structLayout(
            JAVA_INT.withName("version"),
            MemoryLayout.paddingLayout(4),
            JAVA_LONG.withName("super_size"),
            JAVA_LONG.withName("super_ext_size")
        ).withName("super"),
        MemoryLayout.structLayout(
            JAVA_INT.withName("version"),
            MemoryLayout.paddingLayout(4),
            JAVA_LONG.withName("meta_size"),
            JAVA_LONG.withName("tot_space")
        ).withName("free"),
        MemoryLayout.structLayout(
            JAVA_INT.withName("version"),
            MemoryLayout.paddingLayout(4),
            JAVA_LONG.withName("hdr_size"),
            MemoryLayout.structLayout(
                JAVA_LONG.withName("index_size"),
                JAVA_LONG.withName("heap_size")
            ).withName("msgs_info")
        ).withName("sohm")
    ).withName("H5F_info2_t");
}


