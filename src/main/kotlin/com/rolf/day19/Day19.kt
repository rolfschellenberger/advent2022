package com.rolf.day19

import com.rolf.Day

fun main() {
    Day19().run()
}

class Day19 : Day() {
    override fun solve1(lines: List<String>) {
        val blueprints = lines.map { parse(it) }

        for (blueprint in blueprints) {
            println("Trying $blueprint ...")
            runBlueprint(blueprint)
        }
    }

    private fun runBlueprint(blueprint: Blueprint): State {
        // 24 minutes
        val state = State(time = 1)
        return runState(blueprint, state)
    }

    private fun runState(blueprint: Blueprint, state: State): State {
        if (state.time > 24) return state


        while (state.time <= 24) {
            // Can we build something?
            val options = blueprint.getBuildOptions(state)
            println(options)
            if (options.isNotEmpty()) {
                println("Build something!")
                buildRobots(state)
            }

            // Collect resources and continue
            val newState = collectResources(state).next()
        }

        // Go to the next step
        return runState(blueprint, state.next())
    }

    private fun buildRobots(state: State) {
        // Now we have some options to build robots:
        // - Build nothing
        // - Build one to all robots

    }

    private fun collectResources(state: State): State {
        return state.copy(
            ore = state.ore + state.oreRobots,
            clay = state.clay + state.clayRobots,
            obsidian = state.obsidian + state.obsidianRobots,
            geode = state.geode + state.geodeRobots,
        )
    }

    private fun parse(line: String): Blueprint {
        val (ore, clay, obsidian, geode) = line.removeSuffix(".").split(": ")[1].split(". ")
        val oreResources = parseResources(ore)
        val clayResources = parseResources(clay)
        val obsidianResources = parseResources(obsidian)
        val geodeResources = parseResources(geode)
        return Blueprint(oreResources, clayResources, obsidianResources, geodeResources)
    }

    private fun parseResources(line: String): Resources {
        // Each clay robot costs 2 ore
        // Each obsidian robot costs 3 ore and 14 clay
        val parts = line.split(" costs ")[1].split(" and ")
        var ore = 0
        var clay = 0
        var obsidian = 0
        for (part in parts) {
            val (v, type) = part.split(" ")
            val value = v.toInt()
            when (type) {
                "ore" -> ore = value
                "clay" -> clay = value
                "obsidian" -> obsidian = value
                else -> throw Exception("Unknown type $type")
            }
        }
        return Resources(ore, clay, obsidian)
    }

    override fun solve2(lines: List<String>) {
    }
}

data class Blueprint(
    val oreRobotResources: Resources,
    val clayRobotResources: Resources,
    val obsidianRobotResources: Resources,
    val geodeRobotResources: Resources
) {
    fun getBuildOptions(state: State): BuildOptions {
        return BuildOptions(
            state.contains(oreRobotResources),
            state.contains(clayRobotResources),
            state.contains(obsidianRobotResources),
            state.contains(geodeRobotResources),
        )
    }
}

data class Resources(val ore: Int = 0, val clay: Int = 0, val obsidian: Int = 0)

data class State(
    val time: Int,
    val ore: Int = 0, val clay: Int = 0, val obsidian: Int = 0, val geode: Int = 0,
    val oreRobots: Int = 1, val clayRobots: Int = 0, val obsidianRobots: Int = 0, val geodeRobots: Int = 0
) {
    fun contains(resources: Resources): Int {
        val oreTimes = if (resources.ore == 0) Int.MAX_VALUE else ore / resources.ore
        val clayTimes = if (resources.clay == 0) Int.MAX_VALUE else clay / resources.clay
        val obsidianTimes = if (resources.obsidian == 0) Int.MAX_VALUE else obsidian / resources.obsidian
        return minOf(oreTimes, clayTimes, obsidianTimes)
    }

    fun next(): State {
        return this.copy(time = time + 1)
    }
}

data class BuildOptions(
    val oreRobots: Int = 0,
    val clayRobots: Int = 0,
    val obsidianRobots: Int = 0,
    val geodeRobots: Int = 0
) {
    fun isNotEmpty(): Boolean {
        return oreRobots > 0 ||
                clayRobots > 0 ||
                obsidianRobots > 0 ||
                geodeRobots > 0
    }
}
