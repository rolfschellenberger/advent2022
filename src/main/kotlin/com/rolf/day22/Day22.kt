package com.rolf.day22

import com.rolf.Day
import com.rolf.util.*

fun main() {
    Day22().run()
}

class Day22 : Day() {
    override fun solve1(lines: List<String>) {
        val (maze, p) = groupLines(lines, "")
        val path = p.first()
        val grid = buildGrid(maze)
        val instructions = parseInstructions(path)
        var location = grid.find(".").first()
        var direction = Direction.EAST

        for (instruction in instructions) {
            if (instruction.isNumeric()) {
                val steps = instruction.toInt()

                for (i in 0 until steps) {
                    var newLocation = grid.getForward(location, direction, true)!!
                    while (grid.get(newLocation) == " ") {
                        newLocation = grid.getForward(newLocation, direction, true)!!
                    }
                    // Cannot move
                    if (grid.get(newLocation) == "#") {
                        break
                    }
                    location = newLocation
                }
            } else {
                direction = when (instruction) {
                    "R" -> direction.right()
                    "L" -> direction.left()
                    else -> throw Exception("Unknown direction: $instruction")
                }
            }
        }
        println(getScore(location, direction))
    }

    override fun solve2(lines: List<String>) {
        val (maze, p) = groupLines(lines, "")
        val path = p.first()
        val grid = buildGrid(maze)
        val instructions = parseInstructions(path)
        var location = Location(grid.find(".").first(), Direction.EAST)

        for (instruction in instructions) {
            if (instruction.isNumeric()) {
                val steps = instruction.toInt()
                for (i in 0 until steps) {
                    val newLocation = move(grid, location)
                    if (newLocation != location) {
                        location = newLocation
                    }
                }
            } else {
                val newDirection = when (instruction) {
                    "R" -> location.direction.right()
                    "L" -> location.direction.left()
                    else -> throw Exception("Unknown direction: $instruction")
                }
                location = location.copy(direction = newDirection)
            }
        }

        println(getScore(location.point, location.direction))
    }

    private fun buildGrid(maze: List<String>): MatrixString {
        val width = maze.maxOf { it.length }
        val height = maze.size

        val grid = MatrixString.buildDefault(width, height, " ")
        for (y in maze.indices) {
            val line = maze[y]
            for (x in line.indices) {
                val char = line[x]
                grid.set(x, y, char.toString())
            }
        }
        return grid
    }

    private fun parseInstructions(path: String): List<String> {
        val list = mutableListOf<String>()
        var current = ""
        for (char in path) {
            if (current == "") {
                current += char
            } else {
                if (char.isDigit() && current.isNumeric()) {
                    current += char
                } else {
                    list.add(current)
                    current = "" + char
                }
            }
        }
        list.add(current)
        return list
    }

    private fun getScore(location: Point, direction: Direction): Int {
        val facing = when (direction) {
            Direction.EAST -> 0
            Direction.SOUTH -> 1
            Direction.WEST -> 2
            Direction.NORTH -> 3
        }
        return 1000 * (location.y + 1) + 4 * (location.x + 1) + facing
    }

    private fun move(grid: MatrixString, location: Location): Location {
        val x = location.point.x
        val y = location.point.y
        val direction = location.direction
        var newLocation: Location? = null

        // Map edges to edges and direction to a new direction via its transformation
        // Blocks are 50 x 50
        // Going from left to right, top to bottom for each block and open edge
        if (direction == Direction.NORTH && x in 50 until 100 && y == 0) {
            newLocation = Location(Point(0, x + 100), Direction.EAST)
        }
        if (direction == Direction.NORTH && x in 100 until 150 && y == 0) {
            newLocation = Location(Point(x - 100, 199), Direction.NORTH)
        }
        if (direction == Direction.WEST && x == 50 && y in 0 until 50) {
            newLocation = Location(Point(0, 49 - y + 100), Direction.EAST)
        }
        if (direction == Direction.EAST && x == 149 && y in 0 until 50) {
            newLocation = Location(Point(99, 49 - y + 100), Direction.WEST)
        }
        if (direction == Direction.SOUTH && x in 100 until 150 && y == 49) {
            newLocation = Location(Point(99, x - 50), Direction.WEST)
        }
        if (direction == Direction.WEST && x == 50 && y in 50 until 100) {
            newLocation = Location(Point(y - 50, 100), Direction.SOUTH)
        }
        if (direction == Direction.EAST && x == 99 && y in 50 until 100) {
            newLocation = Location(Point(y + 50, 49), Direction.NORTH)
        }
        if (direction == Direction.NORTH && x in 0 until 50 && y == 100) {
            newLocation = Location(Point(50, x + 50), Direction.EAST)
        }
        if (direction == Direction.WEST && x == 0 && y in 100 until 150) {
            newLocation = Location(Point(50, (49 - (y - 100)) + 100 - 100), Direction.EAST)
        }
        if (direction == Direction.EAST && x == 99 && y in 100 until 150) {
            newLocation = Location(Point(149, (49 - (y - 100)) + 100 - 100), Direction.WEST)
        }
        if (direction == Direction.SOUTH && x in 50 until 100 && y == 149) {
            newLocation = Location(Point(49, x + 100), Direction.WEST)
        }
        if (direction == Direction.WEST && x == 0 && y in 150 until 200) {
            newLocation = Location(Point(y - 100, 0), Direction.SOUTH)
        }
        if (direction == Direction.EAST && x == 49 && y in 150 until 200) {
            newLocation = Location(Point(y - 100, 149), Direction.NORTH)
        }
        if (direction == Direction.SOUTH && x in 0 until 50 && y == 199) {
            newLocation = Location(Point(x + 100, 0), Direction.SOUTH)
        }

        // Can we move across edges?
        if (newLocation != null) {
            if (grid.get(newLocation.point) == "#") {
                return location
            }
            return newLocation
        }

        // Move forward on the map
        val forwardLocation = grid.getForward(location.point, location.direction) ?: return location

        // Cannot move through walls
        if (grid.get(forwardLocation) == "#") {
            return location
        }
        return location.copy(point = forwardLocation)
    }
}

data class Location(val point: Point, val direction: Direction)
