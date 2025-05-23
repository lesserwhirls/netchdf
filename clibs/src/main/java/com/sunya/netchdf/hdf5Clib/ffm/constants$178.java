// Generated by jextract

package com.sunya.netchdf.hdf5Clib.ffm;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import java.lang.foreign.*;
import static java.lang.foreign.ValueLayout.*;
final class constants$178 {

    // Suppresses default constructor, ensuring non-instantiability.
    private constants$178() {}
    static final StructLayout const$0 = MemoryLayout.structLayout(
        RuntimeHelper.POINTER.withName("name"),
        JAVA_LONG.withName("maxaddr"),
        JAVA_INT.withName("fc_degree"),
        MemoryLayout.paddingLayout(4),
        RuntimeHelper.POINTER.withName("terminate"),
        RuntimeHelper.POINTER.withName("sb_size"),
        RuntimeHelper.POINTER.withName("sb_encode"),
        RuntimeHelper.POINTER.withName("sb_decode"),
        JAVA_LONG.withName("fapl_size"),
        RuntimeHelper.POINTER.withName("fapl_get"),
        RuntimeHelper.POINTER.withName("fapl_copy"),
        RuntimeHelper.POINTER.withName("fapl_free"),
        JAVA_LONG.withName("dxpl_size"),
        RuntimeHelper.POINTER.withName("dxpl_copy"),
        RuntimeHelper.POINTER.withName("dxpl_free"),
        RuntimeHelper.POINTER.withName("open"),
        RuntimeHelper.POINTER.withName("close"),
        RuntimeHelper.POINTER.withName("cmp"),
        RuntimeHelper.POINTER.withName("query"),
        RuntimeHelper.POINTER.withName("get_type_map"),
        RuntimeHelper.POINTER.withName("alloc"),
        RuntimeHelper.POINTER.withName("free"),
        RuntimeHelper.POINTER.withName("get_eoa"),
        RuntimeHelper.POINTER.withName("set_eoa"),
        RuntimeHelper.POINTER.withName("get_eof"),
        RuntimeHelper.POINTER.withName("get_handle"),
        RuntimeHelper.POINTER.withName("read"),
        RuntimeHelper.POINTER.withName("write"),
        RuntimeHelper.POINTER.withName("flush"),
        RuntimeHelper.POINTER.withName("truncate"),
        RuntimeHelper.POINTER.withName("lock"),
        RuntimeHelper.POINTER.withName("unlock"),
        MemoryLayout.sequenceLayout(7, JAVA_INT).withName("fl_map"),
        MemoryLayout.paddingLayout(4)
    ).withName("H5FD_class_t");
    static final VarHandle const$1 = constants$178.const$0.varHandle(MemoryLayout.PathElement.groupElement("name"));
    static final VarHandle const$2 = constants$178.const$0.varHandle(MemoryLayout.PathElement.groupElement("maxaddr"));
    static final VarHandle const$3 = constants$178.const$0.varHandle(MemoryLayout.PathElement.groupElement("fc_degree"));
    static final MethodHandle const$4 = RuntimeHelper.upcallHandle(H5FD_class_t.terminate.class, "apply", constants$14.const$0);
    static final MethodHandle const$5 = RuntimeHelper.downcallHandle(
        constants$14.const$0
    );
}


