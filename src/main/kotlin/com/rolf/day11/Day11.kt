package com.rolf.day11

import com.rolf.Day
import kotlin.math.floor

fun main() {
    Day11().run()
}

class Day11 : Day() {
    val monkeys = mutableListOf<Monkey>()

    override fun solve1(lines: List<String>) {
        // worry level to be divided by three and rounded down to the nearest integer.
        // 20 rounds
//        val monkey0 = Monkey(
//            0,
//            mutableListOf(79, 98),
//            { a: Int -> a * 19 },
//            { a: Int -> a % 23 == 0 },
//            2,
//            3
//        )
//        val monkey1 = Monkey(
//            1,
//            mutableListOf(54, 65, 75, 74),
//            { a: Int -> a + 6 },
//            { a: Int -> a % 19 == 0 },
//            2,
//            0
//        )
//        val monkey2 = Monkey(
//            2,
//            mutableListOf(79, 60, 97),
//            { a: Int -> a * a },
//            { a: Int -> a % 13 == 0 },
//            1,
//            3
//        )
//        val monkey3 = Monkey(
//            3,
//            mutableListOf(74),
//            { a: Int -> a + 3 },
//            { a: Int -> a % 17 == 0 },
//            0,
//            1
//        )

        val monkey0 = Monkey(
            0,
            mutableListOf(75, 75, 98, 97, 79, 97, 64),
            { a: Int -> a * 13 },
            { a: Int -> a % 19 == 0 },
            2,
            7
        )
        val monkey1 = Monkey(
            1,
            mutableListOf(50, 99, 80, 84, 65, 95),
            { a: Int -> a + 2 },
            { a: Int -> a % 3 == 0 },
            4,
            5
        )
        val monkey2 = Monkey(
            2,
            mutableListOf(96, 74, 68, 96, 56, 71, 75, 53),
            { a: Int -> a + 1 },
            { a: Int -> a % 11 == 0 },
            7,
            3
        )
        val monkey3 = Monkey(
            3,
            mutableListOf(83, 96, 86, 58, 92),
            { a: Int -> a + 8 },
            { a: Int -> a % 17 == 0 },
            6,
            1
        )
        val monkey4 = Monkey(
            4,
            mutableListOf(99),
            { a: Int -> a * a },
            { a: Int -> a % 5 == 0 },
            0,
            5
        )
        val monkey5 = Monkey(
            5,
            mutableListOf(60, 54, 83),
            { a: Int -> a + 4 },
            { a: Int -> a % 2 == 0 },
            2,
            0
        )
        val monkey6 = Monkey(
            6,
            mutableListOf(77, 67),
            { a: Int -> a * 17 },
            { a: Int -> a % 13 == 0 },
            4,
            1
        )
        val monkey7 = Monkey(
            7,
            mutableListOf(95, 65, 58, 76),
            { a: Int -> a + 5 },
            { a: Int -> a % 7 == 0 },
            3,
            6
        )

        monkeys.add(monkey0)
        monkeys.add(monkey1)
        monkeys.add(monkey2)
        monkeys.add(monkey3)
        monkeys.add(monkey4)
        monkeys.add(monkey5)
        monkeys.add(monkey6)
        monkeys.add(monkey7)

        for (turn in 0 until 20) {
            for (monkey in monkeys) {
                turn(monkey)
            }
        }
        for (monkey in monkeys) {
            println("Monkey ${monkey.number}: ${monkey.items}")
            println("Monkey ${monkey.number}: ${monkey.inspectCount}")
        }
        val inspections = monkeys.map { it.inspectCount }
            .sortedDescending()
            .take(2)
            .reduce { a, b -> a * b }
        println(inspections)
    }

    private fun turn(monkey: Monkey) {
        println("Monkey ${monkey.number}")
        val items = monkey.items.toList()
        monkey.items.clear()

        for (item in items) {
            val level = monkey.operation(item)
            val rounded = floor(level / 3.0).toInt()

            monkey.inspectCount++

            if (monkey.test(rounded)) {
                println("Give $rounded to monkey ${monkey.testTrue}")
                monkeys[monkey.testTrue].items.add(rounded)
            } else {
                println("Give $rounded to monkey ${monkey.testFalse}")
                monkeys[monkey.testFalse].items.add(rounded)
            }
        }
    }

    override fun solve2(lines: List<String>) {
    }
}

data class Monkey(
    val number: Int,
    val items: MutableList<Int>,
    val operation: (Int) -> Int,
    val test: (Int) -> Boolean,
    val testTrue: Int,
    val testFalse: Int,
    var inspectCount: Int = 0
)
