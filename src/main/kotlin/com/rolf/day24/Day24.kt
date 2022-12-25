package com.rolf.day24

import com.rolf.Day
import com.rolf.util.MatrixString
import com.rolf.util.Point
import com.rolf.util.leastCommonMultiple
import com.rolf.util.splitLines

fun main() {
    Day24().run()
}

class Day24 : Day() {

    private val directions = listOf(
        Direction("<", -1, 0),
        Direction(">", 1, 0),
        Direction("^", 0, -1),
        Direction("v", 0, 1)
    )

    override fun solve1(lines: List<String>) {
        val grid = parseGrid(lines)
        val blizzards = findBlizzards(grid)

        val startLocation = Point(0, -1)
        val endLocation = grid.bottomRight()
        println(getTime(grid, blizzards, startLocation, endLocation))
    }

    override fun solve2(lines: List<String>) {
        val grid = parseGrid(lines)
        val blizzards = findBlizzards(grid)

        var time = 0
        time = getTime(grid, blizzards, Point(0, -1), grid.bottomRight(), time)
        time = getTime(grid, blizzards, Point(grid.width() - 1, grid.height()), grid.topLeft(), time)
        time = getTime(grid, blizzards, Point(0, -1), grid.bottomRight(), time)
        println(time)
    }

    private fun parseGrid(lines: List<String>): MatrixString {
        val grid = MatrixString.build(splitLines(lines))
        val topLeft = Point(1, 1)
        val bottomRight = Point(grid.width() - 2, grid.height() - 2)
        grid.cutOut(topLeft, bottomRight)
        return grid
    }

    private fun findBlizzards(grid: MatrixString): Map<String, List<Point>> {
        return grid.allPoints()
            .map { grid.get(it) to it }
            .filter { it.first != "." }
            .groupBy { it.first }
            .map { (key, value) -> key to value.map { it.second } }
            .toMap()
    }

    private fun getTime(
        grid: MatrixString,
        blizzards: Map<String, List<Point>>,
        startLocation: Point,
        endLocation: Point,
        time: Int = 0
    ): Int {
        val startState = State(time, startLocation)
        val queue = ArrayDeque<State>()
        queue.add(startState)

        // Keep track of seen states, to stop when we end up in loops
        val seen = mutableSetOf<State>()
        val lcm = leastCommonMultiple(grid.width().toLong(), grid.height().toLong()).toInt()

        while (queue.isNotEmpty()) {
            val state = queue.removeFirst()
            val newTime = state.time + 1

            // Found the destination?
            if (state.location == endLocation) {
                return newTime
            }

            val neighbours = grid.getNeighbours(state.location, diagonal = false) + state.location
            for (neighbour in neighbours) {
                // Check if none blizzard (in any wind direction) ends up on this location after 'time' steps.
                var isPossibleMove = true
                for (direction in directions) {
                    val blizzardLocations = blizzards.getValue(direction.direction)
                    val x =
                        ((neighbour.x - ((direction.xDelta * newTime) % grid.width())) + grid.width()) % grid.width()
                    val y =
                        ((neighbour.y - ((direction.yDelta * newTime) % grid.height())) + grid.height()) % grid.height()
                    if (blizzardLocations.contains(Point(x, y))) {
                        isPossibleMove = false
                        break
                    }
                }

                // When a move is possible, add this state to the queue
                if (isPossibleMove || neighbour == startLocation) {
                    // To make sure we don't end up on a repetitive state, we keep track of the states
                    val key = State(newTime % lcm, neighbour)
                    if (seen.add(key)) {
                        queue.add(State(newTime, neighbour))
                    }
                }
            }
        }

        // No route found
        return -1
    }
}

data class State(val time: Int, val location: Point)

data class Direction(val direction: String, val xDelta: Int, val yDelta: Int)
