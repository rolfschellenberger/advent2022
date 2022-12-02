package com.rolf.day02

import com.rolf.Day
import com.rolf.util.splitLines

fun main() {
    Day02().run()
}

class Day02 : Day() {
    override fun solve1(lines: List<String>) {
        println(
            splitLines(lines, " ").sumOf {
                val (opponent, you) = it.take(2)
                val opponentHandShape = HandShape.from(opponent)
                val myHandShape = HandShape.from(you)
                myHandShape.score + myHandShape.play(opponentHandShape).score
            }
        )
    }

    override fun solve2(lines: List<String>) {
        println(
            splitLines(lines, " ").sumOf {
                val (opponent, outcome) = it.take(2)
                val opponentHandShape = HandShape.from(opponent)
                val expectedOutcome = Outcome.from(outcome)
                val myHandShape = whatToPlay(opponentHandShape, expectedOutcome)
                myHandShape.score + expectedOutcome.score
            }
        )
    }

    private fun whatToPlay(opponentHandShape: HandShape, expectedOutcome: Outcome): HandShape {
        return when (expectedOutcome) {
            Outcome.DRAW -> opponentHandShape
            Outcome.LOST -> HandShape.from(((opponentHandShape.score + 1) % 3) + 1)
            Outcome.WIN -> HandShape.from((opponentHandShape.score % 3) + 1)
        }
    }
}
