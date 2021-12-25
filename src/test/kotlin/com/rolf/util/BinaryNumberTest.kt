package com.rolf.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class BinaryNumberTest {

    @Test(expected = NumberFormatException::class)
    fun testWrongBinaryNumber() {
        val binaryNumber = BinaryNumber("a")
        binaryNumber.toInt()
    }

    @Test
    fun testBinaryNumber() {
        val binaryNumber = BinaryNumber("0001010101".toCharArray())
        assertEquals(10, binaryNumber.size)
        assertEquals('0', binaryNumber[0])
        assertEquals('0', binaryNumber[1])
        assertEquals('0', binaryNumber[2])
        assertEquals('1', binaryNumber[3])
        assertEquals('0', binaryNumber[4])
        assertEquals('1', binaryNumber[5])
        assertEquals('0', binaryNumber[6])
        assertEquals('1', binaryNumber[7])
        assertEquals('0', binaryNumber[8])
        assertEquals('1', binaryNumber[9])
        assertEquals(85, binaryNumber.toInt())

        val newBinaryNumber = binaryNumber.copy()
        assertEquals(binaryNumber, newBinaryNumber)

        newBinaryNumber[0] = '1'
        assertEquals('1', newBinaryNumber[0])
        assertEquals(597, newBinaryNumber.toInt())
        assertNotEquals(binaryNumber, newBinaryNumber)
    }

    @Test
    fun testUnique() {
        val binaryNumber1 = BinaryNumber("110")
        val binaryNumber2 = BinaryNumber("110")
        assertEquals(binaryNumber1, binaryNumber2)
        val set = setOf(binaryNumber1, binaryNumber2)
        assertEquals(1, set.size)
    }
}
