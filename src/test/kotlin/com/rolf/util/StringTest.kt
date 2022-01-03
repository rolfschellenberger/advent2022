package com.rolf.util

import org.junit.Assert.*
import org.junit.Test

class StringTest {

    @Test
    fun getNumeric() {
        assertTrue("1234".isNumeric())
        assertFalse("-23".isNumeric())
        assertFalse("123.45".isNumeric())
        assertFalse("asdf234".isNumeric())
    }

    @Test
    fun testJoinSideBySide() {
        val a = "123\n789"
        val b = "456\nABC\nDEF"
        val joined = joinSideBySide(listOf(a, b))
        assertEquals("123 456\n789 ABC\n    DEF", joined)
    }
}
