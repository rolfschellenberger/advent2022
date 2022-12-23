package com.rolf.day23

import com.rolf.Day
import com.rolf.util.*

fun main() {
    Day23().run()
}

class Day23 : Day() {
    override fun solve1(lines: List<String>) {
        val elves = moveElves(lines, 10).first

        // Now take the smallest rectangle
        val minX = elves.minOf { it.x }
        val maxX = elves.maxOf { it.x }
        val minY = elves.minOf { it.y }
        val maxY = elves.maxOf { it.y }
        val width = maxX - minX + 1
        val height = maxY - minY + 1
        println((width * height) - elves.size)
    }

    override fun solve2(lines: List<String>) {
        println(moveElves(lines, Int.MAX_VALUE).second)
    }

    private fun moveElves(lines: List<String>, maxRounds: Int): Pair<Set<Point>, Int> {
        val grid = MatrixString.build(splitLines(lines))
        val moves = charArrayOf('N', 'S', 'W', 'E')
        val elves = grid.find("#").toMutableSet()
        var rounds = 0
        for (i in 0 until maxRounds) {
            rounds++

            val directions = moves.map { getDirection(it) }
            val movements = elves.map {
                it to newPosition(elves, it, directions)
            }

            // When all the intended movements are known, get the ones that end up on unique locations
            val possibleMovements = movements
                // Keep actual movements
                .filter { (k, v) -> k != v }
                // Group by target locations
                .groupBy { (_, v) -> v }
                // Only keep the ones where only 1 elf moves to
                .filter { (_, v) -> v.size == 1 }
                // Get back the original movements (from, to)
                .map { (_, v) -> v.first() }

            // When there are no elves moving, we can stop
            if (possibleMovements.isEmpty()) {
                break
            }

            // Since every elf is on a unique location, remove the old locations and add the new
            for (p in possibleMovements) {
                elves.remove(p.first)
                elves.add(p.second)
            }

            // Rotate the moves
            moves.pushLeft(1)
        }
        return elves to rounds
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

    private fun newPosition(elves: Set<Point>, elf: Point, moves: List<Direction>): Point {
        val neighbours = getNeighbours(elf)
        // When there are no elves around the elf, don't move.
        if (elves.intersect(neighbours).isEmpty()) {
            return elf
        }

        // Otherwise try to move in the order of the moves
        for (move in moves) {
            // When there are no elves in the moving direction
            if (canMove(elves, elf, move)) {
                // Move into that direction
                return move(elf, move)
            }
        }

        // Stay put
        return elf
    }

    private fun getNeighbours(elf: Point): Set<Point> {
        return setOf(
            Point(elf.x, elf.y - 1),
            Point(elf.x, elf.y + 1),
            Point(elf.x + 1, elf.y - 1),
            Point(elf.x + 1, elf.y),
            Point(elf.x + 1, elf.y + 1),
            Point(elf.x - 1, elf.y - 1),
            Point(elf.x - 1, elf.y),
            Point(elf.x - 1, elf.y + 1)
        )
    }

    private fun canMove(elves: Set<Point>, elf: Point, direction: Direction): Boolean {
        val adjacent = when (direction) {
            Direction.NORTH -> setOf(Point(elf.x, elf.y - 1), Point(elf.x - 1, elf.y - 1), Point(elf.x + 1, elf.y - 1))
            Direction.SOUTH -> setOf(Point(elf.x, elf.y + 1), Point(elf.x - 1, elf.y + 1), Point(elf.x + 1, elf.y + 1))
            Direction.WEST -> setOf(Point(elf.x - 1, elf.y), Point(elf.x - 1, elf.y - 1), Point(elf.x - 1, elf.y + 1))
            Direction.EAST -> setOf(Point(elf.x + 1, elf.y), Point(elf.x + 1, elf.y - 1), Point(elf.x + 1, elf.y + 1))
        }

        // We can move if there is no elf in the adjacent positions
        return adjacent.all { !elves.contains(it) }
    }

    private fun move(elf: Point, direction: Direction): Point {
        return when (direction) {
            Direction.NORTH -> Point(elf.x, elf.y - 1)
            Direction.SOUTH -> Point(elf.x, elf.y + 1)
            Direction.WEST -> Point(elf.x - 1, elf.y)
            Direction.EAST -> Point(elf.x + 1, elf.y)
        }
    }
}
