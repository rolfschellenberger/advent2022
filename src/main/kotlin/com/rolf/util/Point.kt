package com.rolf.util

import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

data class Point(val x: Int, val y: Int) : Comparable<Point> {
    fun rotateRight(xLength: Int, yLength: Int, angleDegrees: Double): Point {
        val radians = Math.toRadians(angleDegrees)
        val centerX = (xLength - 1) / 2.0
        val centerY = (yLength - 1) / 2.0
        val newX = centerX + (x - centerX) * cos(radians) - (y - centerY) * sin(radians);
        val newY = centerY + (x - centerX) * sin(radians) + (y - centerY) * cos(radians);
        return Point(newX.roundToInt(), newY.roundToInt())
    }

    fun distance(other: Point): Int {
        return abs(x - other.x) + abs(y - other.y)
    }

    override fun compareTo(other: Point): Int {
        val yCompare = y.compareTo(other.y)
        if (yCompare != 0) return yCompare
        return x.compareTo(other.x)
    }
}
