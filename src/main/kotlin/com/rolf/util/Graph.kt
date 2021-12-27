package com.rolf.util

open class Graph<T> {

    private val vertices = mutableMapOf<String, Vertex<T>>()
    private val edges = mutableMapOf<String, MutableSet<Edge<T>>>()

    fun addVertex(vertex: Vertex<T>) {
        vertices[vertex.id] = vertex
    }

    fun getVertex(id: String): Vertex<T>? {
        return vertices[id]
    }

    fun hasVertex(id: String): Boolean {
        return vertices.containsKey(id)
    }

    fun vertices(): List<Vertex<T>> {
        return vertices.map { it.value }
    }

    fun addEdge(source: String, destination: String, edgeType: EdgeType = EdgeType.DIRECTED, weight: Double = 1.0) {
        addDirectedEdge(source, destination, weight)
        if (edgeType == EdgeType.UNDIRECTED) {
            addDirectedEdge(destination, source, weight)
        }
    }

    private fun addDirectedEdge(source: String, destination: String, weight: Double = 1.0) {
        val s = getVertex(source)
        val d = getVertex(destination)
        if (s != null && d != null) {
            val edgeList = edges.getOrDefault(source, mutableSetOf())
            // Overwrite edges
            edgeList.remove(Edge(s, d, weight))
            edgeList.add(Edge(s, d, weight))
            edges[source] = edgeList
        }
    }

    fun edge(source: String, destination: String): Edge<T>? {
        return edges[source]?.firstOrNull { it.destination.id == destination }
    }

    fun edges(source: String): Set<Edge<T>> {
        return edges[source] ?: setOf()
    }

    fun edges(): List<Edge<T>> {
        return edges.flatMap { it.value }
    }

    fun path(source: String, destination: String): List<String> {
        // Use Dijkstra's algorithm: https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm
        // Mark all nodes unvisited. Create a set of all the unvisited nodes called the unvisited set.
        val unvisited = vertices().map { it.id }.toMutableSet()

        // Assign to every node a tentative distance value: set it to zero for our initial node and to
        // infinity for all other nodes. The tentative distance of a node v is the length of the shortest
        // path discovered so far between the node v and the starting node. Since initially no path is known
        // to any other vertex than the source itself (which is a path of length zero), all other tentative
        // distances are initially set to infinity. Set the initial node as current.
        val distances = mutableMapOf<String, Double>()
        for (id in unvisited) {
            distances[id] = Double.MAX_VALUE
        }
        distances[source] = 0.0

        return dijkstra(source, destination, unvisited, distances)
    }

    private fun dijkstra(
        current: String,
        destination: String,
        unvisited: MutableSet<String>,
        distances: MutableMap<String, Double>
    ): List<String> {
        // For the current node, consider all of its unvisited neighbors and calculate their tentative distances
        // through the current node. Compare the newly calculated tentative distance to the current assigned
        // value and assign the smaller one. For example, if the current node A is marked with a distance of 6,
        // and the edge connecting it with a neighbor B has length 2, then the distance to B through A will be
        // 6 + 2 = 8. If B was previously marked with a distance greater than 8 then change it to 8. Otherwise,
        // the current value will be kept.
        val currentValue = distances.getOrDefault(current, 0.0)

        for (neighbour in edges(current)) {
            val neighbourId = neighbour.destination.id
            // Only visit the unvisited
            if (unvisited.contains(neighbourId)) {
                val distanceValue = currentValue + neighbour.weight
                val destinationValue = minOf(distanceValue, distances.getOrDefault(neighbourId, Double.MAX_VALUE))
                distances[neighbourId] = destinationValue
            }
        }

        // When we are done considering all of the unvisited neighbors of the current node, mark the current
        // node as visited and remove it from the unvisited set. A visited node will never be checked again.
        unvisited.remove(current)

        // If the destination node has been marked visited (when planning a route between two specific nodes)
        // or if the smallest tentative distance among the nodes in the unvisited set is infinity (when planning
        // a complete traversal; occurs when there is no connection between the initial node and remaining
        // unvisited nodes), then stop. The algorithm has finished.
        if (current == destination) {
            // TODO define path?
            return emptyList()
        }

        var minUnvisited = ""
        var minDistance = Double.MAX_VALUE
        for (id in unvisited) {
            val distance = distances.getOrDefault(id, Double.MAX_VALUE)
            if (distance < minDistance) {
                minDistance = distance
                minUnvisited = id
            }
        }
        // No path possible
        if (minDistance == Double.MAX_VALUE) {
            return emptyList()
        }

        // Otherwise, select the unvisited node that is marked with the smallest tentative distance, set it as
        // the new current node, and go back to step 3.
        return dijkstra(minUnvisited, destination, unvisited, distances)
    }

    fun weight(vertices: List<String>): Double {
        // Go for lowest weight
        return 0.0
    }
}

class Vertex<T>(val id: String, val data: T? = null, val weight: Double = 0.0) {
    override fun toString(): String {
        return "Vertex(id='$id', data=$data, weight=$weight)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Vertex<*>

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

class Edge<T>(val source: Vertex<T>, val destination: Vertex<T>, val weight: Double = 1.0) {
    override fun toString(): String {
        return "Edge(source=$source, destination=$destination, weight=$weight)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Edge<*>

        if (source != other.source) return false
        if (destination != other.destination) return false

        return true
    }

    override fun hashCode(): Int {
        var result = source.hashCode()
        result = 31 * result + destination.hashCode()
        return result
    }
}

enum class EdgeType {
    DIRECTED,
    UNDIRECTED
}
