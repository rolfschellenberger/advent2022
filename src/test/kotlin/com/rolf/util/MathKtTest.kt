package com.rolf.util

import org.junit.Assert.assertEquals
import org.junit.Test

class MathKtTest {

    @Test
    fun testFactorial() {
        assertEquals(1, factorial(0))
        assertEquals(1, factorial(1))
        assertEquals(2, factorial(2))
        assertEquals(6, factorial(3))
        assertEquals(720, factorial(6))
    }
}
