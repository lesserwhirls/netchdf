// Generated by jextract

package com.sunya.netchdf.mfhdfClib.ffm;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import java.lang.foreign.*;
import static java.lang.foreign.ValueLayout.*;
final class constants$128 {

    // Suppresses default constructor, ensuring non-instantiability.
    private constants$128() {}
    static final VarHandle const$0 = constants$127.const$5.varHandle(MemoryLayout.PathElement.groupElement("skp_size"));
    static final StructLayout const$1 = MemoryLayout.structLayout(
        JAVA_INT.withName("level")
    ).withName("");
    static final VarHandle const$2 = constants$128.const$1.varHandle(MemoryLayout.PathElement.groupElement("level"));
    static final StructLayout const$3 = MemoryLayout.structLayout(
        JAVA_INT.withName("options_mask"),
        JAVA_INT.withName("pixels_per_block"),
        JAVA_INT.withName("pixels_per_scanline"),
        JAVA_INT.withName("bits_per_pixel"),
        JAVA_INT.withName("pixels")
    ).withName("");
    static final VarHandle const$4 = constants$128.const$3.varHandle(MemoryLayout.PathElement.groupElement("options_mask"));
    static final VarHandle const$5 = constants$128.const$3.varHandle(MemoryLayout.PathElement.groupElement("pixels_per_block"));
}


