package com.rolf.day06

import com.rolf.Day

fun main() {
    Day06().run()
}

class Day06 : Day() {
    override fun solve1(lines: List<String>) {
        println(getFirstUniqueSequencePositionOfLength(lines.first(), 4))
    }

    override fun solve2(lines: List<String>) {
        println(getFirstUniqueSequencePositionOfLength(lines.first(), 14))
    }

    private fun getFirstUniqueSequencePositionOfLength(input: String, length: Int): Int {
        for (i in length..input.length) {
            val seq = input.subSequence(i - length, i)
            if (seq.toSet().size == length) {
                return i
            }
        }
        return -1
    }
}
