package com.rolf.day18

import com.rolf.Day
import com.rolf.util.Location

fun main() {
    Day18().run()
}

class Day18 : Day() {
    override fun solve1(lines: List<String>) {
        val locations = lines.map { parse(it) }.toSet()
        var sides = 0L
        for (location in locations) {
            val neighbours = location.getNeighbours()
            for (neighbour in neighbours) {
                if (!locations.contains(neighbour)) {
                    sides++
                }
            }
        }
        println(sides)
    }

    override fun solve2(lines: List<String>) {
        val locations = lines.map { parse(it) }.toSet()
        val outside = Location(0, 0, 0)
        val trappedLocations = mutableSetOf<Location>()
        val paths = mutableMapOf<Location, List<Location>>()

        var surface = 0L
        for ((index, location) in locations.withIndex()) {
            print("$index / ${locations.size}\r")
            // Every location, check his neighbours out and if you can travel from there to the outside
            // If so, than that's a side (neighbour) that is exposed to lava
            for (neighbour in location.getNeighbours()) {
                // The neighbour location should be an open space
                if (!locations.contains(neighbour)) {
                    // And this location should not be trapped (speed improvement only)
                    if (!trappedLocations.contains(neighbour)) {
                        // See if you can travel to the outside of the bubble without hitting other locations
                        val path =
                            if (paths.containsKey(neighbour)) paths.getValue(neighbour)
                            else neighbour.findPath(outside, locations + trappedLocations)
                        paths[neighbour] = path
                        if (path.isNotEmpty()) {
                            surface++
                        } else {
                            trappedLocations.add(neighbour)
                        }
                    }
                }
            }
        }
        println(surface)
    }

    private fun parse(line: String): Location {
        val (x, y, z) = line.split(",").map { it.toInt() }
        return Location(x, y, z)
    }
}
