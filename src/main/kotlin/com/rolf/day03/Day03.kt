package com.rolf.day03

import com.rolf.Day

fun main() {
    Day03().run()
}

class Day03 : Day() {
    override fun solve1(lines: List<String>) {
        println(
            lines.asSequence()
                .map {
                    it.subSequence(0, it.length / 2).toSet()
                        .intersect(it.subSequence(it.length / 2, it.length).toSet())
                        .first()
                }
                .map { priority(it) }
                .sum()
        )
    }

    override fun solve2(lines: List<String>) {
        println(
            lines.chunked(3)
                .asSequence()
                .map {
                    val a = it[0].toSet()
                    val b = it[1].toSet()
                    val c = it[2].toSet()
                    a.intersect(b).intersect(c).first()
                }
                .map { priority(it) }
                .sum()
        )
    }

    private fun priority(char: Char): Int {
        // Lowercase item types `a` through `z` have priorities 1 through 26.
        // Uppercase item types `A` through `Z` have priorities 27 through 52.
        return if (char.code >= 97) char.code - 96 else char.code - 64 + 26
    }
}
