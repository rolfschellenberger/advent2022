package com.rolf.day14

import com.rolf.Day
import com.rolf.util.MatrixString
import com.rolf.util.Point
import com.rolf.util.splitLine

fun main() {
    Day14().run()
}

class Day14 : Day() {
    override fun solve1(lines: List<String>) {
        val grid = MatrixString.buildDefault(530, 200, ".")
        for (line in lines) {
            val points = parse(line)
//            println(points)
            for (i in 0 until points.size - 1) {
                val from = points[i]
                val to = points[i + 1]
                drawSand(grid, from, to)
            }
        }
//        println(grid.find("#").maxOf { it.x })
//        println(grid.find("#").maxOf { it.y })
//        println(grid)

        simulateSand(grid, Point(500, 0))
//        println(grid)
        println(grid.find("o").size - 1)
    }

    private fun simulateSand(grid: MatrixString, start: Point) {
        val lowestPoint = grid.find("#").maxOf { it.y }
//        println(lowestPoint)

        var stopPoint = start
        while (stopPoint.y <= lowestPoint) {
            stopPoint = dropSand(grid, start)
        }
    }

    private fun dropSand(grid: MatrixString, start: Point): Point {
        var nextPos = stepDown(grid, start)
        var lastPos = start
        while (nextPos != null) {
            lastPos = nextPos
            nextPos = stepDown(grid, nextPos)
        }
        grid.set(lastPos, "o")
        return lastPos
    }

    private fun stepDown(grid: MatrixString, from: Point): Point? {
        val down = grid.getDown(from)
        if (down != null && grid.get(down) == ".") return down
        val leftDown = grid.getLeftDown(from)
        if (leftDown != null && grid.get(leftDown) == ".") return leftDown
        val leftRight = grid.getRightDown(from)
        if (leftRight != null && grid.get(leftRight) == ".") return leftRight
        return null
    }

    private fun drawSand(grid: MatrixString, from: Point, to: Point) {
        val path = grid.findPath(from, to) + from
        for (point in path) {
            grid.set(point, "#")
        }
    }

    private fun parse(line: String): List<Point> {
        return splitLine(line, " -> ")
            .map {
                val (x, y) = it.split(",").map { part -> part.toInt() }
                Point(x, y)
            }
    }

    override fun solve2(lines: List<String>) {
    }
}
