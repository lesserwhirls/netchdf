package com.sunya.netchdf.testfiles


class SortFiles {

    companion object {
        fun params(): Sequence<String>{
            return sequenceOf(
                H4Files.params(),
                H5Files.params(),
                N3Files.params(),
                N4Files.params(),
                NetchdfExtraFiles.params(false)
            ).flatten()
        }

        val filenames = mutableMapOf<String, MutableList<String>>()
        val showAllFiles = true

        fun afterAll() {
            println("*** nfiles = ${filenames.size}")
            var dups = 0
            filenames.keys.sorted().forEach {
                val paths = filenames[it]!!
                if (paths.size > 1) {
                    println("$it")
                    paths.forEach { println("  $it") }
                }
                dups += paths.size - 1
            }
            println("*** nduplicates = ${dups}")

            if (showAllFiles) {
                println("*** nfiles = ${filenames.size}")
                filenames.keys.sorted().forEach {
                    val paths = filenames[it]!!
                    paths.forEach {path-> println("$path/$it") }
                }
            }
        }
    }
}