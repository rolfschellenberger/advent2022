package com.rolf.day08

import com.rolf.Day
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
        val upValue = getUpValues(grid, point).maxOf { it }
        val downValue = getDownValues(grid, point).maxOf { it }
        val leftValue = getLeftValues(grid, point).maxOf { it }
        val rightValue = getRightValues(grid, point).maxOf { it }
        return upValue < value || downValue < value || leftValue < value || rightValue < value
    }

    private fun getUpValues(grid: MatrixInt, point: Point): List<Int> {
        val result = mutableListOf<Int>()
        var up = grid.getUp(point)
        while (up != null) {
            val other = grid.get(up)
            result.add(other)
            up = grid.getUp(up)
        }
        return result
    }

    private fun getDownValues(grid: MatrixInt, point: Point): List<Int> {
        val result = mutableListOf<Int>()
        var up = grid.getDown(point)
        while (up != null) {
            val other = grid.get(up)
            result.add(other)
            up = grid.getDown(up)
        }
        return result
    }

    private fun getLeftValues(grid: MatrixInt, point: Point): List<Int> {
        val result = mutableListOf<Int>()
        var up = grid.getLeft(point)
        while (up != null) {
            val other = grid.get(up)
            result.add(other)
            up = grid.getLeft(up)
        }
        return result
    }

    private fun getRightValues(grid: MatrixInt, point: Point): List<Int> {
        val result = mutableListOf<Int>()
        var up = grid.getRight(point)
        while (up != null) {
            val other = grid.get(up)
            result.add(other)
            up = grid.getRight(up)
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

        val upCount = getViewCountUp(grid, Point(x, y), value)
        val downCount = getViewCountDown(grid, Point(x, y), value)
        val leftCount = getViewCountLeft(grid, Point(x, y), value)
        val rightCount = getViewCountRight(grid, Point(x, y), value)
        return upCount * downCount * leftCount * rightCount
    }

    private fun getViewCountUp(grid: MatrixInt, point: Point, value: Int): Int {
        var result = 0
        var up = grid.getUp(point)
        while (up != null) {
            val other = grid.get(up)
            result++
            if (other >= value) {
                return result
            }
            up = grid.getUp(up)
        }
        return result
    }

    private fun getViewCountDown(grid: MatrixInt, point: Point, value: Int): Int {
        var result = 0
        var up = grid.getDown(point)
        while (up != null) {
            val other = grid.get(up)
            result++
            if (other >= value) {
                return result
            }
            up = grid.getDown(up)
        }
        return result
    }

    private fun getViewCountLeft(grid: MatrixInt, point: Point, value: Int): Int {
        var result = 0
        var up = grid.getLeft(point)
        while (up != null) {
            val other = grid.get(up)
            result++
            if (other >= value) {
                return result
            }
            up = grid.getLeft(up)
        }
        return result
    }

    private fun getViewCountRight(grid: MatrixInt, point: Point, value: Int): Int {
        var result = 0
        var up = grid.getRight(point)
        while (up != null) {
            val other = grid.get(up)
            result++
            if (other >= value) {
                return result
            }
            up = grid.getRight(up)
        }
        return result
    }
}
