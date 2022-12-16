package com.rolf.day16

import com.rolf.Day
import com.rolf.util.Graph
import com.rolf.util.Vertex

fun main() {
    Day16().run()
}

class Day16 : Day() {
    override fun solve1(lines: List<String>) {
        return
        val graph = Graph<Int>()
        for (line in lines) {
            val split = line.split(" ")
            val id = split[1]
            val rate = split[4].split("=")[1].split(";")[0].toInt()
            val next = line.replace(" valves ", " valve ")
                .split(" valve ")[1].split(", ")
//            println(id)
//            println(rate)
//            println(next)
//            println()
            graph.addVertex(Vertex(id, rate))
        }
        for (line in lines) {
            val split = line.split(" ")
            val id = split[1]
            val rate = split[4].split("=")[1].split(";")[0].toDouble()
            val next = line.replace(" valves ", " valve ")
                .split(" valve ")[1].split(", ")
//            println(id)
//            println(rate)
//            println(next)
//            println()
            for (n in next) {
                graph.addEdge(id, n)
            }
        }
        graph.edges().forEach { println(it) }
//        graph.largestCliques().forEach { println(it) }

//        println(graph.lowestPathAndWeightVisitAll(source = "AA"))
//        println(graph.shortestPathAndWeight("HH", "JJ"))

        // List all locations to open
        val toOpen = graph.vertices().filter { it.data!! > 0 }.map { it.id }
//        val toOpen = listOf("DD", "BB", "JJ", "HH", "EE", "CC")
        println("to open: $toOpen")

        println(open(graph, "AA", 0, toOpen, emptyList()))

        // 1913 too low
        // 2087

//        val paths = visitPaths(graph)
//        paths.forEach {
//            println(it)
//        }
    }

    private fun open(
        graph: Graph<Int>,
        location: String,
        time: Int,
        toOpen: List<String>,
        open: List<String>,
        score: Int = 0
    ): Int {
        // No more to score
        if (time > 30) return score

        // First open the valve
        val vertex = graph.getVertex(location)!!
        val openTime = if (vertex.data!! > 0) 1 else 0
        val openScore = if (vertex.data > 0) (30 - time - openTime) * vertex.data else 0
//        val releasingPressure = open
//            .map {
//                graph.getVertex(it)!!
//            }.sumOf {
//                it.data!!
//            }
        if (toOpen.isEmpty()) return score + openScore

        var maxScore = 0
        for (o in toOpen) {
            // Travel to the new location
            val pathAndWeight = graph.shortestPathAndWeight(location, o)
            val travelTime = pathAndWeight.second.toInt()
//            println("From $location to $o length: $travelTime")
//            println("Open $o, time: ${time + travelTime}, score: $openScore")
            maxScore = maxOf(
                maxScore,
                open(graph, o, time + openTime + travelTime, toOpen - o, open + location, score + openScore)
            )
        }
        return maxScore
    }

    override fun solve2(lines: List<String>) {
        val graph = Graph<Int>()
        for (line in lines) {
            val split = line.split(" ")
            val id = split[1]
            val rate = split[4].split("=")[1].split(";")[0].toInt()
            val next = line.replace(" valves ", " valve ")
                .split(" valve ")[1].split(", ")
//            println(id)
//            println(rate)
//            println(next)
//            println()
            graph.addVertex(Vertex(id, rate))
        }
        for (line in lines) {
            val split = line.split(" ")
            val id = split[1]
            val rate = split[4].split("=")[1].split(";")[0].toDouble()
            val next = line.replace(" valves ", " valve ")
                .split(" valve ")[1].split(", ")
//            println(id)
//            println(rate)
//            println(next)
//            println()
            for (n in next) {
                graph.addEdge(id, n)
            }
        }
        graph.edges().forEach { println(it) }
//        graph.largestCliques().forEach { println(it) }

//        println(graph.lowestPathAndWeightVisitAll(source = "AA"))
//        println(graph.shortestPathAndWeight("HH", "JJ"))

        // List all locations to open
        val toOpen = graph.vertices().filter { it.data!! > 0 }.map { it.id }
//        val toOpen = listOf("DD", "BB", "JJ", "HH", "EE", "CC")
        println("to open: $toOpen")

        println(open(graph, "AA", 0, toOpen, emptyList()))

        // 1913 too low
        // 2087

//        val paths = visitPaths(graph)
//        paths.forEach {
//            println(it)
//        }
    }
}
