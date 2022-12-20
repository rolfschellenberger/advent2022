package com.rolf.day20

import com.rolf.Day

fun main() {
    Day20().run()
}

class Day20 : Day() {
    override fun solve1(lines: List<String>) {
        val list = buildLinkedList(lines)
        mix(list, 1)
        println(find(list, 1000) + find(list, 2000) + find(list, 3000))
    }

    override fun solve2(lines: List<String>) {
        val list = buildLinkedList(lines, 811589153)
        mix(list, 10)
        println(find(list, 1000) + find(list, 2000) + find(list, 3000))
    }

    private fun buildLinkedList(lines: List<String>, decryptionKey: Long = 1): List<Element> {
        val list = lines
            .map { it.toLong() }
            .map { it * decryptionKey }
            .map { Element(it) }
        for ((index, element) in list.withIndex()) {
            val previousIndex = (index - 1 + list.size) % list.size
            val nextIndex = (index + 1) % list.size
            element.previous = list[previousIndex]
            element.next = list[nextIndex]
        }
        return list
    }

    private fun mix(list: List<Element>, count: Int) {
        for (i in 0 until count) {
            list.forEach { move(it, list.size) }
        }
    }

    private fun move(element: Element, listSize: Int) {
        if (element.value == 0L) return

        // Remove from list
        element.previous?.next = element.next
        element.next?.previous = element.previous

        // Move element.value to the right
        var pointer = element.next!!
        // We are moving listSize - 1 locations to do a full round, so modulo = listSize - 1
        val modulo = listSize - 1
        val moveRight = ((element.value % modulo) + modulo) % modulo
        for (i in 0 until moveRight) {
            pointer = pointer.next!!
        }

        // Put back in place
        element.next = pointer
        element.previous = pointer.previous
        element.next?.previous = element
        element.previous?.next = element
    }

    private fun find(list: List<Element>, shift: Int): Long {
        val zero = findZero(list)
        var pointer = zero
        for (i in 0 until shift % list.size) {
            pointer = pointer.next!!
        }
        return pointer.value
    }

    private fun findZero(list: List<Element>): Element {
        for (element in list) {
            if (element.value == 0L) return element
        }
        throw Exception("Zero not found!")
    }
}

data class Element(val value: Long) {
    var previous: Element? = null
    var next: Element? = null
    override fun toString(): String {
        return "Element(value=$value, previous=${previous?.value}, next=${next?.value})"
    }
}
