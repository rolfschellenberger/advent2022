package com.rolf.day19

import com.rolf.Day
import kotlin.math.ceil

fun main() {
    Day19().run()
}

class Day19 : Day() {
    override fun solve1(lines: List<String>) {
        val resources = listOf("ore", "clay", "obsidian")

        var total = 0L
        for ((index, line) in lines.withIndex()) {
            val blueprint = mutableListOf<List<Pair<Int, Int>>>()
//            val maxSpend = mutableMapOf<Int, Int>(0 to 0, 1 to 0, 2 to 0)
            val maxSpend = IntArray(3) { 0 }
            // ore, clay, obsidian, geode
            val parts = line.removeSuffix(".").split(": ")[1].split(". ")
            for (part in parts) {
                val recipe = mutableListOf<Pair<Int, Int>>()
                val costs = part.split(" costs ")[1].split(" and ")
                for (resource in costs) {
                    val (a, t) = resource.split(" ")
                    val amount = a.toInt()
                    val typeIndex = resources.indexOf(t)
                    recipe.add(amount to typeIndex)
                    maxSpend[typeIndex] = maxOf(maxSpend[typeIndex], amount)
                }
                blueprint.add(recipe)
            }
//            println(blueprint)
//            println(maxSpend.toList())
            val value = runBlueprint2(blueprint, maxSpend, 24, intArrayOf(1, 0, 0, 0), intArrayOf(0, 0, 0, 0))
            total += (index + 1) * value
        }

        println(total)

        // Optimize: look for the maximum amount of each resource to spend on robots. We never need more than this
        // amount of robots in general, since we cannot spend this amount.

        // Optimize: when more resources than needed in the time remaining, throw away the extra resources, so the
        // cache hit is increasing.
    }

    private fun runBlueprint2(
        blueprint: List<List<Pair<Int, Int>>>,
        maxSpend: IntArray,
        time: Int,
        bots: IntArray,
        amount: IntArray,
        cache: MutableMap<String, Int> = mutableMapOf()
    ): Int {
        if (time <= 0) return amount[3]

        val key = listOf(time, bots.toList(), amount.toList()).joinToString("-")
        if (cache.containsKey(key)) {
            return cache.getValue(key)
        }

        // Do nothing: value is equal to the geode amount + the number of geode bots and the time remaining
        var maxValue = amount[3] + bots[3] * time

        // Check out the recipes for each bot type: 0=ore robot, 1=clay robot, 2=obsidian robot, 3=geode robot
        // See if we can build a new robot type
        for ((botType, recipe) in blueprint.withIndex()) {
            // Optimize: look for the maximum amount of each resource to spend on robots. We never need more than this
            // amount of robots in general, since we cannot spend this amount.
            // Skip if the number of bots is already at the maximum we can spend for non-geode robots
            if (botType != 3 && bots[botType] >= maxSpend[botType]) {
                continue
            }

            // How long will we need to wait in order to have enough resources for this robot type to build?
            var wait = 0
            var canProduce = true
            for ((resourceAmount, resourceType) in recipe) {
                // If there is no bot to produce this type, we cannot produce this recipe
                if (bots[resourceType] == 0) {
                    canProduce = false
                    break
                }

                // Waiting time is the required amount minus what we have in stock, divided by the bots that produce this
                // 12 - 1 = 11, 11 / 3 = 4 (rounding up, since we need full rounds to exceed the amount needed)
                val w = ceil((resourceAmount - amount[resourceType]) / bots[resourceType].toDouble()).toInt()
                wait = maxOf(wait, w)
            }

            // When done, we found the maximum waiting time to have all resources available for this bot type
            if (canProduce) {
                // How much time is there remaining after waiting?
                val remainingTime = time - wait - 1

                // No more time remaining, so no point in building
                if (remainingTime <= 0) {
                    continue
                }

                // Skip to the future
                val bots_ = bots.copyOf()
                val amount_ = amount.copyOf()
                for (index in bots.indices) {
                    // Every amount will increase with the bots times the waiting time
                    // (since there are no other bots being build in between)
                    amount_[index] = amount[index] + bots[index] * (wait + 1)
                }
                // Spend the resources needed
                for ((resourceAmount, resourceType) in recipe) {
                    amount_[resourceType] -= resourceAmount
                }
                bots_[botType] += 1

                // Next iteration
                maxValue = maxOf(maxValue, runBlueprint2(blueprint, maxSpend, remainingTime, bots_, amount_, cache))
            }
        }

        cache[key] = maxValue
        return maxValue
    }

    private fun runBlueprint(blueprint: Blueprint): State {
        // 24 minutes
        val state = State(time = 1)
        return runState(blueprint, state)
    }

    private val cache = mutableMapOf<State, State>()
    private fun runState(blueprint: Blueprint, state: State): State {
        if (state.time > 24) return state

        if (cache.containsKey(state)) {
            return cache.getValue(state)
        }

        // Can we build something?
        if (state.time < 24) {
            val options = blueprint.getBuildOptions(state)

            // If we have more options than not building, split this path
            if (options.size > 1) {
//                println("Build something!")
//                println(options)
                var bestState = state
                for (option in options) {
                    val newState = build(blueprint, state, option)
                    val newState2 = collectResources(newState)
                    val runState = runState(blueprint, newState2.next())
                    if (runState.geode > state.geode) {
                        bestState = runState
                    }
                }
//                println("Best state: $bestState")
                cache[state] = bestState
                return bestState
            }
        }

        // Collect resources and continue
        val newState = collectResources(state)

        // Go to the next step
//        println(newState)
//        println()
        return runState(blueprint, newState.next())
    }

    private fun build(blueprint: Blueprint, state: State, option: BuildOptions): State {
        // TODO: Change counter to boolean
        return when {
            option.oreRobots > 0 -> state.buildOreRobot(blueprint.oreRobotResources)
            option.clayRobots > 0 -> state.buildClayRobot(blueprint.clayRobotResources)
            option.obsidianRobots > 0 -> state.buildObsidianRobot(blueprint.obsidianRobotResources)
            option.geodeRobots > 0 -> state.buildGeodeRobotsRobot(blueprint.geodeRobotResources)
            else -> state.copy()
        }
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
    fun getBuildOptions(state: State): List<BuildOptions> {
        return listOf(
            // Include the option to build nothing this round
            BuildOptions(
                0,
                0,
                0,
                0
            ),
            BuildOptions(
                state.contains(oreRobotResources),
                0,
                0,
                0,
            ),
            BuildOptions(
                0,
                state.contains(clayRobotResources),
                0,
                0,
            ),
            BuildOptions(
                0,
                0,
                state.contains(obsidianRobotResources),
                0,
            ),
            BuildOptions(
                0,
                0,
                0,
                state.contains(geodeRobotResources),
            )
        ).toSet().toList() // Make sure we only return unique build options
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
        // Either build 1 or none, since we either decide to build or to save up
        val ot = if (oreTimes >= 1) 1 else oreTimes
        val ct = if (clayTimes >= 1) 1 else clayTimes
        val bt = if (obsidianTimes >= 1) 1 else obsidianTimes
        return minOf(ot, ct, bt)
    }

    fun buildOreRobot(resources: Resources): State {
        return copy(
            ore = ore - resources.ore,
            clay = clay - resources.clay,
            obsidian = obsidian - resources.obsidian,
            oreRobots = oreRobots + 1
        )
    }

    fun buildClayRobot(resources: Resources): State {
        return copy(
            ore = ore - resources.ore,
            clay = clay - resources.clay,
            obsidian = obsidian - resources.obsidian,
            clayRobots = clayRobots + 1
        )
    }

    fun buildObsidianRobot(resources: Resources): State {
        return copy(
            ore = ore - resources.ore,
            clay = clay - resources.clay,
            obsidian = obsidian - resources.obsidian,
            obsidianRobots = obsidianRobots + 1
        )
    }

    fun buildGeodeRobotsRobot(resources: Resources): State {
        return copy(
            ore = ore - resources.ore,
            clay = clay - resources.clay,
            obsidian = obsidian - resources.obsidian,
            geodeRobots = geodeRobots + 1
        )
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
