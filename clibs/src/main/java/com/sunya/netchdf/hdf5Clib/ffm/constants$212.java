// Generated by jextract

package com.sunya.netchdf.hdf5Clib.ffm;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import java.lang.foreign.*;
import static java.lang.foreign.ValueLayout.*;
final class constants$212 {

    // Suppresses default constructor, ensuring non-instantiability.
    private constants$212() {}
    static final MethodHandle const$0 = RuntimeHelper.downcallHandle(
        "H5Sget_select_npoints",
        constants$10.const$2
    );
    static final MethodHandle const$1 = RuntimeHelper.downcallHandle(
        "H5Sget_select_type",
        constants$19.const$0
    );
    static final MethodHandle const$2 = RuntimeHelper.downcallHandle(
        "H5Sis_regular_hyperslab",
        constants$19.const$0
    );
    static final FunctionDescriptor const$3 = FunctionDescriptor.of(JAVA_INT,
        JAVA_LONG,
        JAVA_INT,
        JAVA_LONG
    );
    static final MethodHandle const$4 = RuntimeHelper.downcallHandle(
        "H5Smodify_select",
        constants$212.const$3
    );
    static final MethodHandle const$5 = RuntimeHelper.downcallHandle(
        "H5Soffset_simple",
        constants$44.const$4
    );
}


