package com.sunya.netchdf.testdata

import okio.Path

const val testData = "/home/all/testdata/"

fun testFilesIn(dirPath: String): TestFiles.SequenceBuilder {
    return TestFiles.SequenceBuilder(dirPath)
}

// list of suffixes to include
class FileFilterIncludeSuffixes(suffixes: String) : (String) -> Boolean {
    var suffixes: Array<String>

    init {
        this.suffixes = suffixes.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    }

    override fun invoke(filename: String): Boolean {
        suffixes.forEach { suffix ->
            if (filename.endsWith(suffix)) {
                return true
            }
        }
        return false
    }
}

// list of suffixes to exclude
class FileFilterSkipSuffixes(suffixes: String) : (String) -> Boolean {
    var suffixes: Array<String>

    init {
        this.suffixes = suffixes.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    }

    override fun invoke(filename: String): Boolean {
        suffixes.forEach { suffix ->
            if (filename.endsWith(suffix)) {
                return false
            }
        }
        return true
    }
}

class NameFilterAnd(val filters : List<(String) -> Boolean>) : (String) -> Boolean {
    override fun invoke(filename: String): Boolean {
        filters.forEach {
            if (!it.invoke(filename)) {
                return false
            }
        }
        return true
    }
}

class NameFilterOr(val filters : List<(String) -> Boolean>) : (String) -> Boolean {
    override fun invoke(filename: String): Boolean {
        filters.forEach {
            if (it.invoke(filename)) {
                return true
            }
        }
        return false
    }
}

class TestFiles {

    class SequenceBuilder(var dirPath: String) {
        var nameFilters = mutableListOf<(String) -> Boolean>()
        var pathFilter : (Path) -> Boolean = {  true }
        var recursion = false

        // filename only, not path
        fun addNameFilter(filter : (String) -> Boolean): SequenceBuilder {
            this.nameFilters.add(filter)
            return this
        }

        // full path
        fun withPathFilter(filter : (Path) -> Boolean): SequenceBuilder {
            this.pathFilter = filter
            return this
        }

        fun withRecursion(): SequenceBuilder {
            this.recursion = true
            return this
        }

        fun build() : Sequence<String> {
            return if (recursion) all(dirPath) else one(dirPath)
        }

        fun one(dirName : String): Sequence<String> {
            return Files.list(Paths.get(dirName))
                .filter { file: Path -> !Files.isDirectory(file) }
                .filter { this.pathFilter(it) }
                .filter { NameFilterAnd(nameFilters).invoke(it.fileName.toString()) }
                .map { obj: Path -> obj.toString() }
                .map { arguments: String? -> Arguments.of(arguments) }
        }

        fun all(dirName : String): Sequence<String> {
            return Sequence.concat(one(dirName), subdirs(dirName))
        }

        fun subdirs(dirName : String): Sequence<String> {
            return Files.list(Paths.get(dirName))
                .filter { file: Path -> Files.isDirectory(file) }
                .flatMap { obj: Path -> all(obj.toString()) }
        }
    }
}