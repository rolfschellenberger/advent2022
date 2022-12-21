package com.rolf.day21

import com.rolf.Day

fun main() {
    Day21().run()
}

class Day21 : Day() {
    override fun solve1(lines: List<String>) {
        val (map, operands) = parse(lines)
        solve(map, operands)
        println(map["root"])
    }

    override fun solve2(lines: List<String>) {
        val (map, operands) = parse(lines)

        // Find the interval via binary search
        var index = 0L
        var to = Long.MAX_VALUE / 100
        var stepSize = to / 10
        var previousScore: Int? = null
        while (index < to) {
            val compare = solve(map.toMutableMap(), operands.toMutableMap(), index)
            if (compare == 0) {
                println("$index")
                return
            }
            if (previousScore != null && previousScore != compare) {
                to = index
                index -= stepSize
                stepSize = maxOf(1, (to - index) / 10)
                previousScore = null
            } else {
                index += stepSize
                previousScore = compare
            }
        }
    }

    private fun parse(lines: List<String>): Pair<MutableMap<String, Long>, MutableMap<String, String>> {
        val map = mutableMapOf<String, Long>()
        val operands = mutableMapOf<String, String>()
        for (line in lines) {
            val (monkey, remainder) = line.split(": ")
            val value = remainder.toLongOrNull()
            if (value != null) {
                map[monkey] = value
            } else {
                operands[monkey] = remainder
            }
        }
        return Pair(map, operands)
    }

    private fun solve(
        map: MutableMap<String, Long>,
        operands: MutableMap<String, String>,
        input: Long? = null
    ): Int {
        if (input != null) {
            map["humn"] = input
        }
        // Left and right should be equal
        val (leftSide, _, rightSide) = operands.getValue("root").split(" ")

        // Calculate the values
        while (operands.isNotEmpty()) {
            val solvedKeys = mutableListOf<String>()
            for (operand in operands) {
                val (left, operator, right) = operand.value.split(" ")
                if (map.containsKey(left) && map.containsKey(right)) {
                    val a = map.getValue(left)
                    val b = map.getValue(right)
                    val value = when (operator) {
                        "+" -> a + b
                        "-" -> a - b
                        "/" -> a / b
                        "*" -> a * b
                        else -> throw Exception("Unknown operator: $operator")
                    }
                    map[operand.key] = value
                    solvedKeys.add(operand.key)
                }
            }
            for (solvedKey in solvedKeys) {
                operands.remove(solvedKey)
            }
        }

        return map.getValue(leftSide).compareTo(map.getValue(rightSide))
    }
}
