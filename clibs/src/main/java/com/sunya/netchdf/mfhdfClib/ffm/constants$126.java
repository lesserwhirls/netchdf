// Generated by jextract

package com.sunya.netchdf.mfhdfClib.ffm;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import java.lang.foreign.*;
import static java.lang.foreign.ValueLayout.*;
final class constants$126 {

    // Suppresses default constructor, ensuring non-instantiability.
    private constants$126() {}
    static final VarHandle const$0 = constants$125.const$3.varHandle(MemoryLayout.PathElement.groupElement("dims"));
    static final UnionLayout const$1 = MemoryLayout.unionLayout(
        MemoryLayout.structLayout(
            JAVA_INT.withName("quality"),
            JAVA_INT.withName("force_baseline")
        ).withName("jpeg"),
        MemoryLayout.structLayout(
            JAVA_INT.withName("nt"),
            JAVA_INT.withName("sign_ext"),
            JAVA_INT.withName("fill_one"),
            JAVA_INT.withName("start_bit"),
            JAVA_INT.withName("bit_len")
        ).withName("nbit"),
        MemoryLayout.structLayout(
            JAVA_INT.withName("skp_size")
        ).withName("skphuff"),
        MemoryLayout.structLayout(
            JAVA_INT.withName("level")
        ).withName("deflate"),
        MemoryLayout.structLayout(
            JAVA_INT.withName("options_mask"),
            JAVA_INT.withName("pixels_per_block"),
            JAVA_INT.withName("pixels_per_scanline"),
            JAVA_INT.withName("bits_per_pixel"),
            JAVA_INT.withName("pixels")
        ).withName("szip")
    ).withName("tag_comp_info");
    static final StructLayout const$2 = MemoryLayout.structLayout(
        JAVA_INT.withName("quality"),
        JAVA_INT.withName("force_baseline")
    ).withName("");
    static final VarHandle const$3 = constants$126.const$2.varHandle(MemoryLayout.PathElement.groupElement("quality"));
    static final VarHandle const$4 = constants$126.const$2.varHandle(MemoryLayout.PathElement.groupElement("force_baseline"));
    static final StructLayout const$5 = MemoryLayout.structLayout(
        JAVA_INT.withName("nt"),
        JAVA_INT.withName("sign_ext"),
        JAVA_INT.withName("fill_one"),
        JAVA_INT.withName("start_bit"),
        JAVA_INT.withName("bit_len")
    ).withName("");
}


