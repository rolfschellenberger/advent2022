package com.rolf.day17

import com.rolf.Day
import com.rolf.util.MatrixString
import com.rolf.util.Point
import com.rolf.util.pushLeft
import com.rolf.util.splitLine

fun main() {
    Day17().run()
}

class Day17 : Day() {

    private var rockPointer = 0
    private val rocks: List<Rock>
    private var count = 0

    init {
        rocks = createRocks()
    }

    private fun gridHeight(grid: MatrixString): Int {
        return grid.find("#").maxOf { it.y } + 1
    }

    override fun solve1(lines: List<String>) {
        val moves = splitLine(lines.first()).map { it.first() }.toCharArray()
        val grid = MatrixString.buildDefault(7, 7000, ".")

        // 2783 is the repeating pattern
        // It starts after the first 452 lines
        // So look for the rock that leads up the 452 lines
        // Look for the rock that leads up to the 3235 lines (452 + 2783)
        // When you know the rock counts, we know the pattern + the offset, so we can skip to the closes
        // below 1000000000000 and run the simulation
        // (1000000000000 - rocks to go up to 452) % 2783 = additional runs to do
        var startGrid = grid.copy()
        for (i in 0 until 275+735) { //275+(0*1745)) {
            dropRock(grid, moves)
//            if (gridHeight(grid) == 3235) {
//                startGrid = grid.copy()
//            }
//            if (gridHeight(grid) > 3235) {
//                println(i+1)
//                break
//            }
        }
//        printGrid(startGrid)
//        println(gridHeight(startGrid))
        // 275 rocks were dropped to get to the starting point of the pattern
        // 2020 rocks were dropped to get to the end point of the pattern
        // So every (2020-275=1745) rocks, an additional 2783 height will be added
        // 275 = 452 high
        // 2020 = 3235 high
        // 3765 = 6018 high
        // (1000000000000 - 275) / 1745 = number of times 2783 height
        // (1000000000000 - 275) % 1745 = number of additional runs to do on top of the 275
        println((1000000000000L - 275L) / 1745L) // = 573065902
        println((1000000000000L - 275L) % 1745L) // = 735
        // Height of 275 + 735 rocks = 1616, this is 1616-452=1164 more height by the last bit
        // So we have 452 + (573065902 * 2783) + 1164 =
        println(452L + (573065902L * 2783L) + 1164L)

        // 735

//        printGrid(grid)
        println(gridHeight(grid))
    }

    private fun dropRock(grid: MatrixString, moves: CharArray) {
        // Put new rock
        val highestY = grid.find("#").maxOfOrNull { it.y } ?: -1
        val rockToPlace = nextRock()
        val rock = rockToPlace.moveRight(2).moveUp(highestY + 1 + 3)

//        println("Dropping ${rock.name}")
//        printGrid(grid, rock)

        // Repeat until y < 0:
        var previousRock = rock
        count = 0 // FIXME: remove
        while (true) {
            //  Push
            val move = moves.first()
            moves.pushLeft(1)

            // Check if the rock can move to the left or right
            var movedRock = when (move) {
                '<' -> previousRock.moveLeft(1)
                '>' -> previousRock.moveRight(1)
                else -> throw Exception("Incorrect move found: $move")
            }

            // When the move is not possible, revert the move
            if (!isPossible(grid, movedRock)) {
                movedRock = previousRock
            }

            // Now move down
            val downRock = movedRock.moveDown(1)
            previousRock = downRock

            // When the down move is not possible (hitting things), revert the move and fix the rock
            if (hasHit(grid, downRock)) {
                previousRock = movedRock
                break
            }
        }

        // Fix the rock
        for (point in previousRock.points) {
            grid.set(point, "#")
        }
//        printGrid(grid)
    }

    private fun isPossible(grid: MatrixString, rock: Rock): Boolean {
        // Check if any of the rock locations is out of bounds
        // or overlapping with some #
        if (rock.outOfBounds()) return false
        for (point in rock.points) {
            if (grid.get(point) == "#") return false
        }
        return true
    }

    private fun hasHit(grid: MatrixString, rock: Rock): Boolean {
        // Check if any of the rock locations is passed y = 0 or overlapping with some #
        for (point in rock.points) {
            if (point.y < 0) return true
            if (grid.get(point) == "#") return true
        }
        return false
//        count++
//        return count > 10
    }

    private fun printGrid(grid: MatrixString) {
        val copy = grid.copy()
        copy.flip(false)
        println(copy)
    }

    private fun printGrid(grid: MatrixString, rock: Rock) {
        val copy = grid.copy()
        rock.points.forEach {
            copy.set(it, "@")
        }
        copy.flip(false)
        println(copy)
        println()
    }

    private fun createRocks(): List<Rock> {
        val minus = Rock(
            "minus", listOf(
                Point(0, 0),
                Point(1, 0),
                Point(2, 0),
                Point(3, 0)
            )
        )
        val plus = Rock(
            "plus", listOf(
                Point(0, 1),
                Point(1, 0),
                Point(1, 1),
                Point(1, 2),
                Point(2, 1)
            )
        )
        val corner = Rock(
            "corner", listOf(
                Point(0, 0),
                Point(1, 0),
                Point(2, 0),
                Point(2, 1),
                Point(2, 2)
            )
        )
        val vertical = Rock(
            "vertical", listOf(
                Point(0, 0),
                Point(0, 1),
                Point(0, 2),
                Point(0, 3)
            )
        )
        val square = Rock(
            "square", listOf(
                Point(0, 0),
                Point(1, 0),
                Point(0, 1),
                Point(1, 1)
            )
        )
        return listOf(
            minus, plus, corner, vertical, square
        )
    }

    private fun nextRock(): Rock {
        val rock = rocks[rockPointer]
        rockPointer = (rockPointer + 1) % rocks.size
        return rock
    }

    override fun solve2(lines: List<String>) {
    }
}

data class Rock(val name: String, val points: List<Point>) {
    val height: Int = points.maxOfOrNull { it.y } ?: 0

    fun moveRight(i: Int): Rock {
        return Rock(name, points.map {
            Point(it.x + i, it.y)
        })
    }

    fun moveLeft(i: Int): Rock {
        return Rock(name, points.map {
            Point(it.x - i, it.y)
        })
    }

    fun moveDown(i: Int): Rock {
        return Rock(name, points.map {
            Point(it.x, it.y - i)
        })
    }

    fun moveUp(i: Int): Rock {
        return Rock(name, points.map {
            Point(it.x, it.y + i)
        })
    }

    fun outOfBounds(): Boolean {
        for (point in points) {
            if (point.x < 0 || point.x >= 7 || point.y < 0) return true
        }
        return false
    }
}
