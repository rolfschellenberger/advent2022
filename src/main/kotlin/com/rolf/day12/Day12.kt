package com.rolf.day12

import com.rolf.Day
import com.rolf.util.Matrix
import com.rolf.util.MatrixString
import com.rolf.util.Point
import com.rolf.util.splitLines

fun main() {
    Day12().run()
}

class Day12 : Day() {
    override fun solve1(lines: List<String>) {
        val grid = MatrixString.build(splitLines(lines))
        val start = grid.find("S")
        val end = grid.find("E").first()
        val path = findBestPath(grid, start, end)
        println(path.size)
    }

    override fun solve2(lines: List<String>) {
        val grid = MatrixString.build(splitLines(lines))
        val start = grid.find("S") + grid.find("a")
        val end = grid.find("E").first()
        val path = findBestPath(grid, start, end)
        println(path.size)
    }

    private fun findBestPath(grid: MatrixString, start: List<Point>, end: Point): List<Point> {
        start.forEach { grid.set(it, "a") }
        grid.set(end, "z")

        var bestPath = emptyList<Point>()
        val allowedFunction = { matrix: Matrix<String>, from: Point, to: Point ->
            val f = matrix.get(from).first().code
            val t = matrix.get(to).first().code
            (t - f) <= 1
        }
        for (startPoint in start) {
            val path = grid.findPath(startPoint, setOf(end), customAllowedFunction = allowedFunction)
            if (path.isNotEmpty() && (bestPath.isEmpty() || path.size < bestPath.size)) {
                bestPath = path
            }
        }
        return bestPath
    }
}
