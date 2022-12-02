package com.rolf.day02

import com.rolf.Day

fun main() {
    Day02().run()
}

class Day02 : Day() {
    override fun solve1(lines: List<String>) {
        var sum = 0
        for (line in lines) {
            val (opponent, you) = line.split(" ")
            val score = score(you)
            val win = win(opponent, you)
            sum += score + win
        }
        println(sum)
    }

    private fun win(opponent: String, you: String): Int {
        if (opponent == "A" && you == "X") return 3
        if (opponent == "B" && you == "Y") return 3
        if (opponent == "C" && you == "Z") return 3
        if (opponent == "A" && you == "Y") return 6
        if (opponent == "B" && you == "Z") return 6
        if (opponent == "C" && you == "X") return 6
        return 0
    }

    private fun score(you: String): Int {
        return when (you) {
            "X" -> 1
            "Y" -> 2
            "Z" -> 3
            else -> throw Exception("Wrong input $you")
        }
    }

    override fun solve2(lines: List<String>) {
        var sum = 0
        for (line in lines) {
            val (opponent, outcome) = line.split(" ")
            val score = outcomeScore(outcome)
            val you = whatToPlay(opponent, outcome)
            val win = score(you)
            sum += score + win
        }
        println(sum)
    }

    private fun whatToPlay(opponent: String, outcome: String): String {
        // Lose
        if (opponent == "A" && outcome == "X") return "Z"
        if (opponent == "B" && outcome == "X") return "X"
        if (opponent == "C" && outcome == "X") return "Y"

        // Draw
        if (opponent == "A" && outcome == "Y") return "X"
        if (opponent == "B" && outcome == "Y") return "Y"
        if (opponent == "C" && outcome == "Y") return "Z"

        // Win
        if (opponent == "A" && outcome == "Z") return "Y"
        if (opponent == "B" && outcome == "Z") return "Z"
        if (opponent == "C" && outcome == "Z") return "X"

        throw Exception("Wrong combination")
    }

    private fun outcomeScore(outcome: String): Int {
        return when (outcome) {
            "X" -> 0
            "Y" -> 3
            "Z" -> 6
            else -> throw Exception("Wrong input $outcome")
        }
    }
}
