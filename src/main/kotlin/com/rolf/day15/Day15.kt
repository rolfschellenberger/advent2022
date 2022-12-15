package com.rolf.day15

import com.rolf.Day
import com.rolf.util.Point
import java.util.regex.Pattern
import kotlin.math.absoluteValue

fun main() {
    Day15().run()
}

class Day15 : Day() {
    override fun solve1(lines: List<String>) {
        val sensorsWithBeacons = lines.map { parse(it) }
        val y = 2000000
        val range = getRange(y, sensorsWithBeacons).first()
        // Remove the existing beacons from the range
        val beaconsX = sensorsWithBeacons.map { it.beacon }
            .filter { it.y == y }
            .map { it.x }
        val locations = range.toSet() - beaconsX.toSet()
        println(locations.size)
    }

    override fun solve2(lines: List<String>) {
        val sensorsWithBeacons = lines.map { parse(it) }
        val maxY = 4000000
        val range = scan(sensorsWithBeacons, 0, maxY)
        val y = range.first
        val x = range.second.first().last + 1
        println(x * maxY.toLong() + y)
    }

    private fun parse(line: String): Sensor {
        val regex = Pattern.compile("[^\\-0-9]+").toRegex()
        val clean = line.replace(regex, " ").trim()
        val (sx, sy, bx, by) = clean.split(" ").map { it.toInt() }
        return Sensor(Point(sx, sy), Point(bx, by))
    }

    private fun scan(sensorsWithBeacons: List<Sensor>, yStart: Int, yEnd: Int): Pair<Int, List<IntRange>> {
        // For each y, see what range is covered by each sensor
        for (y in yStart..yEnd) {
            val range = getRange(y, sensorsWithBeacons)
            // If this range is not 1 range, there must be a location where hidden beacon can be found
            if (range.size > 1) {
                return y to range
            }
        }
        throw Exception("No broken range found from $yStart till $yEnd")
    }

    private fun getRange(y: Int, sensorsWithBeacons: List<Sensor>): List<IntRange> {
        val intervals = mutableListOf<IntRange>()
        for (sensorWithBeacon in sensorsWithBeacons) {
            val distance = sensorWithBeacon.distance
            val offset = distance - (sensorWithBeacon.sensor.y - y).absoluteValue
            if (offset < 0) continue
            val lowX = sensorWithBeacon.sensor.x - offset
            val highX = sensorWithBeacon.sensor.x + offset
            intervals.add(lowX..highX)
        }
        intervals.sortWith(IntRangeComparator())

        // Merge intervals
        val range = mutableListOf<IntRange>()
        for (interval in intervals) {
            if (range.isEmpty()) {
                range.add(interval)
                continue
            }

            val last = range.last()

            if (interval.first > last.last + 1) {
                range.add(interval)
                continue
            }

            // Extend last range
            val newInterval = last.first..maxOf(last.last, interval.last)
            range.removeLast()
            range.add(newInterval)
        }
        return range
    }
}

data class Sensor(val sensor: Point, val beacon: Point) {
    val distance = sensor.distance(beacon)
    override fun toString(): String {
        return "Sensor(sensor=$sensor, beacon=$beacon, distance=$distance)"
    }
}

class IntRangeComparator : Comparator<IntRange> {
    override fun compare(o1: IntRange, o2: IntRange): Int {
        return o1.first.compareTo(o2.first)
    }
}
