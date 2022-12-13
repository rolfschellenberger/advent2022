package com.rolf.day13

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.rolf.Day
import com.rolf.util.groupLines


fun main() {
    Day13().run()
}

class Day13 : Day() {
    override fun solve1(lines: List<String>) {
        val comparator = JsonArrayComparator()
        println(
            groupLines(lines, "")
                .asSequence()
                .flatten()
                .map { parse(it) }
                .chunked(2)
                .withIndex()
                .filter {
                    comparator.compare(it.value[0], it.value[1]) == -1
                }
                .sumOf {
                    it.index + 1
                }
        )
    }

    override fun solve2(lines: List<String>) {
        val marker1 = "[[2]]"
        val marker2 = "[[6]]"
        val filter = setOf(marker1, marker2)

        println(
            (groupLines(lines, "").flatten() + marker1 + marker2)
                .asSequence()
                .map { parse(it) }
                .sortedWith(JsonArrayComparator())
                .withIndex()
                .filter {
                    filter.contains(it.value.toString())
                }
                .map { it.index + 1 }
                .reduce { a, b -> a * b }
        )
    }

    private fun parse(line: String): JsonArray {
        return Gson().fromJson(line, JsonArray::class.java)
    }
}

class JsonArrayComparator : Comparator<JsonArray> {
    override fun compare(left: JsonArray, right: JsonArray): Int {
        return when (compareJsonElements(left, right)) {
            null -> 0
            true -> -1
            false -> 1
        }
    }

    private fun compareJsonElements(left: JsonElement, right: JsonElement): Boolean? {
        if (left is JsonPrimitive && right is JsonArray) {
            val l = JsonArray()
            l.add(left.asInt)
            return compareJsonElements(l, right)
        }
        if (left is JsonArray && right is JsonPrimitive) {
            val r = JsonArray()
            r.add(right.asInt)
            return compareJsonElements(left, r)
        }
        if (left is JsonPrimitive && right is JsonPrimitive) {
            if (left.asInt < right.asInt) {
                // Left side is smaller, so inputs are in the right order"
                return true
            }
            if (left.asInt > right.asInt) {
                // Right side is smaller, so inputs are not in the right order
                return false
            }
            return null
        }
        if (left is JsonArray && right is JsonArray) {
            for ((index, element) in left.withIndex()) {
                if (index < right.size()) {
                    val compare = compareJsonElements(element, right.get(index))
                    if (compare != null) {
                        return compare
                    }
                }
                if (index >= right.size()) {
                    // Right side ran out of items, so inputs are not in the right order
                    return false
                }
            }

            if (right.size() > left.size()) {
                // Left side ran out of items, so inputs are in the right order
                return true
            }
        }
        return null
    }
}
