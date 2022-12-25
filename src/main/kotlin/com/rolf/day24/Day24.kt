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
    override fun solve1(lines: List<String>) {
        val grid = MatrixString.build(splitLines(lines))
        val topLeft = Point(1, 1)
        val bottomRight = Point(grid.width() - 2, grid.height() - 2)
        grid.cutOut(topLeft, bottomRight)

        val blizzards = grid.allPoints()
            .map { grid.get(it) to it }
            .filter { it.first != "." }
            .groupBy { it.first }
            .map { (key, value) -> key to value.map { it.second } }
            .toMap()
        val directions = listOf(
            Direction("<", -1, 0),
            Direction(">", 1, 0),
            Direction("^", 0, -1),
            Direction("v", 0, 1)
        )

        val startLocation = Point(0, -1)
        val endLocation = grid.bottomRight()
        val startState = State(0, startLocation)
        val queue = ArrayDeque<State>()
        queue.add(startState)

        // Keep track of seen states, to stop when we end up in loops
        val seen = mutableSetOf<State>()
        val lcm = leastCommonMultiple(grid.width().toLong(), grid.height().toLong()).toInt()

        while (queue.isNotEmpty()) {
            val state = queue.removeFirst()
            val newTime = state.time + 1
//            println("Minute: $newTime (${state.location.x}, ${state.location.y})")

            if (state.location == endLocation) {
                println(newTime)
                return
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
                if (isPossibleMove) {
                    // To make sure we don't end up on a repetitive state, we keep track of the states
                    val key = State(newTime % lcm, neighbour)
                    if (seen.add(key)) {
                        queue.add(State(newTime, neighbour))
                    }
                }
            }
        }
    }

    override fun solve2(lines: List<String>) {
    }
}

data class State(val time: Int, val location: Point)

data class Direction(val direction: String, val xDelta: Int, val yDelta: Int)
