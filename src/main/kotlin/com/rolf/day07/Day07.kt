package com.rolf.day07

import com.rolf.Day

fun main() {
    Day07().run()
}

class Day07 : Day() {
    override fun solve1(lines: List<String>) {
        val root = parse(lines)
        val maximumDirectorySize = 100000
        println(
            directoryToSize(root)
                .map { it.value }
                .filter { it <= maximumDirectorySize }
                .sum()
        )
    }

    override fun solve2(lines: List<String>) {
        val root = parse(lines)
        val directorySizeMap = directoryToSize(root)
        val diskUsage = directorySizeMap.getValue(root)
        val diskSpace = 70000000
        val unused = diskSpace - diskUsage
        val freeUp = 30000000 - unused

        println(
            directorySizeMap
                .map { it.value }
                .filter { it >= freeUp }
                .minOf { it }
        )
    }

    private fun parse(lines: List<String>): Directory {
        val root = Directory("/", null)
        var pointer = root

        for (line in lines) {
            when {
                line.startsWith("$ cd") -> {
                    val goTo = line.substring(5, line.length)
                    pointer = when (goTo) {
                        "/" -> root
                        ".." -> pointer.parent!!
                        else -> pointer.getChild(goTo)
                    }
                }

                line.startsWith("dir ") -> {
                    val dir = line.substring(4, line.length)
                    pointer.children.add(Directory(dir, pointer))
                }

                line.startsWith("$").not() -> {
                    val (size, file) = line.split(" ")
                    pointer.files.add(File(file, size.toLong()))
                }
            }
        }
        return root
    }

    private fun directoryToSize(
        root: Directory,
        map: MutableMap<Directory, Long> = mutableMapOf()
    ): Map<Directory, Long> {
        map[root] = root.totalSize()

        for (child in root.children) {
            directoryToSize(child, map)
        }

        return map
    }
}

data class Directory(val name: String, val parent: Directory?) {

    val children: MutableList<Directory> = mutableListOf()
    val files: MutableList<File> = mutableListOf()

    fun getChild(dir: String): Directory {
        for (child in children) {
            if (child.name == dir) return child
        }
        throw Exception("Child $dir not found in $name")
    }

    fun totalSize(): Long {
        return files.sumOf { it.size } +
                children.sumOf { it.totalSize() }
    }

    override fun toString(): String {
        return "Directory(name='$name', parent=${parent?.name}, children=$children, files=$files)"
    }
}

data class File(val name: String, val size: Long)
