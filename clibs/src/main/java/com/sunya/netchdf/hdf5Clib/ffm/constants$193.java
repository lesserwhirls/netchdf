// Generated by jextract

package com.sunya.netchdf.hdf5Clib.ffm;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import java.lang.foreign.*;
import static java.lang.foreign.ValueLayout.*;
final class constants$193 {

    // Suppresses default constructor, ensuring non-instantiability.
    private constants$193() {}
    static final VarHandle const$0 = constants$191.const$3.varHandle(MemoryLayout.PathElement.groupElement("alignment"));
    static final VarHandle const$1 = constants$191.const$3.varHandle(MemoryLayout.PathElement.groupElement("paged_aggr"));
    static final StructLayout const$2 = MemoryLayout.structLayout(
        RuntimeHelper.POINTER.withName("image_malloc"),
        RuntimeHelper.POINTER.withName("image_memcpy"),
        RuntimeHelper.POINTER.withName("image_realloc"),
        RuntimeHelper.POINTER.withName("image_free"),
        RuntimeHelper.POINTER.withName("udata_copy"),
        RuntimeHelper.POINTER.withName("udata_free"),
        RuntimeHelper.POINTER.withName("udata")
    ).withName("");
    static final FunctionDescriptor const$3 = FunctionDescriptor.of(RuntimeHelper.POINTER,
        JAVA_LONG,
        JAVA_INT,
        RuntimeHelper.POINTER
    );
    static final MethodHandle const$4 = RuntimeHelper.upcallHandle(H5FD_file_image_callbacks_t.image_malloc.class, "apply", constants$193.const$3);
    static final MethodHandle const$5 = RuntimeHelper.downcallHandle(
        constants$193.const$3
    );
}


