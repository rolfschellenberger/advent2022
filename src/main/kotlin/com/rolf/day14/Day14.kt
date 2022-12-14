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
        println(runSimulation(lines))
    }

    override fun solve2(lines: List<String>) {
        println(runSimulation(lines, withFloor = true))
    }

    private fun runSimulation(lines: List<String>, withFloor: Boolean = false): Int {
        val grid = MatrixString.buildDefault(1000, 1000, ".")
        for (line in lines) {
            val points = parse(line)
            for (i in 0 until points.size - 1) {
                val from = points[i]
                val to = points[i + 1]
                drawSand(grid, from, to)
            }
        }

        if (withFloor) {
            drawFloor(grid)
        }

        simulateSandDrop(grid, Point(500, 0))
        return grid.find("o").size
    }

    private fun parse(line: String): List<Point> {
        return splitLine(line, " -> ")
            .map {
                val (x, y) = it.split(",").map { part -> part.toInt() }
                Point(x, y)
            }
    }

    private fun drawSand(grid: MatrixString, from: Point, to: Point) {
        val path = grid.findPath(from, to) + from
        for (point in path) {
            grid.set(point, "#")
        }
    }

    private fun drawFloor(grid: MatrixString) {
        val floorY = grid.find("#").maxOf { it.y } + 2
        for (x in 0 until grid.width()) {
            grid.set(x, floorY, "#")
        }
    }

    private fun simulateSandDrop(grid: MatrixString, start: Point) {
        val lowestPoint = grid.find("#").maxOf { it.y }

        var stopPoint = start
        while (stopPoint.y <= lowestPoint) {
            stopPoint = dropSand(grid, start)
            // When no sand can be dropped from the starting point, break
            if (stopPoint == start) {
                break
            }
        }
    }

    private fun dropSand(grid: MatrixString, start: Point): Point {
        var nextPos = stepDown(grid, start)
        var lastPos = start
        while (nextPos != null) {
            lastPos = nextPos
            nextPos = stepDown(grid, nextPos)
        }
        // Don't draw on the bottom
        if (lastPos.y < grid.bottomLeft().y) {
            grid.set(lastPos, "o")
        }
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
}
