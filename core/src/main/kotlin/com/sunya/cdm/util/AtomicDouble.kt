package com.sunya.cdm.util

import java.util.concurrent.atomic.AtomicReference

class AtomicDouble(initialValue: Double) {
    private val atomicReference = AtomicReference(initialValue)

    fun get(): Double = atomicReference.get()

    fun set(newValue: Double) {
        atomicReference.set(newValue)
    }

    fun getAndAdd(delta: Double): Double {
        while (true) {
            val current = atomicReference.get()
            val new = current + delta
            if (atomicReference.compareAndSet(current, new)) {
                return current
            }
        }
    }
}