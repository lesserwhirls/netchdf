package com.sunya.netchdf.testutils

import kotlin.test.*

class TestMisc {

    @Test
    fun wtfAreSequences() {
        val seq: Sequence<Int> = generateSequence(1) {
            it + 3
        }.take(10)
        seq.forEach { println("$it,") }
        println(" sum = ${seq.sum()}") // Prints 145
    }

    /*
    @Test
    fun regex() {
        match("what:thefuck", "[A-Za-z_]+:[A-Za-z_]+")
        match("what:thefuck", "\\w+:\\w+")
    }

    fun match(input: CharSequence, pattern: String) {
        val regex = Regex(pattern)
        val matcher = regex.matchEntire(input)

        println("match = ${matcher.find()}")
    }

     */

}