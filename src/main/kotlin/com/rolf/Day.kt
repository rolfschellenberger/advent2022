package com.rolf

import com.rolf.util.readLines

abstract class Day {

    open fun getDay(): Int {
        return javaClass.simpleName.replace("Day", "").toInt()
    }

    fun run() {
        val day = getDay().toString().padStart(2, '0')

        println("+--------+")
        println("| Day $day |")
        println("+--------+")

        println("-- Part 1 --")
        solve1(readLines("/$day.txt"))
        println("-- Part 2 --")
        solve2(readLines("/$day.txt"))
    }

    abstract fun solve1(lines: List<String>)
    abstract fun solve2(lines: List<String>)
}
