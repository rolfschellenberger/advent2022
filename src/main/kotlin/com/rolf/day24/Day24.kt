package com.rolf.day24

import com.rolf.Day
import com.rolf.util.Direction
import com.rolf.util.MatrixString
import com.rolf.util.Point
import com.rolf.util.splitLines

fun main() {
    Day24().run()
}

class Day24 : Day() {
    override fun solve1(lines: List<String>) {
        val grid = MatrixString.build(splitLines(lines))
        val topLeft = Point(1, 1)
        val bottomRight = Point(grid.width() - 2, grid.height() - 2)
        grid.cutOut(topLeft, bottomRight)
        val start = grid.topLeft()
        val end = grid.bottomRight()
//        val blizzards = grid.allPoints()
//            .map { grid.get(it) to it }
//            .filter { it.first != "." }
//            .toMap()



        var blizzards = grid.allPoints()
            .map { it to grid.get(it) }
            .map { it.first to getDirection(it.second) }
        grid.replace(
            mapOf(
                "<" to ".",
                ">" to ".",
                "^" to ".",
                "v" to ".",
            )
        )
        printGrid(grid, blizzards)

        // Move blizzards once to create a starting location on (0, 0)
        blizzards = move(grid, blizzards)
//        printGrid(grid, blizzards)
        if (grid.get(grid.topLeft()) != ".") throw Exception("Invalid start state")
//        println("------------------------")

        // First build the simulation
        val time = findPath(grid, blizzards, start, end)
        println(time + 1)

//        for (i in 0 until 4) {
//            location
//            if (location == end) {
//                break
//            }
//
//            // Draw grid
//            val state = buildGrid(grid, blizzards)
//            println("location: $location")
//            println(state)
//            // Can we move?
//            val neighbours = state.getNeighbours(location, diagonal = false)
//                .filter { state.get(it) == "." }
//            for (neighbour in neighbours) {
//                println(neighbour)
//            }
//
//            // Move blizzards
//            blizzards = move(grid, blizzards)
//            println()
//        }
    }

    private fun findPath(
        grid: MatrixString,
        blizzards: List<Pair<Point, Direction>>,
        location: Point,
        end: Point,
        path: List<Point> = listOf(location),
        states: Set<String> = setOf(),
        minute: Int = 1,
        cache: MutableMap<Pair<Point, List<Pair<Point, Direction>>>, Int> = mutableMapOf()
    ): Int {
        if (location == end) {
            println("Reached the end in $minute minutes")
            return minute
        }

        val currentState = buildGrid(grid, blizzards)
//        println("minute: $minute, location: $location")
        currentState.set(location, "E")
//        println(currentState)
//        println()
        val key = location to blizzards
        if (cache.containsKey(key)) {
            return maxOf(cache.getValue(key), minute + cache.getValue(key))
        }
        // Dead end
        if (states.contains(currentState.toString())) {
            return Int.MAX_VALUE
        }

        // Let the blizzards move
        val newBlizzards = move(grid, blizzards)
        val newState = buildGrid(grid, newBlizzards)
        newState.set(location, "E")
//        println(newState)
//        println("---------------------")


        // Can we move?
        val neighbours = newState.getNeighbours(location, diagonal = false)
            .filter { newState.get(it) == "." }
        // No neighbours, wait
        if (neighbours.isEmpty()) {
//            println("Do not move")
            return findPath(
                grid,
                newBlizzards,
                location,
                end,
                path,
                states + currentState.toString(),
                minute + 1,
                cache
            )
        }

        var bestTime = Int.MAX_VALUE
        for (neighbour in neighbours) {
//            println("Move to: $neighbour")
            val newTime = findPath(
                grid,
                newBlizzards,
                neighbour,
                end,
                path + neighbour,
                states + currentState.toString(),
                minute + 1,
                cache
            )
            bestTime = minOf(bestTime, newTime)
        }
        cache[key] = bestTime
        return bestTime
    }

    private fun move(grid: MatrixString, blizzards: List<Pair<Point, Direction>>): List<Pair<Point, Direction>> {
        return blizzards.map {
            grid.getForward(it.first, it.second, wrap = true)!! to it.second
        }
    }

    private fun getDirection(value: String): Direction {
        return when (value) {
            "<" -> Direction.WEST
            ">" -> Direction.EAST
            "^" -> Direction.NORTH
            "v" -> Direction.SOUTH
            else -> throw Exception("Unknown direction $value")
        }
    }

    private fun buildGrid(
        grid: MatrixString,
        blizzards: List<Pair<Point, Direction>>
    ): MatrixString {
        val copy = grid.copy()
        blizzards.forEach {
            copy.set(it.first, getArrow(it.second))
        }
        return copy
    }

    private fun printGrid(
        grid: MatrixString,
        blizzards: List<Pair<Point, Direction>>
    ) {
        println(buildGrid(grid, blizzards))
        println()
    }

    private fun getArrow(direction: Direction): String {
        return when (direction) {
            Direction.WEST -> "<"
            Direction.EAST -> ">"
            Direction.NORTH -> "^"
            Direction.SOUTH -> "v"
            else -> throw Exception("Unknown direction $direction")
        }
    }

    override fun solve2(lines: List<String>) {
    }
}
