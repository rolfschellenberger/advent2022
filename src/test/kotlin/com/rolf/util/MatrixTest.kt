package com.rolf.util

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class MatrixTest {

    private var matrix = MatrixString.buildDefault(10, 20, ".")

    @Before
    fun before() {
        matrix = MatrixString.buildDefault(10, 20, ".")
    }

    @Test
    fun testAllElements() {
        assertEquals(200, matrix.allElements().size)
        assertEquals(200, matrix.allElements().filter { it == "." }.size)

        matrix.set(3, 3, "a")
        matrix.set(5, 5, "b")
        assertEquals(200, matrix.allElements().size)
        assertEquals(198, matrix.allElements().filter { it == "." }.size)
    }

    @Test
    fun testAllPoints() {
        assertTrue(matrix.allPoints().toSet().contains(Point(9, 19)))

        assertEquals(200, matrix.allPoints().size)
        val xValues = matrix.allPoints().groupBy { it.x }
        assertEquals(10, xValues.size)
        for (y in xValues.values) {
            assertEquals(20, y.size)
        }

        val yValues = matrix.allPoints().groupBy { it.y }
        assertEquals(20, yValues.size)
        for (x in yValues.values) {
            assertEquals(10, x.size)
        }
    }

    @Test
    fun testHeight() {
        assertEquals(20, matrix.height())
    }

    @Test
    fun testWidth() {
        assertEquals(10, matrix.width())
    }

    @Test
    fun testIsOutside() {
        assertTrue(matrix.isOutside(-1, 3))
        assertFalse(matrix.isOutside(0, 3))
        assertFalse(matrix.isOutside(9, 3))
        assertTrue(matrix.isOutside(10, 3))

        assertTrue(matrix.isOutside(3, -1))
        assertFalse(matrix.isOutside(3, 0))
        assertFalse(matrix.isOutside(3, 19))
        assertTrue(matrix.isOutside(3, 20))

        assertTrue(matrix.isOutside(Point(10, 20)))
        assertFalse(matrix.isOutside(Point(3, 3)))
    }

    @Test
    fun testGetColumns() {
        val columns = matrix.getColumns()
        assertEquals(10, columns.size)
        assertEquals(20, columns[0].size)
    }

    @Test
    fun testGetColumn() {
        val column = matrix.getColumn(3)
        assertEquals(20, column.size)
        assertEquals(".", column[0])
        assertEquals(".", column[1])

        matrix.set(3, 1, "B")
        assertEquals(".", column[0])
        assertEquals(".", column[1])
        val newColumn = matrix.getColumn(3)
        assertEquals(".", newColumn[0])
        assertEquals("B", newColumn[1])
    }

    @Test
    fun testGetRows() {
        val rows = matrix.getRows()
        assertEquals(20, rows.size)
        assertEquals(10, rows[0].size)
    }

    @Test
    fun testGetRow() {
        val row = matrix.getRow(3)
        assertEquals(10, row.size)
        assertEquals(".", row[0])
        assertEquals(".", row[1])

        matrix.set(1, 3, "B")
        assertEquals(".", row[0])
        assertEquals(".", row[1])
        val newRow = matrix.getRow(3)
        assertEquals(".", newRow[0])
        assertEquals("B", newRow[1])
    }

    @Test
    fun testGetSetCount() {
        assertEquals(200, matrix.count("."))
        assertEquals(0, matrix.count("A"))
        assertEquals(0, matrix.count("B"))
        assertEquals(0, matrix.count("C"))

        val point = Point(3, 5)
        assertEquals(".", matrix.get(point))
        assertEquals(".", matrix.get(3, 5))
        matrix.set(point, "B")
        assertEquals("B", matrix.get(point))
        matrix.set(6, 7, "C")
        assertEquals("C", matrix.get(6, 7))

        assertEquals(198, matrix.count("."))
        assertEquals(0, matrix.count("A"))
        assertEquals(1, matrix.count("B"))
        assertEquals(1, matrix.count("C"))
    }

    @Test
    fun testWrap() {
        assertEquals(Point(0, 0), matrix.wrap(Point(0, 0)))
        assertEquals(Point(1, 5), matrix.wrap(Point(1, 5)))
        assertEquals(Point(3, 5), matrix.wrap(Point(13, 5)))
        assertEquals(Point(6, 5), matrix.wrap(Point(1056, 5)))
        assertEquals(Point(6, 2), matrix.wrap(Point(1056, 5542)))
        assertEquals(Point(3, 17), matrix.wrap(Point(3, 437)))
        assertEquals(Point(9, 15), matrix.wrap(Point(-1, -5)))
        assertEquals(Point(6, 4), matrix.wrap(Point(-4354, -1236)))
    }

    @Test
    fun testLeft() {
        assertNull(matrix.getLeft(Point(0, 4)))
        assertEquals(Point(9, 0), matrix.getLeft(Point(0, 0), wrap = true))
        assertEquals(Point(6, 4), matrix.getLeft(Point(-93, 4), wrap = true))
    }

    @Test
    fun testRight() {
        assertNull(matrix.getRight(Point(9, 4)))
        assertEquals(Point(0, 0), matrix.getRight(Point(9, 0), wrap = true))
        assertEquals(Point(8, 4), matrix.getRight(Point(-93, 4), wrap = true))
    }

    @Test
    fun testUp() {
        assertNull(matrix.getUp(Point(4, 0)))
        assertEquals(Point(0, 19), matrix.getUp(Point(0, 0), wrap = true))
        assertEquals(Point(4, 3), matrix.getUp(Point(4, -56), wrap = true))
    }

    @Test
    fun testDown() {
        assertNull(matrix.getDown(Point(4, 19)))
        assertEquals(Point(0, 0), matrix.getDown(Point(0, 19), wrap = true))
        assertEquals(Point(4, 5), matrix.getDown(Point(4, -56), wrap = true))
    }

    @Test
    fun testLeftUp() {
        assertNull(matrix.getLeftUp(Point(5, 0)))
        assertNull(matrix.getLeftUp(Point(0, 5)))
        assertEquals(Point(9, 19), matrix.getLeftUp(Point(0, 0), wrap = true))
        assertEquals(Point(5, 3), matrix.getLeftUp(Point(-4, -56), wrap = true))
    }

    @Test
    fun testLeftDown() {
        assertNull(matrix.getLeftDown(Point(5, 19)))
        assertNull(matrix.getLeftDown(Point(0, 5)))
        assertEquals(Point(9, 0), matrix.getLeftDown(Point(0, 19), wrap = true))
        assertEquals(Point(5, 5), matrix.getLeftDown(Point(-4, -56), wrap = true))
    }

    @Test
    fun testRightUp() {
        assertNull(matrix.getRightUp(Point(5, 0)))
        assertNull(matrix.getRightUp(Point(9, 5)))
        assertEquals(Point(0, 19), matrix.getRightUp(Point(9, 0), wrap = true))
        assertEquals(Point(7, 3), matrix.getRightUp(Point(-4, -56), wrap = true))
    }

    @Test
    fun testRightDown() {
        assertNull(matrix.getRightDown(Point(5, 19)))
        assertNull(matrix.getRightDown(Point(9, 5)))
        assertEquals(Point(0, 0), matrix.getRightDown(Point(9, 19), wrap = true))
        assertEquals(Point(7, 5), matrix.getRightDown(Point(-4, -56), wrap = true))
    }

    @Test
    fun testGetArea() {
        val from = Point(4, 6)
        val to = Point(8, 9)
        matrix.set(from, "A")
        matrix.set(to, "B")

        val area = matrix.getArea(from, to)
        assertEquals(20, area.size)
        assertEquals(1, area.filter { matrix.get(it) == "A" }.count())
        assertEquals(1, area.filter { matrix.get(it) == "B" }.count())
        assertEquals(18, area.filter { matrix.get(it) == "." }.count())
    }

    @Test
    fun testCutOut() {
        matrix.set(3, 4, "B")
        matrix.set(5, 5, "C")
        assertEquals(10, matrix.width())
        assertEquals(20, matrix.height())
        assertEquals("B", matrix.get(3, 4))
        assertEquals("C", matrix.get(5, 5))

        matrix.cutOut(Point(3, 4), Point(7, 6))
        assertEquals(5, matrix.width())
        assertEquals(3, matrix.height())
        assertEquals("B", matrix.get(0, 0))
        assertEquals("C", matrix.get(2, 1))
    }

    @Test
    fun testGrow() {
        matrix.cutOut(Point(0, 0), Point(9, 4))
        assertEquals(10, matrix.width())
        assertEquals(5, matrix.height())

        matrix.grow(0, 5, 0, 0, "r")
        assertEquals(15, matrix.width())
        assertEquals(5, matrix.height())

        matrix.grow(0, 0, 0, 5, "b")
        assertEquals(15, matrix.width())
        assertEquals(10, matrix.height())

        matrix.grow(3, 0, 0, 0, "l")
        assertEquals(18, matrix.width())
        assertEquals(10, matrix.height())

        matrix.grow(0, 0, 4, 0, "t")
        assertEquals(18, matrix.width())
        assertEquals(14, matrix.height())

        val expected1 =
            """
            tttttttttttttttttt
            tttttttttttttttttt
            tttttttttttttttttt
            tttttttttttttttttt
            lll..........rrrrr
            lll..........rrrrr
            lll..........rrrrr
            lll..........rrrrr
            lll..........rrrrr
            lllbbbbbbbbbbbbbbb
            lllbbbbbbbbbbbbbbb
            lllbbbbbbbbbbbbbbb
            lllbbbbbbbbbbbbbbb
            lllbbbbbbbbbbbbbbb
            """.trimIndent()
        assertEquals(expected1, matrix.toString())
    }

    @Test
    fun testToString() {
        val smaller = MatrixString.buildDefault(10, 4, "x")
        val expected1 =
            """
            xxxxxxxxxx
            xxxxxxxxxx
            xxxxxxxxxx
            xxxxxxxxxx
            """.trimIndent()
        assertEquals(expected1, smaller.toString())

        smaller.set(3, 2, "B")
        val expected2 =
            """
            xxxxxxxxxx
            xxxxxxxxxx
            xxxBxxxxxx
            xxxxxxxxxx
            """.trimIndent()
        assertEquals(expected2, smaller.toString())

        val expected3 = "x x x x x x x x x x|x x x x x x x x x x|x x x B x x x x x x|x x x x x x x x x x"
        assertEquals(expected3, smaller.toString(" ", "|"))
    }

    @Test
    fun testBuild() {
        val lines = readLines("/matrix.txt")
        val input = splitLines(lines, chunkSize = 1)
        val matrixString = MatrixString.build(input)
        assertEquals(100, matrixString.width())
        assertEquals(100, matrixString.height())
        assertEquals("8", matrixString.get(17, 7))

        val matrixInt = MatrixInt.build(input)
        assertEquals(100, matrixInt.width())
        assertEquals(100, matrixInt.height())
        assertEquals(8, matrixInt.get(17, 7))

        val matrixLong = MatrixLong.build(input)
        assertEquals(100, matrixLong.width())
        assertEquals(100, matrixLong.height())
        assertEquals(8L, matrixLong.get(17, 7))
    }

    @Test
    fun testMatrixInt() {
        val matrix = MatrixInt.buildDefault(5, 3, 4)
        val expected =
            """
            44444
            44444
            44444
            """.trimIndent()
        assertEquals(expected, matrix.toString())
    }

    @Test
    fun testMatrixLong() {
        val matrix = MatrixLong.buildDefault(2, 5, 5)
        val expected =
            """
            55
            55
            55
            55
            55
            """.trimIndent()
        assertEquals(expected, matrix.toString())
    }

    @Test
    fun testReplace() {
        matrix.set(3, 3, "a")
        matrix.set(3, 4, "a")
        matrix.set(3, 5, "a")
        matrix.set(4, 6, "b")
        matrix.set(4, 7, "b")

        assertEquals(3, matrix.count("a"))
        assertEquals(2, matrix.count("b"))
        assertEquals(195, matrix.count("."))
        assertEquals(0, matrix.count("z"))
        assertEquals(0, matrix.count("#"))

        val map = mapOf(
            "a" to "z",
            "." to "#"
        )
        matrix.replace(map)

        assertEquals(0, matrix.count("a"))
        assertEquals(2, matrix.count("b"))
        assertEquals(0, matrix.count("."))
        assertEquals(3, matrix.count("z"))
        assertEquals(195, matrix.count("#"))
    }

    @Test
    fun testRotate() {
        matrix.cutOut(Point(0, 0), Point(9, 9))
        matrix.set(matrix.topLeft(), "A")
        matrix.set(matrix.topRight(), "B")
        matrix.set(matrix.bottomLeft(), "C")
        matrix.set(matrix.bottomRight(), "D")

        matrix.rotateRight()

        assertEquals("C", matrix.get(matrix.topLeft()))
        assertEquals("A", matrix.get(matrix.topRight()))
        assertEquals("D", matrix.get(matrix.bottomLeft()))
        assertEquals("B", matrix.get(matrix.bottomRight()))
    }

    @Test
    fun testCopy() {
        val a = MatrixInt.buildDefault(4, 4, 0)
        a.set(3, 3, 3)
        val b = a.copy()
        a.set(1, 1, 1)
        assertEquals(3, b.get(3, 3))
        assertEquals(0, b.get(1, 1))
    }

    @Test
    fun testShortestPath() {
        val input = """
            ..#.......
            .#........
            .#........
            .#........
            .#........
            .#........
            .#.#......
            .#.#......
            .#.#......
            ...#......
            """.trimIndent().lines()
        val maze = MatrixString.build(splitLines(input))

        val path = MatrixInt.buildForShortestPath(maze, "#")
        val start = Point(0, 0)
        val end = Point(9, 9)
        val distance = path.shortestPath(start, end)
        assertEquals(26, distance)

        // Diagonal test
        val path2 = MatrixInt.buildForShortestPath(maze, "#")
        val distance2 = path2.shortestPath(start, end, diagonal = true)
        assertEquals(10, distance2)

        // 2,6 closing path
        val path3 = MatrixInt.buildForShortestPath(maze, "#")
        path3.set(2, 6, Int.MIN_VALUE)
        try {
            path3.shortestPath(start, end)
            fail("Expected not to find a shortest path")
        } catch (e: Exception) {
            assertEquals("No shortest path found", e.message)
        }
    }
}
