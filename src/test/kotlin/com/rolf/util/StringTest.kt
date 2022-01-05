package com.rolf.util

import org.junit.Assert.*
import org.junit.Test

class StringTest {

    @Test
    fun getNumeric() {
        assertTrue("1234".isNumeric())
        assertTrue("-23".isNumeric())
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

    @Test
    fun testCharacterCounts() {
        val string = "abcefkfkfooooapppruiooolll"
        val map = getCharacterCounts(string)
        assertEquals(3, map.size)
        assertEquals(listOf('a', 'b', 'c', 'e', 'f', 'k', 'f', 'k', 'f', 'a', 'r', 'u', 'i'), map[1])
        assertEquals(listOf('p', 'o', 'l'), map[3])
        assertEquals(listOf('o'), map[4])
    }
}
