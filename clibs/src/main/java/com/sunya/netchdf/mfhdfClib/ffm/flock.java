// Generated by jextract

package com.sunya.netchdf.mfhdfClib.ffm;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import java.lang.foreign.*;
import static java.lang.foreign.ValueLayout.*;
/**
 * {@snippet :
 * struct flock {
 *     short l_type;
 *     short l_whence;
 *     __off_t l_start;
 *     __off_t l_len;
 *     __pid_t l_pid;
 * };
 * }
 */
public class flock {

    public static MemoryLayout $LAYOUT() {
        return constants$90.const$3;
    }
    public static VarHandle l_type$VH() {
        return constants$90.const$4;
    }
    /**
     * Getter for field:
     * {@snippet :
     * short l_type;
     * }
     */
    public static short l_type$get(MemorySegment seg) {
        return (short)constants$90.const$4.get(seg);
    }
    /**
     * Setter for field:
     * {@snippet :
     * short l_type;
     * }
     */
    public static void l_type$set(MemorySegment seg, short x) {
        constants$90.const$4.set(seg, x);
    }
    public static short l_type$get(MemorySegment seg, long index) {
        return (short)constants$90.const$4.get(seg.asSlice(index*sizeof()));
    }
    public static void l_type$set(MemorySegment seg, long index, short x) {
        constants$90.const$4.set(seg.asSlice(index*sizeof()), x);
    }
    public static VarHandle l_whence$VH() {
        return constants$90.const$5;
    }
    /**
     * Getter for field:
     * {@snippet :
     * short l_whence;
     * }
     */
    public static short l_whence$get(MemorySegment seg) {
        return (short)constants$90.const$5.get(seg);
    }
    /**
     * Setter for field:
     * {@snippet :
     * short l_whence;
     * }
     */
    public static void l_whence$set(MemorySegment seg, short x) {
        constants$90.const$5.set(seg, x);
    }
    public static short l_whence$get(MemorySegment seg, long index) {
        return (short)constants$90.const$5.get(seg.asSlice(index*sizeof()));
    }
    public static void l_whence$set(MemorySegment seg, long index, short x) {
        constants$90.const$5.set(seg.asSlice(index*sizeof()), x);
    }
    public static VarHandle l_start$VH() {
        return constants$91.const$0;
    }
    /**
     * Getter for field:
     * {@snippet :
     * __off_t l_start;
     * }
     */
    public static long l_start$get(MemorySegment seg) {
        return (long)constants$91.const$0.get(seg);
    }
    /**
     * Setter for field:
     * {@snippet :
     * __off_t l_start;
     * }
     */
    public static void l_start$set(MemorySegment seg, long x) {
        constants$91.const$0.set(seg, x);
    }
    public static long l_start$get(MemorySegment seg, long index) {
        return (long)constants$91.const$0.get(seg.asSlice(index*sizeof()));
    }
    public static void l_start$set(MemorySegment seg, long index, long x) {
        constants$91.const$0.set(seg.asSlice(index*sizeof()), x);
    }
    public static VarHandle l_len$VH() {
        return constants$91.const$1;
    }
    /**
     * Getter for field:
     * {@snippet :
     * __off_t l_len;
     * }
     */
    public static long l_len$get(MemorySegment seg) {
        return (long)constants$91.const$1.get(seg);
    }
    /**
     * Setter for field:
     * {@snippet :
     * __off_t l_len;
     * }
     */
    public static void l_len$set(MemorySegment seg, long x) {
        constants$91.const$1.set(seg, x);
    }
    public static long l_len$get(MemorySegment seg, long index) {
        return (long)constants$91.const$1.get(seg.asSlice(index*sizeof()));
    }
    public static void l_len$set(MemorySegment seg, long index, long x) {
        constants$91.const$1.set(seg.asSlice(index*sizeof()), x);
    }
    public static VarHandle l_pid$VH() {
        return constants$91.const$2;
    }
    /**
     * Getter for field:
     * {@snippet :
     * __pid_t l_pid;
     * }
     */
    public static int l_pid$get(MemorySegment seg) {
        return (int)constants$91.const$2.get(seg);
    }
    /**
     * Setter for field:
     * {@snippet :
     * __pid_t l_pid;
     * }
     */
    public static void l_pid$set(MemorySegment seg, int x) {
        constants$91.const$2.set(seg, x);
    }
    public static int l_pid$get(MemorySegment seg, long index) {
        return (int)constants$91.const$2.get(seg.asSlice(index*sizeof()));
    }
    public static void l_pid$set(MemorySegment seg, long index, int x) {
        constants$91.const$2.set(seg.asSlice(index*sizeof()), x);
    }
    public static long sizeof() { return $LAYOUT().byteSize(); }
    public static MemorySegment allocate(SegmentAllocator allocator) { return allocator.allocate($LAYOUT()); }
    public static MemorySegment allocateArray(long len, SegmentAllocator allocator) {
        return allocator.allocate(MemoryLayout.sequenceLayout(len, $LAYOUT()));
    }
    public static MemorySegment ofAddress(MemorySegment addr, Arena arena) { return RuntimeHelper.asArray(addr, $LAYOUT(), 1, arena); }
}


