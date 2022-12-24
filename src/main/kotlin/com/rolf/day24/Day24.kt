package com.rolf.day24

import com.rolf.Day
import com.rolf.util.*
import com.rolf.util.Direction

fun main() {
    Day24().run()
}

class Day24 : Day() {
    override fun solve1(lines: List<String>) {
        val grid = MatrixString.build(splitLines(lines))
        val topLeft = Point(1, 1)
        val bottomRight = Point(grid.width() - 2, grid.height() - 2)
        grid.cutOut(topLeft, bottomRight)
        val blizzards = grid.allPoints()
            .map { grid.get(it) to it }
            .filter { it.first != "." }
            .groupBy { it.first }
            .map { (key, value) -> key to value.map { it.second }.toSet() }
            .toMap()
        println(blizzards)
        val directions = listOf(
            Direction("<", -1, 0),
            Direction(">", 1, 0),
            Direction("^", 0, -1),
            Direction("v", 0, 1)
        )

        val startLocation = Point(0, -1)
        val endLocation = Point(grid.width() - 1, grid.height())
        val startState = State(0, startLocation)
        val queue = ArrayDeque<State>()
        queue.add(startState)

        // Keep track of seen states, to stop when we end up in loops
        val seen = mutableSetOf<State>()
        val lcm = leastCommonMultiple(grid.width().toLong(), grid.height().toLong()).toInt()

        while (queue.isNotEmpty()) {
            val state = queue.removeFirst()
            val newTime = state.time + 1

            val neighbours = grid.getNeighbours(state.location, diagonal = false, includeOwn = true)
            for (neighbour in neighbours) {
                println(neighbour)
                if (neighbour == endLocation) {
                    println(newTime)
                    return
                }

                // Check if none blizzard (in any wind direction) ends up on this location after 'time' steps.
                var isPossibleMove = true
                for (direction in directions) {
                    val blizzardLocations = blizzards.getValue(direction.direction)
                    val x = ((neighbour.x - direction.xDelta) * newTime) % grid.width()
                    val y = ((neighbour.y - direction.yDelta) * newTime) % grid.height()
                    if (blizzardLocations.contains(Point(x, y))) {
                        isPossibleMove = false
                        break
                    }
                }

                // When a move is possible, add this state to the queue
                if (isPossibleMove) {
                    // To make sure we don't end up on a repetitive state, we keep track of the states
                    val key = State(newTime % lcm, neighbour)
                    if (seen.add(key)) {
                        queue.add(State(newTime, neighbour))
                    } else {
                        println("double")
                    }
                }
            }
        }

//
//
//        var blizzards = grid.allPoints()
//            .map { it to grid.get(it) }
//            .map { it.first to getDirection(it.second) }
//        grid.replace(
//            mapOf(
//                "<" to ".",
//                ">" to ".",
//                "^" to ".",
//                "v" to ".",
//            )
//        )
//        printGrid(grid, blizzards)
//
//        // Move blizzards once to create a starting location on (0, 0)
//        blizzards = move(grid, blizzards)
////        printGrid(grid, blizzards)
//        if (grid.get(grid.topLeft()) != ".") throw Exception("Invalid start state")
////        println("------------------------")
//
//        // First build the simulation
//        val time = findPath(grid, blizzards, start, end)
//        println(time + 1)

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

data class State(val time: Int, val location: Point)

data class Direction(val direction: String, val xDelta: Int, val yDelta: Int) {
    fun newPoint(point: Point): Point {
        return Point(point.x + xDelta, point.y + yDelta)
    }
}
