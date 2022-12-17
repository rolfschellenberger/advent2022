package com.rolf.day16

import com.rolf.Day
import com.rolf.util.Graph
import com.rolf.util.Vertex
import com.rolf.util.getCombinations

fun main() {
    Day16().run()
}

class Day16 : Day() {
    private val cache = mutableMapOf<String, Int>()

    override fun solve1(lines: List<String>) {
        val graph = parseGraph(lines)

        // List all locations to open
        val toOpen = graph.vertices().filter { it.data!! > 0 }.map { it.id }.toSet()

        // Open the valves to release maximum pressure
        println(open(graph, "AA", 0, toOpen, emptySet(), 30))
        // 2087
    }

    override fun solve2(lines: List<String>) {
        val graph = parseGraph(lines)

        // List all locations to open
        val toOpen = graph.vertices().filter { it.data!! > 0 }.map { it.id }

        val combinations = getCombinations(toOpen)
        val combinationCache = mutableMapOf<Set<String>, Int>()
        combinationCache[emptySet()] = 0

        for ((index, combination) in combinations.withIndex()) {
            val score = open(graph, "AA", 0, combination.toSet(), emptySet(), maxTime = 26)
            combinationCache[combination.toSet()] = score
            println("$index / ${combinations.size} [${combination.size}]")
        }

        // When all combinations are calculated, try to combine the two most optimal
        var maxScore = 0
        for (combination in combinations) {
            val other = toOpen.toSet() - combination.toSet()
            val left = combinationCache.getValue(combination.toSet())
            val right = combinationCache.getValue(other)
            val sum = left + right
//            println("$combination and $other: $left + $right = $sum")
            maxScore = maxOf(maxScore, sum)
        }
        println(maxScore)
    }

    private fun parseGraph(lines: List<String>): Graph<Int> {
        val caves = lines.map { parse(it) }
        val graph = Graph<Int>()
        for (cave in caves) {
            graph.addVertex(Vertex(cave.id, cave.rate))
        }
        for (cave in caves) {
            for (child in cave.children) {
                graph.addEdge(cave.id, child)
            }
        }
        return graph
    }

    private fun parse(line: String): Cave {
        val split = line.split(" ")
        val id = split[1]
        val rate = split[4].split("=")[1].split(";")[0].toInt()
        val next = line.replace(" valves ", " valve ")
            .split(" valve ")[1].split(", ")
        return Cave(id, rate, next)
    }

    private fun open(
        graph: Graph<Int>,
        location: String,
        time: Int,
        toOpen: Set<String>,
        open: Set<String>,
        maxTime: Int,
        score: Int = 0
    ): Int {
        // No more to score
        if (time > maxTime) return score

        // Cache hit?
        val key = listOf(
            location,
            time,
            toOpen
        ).joinToString("-")
        if (cache.containsKey(key)) {
            return score + cache.getValue(key)
        }

        // First open the valve
        val vertex = graph.getVertex(location)!!
        val openTime = if (vertex.data!! > 0) 1 else 0
        val openScore = if (vertex.data > 0) (maxTime - time - openTime) * vertex.data else 0
        if (toOpen.isEmpty()) return score + openScore

        var maxScore = 0
        for (o in toOpen) {
            // Travel to the new location
            val pathAndWeight = graph.shortestPathAndWeight(location, o)
            val travelTime = pathAndWeight.second.toInt()

            maxScore = maxOf(
                maxScore,
                open(graph, o, time + openTime + travelTime, toOpen - o, open + location, maxTime, score + openScore)
            )
        }
        cache[key] = maxScore - score
        return maxScore
    }
}

data class Cave(val id: String, val rate: Int, val children: List<String>)
