package com.rolf.day25

import com.rolf.Day
import kotlin.math.absoluteValue
import kotlin.math.pow

fun main() {
    Day25().run()
}

class Day25 : Day() {
    override fun solve1(lines: List<String>) {
        var total = getTotal(lines)

        // Search the index that comes closes and is more than the total
        val index = getMaxIndex(total)

        // Find the best value to subtract on each index, to be closes to the remaining total value
        val output = mutableListOf<Int>()
        for (i in index downTo 0) {
            val best = getBest(i, total)
            val subtract = getPowerValue(i) * best
            total -= subtract
            output.add(best)
        }

        println(output.joinToString("") {
            when (it) {
                -2 -> "="
                -1 -> "-"
                else -> it.toString()
            }
        })
    }

    private fun getTotal(lines: List<String>): Long {
        var total = 0L
        for (line in lines) {
            for ((index, char) in line.reversed().withIndex()) {
                val value = when (char) {
                    '=' -> -2
                    '-' -> -1
                    else -> char.toString().toInt()
                }.toLong()
                total += value * getPowerValue(index)
            }
        }
        return total
    }

    private fun getPowerValue(i: Int): Long {
        return 5.0.pow(i).toLong()
    }

    private fun getMaxIndex(total: Long): Int {
        for (i in 0 until Int.MAX_VALUE) {
            val v = 5.0.pow(i).toLong() * 2
            if (v > total) {
                return i
            }
        }
        throw Exception("Total ($total) does not fit in Int range")
    }

    private fun getBest(i: Int, total: Long): Int {
        var best = Long.MAX_VALUE
        var value = 0
        for (option in listOf(-2, -1, 0, 1, 2)) {
            val v = getPowerValue(i) * option
            val diff = (total - v).absoluteValue
            if (diff < best) {
                best = diff
                value = option
            }
        }
        return value
    }

    override fun solve2(lines: List<String>) {
    }
}
