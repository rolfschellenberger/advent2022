package com.rolf.util

fun readLines(fileName: String): List<String> {
    return object {}.javaClass.getResource(fileName).readText().split("\n")
}

fun splitLine(line: String, delimiter: String = "", chunkSize: Int = 1): List<String> {
    if (delimiter.isEmpty()) {
        return splitLine(line, maxOf(1, chunkSize))
    }
    return splitLine(line, delimiter)
}

private fun splitLine(line: String, chunkSize: Int): List<String> {
    return line.chunked(chunkSize)
}

private fun splitLine(line: String, delimiter: String): List<String> {
    return line.split(delimiter)
}

fun splitLines(lines: List<String>, delimiter: String = "", chunkSize: Int = 1): List<List<String>> {
    return lines.map { splitLine(it, delimiter, chunkSize) }
}

fun groupLines(lines: List<String>, match: String): List<List<String>> {
    val groups = mutableListOf<MutableList<String>>()
    var group = mutableListOf<String>()

    for (line in lines) {
        if (line == match) {
            groups.add(group)
            group = mutableListOf()
        } else {
            group.add(line)
        }
    }
    groups.add(group)
    return groups
}

fun <T> getPermutations(options: List<T>, size: Int = options.size, position: Int = 0): List<List<T>> {
    if (position >= size || options.isEmpty()) {
        return listOf(emptyList())
    }
    if (options.size == 1) {
        return listOf(listOf(options[0]))
    }

    val result = mutableListOf<List<T>>()
    for (option in options) {
        val newOptions = options.toMutableList()
        newOptions.remove(option)
        for (permutation in getPermutations(newOptions, size, position + 1)) {
            result.add(listOf(option) + permutation)
        }
    }

    return result
}
