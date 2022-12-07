package com.rolf.day07

import com.rolf.Day

fun main() {
    Day07().run()
}

class Day07 : Day() {
    override fun solve1(lines: List<String>) {
        parse(lines)
    }

    private fun parse(lines: List<String>) {
        val root = Directory("/", null)
        var pointer = root

        // Skip first root
        var i = 1
        while (i < lines.size) {
            val line = lines[i]
            if (line.startsWith("$")) {
                // Go to root
                if (line == "$ cd /") {
//                    println("go to root")
                    pointer = root
                }
                // Get out of dir
                else if (line == "cd ..") {
//                    println("move out of dir")
                    pointer = pointer.parent!!
                }
                // Go into dir
                else if (line.startsWith("$ cd ")) {
                    val dir = line.subSequence(5, line.length)
//                    println("move into $dir")
                    pointer = if (dir == "..") pointer.parent!! else pointer.getChild(dir)
                }
                // list files
                else if (line == "$ ls") {
                    i++
                    var l = lines[i]
//                    println("file/dir listing:")
                    while (!l.startsWith("$")) {

//                        println("- $l")
                        if (l.startsWith("dir ")) {
                            val dirName = l.substring(4, l.length)
//                            println("dir: $dirName")
                            pointer.children.add(Directory(dirName, pointer))
                        } else {
                            val (size, name) = l.split(" ")
//                            println("file: $name ($size)")
                            pointer.files.add(File(size.toLong(), name))
                        }

                        // Next line
                        i++
                        if (i >= lines.size) break
                        l = lines[i]
                    }
                    i--
                } else {
                    throw Exception("Unexpected command $line")
                }
            }
            i++
        }

        // Find the directories with size <= 100000
//        println(root)
        val candidates = scanForDirs(root, 100000)
        println(
            candidates.sumOf { sumSize(it) }
        )

        println("===============")
        val used = sumSize(root)
        val unused = 70000000 - used
//        println("unused: $unused")
        val freeUp = 30000000 - unused
//        println("free up: $freeUp")

        // Look for dirs that free up enough space
        val toDelete = scanForDirsMin(root, freeUp)
        println(
            toDelete.map {
                sumSize(it)
            }.minOf { it }
        )
    }

    private fun scanForDirsMin(root: Directory, minSize: Long): List<Directory> {
        val result = mutableListOf<Directory>()

        // Sum size
        val sum = sumSize(root)
        if (sum >= minSize) {
            result.add(root)
        }
        for (child in root.children) {
            result.addAll(scanForDirsMin(child, minSize))
        }

        return result
    }


    private fun scanForDirs(root: Directory, maxSize: Long): List<Directory> {
        val result = mutableListOf<Directory>()

        // Sum size
        val sum = sumSize(root)
        if (sum <= maxSize) {
            result.add(root)
        }
        for (child in root.children) {
            result.addAll(scanForDirs(child, maxSize))
        }

        return result
    }

    private fun sumSize(root: Directory): Long {
        var sum: Long = 0
        for (file in root.files) {
            sum += file.size
        }

        for (child in root.children) {
            sum += sumSize(child)
        }
        return sum
    }

    override fun solve2(lines: List<String>) {
    }
}

data class Directory(val name: String, val parent: Directory?) {
    fun getChild(dir: CharSequence): Directory {
        for (child in children) {
            if (child.name == dir) return child
        }
        throw Exception("Child $dir not found in $name")
    }

    override fun toString(): String {
        return "Directory(name='$name', parent=${parent?.name}, children=$children, files=$files)"
    }

    val children: MutableList<Directory> = mutableListOf()
    val files: MutableList<File> = mutableListOf()
}

data class File(val size: Long, val name: String)
