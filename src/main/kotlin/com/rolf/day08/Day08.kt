package com.rolf.day08

import com.rolf.Day
import com.rolf.util.Direction
import com.rolf.util.MatrixInt
import com.rolf.util.Point
import com.rolf.util.splitLines

fun main() {
    Day08().run()
}

class Day08 : Day() {
    override fun solve1(lines: List<String>) {
        val grid = MatrixInt.build(splitLines(lines))
        var visibleCount = 0
        for (y in 1 until grid.height() - 1) {
            for (x in 1 until grid.width() - 1) {
                val visible = isVisible(grid, x, y)
                if (visible) {
                    visibleCount++
                }
            }
        }

        // Add edges
        visibleCount += grid.width()
        visibleCount += grid.width()
        visibleCount += grid.height() - 2
        visibleCount += grid.height() - 2

        println(visibleCount)
    }

    private fun isVisible(grid: MatrixInt, x: Int, y: Int): Boolean {
        // Get value
        val value = grid.get(x, y)
        val point = Point(x, y)

        // Move in all directions and when equal or higher value found, it is not visible in that direction
        val north = getValues(grid, point, Direction.NORTH).maxOf { it }
        val east = getValues(grid, point, Direction.EAST).maxOf { it }
        val south = getValues(grid, point, Direction.SOUTH).maxOf { it }
        val west = getValues(grid, point, Direction.WEST).maxOf { it }
        return north < value || east < value || south < value || west < value
    }

    private fun getValues(grid: MatrixInt, point: Point, direction: Direction): List<Int> {
        val result = mutableListOf<Int>()
        var next = grid.getForward(point, direction)
        while (next != null) {
            result.add(grid.get(next))
            next = grid.getForward(next, direction)
        }
        return result
    }

    override fun solve2(lines: List<String>) {
        val grid = MatrixInt.build(splitLines(lines))
        var maxViewCount = 0
        for (y in 0 until grid.height()) {
            for (x in 0 until grid.width()) {
                val viewCount = getViewCount(grid, x, y)
                maxViewCount = maxOf(maxViewCount, viewCount)
            }
        }
        println(maxViewCount)
    }

    private fun getViewCount(grid: MatrixInt, x: Int, y: Int): Int {
        // Move all directions and count the number of trees of height < ours
        val value = grid.get(x, y)

        val north = getViewCount(grid, Point(x, y), Direction.NORTH, value)
        val east = getViewCount(grid, Point(x, y), Direction.EAST, value)
        val south = getViewCount(grid, Point(x, y), Direction.SOUTH, value)
        val west = getViewCount(grid, Point(x, y), Direction.WEST, value)
        return north * east * south * west
    }

    private fun getViewCount(grid: MatrixInt, point: Point, direction: Direction, value: Int): Int {
        var result = 0
        var next = grid.getForward(point, direction)
        while (next != null) {
            val other = grid.get(next)
            result++
            if (other >= value) {
                return result
            }
            next = grid.getForward(next, direction)
        }
        return result
    }
}
