// Generated by jextract

package com.sunya.netchdf.hdf5Clib.ffm;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import java.lang.foreign.*;
import static java.lang.foreign.ValueLayout.*;
final class constants$202 {

    // Suppresses default constructor, ensuring non-instantiability.
    private constants$202() {}
    static final MethodHandle const$0 = RuntimeHelper.downcallHandle(
        "H5Gget_info_by_name",
        constants$43.const$3
    );
    static final MethodHandle const$1 = RuntimeHelper.downcallHandle(
        "H5Gget_info_by_idx",
        constants$65.const$0
    );
    static final MethodHandle const$2 = RuntimeHelper.downcallHandle(
        "H5Gflush",
        constants$19.const$0
    );
    static final MethodHandle const$3 = RuntimeHelper.downcallHandle(
        "H5Grefresh",
        constants$19.const$0
    );
    static final MethodHandle const$4 = RuntimeHelper.downcallHandle(
        "H5Gclose",
        constants$19.const$0
    );
    static final MethodHandle const$5 = RuntimeHelper.upcallHandle(H5G_iterate_t.class, "apply", constants$41.const$4);
}


