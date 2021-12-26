package com.rolf.util

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class StringTest {

    @Test
    fun getNumeric() {
        assertTrue("1234".isNumeric())
        assertFalse("-23".isNumeric())
        assertFalse("123.45".isNumeric())
        assertFalse("asdf234".isNumeric())
    }
}
