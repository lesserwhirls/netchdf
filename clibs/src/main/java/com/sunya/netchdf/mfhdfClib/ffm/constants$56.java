// Generated by jextract

package com.sunya.netchdf.mfhdfClib.ffm;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import java.lang.foreign.*;
import static java.lang.foreign.ValueLayout.*;
final class constants$56 {

    // Suppresses default constructor, ensuring non-instantiability.
    private constants$56() {}
    static final MethodHandle const$0 = RuntimeHelper.downcallHandle(
        "seed48",
        constants$20.const$4
    );
    static final MethodHandle const$1 = RuntimeHelper.downcallHandle(
        "lcong48",
        constants$24.const$2
    );
    static final StructLayout const$2 = MemoryLayout.structLayout(
        MemoryLayout.sequenceLayout(3, JAVA_SHORT).withName("__x"),
        MemoryLayout.sequenceLayout(3, JAVA_SHORT).withName("__old_x"),
        JAVA_SHORT.withName("__c"),
        JAVA_SHORT.withName("__init"),
        JAVA_LONG.withName("__a")
    ).withName("drand48_data");
    static final VarHandle const$3 = constants$56.const$2.varHandle(MemoryLayout.PathElement.groupElement("__c"));
    static final VarHandle const$4 = constants$56.const$2.varHandle(MemoryLayout.PathElement.groupElement("__init"));
    static final VarHandle const$5 = constants$56.const$2.varHandle(MemoryLayout.PathElement.groupElement("__a"));
}


