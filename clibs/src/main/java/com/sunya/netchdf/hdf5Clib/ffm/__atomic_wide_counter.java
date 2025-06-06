// Generated by jextract

package com.sunya.netchdf.hdf5Clib.ffm;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import java.lang.foreign.*;
import static java.lang.foreign.ValueLayout.*;
/**
 * {@snippet :
 * union {
 *     unsigned long long __value64;
 *     struct  __value32;
 * };
 * }
 */
public class __atomic_wide_counter {

    public static MemoryLayout $LAYOUT() {
        return constants$2.const$0;
    }
    public static VarHandle __value64$VH() {
        return constants$2.const$1;
    }
    /**
     * Getter for field:
     * {@snippet :
     * unsigned long long __value64;
     * }
     */
    public static long __value64$get(MemorySegment seg) {
        return (long)constants$2.const$1.get(seg);
    }
    /**
     * Setter for field:
     * {@snippet :
     * unsigned long long __value64;
     * }
     */
    public static void __value64$set(MemorySegment seg, long x) {
        constants$2.const$1.set(seg, x);
    }
    public static long __value64$get(MemorySegment seg, long index) {
        return (long)constants$2.const$1.get(seg.asSlice(index*sizeof()));
    }
    public static void __value64$set(MemorySegment seg, long index, long x) {
        constants$2.const$1.set(seg.asSlice(index*sizeof()), x);
    }
    /**
     * {@snippet :
     * struct {
     *     unsigned int __low;
     *     unsigned int __high;
     * };
     * }
     */
    public static final class __value32 {

        // Suppresses default constructor, ensuring non-instantiability.
        private __value32() {}
        public static MemoryLayout $LAYOUT() {
            return constants$2.const$2;
        }
        public static VarHandle __low$VH() {
            return constants$2.const$3;
        }
        /**
         * Getter for field:
         * {@snippet :
         * unsigned int __low;
         * }
         */
        public static int __low$get(MemorySegment seg) {
            return (int)constants$2.const$3.get(seg);
        }
        /**
         * Setter for field:
         * {@snippet :
         * unsigned int __low;
         * }
         */
        public static void __low$set(MemorySegment seg, int x) {
            constants$2.const$3.set(seg, x);
        }
        public static int __low$get(MemorySegment seg, long index) {
            return (int)constants$2.const$3.get(seg.asSlice(index*sizeof()));
        }
        public static void __low$set(MemorySegment seg, long index, int x) {
            constants$2.const$3.set(seg.asSlice(index*sizeof()), x);
        }
        public static VarHandle __high$VH() {
            return constants$2.const$4;
        }
        /**
         * Getter for field:
         * {@snippet :
         * unsigned int __high;
         * }
         */
        public static int __high$get(MemorySegment seg) {
            return (int)constants$2.const$4.get(seg);
        }
        /**
         * Setter for field:
         * {@snippet :
         * unsigned int __high;
         * }
         */
        public static void __high$set(MemorySegment seg, int x) {
            constants$2.const$4.set(seg, x);
        }
        public static int __high$get(MemorySegment seg, long index) {
            return (int)constants$2.const$4.get(seg.asSlice(index*sizeof()));
        }
        public static void __high$set(MemorySegment seg, long index, int x) {
            constants$2.const$4.set(seg.asSlice(index*sizeof()), x);
        }
        public static long sizeof() { return $LAYOUT().byteSize(); }
        public static MemorySegment allocate(SegmentAllocator allocator) { return allocator.allocate($LAYOUT()); }
        public static MemorySegment allocateArray(long len, SegmentAllocator allocator) {
            return allocator.allocate(MemoryLayout.sequenceLayout(len, $LAYOUT()));
        }
        public static MemorySegment ofAddress(MemorySegment addr, Arena arena) { return RuntimeHelper.asArray(addr, $LAYOUT(), 1, arena); }
    }

    public static MemorySegment __value32$slice(MemorySegment seg) {
        return seg.asSlice(0, 8);
    }
    public static long sizeof() { return $LAYOUT().byteSize(); }
    public static MemorySegment allocate(SegmentAllocator allocator) { return allocator.allocate($LAYOUT()); }
    public static MemorySegment allocateArray(long len, SegmentAllocator allocator) {
        return allocator.allocate(MemoryLayout.sequenceLayout(len, $LAYOUT()));
    }
    public static MemorySegment ofAddress(MemorySegment addr, Arena arena) { return RuntimeHelper.asArray(addr, $LAYOUT(), 1, arena); }
}


