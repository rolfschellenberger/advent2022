package com.rolf.day11

import com.rolf.Day
import com.rolf.util.groupLines
import kotlin.math.floor


fun main() {
    Day11().run()
}

class Day11 : Day() {
    private val monkeys = mutableListOf<Monkey>()
    private var mod = 1L

    private val reductionFunction1 = { input: Long -> floor(input / 3.0).toLong() }
    private val reductionFunction2 = { input: Long -> input % mod }

    override fun solve1(lines: List<String>) {
        initialize(lines)
        println(takeTurns(20, reductionFunction1))
    }

    override fun solve2(lines: List<String>) {
        initialize(lines)
        println(takeTurns(10000, reductionFunction2))
    }

    private fun initialize(lines: List<String>) {
        monkeys.clear()
        monkeys.addAll(groupLines(lines, "").map { parse(it) })

        mod = 1L
        for (monkey in monkeys) {
            mod *= monkey.modValue
        }
    }

    private fun parse(lines: List<String>): Monkey {
        val number = lines[0].split(" ")[1].replace(":", "").toInt()
        val items = lines[1].split(": ")[1].split(", ").map { it.toLong() }
        val operation = lines[2].split(" = ")[1]
        val divisible = lines[3].split(" ").last().toInt()
        val trueMonkey = lines[4].split(" ").last().toInt()
        val falseMonkey = lines[5].split(" ").last().toInt()

        return Monkey(
            number,
            items.toMutableList(),
            operation,
            divisible,
            trueMonkey,
            falseMonkey
        )
    }

    private fun takeTurns(turns: Int, reductionFunction1: (Long) -> Long): Long {
        for (turn in 0 until turns) {
            monkeys.forEach {
                turn(it, reductionFunction1)
            }
        }
        return monkeys.map { it.inspectCount }
            .sortedDescending()
            .take(2)
            .reduce { a, b -> a * b }
    }

    private fun turn(monkey: Monkey, reductionFunction: (Long) -> Long) {
        val items = monkey.items.toList()
        monkey.items.clear()

        for (item in items) {
            val level = monkey.operation(item)
            val rounded = reductionFunction(level)
            monkeys[monkey.test(rounded)].items.add(rounded)
        }
    }
}

data class Monkey(
    val number: Int,
    val items: MutableList<Long>,
    val eval: String,
    val modValue: Int,
    val testTrue: Int,
    val testFalse: Int,
    var inspectCount: Long = 0
) {
    fun test(input: Long): Int {
        return if (input % modValue == 0L) testTrue else testFalse
    }

    fun operation(item: Long): Long {
        inspectCount++

        val statement = eval.replace("old", item.toString())
        val (left, operator, right) = statement.split(" ").map { it.trim() }
        val l = left.toLong()
        val r = right.toLong()
        return when (operator) {
            "+" -> l + r
            "*" -> l * r
            else -> throw Exception("Unknown expression $eval")
        }
    }
}
