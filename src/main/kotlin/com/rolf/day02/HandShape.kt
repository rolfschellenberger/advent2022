package com.rolf.day02

enum class HandShape(val score: Int) {
    ROCK(1),
    PAPER(2),
    SCISSORS(3);

    fun play(against: HandShape): Outcome {
        if (against == this) return Outcome.DRAW
        if (this == ROCK && against == SCISSORS) return Outcome.WIN
        if (this == PAPER && against == ROCK) return Outcome.WIN
        if (this == SCISSORS && against == PAPER) return Outcome.WIN
        return Outcome.LOST
    }

    fun playTo(outcome: Outcome): HandShape {
        return when (outcome) {
            Outcome.DRAW -> this
            Outcome.LOST -> HandShape.from(((score + 1) % 3) + 1)
            Outcome.WIN -> HandShape.from((score % 3) + 1)
        }
    }

    companion object {
        fun from(value: String): HandShape {
            return when (value) {
                "A", "X" -> ROCK
                "B", "Y" -> PAPER
                "C", "Z" -> SCISSORS
                else -> throw Exception("Unexpected input $value")
            }
        }

        fun from(value: Int): HandShape {
            for (handShape in HandShape.values()) {
                if (handShape.score == value) return handShape
            }
            throw Exception("No HandShape found with value $value")
        }
    }
}
