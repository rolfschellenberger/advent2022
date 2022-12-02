package com.rolf.day02

enum class Outcome(val score: Int) {
    LOST(0),
    DRAW(3),
    WIN(6);

    companion object {
        fun from(value: String): Outcome {
            return when (value) {
                "X" -> LOST
                "Y" -> DRAW
                "Z" -> WIN
                else -> throw Exception("Unexpected input $value")
            }
        }
    }
}
