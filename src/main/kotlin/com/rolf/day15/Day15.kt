package com.rolf.day15

import com.rolf.Day
import com.rolf.util.Point

fun main() {
    Day15().run()
}

class Day15 : Day() {
    override fun solve1(lines: List<String>) {
        if(true) return
        val sensorsWithBeacons = lines.map { parse(it) }
        sensorsWithBeacons.forEach { println(it) }
        val sensorsAndBeacons = sensorsWithBeacons.map {
            listOf(it.sensor, it.beacon)
        }.flatten()
        val beacons = sensorsWithBeacons.map { it.beacon }.toSet()

        val y = 2000000
        val minX = sensorsAndBeacons.minOf { it.x } * 4
        val maxX = sensorsAndBeacons.maxOf { it.x } * 4
        println(minX)
        println(maxX)
        var count = 0

        // Calculate distance between sensor and beacon
        // When inspecting a line, calculate the distance from the point to a sensor
        // When this distance is lower than the sensor distance, it cannot contain a beacon
        // When this holds for all sensors, the point cannot contain a beacon
        for (x in minX..maxX) {
            val point = Point(x, y)
            if (beacons.contains(point)) {
                continue
            }
            var canBeBeacon = true
            for (sensorWithBeacon in sensorsWithBeacons) {
                val distance = point.distance(sensorWithBeacon.sensor)
                if (distance <= sensorWithBeacon.distance) {
                    canBeBeacon = false
                    break
                }
            }
            if (!canBeBeacon) count++
        }
        println(count)
        //5716881
    }

    private fun parse(line: String): Sensor {
        val sensor = parseSensor(line)
        val beacon = parseBeacon(line)
        return Sensor(sensor, beacon)
    }

    private fun parseSensor(line: String): Point {
        val parts = line.split(": ").first().split(" ")
        val x = parts[2].split("=")[1].replace(",", "").toInt()
        val y = parts[3].split("=")[1].toInt()
        return Point(x, y)
    }

    private fun parseBeacon(line: String): Point {
        val parts = line.split(": ")[1].split(" ")
        val x = parts[4].split("=")[1].replace(",", "").toInt()
        val y = parts[5].split("=")[1].toInt()
        return Point(x, y)
    }

    override fun solve2(lines: List<String>) {
        val sensorsWithBeacons = lines.map { parse(it) }
        sensorsWithBeacons.forEach { println(it) }
        val sensorsAndBeacons = sensorsWithBeacons.map {
            listOf(it.sensor, it.beacon)
        }.flatten()
        val beacons = sensorsWithBeacons.map { it.beacon }.toSet()

        // the distress beacon must have x and y coordinates each no lower than 0 and no larger than 4000000.
        // Build up set of all points in this area
        // Take every sensor in this area
        // Remove all points that cannot have a beacon from this sensor cannotHaveBeacon(sensor)
        // What is left should be the location

    }

//    private fun canContainBeacon(point:Point) {
//        var canBeBeacon = true
//        for (sensorWithBeacon in sensorsWithBeacons) {
//            val distance = point.distance(sensorWithBeacon.sensor)
//            if (distance <= sensorWithBeacon.distance) {
//                canBeBeacon = false
//                break
//            }
//        }
//    }
}

data class Sensor(val sensor: Point, val beacon: Point) {
    val distance = sensor.distance(beacon)
}
