package com.rolf.day01

import com.rolf.Day
import com.rolf.util.groupLines

fun main() {
    Day01().run()
}

class Day01 : Day() {
    override fun solve1(lines: List<String>) {
        println(
            groupLines(lines, "")
                .map { elf -> elf.map { cal -> cal.toLong() } }
                .map { it.sum() }
                .maxOf { it }
        )
    }

    override fun solve2(lines: List<String>) {
        println(
            groupLines(lines, "")
                .asSequence()
                .map { elf -> elf.map { cal -> cal.toLong() } }
                .map { it.sum() }
                .sortedDescending()
                .take(3)
                .sum()
        )
    }
}
