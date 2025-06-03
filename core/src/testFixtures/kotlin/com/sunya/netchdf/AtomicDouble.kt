package com.sunya.netchdf

import java.util.concurrent.atomic.AtomicReference

// import kotlin.concurrent.atomics.AtomicReference // not quite ready

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
// not quite ready
/*
import kotlinx.atomicfu.*

class AtomicDouble(initialValue: Double) {
    private val _value: AtomicRef<Double> = atomic(initialValue)

    fun get(): Double = _value.value

    fun set(newValue: Double) {
        _value.value = newValue
    }

    fun getAndSet(newValue: Double): Double = _value.getAndSet(newValue)

    fun compareAndSet(expect: Double, update: Double): Boolean = _value.compareAndSet(expect, update)

   // fun addAndGet(delta: Double): Double = _value.getAndAdd(delta) + delta

    fun getAndAdd(delta: Double): Double = _value.getAndAdd(delta)
} */