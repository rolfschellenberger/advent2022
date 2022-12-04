package com.rolf.day04

import com.rolf.Day
import com.rolf.util.contains
import com.rolf.util.overlap
import com.rolf.util.splitLine
import java.util.regex.Pattern

fun main() {
    Day04().run()
}

class Day04 : Day() {
    override fun solve1(lines: List<String>) {
        println(lines
            .map { parse(it) }
            .count { hasFullOverlap(it) }
        )
    }

    override fun solve2(lines: List<String>) {
        println(lines
            .map { parse(it) }
            .count { hasSomeOverlap(it) }
        )
    }

    private fun parse(line: String): Pair<IntRange, IntRange> {
        val elements = splitLine(line, pattern = Pattern.compile("[,\\-]")).map { it.toInt() }
        return elements[0]..elements[1] to elements[2]..elements[3]
    }

    private fun hasFullOverlap(it: Pair<IntRange, IntRange>): Boolean {
        return it.first.contains(it.second) || it.second.contains(it.first)
    }

    private fun hasSomeOverlap(it: Pair<IntRange, IntRange>): Boolean {
        return it.first.overlap(it.second) != null
    }
}
