package com.rolf.util

fun IntArray.swap(a: Int, b: Int): IntArray {
    val tmp = this[a]
    this[a] = this[b]
    this[b] = tmp
    return this
}

fun IntArray.pushLeft(steps: Int): IntArray {
    return pushRight(size - (steps % size))
}

fun IntArray.pushRight(steps: Int): IntArray {
    val result = takeLast(steps % size) + dropLast(steps % size)
    for ((index, r) in result.withIndex()) {
        set(index, r)
    }
    return this
}

fun CharArray.swap(a: Int, b: Int): CharArray {
    val tmp = this[a]
    this[a] = this[b]
    this[b] = tmp
    return this
}

fun CharArray.swap(a: Char, b: Char): CharArray {
    return swap(indexOf(a), indexOf(b))
}

fun CharArray.pushLeft(steps: Int): CharArray {
    return pushRight(size - (steps % size))
}

fun CharArray.pushRight(steps: Int): CharArray {
    val result = takeLast(steps % size) + dropLast(steps % size)
    for ((index, r) in result.withIndex()) {
        set(index, r)
    }
    return this
}

/**
 * options: [a, b, c]
 * return: [[a, b, c], [a, c, b], [b, a, c], [b, c, a], [c, a, b], [c, b, a]]
 */
fun <T> getPermutations(
    options: List<T>,
    size: Int = options.size,
    position: Int = 0,
    cache: MutableMap<String, List<List<T>>> = mutableMapOf()
): List<List<T>> {
    if (position >= size || options.isEmpty()) {
        return listOf(emptyList())
    }
    if (options.size == 1) {
        return listOf(listOf(options[0]))
    }

    val key = options.joinToString { "-" }
    if (cache.containsKey(key)) {
        return cache[key]!!
    }

    val result = mutableListOf<List<T>>()
    for (option in options) {
        val newOptions = options.toMutableList()
        newOptions.remove(option)
        for (permutation in getPermutations(newOptions, size, position + 1)) {
            result.add(listOf(option) + permutation)
        }
    }
    cache[key] = result
    return result
}

fun <T> getPermutations(options: List<T>, onNextPermutation: (List<T>) -> Unit, prefix: List<T> = emptyList()) {
    if (options.isEmpty()) {
        onNextPermutation(prefix)
    }
    for (option in options) {
        val newOptions = options.toMutableList()
        newOptions.remove(option)
        getPermutations(newOptions, onNextPermutation, prefix + option)
    }
}

/**
 * options: [a, b, c]
 * return: [[a, b, c], [a, b], [a, c], [a], [b, c], [b], [c]]
 */
fun <T> getCombinations(options: List<T>, cache: MutableMap<String, List<List<T>>> = mutableMapOf()): List<List<T>> {
    if (options.isEmpty()) {
        return emptyList()
    }
    if (options.size == 1) {
        return listOf(listOf(options.first()))
    }

    val key = options.joinToString { "-" }
    if (cache.containsKey(key)) {
        return cache[key]!!
    }

    val result = mutableListOf<List<T>>()
    for (index in 1..options.size) {
        val current = options[index - 1]
        val subList = options.subList(index, options.size)
        for (p in getCombinations(subList, cache)) {
            result.add(listOf(current) + p)
        }
        result.add(listOf(current))
    }
    cache[key] = result
    return result
}

fun <T> getCombinations(
    options: List<T>,
    onNextCombination: (List<T>) -> Unit,
    earlyTermination: (List<T>) -> Boolean = { _ -> false },
    prefix: List<T> = emptyList()
) {
    if (earlyTermination(prefix)) {
        return
    }
    for (index in 1..options.size) {
        val current = options[index - 1]
        val newOptions = options.subList(index, options.size)
        getCombinations(newOptions, onNextCombination, earlyTermination, prefix + current)
    }
    if (prefix.isNotEmpty()) {
        onNextCombination(prefix)
    }
}
