package com.rolf.util

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class GraphTest {

    private var graph = Graph<Int>()

    @Before
    fun before() {
        graph = Graph()
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
        assertEquals(2.0, graph.edges("a").first { it.destination == "c" }.weight, 0.0)

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
    fun testPathAndWeight() {
        graph.addVertex(Vertex("a"))
        graph.addVertex(Vertex("b"))
        graph.addVertex(Vertex("c"))
        graph.addVertex(Vertex("d"))
        graph.addVertex(Vertex("e"))

        graph.addEdge("a", "b")
        graph.addEdge("b", "d")
        graph.addEdge("d", "e")
        graph.addEdge("a", "c")
        graph.addEdge("c", "e")

        val (path, weight) = graph.shortestPathAndWeight("a", "e")
        assertEquals(3, path.size)
        assertEquals(listOf("a", "c", "e"), path)
        assertEquals(2.0, weight, 0.0)
    }

    @Test
    fun testWeightedVertices() {
        graph.addVertex(Vertex("a", weight = 5.0))
        graph.addVertex(Vertex("b", weight = 5.0))
        graph.addVertex(Vertex("c", weight = 5.0))
        graph.addVertex(Vertex("d", weight = 5.0))
        graph.addVertex(Vertex("e", weight = 5.0))

        graph.addEdge("a", "b", weight = 1.0)
        graph.addEdge("b", "d", weight = 1.0)
        graph.addEdge("d", "e", weight = 1.0)
        graph.addEdge("a", "c", weight = 1.0)
        graph.addEdge("c", "e", weight = 1.0)

        val (path, weight) = graph.shortestPathAndWeight("a", "e")
        assertEquals(3, path.size)
        assertEquals(listOf("a", "c", "e"), path)
        assertEquals(17.0, weight, 0.0)

        // Let's make this path worse
        graph.addEdge("a", "c", weight = 11.0)
        val (path2, weight2) = graph.shortestPathAndWeight("a", "e")
        assertEquals(4, path2.size)
        assertEquals(listOf("a", "b", "d", "e"), path2)
        assertEquals(23.0, weight2, 0.0)

        // Let's make d worse
        graph.addVertex(Vertex("d", weight = 15.0))
        val (path3, weight3) = graph.shortestPathAndWeight("a", "e")
        assertEquals(3, path3.size)
        assertEquals(listOf("a", "c", "e"), path3)
        assertEquals(27.0, weight3, 0.0)
    }

    @Test
    fun testUndirectedPath() {
        graph.addVertex(Vertex("a"))
        graph.addVertex(Vertex("b"))
        graph.addVertex(Vertex("c"))
        graph.addVertex(Vertex("d"))
        graph.addVertex(Vertex("e"))

        graph.addEdge("a", "b", EdgeType.UNDIRECTED)
        graph.addEdge("b", "d", EdgeType.UNDIRECTED)
        graph.addEdge("d", "e", EdgeType.UNDIRECTED)
        graph.addEdge("a", "c", EdgeType.UNDIRECTED)
        graph.addEdge("c", "e", EdgeType.UNDIRECTED)
        graph.addEdge("b", "c", EdgeType.UNDIRECTED)
        graph.addEdge("c", "d", EdgeType.UNDIRECTED)
        graph.addEdge("e", "a", EdgeType.DIRECTED)

        val (path, weight) = graph.shortestPathAndWeight("a", "e")
        assertEquals(3, path.size)
        assertEquals(listOf("a", "c", "e"), path)
        assertEquals(2.0, weight, 0.0)

        val (path2, weight2) = graph.shortestPathAndWeight("e", "a")
        assertEquals(2, path2.size)
        assertEquals(listOf("e", "a"), path2)
        assertEquals(1.0, weight2, 0.0)
    }

    @Test
    fun testWrongFromTo() {
        graph.addVertex(Vertex("a"))
        graph.addVertex(Vertex("b"))
        graph.addVertex(Vertex("c"))
        graph.addVertex(Vertex("d"))
        graph.addVertex(Vertex("e"))

        graph.addEdge("a", "b")
        graph.addEdge("b", "d")
        graph.addEdge("c", "e")

        val wrong = emptyList<String>() to Double.MAX_VALUE
        assertEquals(wrong, graph.shortestPathAndWeight("wrong", "e"))
        assertEquals(wrong, graph.shortestPathAndWeight("a", "wrong"))
        assertEquals(wrong, graph.shortestPathAndWeight("a", "e"))
    }

    @Test
    fun testAllPaths() {
        graph.addVertex(Vertex("a"))
        graph.addVertex(Vertex("b"))
        graph.addVertex(Vertex("c"))
        graph.addVertex(Vertex("d"))
        graph.addVertex(Vertex("e"))

        graph.addEdge("a", "b", EdgeType.UNDIRECTED)
        graph.addEdge("b", "d", EdgeType.UNDIRECTED)
        graph.addEdge("d", "e", EdgeType.UNDIRECTED)
        graph.addEdge("a", "c", EdgeType.UNDIRECTED)
        graph.addEdge("c", "e", EdgeType.DIRECTED)
        val (path, weight) = graph.lowestPathAndWeightVisitAll(source = "b")
        assertEquals(listOf("b", "a", "c", "e", "d"), path)
        assertEquals(4.0, weight, 0.0)

        val (path2, weight2) = graph.lowestPathAndWeightVisitAll(source = "b", destination = "a")
        assertEquals(emptyList<String>(), path2)
        assertEquals(Double.MAX_VALUE, weight2, 0.0)

        graph.addEdge("a", "c", EdgeType.UNDIRECTED, 2.0)
        val (path3, weight3) = graph.lowestPathAndWeightVisitAll()
        assertEquals(listOf("c", "e", "d", "b", "a"), path3)
        assertEquals(4.0, weight3, 0.0)

        graph.addEdge("d", "e", EdgeType.UNDIRECTED, 3.0)
        val (path4, weight4) = graph.lowestPathAndWeightVisitAll()
        assertEquals(listOf("d", "b", "a", "c", "e"), path4)
        assertEquals(5.0, weight4, 0.0)

        val (path5, weight5) = graph.highestPathAndWeightVisitAll()
        assertEquals(listOf("a", "c", "e", "d", "b"), path5)
        assertEquals(7.0, weight5, 0.0)
    }
}
