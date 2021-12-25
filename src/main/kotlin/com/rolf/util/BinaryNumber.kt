package com.rolf.util

data class BinaryNumber(val input: CharArray) {

    constructor(input: String) : this(input.toCharArray())

    val size: Int = input.size

    operator fun get(i: Int): Char {
        return input[i]
    }

    operator fun set(i: Int, value: Char) {
        input[i] = value
    }

    fun toInt() = Integer.parseInt(String(input), 2)

    fun copy(): BinaryNumber {
        return BinaryNumber(String(input))
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BinaryNumber

        if (!input.contentEquals(other.input)) return false
        if (size != other.size) return false

        return true
    }

    override fun hashCode(): Int {
        var result = input.contentHashCode()
        result = 31 * result + size
        return result
    }

}
