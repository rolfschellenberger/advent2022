package com.rolf.util

import java.util.*
import kotlin.math.abs

open class Matrix<T>(internal val input: MutableList<MutableList<T>>) {

    fun topLeft() = Point(0, 0)
    fun topRight() = Point(width() - 1, 0)
    fun bottomLeft() = Point(0, height() - 1)
    fun bottomRight() = Point(width() - 1, height() - 1)

    fun allElements(): List<T> {
        return input.flatten()
    }

    fun allPoints(): List<Point> {
        val points = mutableListOf<Point>()
        for (y in 0 until height()) {
            for (x in 0 until width()) {
                points.add(Point(x, y))
            }
        }
        return points
    }

    fun height(): Int {
        return input.size
    }

    fun width(): Int {
        if (height() == 0) return 0
        return input[0].size
    }

    fun isOutside(x: Int, y: Int): Boolean {
        return x < 0 || x >= width() || y < 0 || y >= height()
    }

    fun isOutside(point: Point): Boolean {
        return isOutside(point.x, point.y)
    }

    fun getColumns(): List<List<T>> {
        val result = mutableListOf<List<T>>()
        for (x in 0 until width()) {
            result.add(getColumn(x))
        }
        return result
    }

    fun getColumn(x: Int): List<T> {
        val result = mutableListOf<T>()
        for (row in input) {
            result.add(row[x])
        }
        return result
    }

    fun getRows(): List<List<T>> {
        val result = mutableListOf<List<T>>()
        for (y in 0 until height()) {
            result.add(getRow(y))
        }
        return result
    }

    fun getRow(y: Int): List<T> {
        return input[y].toList()
    }

    fun get(x: Int, y: Int): T {
        return input[y][x]
    }

    fun get(point: Point): T {
        return get(point.x, point.y)
    }

    fun set(x: Int, y: Int, value: T) {
        input[y][x] = value
    }

    fun set(point: Point, value: T) {
        set(point.x, point.y, value)
    }

    fun count(value: T): Int {
        return allElements().filter { it == value }.count()
    }

    fun wrap(point: Point): Point {
        val x = (point.x % width() + 2 * width()) % width()
        val y = (point.y % height() + 2 * height()) % height()
        return Point(x, y)
    }

    fun getLeft(point: Point, wrap: Boolean = false): Point? {
        return move(point, -1, 0, wrap)
    }

    fun getRight(point: Point, wrap: Boolean = false): Point? {
        return move(point, 1, 0, wrap)
    }

    fun getUp(point: Point, wrap: Boolean = false): Point? {
        return move(point, 0, -1, wrap)
    }

    fun getDown(point: Point, wrap: Boolean = false): Point? {
        return move(point, 0, 1, wrap)
    }

    fun getLeftUp(point: Point, wrap: Boolean = false): Point? {
        return move(point, -1, -1, wrap)
    }

    fun getLeftDown(point: Point, wrap: Boolean = false): Point? {
        return move(point, -1, 1, wrap)
    }

    fun getRightUp(point: Point, wrap: Boolean = false): Point? {
        return move(point, 1, -1, wrap)
    }

    fun getRightDown(point: Point, wrap: Boolean = false): Point? {
        return move(point, 1, 1, wrap)
    }

    private fun move(point: Point, xDelta: Int, yDelta: Int, wrap: Boolean = false): Point? {
        var x = point.x + xDelta
        var y = point.y + yDelta
        if (wrap) {
            val wrapped = wrap(Point(x, y))
            x = wrapped.x
            y = wrapped.y
        }
        if (x < 0 || x >= width()) return null
        if (y < 0 || y >= height()) return null
        return Point(x, y)
    }

    fun getNeighbours(
        point: Point,
        horizontal: Boolean = true,
        vertical: Boolean = true,
        diagonal: Boolean = true,
        includeOwn: Boolean = false,
        wrap: Boolean = false
    ): Set<Point> {
        val result = mutableSetOf<Point>()
        if (includeOwn) result.add(point)
        if (horizontal) {
            getLeft(point, wrap)?.let { result.add(it) }
            getRight(point, wrap)?.let { result.add(it) }
        }
        if (vertical) {
            getUp(point, wrap)?.let { result.add(it) }
            getDown(point, wrap)?.let { result.add(it) }
        }
        if (diagonal) {
            getLeftUp(point, wrap)?.let { result.add(it) }
            getLeftDown(point, wrap)?.let { result.add(it) }
            getRightUp(point, wrap)?.let { result.add(it) }
            getRightDown(point, wrap)?.let { result.add(it) }
        }
        return result
    }

    fun getArea(topLeftInclusive: Point, bottomRightInclusive: Point): List<Point> {
        val points = mutableListOf<Point>()
        for (y in topLeftInclusive.y..bottomRightInclusive.y) {
            for (x in topLeftInclusive.x..bottomRightInclusive.x) {
                points.add(Point(x, y))
            }
        }
        return points
    }

    fun getOppositePointOverX(x: Int, y: Int, centerX: Int): Point {
        val diff = abs(centerX - x)
        return if (centerX > x) {
            Point(centerX + diff, y)
        } else {
            Point(centerX - diff, y)
        }
    }

    fun getOppositePointOverY(x: Int, y: Int, centerY: Int): Point {
        val diff = abs(centerY - y)
        return if (centerY > y) {
            Point(x, centerY + diff)
        } else {
            Point(x, centerY - diff)
        }
    }

    fun cutOut(topLeftInclusive: Point, bottomRightInclusive: Point) {
        val rows = mutableListOf<MutableList<T>>()
        for (y in topLeftInclusive.y..bottomRightInclusive.y) {
            val row = mutableListOf<T>()
            for (x in topLeftInclusive.x..bottomRightInclusive.x) {
                row.add(get(x, y))
            }
            rows.add(row)
        }
        input.clear()
        input.addAll(rows)
    }

    fun grow(left: Int, right: Int, top: Int, bottom: Int, defaultValue: T) {
        val newMatrix = buildDefault(width() + left + right, height() + top + bottom, defaultValue)

        for (point in allPoints()) {
            newMatrix.set(point.x + left, point.y + top, get(point))
        }

        input.clear()
        input.addAll(newMatrix.input)
    }

    fun replace(map: Map<T, T>) {
        for (point in allPoints()) {
            val value = get(point)
            if (map.containsKey(value)) {
                set(point, map[value]!!)
            }
        }
    }

    fun rotateRight() {
        if (width() != height()) throw Exception("Cannot rotate when dimensions are unequal!")

        val rows = mutableListOf<MutableList<T>>()
        for (x in 0 until width()) {
            rows.add(getColumn(x).reversed().toMutableList())
        }

        input.clear()
        input.addAll(rows)
    }

    fun copy(): Matrix<T> {
        val inputCopy = input.map { it -> it.map { it }.toMutableList() }.toMutableList()
        return Matrix(inputCopy)
    }

    fun toString(separatorElement: String, separatorLine: String): String {
        val builder = StringBuilder()
        for (y in 0 until height()) {
            for (x in 0 until width()) {
                val value = get(x, y)
                builder.append(value)
                builder.append(separatorElement)
            }
            builder.delete(builder.length - separatorElement.length, builder.length)
            builder.append(separatorLine)
        }
        return builder.removeSuffix(separatorLine).toString()
    }

    override fun toString(): String {
        return toString("", "\n")
    }

    companion object {
        fun <T> buildDefault(width: Int, height: Int, defaultValue: T): Matrix<T> {
            val rows = mutableListOf<MutableList<T>>()
            for (y in 0 until height) {
                val row = mutableListOf<T>()
                for (x in 0 until width) {
                    row.add(defaultValue)
                }
                rows.add(row)
            }
            return Matrix(rows)
        }
    }
}

open class MatrixString(input: MutableList<MutableList<String>>) : Matrix<String>(input) {
    companion object {
        fun buildDefault(width: Int, height: Int, defaultValue: String): MatrixString {
            return MatrixString(Matrix.buildDefault(width, height, defaultValue).input)
        }

        fun build(input: List<List<String>>): MatrixString {
            return MatrixString(input.map { it.toMutableList() }.toMutableList())
        }
    }
}

open class MatrixInt(input: MutableList<MutableList<Int>>) : Matrix<Int>(input) {

    fun shortestPath(from: Point, to: Point, diagonal: Boolean = false, prioritizeByDistance: Boolean = false): Int {
        // Start from 0 at the starting point
        set(from, 0)

        val compareBySteps: Comparator<Pair<Int, Point>> = compareBy { it.first }
        val compareByDistanceToEnd: Comparator<Pair<Int, Point>> = compareBy { it.second.distance(to) }
        val priorityQueue = PriorityQueue(if (prioritizeByDistance) compareByDistanceToEnd else compareBySteps)
        priorityQueue.add(get(from) to from)

        while (priorityQueue.isNotEmpty()) {
            val next = priorityQueue.remove()
            val minSteps = next.first
            val location = next.second

            if (location == to) {
                return minSteps
            }

            // Push the neighbours to the steps queue
            for (neighbour in getNeighbours(location, diagonal = diagonal)) {
                if (get(neighbour) > minSteps) {
                    set(neighbour, minSteps + 1)
                    priorityQueue.add(minSteps + 1 to neighbour)
                }
            }
        }
        throw Exception("No shortest path found")
    }

    companion object {
        fun buildDefault(width: Int, height: Int, defaultValue: Int): MatrixInt {
            return MatrixInt(Matrix.buildDefault(width, height, defaultValue).input)
        }

        fun build(input: List<List<String>>): MatrixInt {
            return MatrixInt(input.map { list -> list.map { it.toInt() }.toMutableList() }.toMutableList())
        }

        fun buildForShortestPath(matrix: MatrixString, wallValue: String): MatrixInt {
            // Every field will get the maximum penalty to find the shortest path
            val result = buildDefault(matrix.width(), matrix.height(), Int.MAX_VALUE)
            // The wall values are going to be replaced with the lowest value, so they will not be picked during
            // the shortest path traversal
            for (point in matrix.allPoints()) {
                if (matrix.get(point) == wallValue) {
                    result.set(point, Int.MIN_VALUE)
                }
            }
            return result
        }
    }
}

open class MatrixLong(input: MutableList<MutableList<Long>>) : Matrix<Long>(input) {
    companion object {
        fun buildDefault(width: Int, height: Int, defaultValue: Long): MatrixLong {
            return MatrixLong(Matrix.buildDefault(width, height, defaultValue).input)
        }

        fun build(input: List<List<String>>): MatrixLong {
            return MatrixLong(input.map { list -> list.map { it.toLong() }.toMutableList() }.toMutableList())
        }
    }
}
