package com.rolf.day05

import com.rolf.Day
import com.rolf.util.groupLines
import com.rolf.util.splitLine
import java.util.*

fun main() {
    Day05().run()
}

class Day05 : Day() {
    override fun solve1(lines: List<String>) {
        val (startLines, moveLines) = groupLines(lines, "")
        val stacks = parseStack(startLines)
        val moves = parseMoves(moveLines)

        move(stacks, moves)
        println(getTopCrates(stacks).joinToString(""))
    }

    override fun solve2(lines: List<String>) {
        val (startLines, moveLines) = groupLines(lines, "")
        val stacks = parseStack(startLines)
        val moves = parseMoves(moveLines)

        move(stacks, moves, true)
        println(getTopCrates(stacks).joinToString(""))
    }

    private fun parseMoves(moveLines: List<String>): List<Move> {
        val moves = mutableListOf<Move>()

        for (line in moveLines) {
            val elements = splitLine(line, " ")
            moves.add(
                Move(
                    elements[1].toInt(),
                    elements[3].toInt(),
                    elements[5].toInt()
                )
            )
        }

        return moves
    }

    private fun parseStack(startLines: List<String>): List<Stack<String>> {
        val stacks = mutableListOf<Stack<String>>()

        val letterStack = mutableListOf<List<String>>()
        var stackCount = 0
        for (line in startLines) {
            val letters = splitLine(line, chunkSize = 4)
                .map { it.replace("[", "") }
                .map { it.replace("]", "") }
                .map { it.trim() }
            letterStack.add(letters)
            stackCount = maxOf(stackCount, letters.size)
        }

        for (i in 0 until stackCount) {
            stacks.add(Stack<String>())
        }

        for (letter in letterStack.take(startLines.size - 1)) {
            for ((index, entry) in letter.withIndex()) {
                if (entry.isNotEmpty()) {
                    // Add at the front
                    stacks[index].add(0, entry)
                }
            }
        }

        return stacks
    }

    private fun move(stacks: List<Stack<String>>, moves: List<Move>, carryMultipleCrates: Boolean = false) {
        moves.forEach {
            val takes = mutableListOf<String>()
            for (i in 0 until it.number) {
                takes.add(stacks[it.from - 1].pop())
            }
            // Reverse the order, since they are carried together
            if (carryMultipleCrates) takes.reverse()
            for (crate in takes) {
                stacks[it.to - 1].push(crate)
            }
        }
    }

    private fun getTopCrates(stacks: List<Stack<String>>): List<String> {
        return stacks.map {
            it.peek()
        }
    }
}

data class Move(val number: Int, val from: Int, val to: Int)
