package com.rolf.util

import java.math.BigInteger
import kotlin.math.pow

class Binary(var value: Long, val bits: Int = 16) {

    constructor(value: String, bits: Int = 16) : this(toLong(value), bits)

    val length = bits
    private val maskValue: Long = 2.0.pow(bits).toLong() - 1

    init {
        value = mask(value)
    }

    private fun mask(value: Long): Long {
        return value and maskValue
    }

    fun and(other: Long): Binary {
        value = mask(value and other)
        return this
    }

    fun and(other: String): Binary {
        return and(toLong(other))
    }

    fun or(other: Long): Binary {
        value = mask(value or other)
        return this
    }

    fun or(other: String): Binary {
        return or(toLong(other))
    }

    fun xor(other: Long): Binary {
        value = mask(value xor other)
        return this
    }

    fun xor(other: String): Binary {
        return xor(toLong(other))
    }

    fun not(): Binary {
        value = mask(value.inv())
        return this
    }

    fun shiftLeft(bits: Int): Binary {
        value = mask(value shl bits)
        return this
    }

    fun shiftRight(bits: Int): Binary {
        value = mask(value shr bits)
        return this
    }

    fun toString(leadingZeros: Boolean = false): String {
        val string = BigInteger.valueOf(value).toString(2)
        if (leadingZeros) {
            return string.padStart(bits, '0')
        }
        return string
    }

    fun copy(): Binary {
        return Binary(value, bits)
    }

    override fun toString(): String {
        return toString(false)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Binary

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

    operator fun get(index: Int): Boolean {
        return toString(true).reversed()[index] == '1'
    }

    operator fun set(index: Int, newValue: Boolean) {
        val binaryString = toString(true)
        val to = length - index
        val from = to - 1
        val replacement = if (newValue) "1" else "0"
        val newBinaryString = binaryString.replaceRange(from, to, replacement)
        value = mask(toLong(newBinaryString))
    }

    companion object {
        private fun toLong(binaryNumber: String): Long {
            var sum = 0L
            binaryNumber.reversed().forEachIndexed { k, v ->
                sum += (v.toString().toInt() * 2.0.pow(k)).toLong()
            }
            return sum
        }
    }
}
