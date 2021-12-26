package com.rolf.util

fun String.isNumeric() = this.all { it in '0'..'9' }
