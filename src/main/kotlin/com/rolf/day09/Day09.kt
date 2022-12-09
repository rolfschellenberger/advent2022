package com.rolf.day09

import com.rolf.Day
import com.rolf.util.Direction
import com.rolf.util.MatrixString
import com.rolf.util.Point
import kotlin.math.abs

fun main() {
    Day09().run()
}

class Day09 : Day() {
    override fun solve1(lines: List<String>) {
        val grid = MatrixString.buildDefault(1000, 1000, ".")
        val start = grid.center()
//        val start = grid.bottomLeft()
        var head = start
        var tail = start
//        print(grid, start, head, tail)

        val moves = parse(lines)
//        moves.forEach { println(it) }

        val tailLocations = mutableSetOf(tail)
        for (move in moves) {
            for (i in 0 until move.number) {
                val newHead = grid.getForward(head, move.direction)!!

                // Drag tail along when needed
                if (abs(tail.x - newHead.x) > 1 || abs(tail.y - newHead.y) > 1) {
                    tail = head
                    tailLocations.add(tail)
                }
                head = newHead

//                print(grid, start, head, tail)
            }
        }
        println(tailLocations.size)
    }

    private fun print(grid: MatrixString, start: Point, head: Point, tail: Point) {
        val print = grid.copy()
        print.set(start, "s")
        print.set(tail, "T")
        print.set(head, "H")
        println(print)
        println()
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

    override fun solve2(lines: List<String>) {
        val grid = MatrixString.buildDefault(400, 400, ".")
//        val grid = MatrixString.buildDefault(40, 40, ".")
        val start = grid.center()
//        val start = Point(11, 15)
        var head = start
        var tails = mutableListOf<Point>()
        for (i in 0 until 9) {
            tails.add(start)
        }
//        print(grid, start, head, tails)

        val moves = parse(lines)
//        moves.forEach { println(it) }

        val tailLocations = mutableSetOf(tails.last())
        for (move in moves) {
            for (i in 0 until move.number) {
                val newHead = grid.getForward(head, move.direction)!!

                // Drag tails along when needed
                tails = dragTails(newHead, tails)
                tailLocations.add(tails.last())
                head = newHead
//                println(tailLocations.size)
//                print(grid, start, head, tails)
            }
        }
        println(tailLocations.size)
        for (tail in tailLocations) {
            grid.set(tail, "#")
        }
//        println(grid)
        // 2342
    }

    private fun dragTails(newHead: Point, tails: MutableList<Point>): MutableList<Point> {
        var newPos = newHead

        val newTails = mutableListOf<Point>()
        for (tail in tails) {
            val newTail = dragTrail(newPos, tail)
            newTails.add(newTail)
            newPos = newTail
        }
        return newTails
    }

    private fun dragTrail(newHead: Point, tail: Point): Point {
        var newTail = tail

        // If the head is ever two steps directly up, down, left, or right from the tail,
        // the tail must also move one step in that direction, so it remains close enough:
        if (abs(tail.x - newHead.x) > 1 && tail.y == newHead.y) {
            newTail = Point((tail.x + newHead.x) / 2, tail.y)
        }
        if (abs(tail.y - newHead.y) > 1 && tail.x == newHead.x) {
            newTail = Point(tail.x, (tail.y + newHead.y) / 2)
        }

        // Otherwise, if the head and tail aren't touching and aren't in the same row or column,
        // the tail always moves one step diagonally to keep up:
        if (!newHead.touching(tail)) {
            // FIXME: Bug, move diagonal!
            if (abs(tail.x - newHead.x) > 1 && abs(tail.y - newHead.y) > 1) {
                // Close the gap
                newTail = Point((tail.x + newHead.x) / 2, (tail.y + newHead.y) / 2)
            }
            else if (abs(tail.x - newHead.x) > 1) {
                newTail = Point((tail.x + newHead.x) / 2, newHead.y)
            }
            else if (abs(tail.y - newHead.y) > 1) {
                newTail = Point(newHead.x, (tail.y + newHead.y) / 2)
            }
//            newTail = oldHead
        }
        return newTail
    }

    private fun print(grid: MatrixString, start: Point, head: Point, tails: List<Point>) {
        val print = grid.copy()
        print.set(start, "s")
        for (i in tails.size downTo 1) {
            print.set(tails[i - 1], "$i")
        }
        print.set(head, "H")
        println(print)
        println()
    }
}

data class Move(val direction: Direction, val number: Int)