// Generated by jextract

package com.sunya.netchdf.hdf5Clib.ffm;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import java.lang.foreign.*;
import static java.lang.foreign.ValueLayout.*;
final class constants$237 {

    // Suppresses default constructor, ensuring non-instantiability.
    private constants$237() {}
    static final MethodHandle const$0 = RuntimeHelper.downcallHandle(
        "H5Pget_userblock",
        constants$44.const$4
    );
    static final MethodHandle const$1 = RuntimeHelper.downcallHandle(
        "H5Pset_file_space_page_size",
        constants$40.const$0
    );
    static final FunctionDescriptor const$2 = FunctionDescriptor.of(JAVA_INT,
        JAVA_LONG,
        JAVA_INT,
        JAVA_BOOLEAN,
        JAVA_LONG
    );
    static final MethodHandle const$3 = RuntimeHelper.downcallHandle(
        "H5Pset_file_space_strategy",
        constants$237.const$2
    );
    static final MethodHandle const$4 = RuntimeHelper.downcallHandle(
        "H5Pset_istore_k",
        constants$45.const$5
    );
    static final FunctionDescriptor const$5 = FunctionDescriptor.of(JAVA_INT,
        JAVA_LONG,
        JAVA_INT,
        JAVA_INT,
        JAVA_INT
    );
    static final MethodHandle const$6 = RuntimeHelper.downcallHandle(
        "H5Pset_shared_mesg_index",
        constants$237.const$5
    );
}


