package com.rolf.util

import org.junit.Assert.*
import org.junit.Test

class MathTest {

    @Test
    fun testFactorial() {
        assertEquals(1, factorial(0))
        assertEquals(1, factorial(1))
        assertEquals(2, factorial(2))
        assertEquals(6, factorial(3))
        assertEquals(720, factorial(6))
    }

    @Test
    fun testEven() {
        assertTrue(4.isEven())
        assertTrue(1232141456.isEven())
        assertFalse(3.isEven())
        assertFalse((-3).isEven())
        assertTrue((-4).isEven())
        assertTrue(2342352355520.isEven())
    }

    @Test
    fun testOdd() {
        assertTrue(3.isOdd())
        assertTrue(1232141457.isOdd())
        assertFalse(6.isOdd())
        assertFalse((-6).isOdd())
        assertTrue((-7).isOdd())
        assertTrue(2342352355529.isOdd())
    }

    @Test
    fun testIsPrime() {
        assertFalse(4.isPrime())
        assertTrue(7.isPrime())
        assertTrue(999999000001.isPrime())
    }
}
