// Generated by jextract

package com.sunya.netchdf.hdf5Clib.ffm;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import java.lang.foreign.*;
import static java.lang.foreign.ValueLayout.*;
final class constants$234 {

    // Suppresses default constructor, ensuring non-instantiability.
    private constants$234() {}
    static final MethodHandle const$0 = RuntimeHelper.downcallHandle(
        "H5Pget_obj_track_times",
        constants$44.const$4
    );
    static final FunctionDescriptor const$1 = FunctionDescriptor.of(JAVA_INT,
        JAVA_LONG,
        JAVA_INT,
        JAVA_INT,
        JAVA_LONG,
        RuntimeHelper.POINTER
    );
    static final MethodHandle const$2 = RuntimeHelper.downcallHandle(
        "H5Pmodify_filter",
        constants$234.const$1
    );
    static final MethodHandle const$3 = RuntimeHelper.downcallHandle(
        "H5Premove_filter",
        constants$45.const$5
    );
    static final MethodHandle const$4 = RuntimeHelper.downcallHandle(
        "H5Pset_attr_creation_order",
        constants$45.const$5
    );
    static final MethodHandle const$5 = RuntimeHelper.downcallHandle(
        "H5Pset_attr_phase_change",
        constants$50.const$3
    );
}


