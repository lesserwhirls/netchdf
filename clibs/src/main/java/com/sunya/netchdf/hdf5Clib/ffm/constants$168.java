// Generated by jextract

package com.sunya.netchdf.hdf5Clib.ffm;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import java.lang.foreign.*;
import static java.lang.foreign.ValueLayout.*;
final class constants$168 {

    // Suppresses default constructor, ensuring non-instantiability.
    private constants$168() {}
    static final VarHandle const$0 = constants$167.const$4.varHandle(MemoryLayout.PathElement.groupElement("meta_size"));
    static final VarHandle const$1 = constants$167.const$4.varHandle(MemoryLayout.PathElement.groupElement("tot_space"));
    static final StructLayout const$2 = MemoryLayout.structLayout(
        JAVA_INT.withName("version"),
        MemoryLayout.paddingLayout(4),
        JAVA_LONG.withName("hdr_size"),
        MemoryLayout.structLayout(
            JAVA_LONG.withName("index_size"),
            JAVA_LONG.withName("heap_size")
        ).withName("msgs_info")
    ).withName("");
    static final VarHandle const$3 = constants$168.const$2.varHandle(MemoryLayout.PathElement.groupElement("version"));
    static final VarHandle const$4 = constants$168.const$2.varHandle(MemoryLayout.PathElement.groupElement("hdr_size"));
    static final StructLayout const$5 = MemoryLayout.structLayout(
        JAVA_LONG.withName("addr"),
        JAVA_LONG.withName("size")
    ).withName("H5F_sect_info_t");
}


