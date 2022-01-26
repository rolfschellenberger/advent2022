package com.rolf.util

fun Int.isEven(): Boolean = this % 2 == 0

fun Int.isOdd(): Boolean = this % 2 != 0

fun Int.isPrime(certainty: Int = 5): Boolean = this.toBigInteger().isProbablePrime(certainty)

fun Long.isEven(): Boolean = this % 2 == 0L

fun Long.isOdd(): Boolean = this % 2 != 0L

fun Long.isPrime(certainty: Int = 5): Boolean = this.toBigInteger().isProbablePrime(certainty)

fun factorial(num: Int): Long {
    var factorial: Long = 1
    for (i in num downTo 2) {
        factorial *= i
    }
    return factorial
}

fun greatestCommonDivisor(a: Int, b: Int): Int {
    return greatestCommonDivisor(a.toLong(), b.toLong()).toInt()
}

fun greatestCommonDivisor(a: Long, b: Long): Long {
    return if (b == 0L) {
        a
    } else {
        greatestCommonDivisor(b, a % b)
    }
}

fun leastCommonMultiple(a: Long, b: Long): Long {
    return (a * b) / greatestCommonDivisor(a, b)
}
