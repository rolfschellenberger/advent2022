package com.rolf.util

import org.junit.Assert
import org.junit.Test

class PointTest {

    @Test
    fun testPointRotate() {
        Assert.assertEquals(Point(0, 2), Point(2, 3).rotateRight(4, 4, 90.0))
        Assert.assertEquals(Point(0, 1), Point(1, 2).rotateRight(3, 3, 90.0))
        Assert.assertEquals(Point(1, 1), Point(1, 1).rotateRight(3, 3, 90.0))
        Assert.assertEquals(Point(4, 7), Point(7, 5).rotateRight(10, 10, 90.0))
        Assert.assertEquals(Point(2, 2), Point(7, 2).rotateRight(10, 10, -90.0))
    }

    @Test
    fun testPointDistance() {
        Assert.assertEquals(0, Point(3, 6).distance(Point(3, 6)))
        Assert.assertEquals(5, Point(3, 6).distance(Point(4, 2)))
    }
}