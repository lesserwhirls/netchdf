// Generated by jextract

package com.sunya.netchdf.hdf5Clib.ffm;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import java.lang.foreign.*;
import static java.lang.foreign.ValueLayout.*;
final class constants$272 {

    // Suppresses default constructor, ensuring non-instantiability.
    private constants$272() {}
    static final VarHandle const$0 = constants$271.const$5.varHandle(MemoryLayout.PathElement.groupElement("magic"));
    static final VarHandle const$1 = constants$271.const$5.varHandle(MemoryLayout.PathElement.groupElement("version"));
    static final VarHandle const$2 = constants$271.const$5.varHandle(MemoryLayout.PathElement.groupElement("rw_fapl_id"));
    static final VarHandle const$3 = constants$271.const$5.varHandle(MemoryLayout.PathElement.groupElement("wo_fapl_id"));
    static final VarHandle const$4 = constants$271.const$5.varHandle(MemoryLayout.PathElement.groupElement("ignore_wo_errs"));
    static final MethodHandle const$5 = RuntimeHelper.downcallHandle(
        "H5FD_splitter_init",
        constants$160.const$4
    );
}


