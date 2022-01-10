package com.rolf.util

fun Int.isEven(): Boolean = this % 2 == 0

fun Int.isOdd(): Boolean = this % 2 != 0

fun Long.isEven(): Boolean = this % 2 == 0L

fun Long.isOdd(): Boolean = this % 2 != 0L

fun factorial(num: Int): Long {
    var factorial: Long = 1
    for (i in num downTo 2) {
        factorial *= i
    }
    return factorial
}
