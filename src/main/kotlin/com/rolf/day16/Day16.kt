package com.rolf.day16

import com.rolf.Day
import com.rolf.util.Graph
import com.rolf.util.Vertex
import com.rolf.util.getPermutations

fun main() {
    Day16().run()
}

class Day16 : Day() {
    override fun solve1(lines: List<String>) {
//        val graph = Graph<Int>()
//        for (line in lines) {
//            val split = line.split(" ")
//            val id = split[1]
//            val rate = split[4].split("=")[1].split(";")[0].toInt()
//            val next = line.replace(" valves ", " valve ")
//                .split(" valve ")[1].split(", ")
////            println(id)
////            println(rate)
////            println(next)
////            println()
//            graph.addVertex(Vertex(id, rate))
//        }
//        for (line in lines) {
//            val split = line.split(" ")
//            val id = split[1]
//            val rate = split[4].split("=")[1].split(";")[0].toDouble()
//            val next = line.replace(" valves ", " valve ")
//                .split(" valve ")[1].split(", ")
////            println(id)
////            println(rate)
////            println(next)
////            println()
//            for (n in next) {
//                graph.addEdge(id, n)
//            }
//        }
//        graph.edges().forEach { println(it) }
////        graph.largestCliques().forEach { println(it) }
//
////        println(graph.lowestPathAndWeightVisitAll(source = "AA"))
////        println(graph.shortestPathAndWeight("HH", "JJ"))
//
//        // List all locations to open
//        val toOpen = graph.vertices().filter { it.data!! > 0 }.map { it.id }
////        val toOpen = listOf("DD", "BB", "JJ", "HH", "EE", "CC")
//        println("to open: $toOpen")
//
//        println(execute(graph, listOf("AA"), listOf("AA"), 0, toOpen, emptySet()))

        // 1913 too low
        // 2087 

//        val paths = visitPaths(graph)
//        paths.forEach {
//            println(it)
//        }
    }

//    private fun open2(graph: Graph<Int>, path: List<String>, time: Int): Int {
//        // We have arrived
//        if (path.size == 1) {
//            val destination = path.first()
//            val vertex = graph.getVertex(destination)!!
//            if (vertex.data!! > 0) {
//                return (30 - time - 1) * vertex.data
//            }
//        }
//
//        // Nothing to open
//        return 0
//    }
//
//    private fun open(
//        graph: Graph<Int>,
//        location: String,
//        elephant: String,
//        time: Int,
//        toOpen: List<String>,
//        open: List<String>,
//        score: Int = 0
//    ): Int {
//        // No more to score
//        if (time > 30) return score
//
//        // First open the valves
//        val vertex = graph.getVertex(location)!!
//        val openTime = if (vertex.data!! > 0) 1 else 0
//        val openScore = if (vertex.data > 0) (30 - time - openTime) * vertex.data else 0
//
//        val vertex2 = graph.getVertex(elephant)!!
//        val openScore2 = if (vertex2.data > 0) (30 - time - openTime) * vertex2.data!! else 0
//
//        if (toOpen.isEmpty()) return score + openScore + openScore2
//
//
//        var maxScore = 0
//        for (o in toOpen) {
//            // Travel to the new location
//            val pathAndWeight = graph.shortestPathAndWeight(location, o)
//            val travelTime = pathAndWeight.second.toInt()
//
//            for (o2 in toOpen - o) {
//                // Travel to the new location
//                val pathAndWeight2 = graph.shortestPathAndWeight(elephant, o2)
//                val travelTime2 = pathAndWeight2.second.toInt()
//                maxScore = maxOf(
//                    maxScore,
//                    open(graph, o, o2, time + openTime + travelTime, toOpen - o, open + location, score + openScore)
//                )
//            }
//        }
//        return maxScore
//    }

    override fun solve2(lines: List<String>) {
        val graph = Graph<Int>()
        for (line in lines) {
            val split = line.split(" ")
            val id = split[1]
            val rate = split[4].split("=")[1].split(";")[0].toInt()
            graph.addVertex(Vertex(id, rate))
        }
        for (line in lines) {
            val split = line.split(" ")
            val id = split[1]
            val next = line.replace(" valves ", " valve ")
                .split(" valve ")[1].split(", ")
            for (n in next) {
                graph.addEdge(id, n)
            }
        }

        // List all locations to open
        val toOpen = graph.vertices().filter { it.data!! > 0 }.map { it.id }.toSet()
//        val toOpen = listOf("DD", "BB", "JJ", "HH", "EE", "CC")
        println("to open: $toOpen")

//        println(getPermutations(listOf("AA", "BB", "CC"), 0))

        println(execute(graph, listOf("AA"), listOf("AA"), 0, toOpen, emptySet()))

    }

    private fun execute(
        graph: Graph<Int>,
        path: List<String>,
        elephant: List<String>,
        time: Int,
        toOpen: Set<String>,
        open: Set<String>,
        score: Int = 0
    ): Int {
        // Make sure we are never done with the open locations
        val newToOpen = if (toOpen.size <= 1) toOpen + "AA" else toOpen

        // No more to score
        if (time > 26) return score

        // Release pressure
        val pressure = open.map { graph.getVertex(it)!! }.sumOf { it.data!! }
//        println("Valves $open are open releasing $pressure pressure")

        // Did we arrive?
        var maxScore = 0
        if (path.size == 1 && elephant.size == 1) {
            val vertex = graph.getVertex(path.first())!!
            val vertexElephant = graph.getVertex(elephant.first())!!
            val data = vertex.data!! + vertexElephant.data!!
            val openTime = if (data > 0) 1 else 0
            for (permutation in getPermutations(newToOpen.toList(), 2)) {
                maxScore = maxOf(
                    maxScore,
                    execute(
                        graph,
                        graph.shortestPathAndWeight(path.first(), permutation[0]).first,
                        graph.shortestPathAndWeight(elephant.first(), permutation[1]).first,
                        time + openTime,
                        newToOpen - permutation[0] - permutation[1],
                        // When both arrived, open both valves
                        open + path.first() + elephant.first(),
                        score + pressure
                    )
                )
            }
        }
        // Only arrived at path
        else if (path.size == 1) {
            val vertex = graph.getVertex(path.first())!!
            val openTime = if (vertex.data!! > 0) 1 else 0
            for (o in newToOpen) {
                maxScore = maxOf(
                    maxScore,
                    execute(
                        graph,
                        graph.shortestPathAndWeight(path.first(), o).first,
                        elephant - elephant.first(),
                        time + openTime,
                        newToOpen - o,
                        open + path.first(),
                        score + pressure
                    )
                )
            }
        }
        // Only arrived at elephant
        else if (elephant.size == 1) {
            val vertexElephant = graph.getVertex(elephant.first())!!
            val openTime = if (vertexElephant.data!! > 0) 1 else 0
            for (o in newToOpen) {
                maxScore = maxOf(
                    maxScore,
                    execute(
                        graph,
                        path - path.first(),
                        graph.shortestPathAndWeight(elephant.first(), o).first,
                        time + openTime,
                        newToOpen - o,
                        open + elephant.first(),
                        score + pressure
                    )
                )
            }
        }
        // Nobody arrived
        else {
            maxScore = maxOf(
                maxScore,
                execute(
                    graph,
                    path - path.first(),
                    elephant - elephant.first(),
                    time + 1,
                    newToOpen,
                    open,
                    score + pressure
                )
            )
        }
        return maxScore
    }
}
