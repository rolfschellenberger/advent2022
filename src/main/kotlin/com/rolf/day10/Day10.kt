package com.rolf.day10

import com.rolf.Day
import com.rolf.util.Computer
import com.rolf.util.Instruction
import com.rolf.util.MatrixString
import com.rolf.util.Point
import kotlin.math.floor

fun main() {
    Day10().run()
}

class Day10 : Day() {
    override fun solve1(lines: List<String>) {
        val computer = MyComputer(parse(lines))
        computer.setValue("X", 1)
        computer.execute()

        println(
            listOf(
                (20 * computer.duringCycle.getValue(20)),
                (60 * computer.duringCycle.getValue(60)),
                (100 * computer.duringCycle.getValue(100)),
                (140 * computer.duringCycle.getValue(140)),
                (180 * computer.duringCycle.getValue(180)),
                (220 * computer.duringCycle.getValue(220))
            ).sum()
        )
    }

    override fun solve2(lines: List<String>) {
        val computer = MyComputer(parse(lines))
        computer.setValue("X", 1)
        computer.execute()

        println(computer.crt)
    }

    private fun parse(lines: List<String>): List<Instruction> {
        return lines.map {
            Instruction(it)
        }
    }
}

class MyComputer(instructions: List<Instruction>) : Computer(instructions) {

    val duringCycle = mutableMapOf<Long, Long>()
    val crt = MatrixString.buildDefault(40, 6, ".")

    override fun execute(instruction: Instruction) {
        when (instruction.operator) {
            "noop" -> {
                increaseValue("c", 1)
                duringCycle[getValue("c")] = getValue("X")
                draw()
            }

            "addx" -> {
                increaseValue("c", 1)
                duringCycle[getValue("c")] = getValue("X")
                draw()
                increaseValue("c", 1)
                duringCycle[getValue("c")] = getValue("X")
                draw()
                increaseValue("X", instruction.parts[0].toInt())
            }

            else -> throw Exception("Wrong ${instruction.input}")
        }
        pointer++
    }

    private fun increaseValue(register: String, value: Int) {
        setValue(register, getValue(register) + value)
    }

    private fun draw() {
        // Cycle
        val c = getValue("c")
        val X = getValue("X")
        val x = ((c - 1) % 40).toInt()
        val y = floor((c - 1) / 40.0).toInt()
        val point = Point(x, y)

        // Draw location
        if ((X - 1..X + 1).contains(x)) {
            crt.set(point, "#")
        }
    }
}
