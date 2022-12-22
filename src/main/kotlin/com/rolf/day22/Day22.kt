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
//        println(path)
//        println(grid)

        val instructions = parseInstructions(path)
//        println(instructions)

        var location = grid.find(".").first()
//        println(location)

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
//                println("$steps -> $location")
//                printGrid(grid, location)
            } else {
//                println(instruction)
                direction = when (instruction) {
                    "R" -> direction.right()
                    "L" -> direction.left()
                    else -> throw Exception("Unknown direction: $instruction")
                }
//                println(direction)
            }
        }
//        println(location)
        val facing = when (direction) {
            Direction.EAST -> 0
            Direction.SOUTH -> 1
            Direction.WEST -> 2
            Direction.NORTH -> 3
        }
        println(1000 * (location.y + 1) + 4 * (location.x + 1) + facing)

    }

    private fun buildGrid(maze: List<String>): MatrixString {
        val width = maze.maxOf { it.length }
        val height = maze.size

        val grid = MatrixString.buildDefault(width, height, " ")
        for (y in maze.indices) {
            val line = maze.get(y)
            for (x in line.indices) {
                var char = line.get(x)
                grid.set(x, y, char.toString())
            }
        }
        return grid
    }

    private fun printGrid(grid: MatrixString, location: Point) {
        val print = grid.copy()
        print.set(location, "@")
        println(print)
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

    override fun solve2(lines: List<String>) {
        // What options do we have?

        // Find out when you walk off the map or into the void, how to translate your point
        // to the right location. That's hard, because you don't know if you should wrap or transform.

        // Split map into 6 smaller maps
        // Know what is your neighbour on 4 sides
        // When walking off the grid, continue to the next on the right location.
        // This will require rotating the location somehow?

        // Go manual: draw until a movement leads to a void
        // Manually determine the new location
        // This is actually mapping the movement locations
        val (maze, p) = groupLines(lines, "")
        val path = p.first()
        val grid = buildGrid(maze)
//        println(path)
//        println(grid)

        val instructions = parseInstructions(path)
//        println(instructions)

        var location = grid.find(".").first()
//        println(location)

        var direction = Direction.EAST
        for (instruction in instructions) {
            if (instruction.isNumeric()) {
                val steps = instruction.toInt()

                for (i in 0 until steps) {
                    var newLocation = grid.getForward(location, direction)
                    if (newLocation == null || grid.get(newLocation) == " ") {
                        println("Void: $direction: $location")
                        printGrid(grid, location)
                        return
                    }
                    // Cannot move
                    else if (grid.get(newLocation) != "#") {
                        location = newLocation
                    }
                }
//                println("$steps -> $location")
//                printGrid(grid, location)
            } else {
//                println(instruction)
                direction = when (instruction) {
                    "R" -> direction.right()
                    "L" -> direction.left()
                    else -> throw Exception("Unknown direction: $instruction")
                }
//                println(direction)
            }
        }
//        println(location)
        val facing = when (direction) {
            Direction.EAST -> 0
            Direction.SOUTH -> 1
            Direction.WEST -> 2
            Direction.NORTH -> 3
        }
        println(1000 * (location.y + 1) + 4 * (location.x + 1) + facing)

    }
}

data class Map<T, U>(val grid: MatrixString, val connections: Map<Direction, MatrixString>)
