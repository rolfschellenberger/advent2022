package com.rolf.day23

import com.rolf.Day
import com.rolf.util.*

fun main() {
    Day23().run()
}

class Day23 : Day() {
    override fun solve1(lines: List<String>) {
        val grid = MatrixString.build(splitLines(lines))
//        println(grid)
        // 0=N 1=S 2=W 3=E
        val moves = charArrayOf('N', 'S', 'W', 'E')
        var rounds = 0
//        for (i in 0 until 10) {
        while (true) {
            rounds++
//            println()
//            println(grid)
            if (grid.getLeftEdge().contains("#") ||
                grid.getTopEdge().contains("#") ||
                grid.getRightEdge().contains("#") ||
                grid.getBottomEdge().contains("#")
            ) {
                grid.grow(1, 1, 1, 1, ".")
            }
//            println(moves)

            val movesMap = mutableMapOf<Point, Pair<Point, Int>>()
            val elves = grid.find("#").toSet()
            for (elf in elves) {
                val newPos = newPosition(grid, elves, elf, moves)
                if (newPos != elf) {
                    movesMap.computeIfAbsent(newPos) { elf to 0 }
                    movesMap.computeIfPresent(newPos) { _, v -> v.first to v.second + 1 }
//                println("$elf -> $newPos")
                }
            }

            // Inspect the new positions that have only 1 elf move to it
            val movingElves = movesMap.filter { (_, value) -> value.second == 1 }
            println("${movingElves.size} elves move")
            if (movingElves.isEmpty()) {
                break
            }
            for (movingElf in movingElves) {
                val from = movingElf.value.first
                val to = movingElf.key
//                println("$from -> $to")
                grid.set(from, ".")
                grid.set(to, "#")
            }

            moves.pushLeft(1)
        }
//        println(grid)

        // Now take the smallest rectangle
        val elves = grid.find("#")
        val minX = elves.minOf { it.x }
        val maxX = elves.maxOf { it.x }
        val minY = elves.minOf { it.y }
        val maxY = elves.maxOf { it.y }
        grid.cutOut(Point(minX, minY), Point(maxX, maxY))
        println(grid)
        println(grid.count("."))
        println(rounds)
    }

    private fun newPosition(grid: MatrixString, elves: Set<Point>, elf: Point, moves: CharArray): Point {
        val neighbours = grid.getNeighbours(elf)
        if (elves.intersect(neighbours).isEmpty()) {
            // Do nothing
//            println("No elfs around me!")
            return elf
        }
        for (move in moves) {
            val direction = getDirection(move)
            // When there are no elves in the moving direction
            if (canMove(grid, elves, elf, direction)) {
                // Move into that direction
//                println("$elf: $direction")
                return grid.getForward(elf, direction)!!
            }
        }
        // Stay put
        return elf
    }

    private fun canMove(grid: MatrixString, elves: Set<Point>, elf: Point, direction: Direction): Boolean {
        val adjacent = when (direction) {
            Direction.NORTH -> listOf(grid.getUp(elf), grid.getLeftUp(elf), grid.getRightUp(elf))
            Direction.SOUTH -> listOf(grid.getDown(elf), grid.getLeftDown(elf), grid.getRightDown(elf))
            Direction.WEST -> listOf(grid.getLeft(elf), grid.getLeftUp(elf), grid.getLeftDown(elf))
            Direction.EAST -> listOf(grid.getRight(elf), grid.getRightUp(elf), grid.getRightDown(elf))
        }

        // We can move if there is no elf in the adjacent positions
        for (point in adjacent) {
            if (elves.contains(point)) {
                return false
            }
        }
        return true
    }

    private fun getDirection(move: Char): Direction {
        return when (move) {
            'N' -> Direction.NORTH
            'S' -> Direction.SOUTH
            'W' -> Direction.WEST
            'E' -> Direction.EAST
            else -> throw Exception("Wrong move $move")
        }
    }

    override fun solve2(lines: List<String>) {
    }
}
