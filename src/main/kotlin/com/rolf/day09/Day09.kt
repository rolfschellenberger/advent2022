package com.rolf.day09

import com.rolf.Day
import com.rolf.util.Direction
import com.rolf.util.MatrixString
import com.rolf.util.Point
import kotlin.math.absoluteValue

fun main() {
    Day09().run()
}

class Day09 : Day() {
    override fun solve1(lines: List<String>) {
        println(
            simulateRope(parse(lines), 2).size
        )
    }

    override fun solve2(lines: List<String>) {
        println(
            simulateRope(parse(lines), 10).size
        )
    }

    private fun parse(lines: List<String>): List<Move> {
        return lines.map {
            val (a, b) = it.split(" ")
            val direction = when (a) {
                "R" -> Direction.EAST
                "L" -> Direction.WEST
                "D" -> Direction.SOUTH
                "U" -> Direction.NORTH
                else -> throw Exception("Wrong direction $a")
            }
            Move(direction, b.toInt())
        }
    }

    private fun simulateRope(moves: List<Move>, ropeLength: Int): MutableSet<Point> {
        val grid = MatrixString.buildDefault(400, 400, ".")
        val start = grid.center()
        val rope = mutableListOf<Point>()
        for (i in 0 until ropeLength) {
            rope.add(start)
        }
        val tailLocations = mutableSetOf(rope.last())

        for (move in moves) {
            for (i in 0 until move.number) {
                // Move the head
                rope[0] = grid.getForward(rope.first(), move.direction)!!

                // Drag tails along when needed
                dragTails(rope)

                // Remember the last rope location
                tailLocations.add(rope.last())
            }
        }
        return tailLocations
    }

    private fun dragTails(rope: MutableList<Point>) {
        for (i in 1 until rope.size) {
            rope[i] = dragTrail(rope[i - 1], rope[i])
        }
    }

    private fun dragTrail(head: Point, tail: Point): Point {
        if (head.touching(tail)) return tail

        // If the head is ever two steps directly up, down, left, or right from the tail,
        // the tail must also move one step in that direction, so it remains close enough:
        if (head.distance(tail) > 1 && (tail.x == head.x || tail.y == head.y)) {
            return Point((tail.x + head.x) / 2, (tail.y + head.y) / 2)
        }

        // Otherwise, if the head and tail aren't touching and aren't in the same row or column,
        // the tail always moves one step diagonally to keep up:
        if ((tail.x - head.x).absoluteValue > 1 && (tail.y - head.y).absoluteValue > 1) {
            return Point((tail.x + head.x) / 2, (tail.y + head.y) / 2)
        }
        if ((tail.x - head.x).absoluteValue > 1) {
            return Point((tail.x + head.x) / 2, head.y)
        }
        return Point(head.x, (tail.y + head.y) / 2)
    }
}

data class Move(val direction: Direction, val number: Int)