package com.rolf.util

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class GraphTest {

    val graph = Graph<Int>()

    @Before
    fun before() {
        val graph = Graph<Int>()
    }

    @Test
    fun testConstructor() {
        assertEquals(0, graph.vertices().size)
        assertEquals(0, graph.edges().size)
    }

    @Test
    fun testAddVertex() {
        graph.addVertex(Vertex("a"))
        graph.addVertex(Vertex("b", 5))
        graph.addVertex(Vertex("c"))
        graph.addVertex(Vertex("b", 6))
        graph.addVertex(Vertex("d"))
        assertEquals(4, graph.vertices().size)
        assertEquals(0, graph.edges().size)
        assertEquals(6, graph.getVertex("b")?.data)
        assertNull(graph.getVertex("z"))
        assertFalse(graph.hasVertex("z"))
        assertTrue(graph.hasVertex("d"))
    }

    @Test
    fun testAddEdge() {
        graph.addEdge("a", "b")
        assertEquals(0, graph.vertices().size)
        assertEquals(0, graph.edges().size)

        graph.addVertex(Vertex("a"))
        graph.addEdge("a", "b")
        assertEquals(1, graph.vertices().size)
        assertEquals(0, graph.edges().size)

        graph.addVertex(Vertex("b"))
        graph.addEdge("a", "b")
        assertEquals(2, graph.vertices().size)
        assertEquals(1, graph.edges().size)

        graph.addVertex(Vertex("c"))
        graph.addEdge("a", "c", edgeType = EdgeType.UNDIRECTED, weight = 1.0)
        graph.addEdge("a", "c", edgeType = EdgeType.UNDIRECTED, weight = 2.0)
        assertEquals(3, graph.vertices().size)
        assertEquals(3, graph.edges().size)
        assertEquals(2, graph.edges("a").size) // a->b and a single a->c
        assertEquals(2.0, graph.edges("a").first { it.destination.id == "c" }.weight, 0.0)

        graph.addEdge("a", "b", edgeType = EdgeType.UNDIRECTED)
        assertEquals(3, graph.vertices().size)
        assertEquals(4, graph.edges().size)

        graph.addEdge("c", "b", edgeType = EdgeType.DIRECTED)
        assertEquals(3, graph.vertices().size) // a,b,c
        assertEquals(5, graph.edges().size) // a->b, a->c, c->a, b->a, c->b

        assertNotNull(graph.edge("c", "b"))
        assertNull(graph.edge("b", "c"))
    }

    @Test
    fun testPath() {
        graph.addVertex(Vertex("a"))
        graph.addVertex(Vertex("b"))
        graph.addVertex(Vertex("c"))
        graph.addVertex(Vertex("d"))
        graph.addVertex(Vertex("e"))

        graph.addEdge("a", "b", weight = 1.0)
        graph.addEdge("b", "d", weight = 1.0)
        graph.addEdge("d", "e", weight = 1.0)
        graph.addEdge("a", "c", weight = 1.0)
        graph.addEdge("c", "e", weight = 1.0)

        val path = graph.path("a", "e")
        assertEquals(3, path.size)
        assertEquals(listOf("a", "b", "e"), path)
    }

    @Test
    fun testWeight() {

    }
}
