package com.rolf.day19

import com.rolf.Day
import kotlin.math.ceil

fun main() {
    Day19().run()
}

class Day19 : Day() {
    private val resources = listOf("ore", "clay", "obsidian")

    override fun solve1(lines: List<String>) {
        println(getMaxValues(lines, 24)
            .mapIndexed { index, value -> (index + 1) * value }
            .sum()
        )
    }

    override fun solve2(lines: List<String>) {
        println(getMaxValues(lines.take(3), 32)
            .reduce { a, b -> a * b }
        )
    }

    private fun getMaxValues(lines: List<String>, time: Int): List<Int> {
        return lines.map { line ->
            val blueprint = mutableListOf<List<Pair<Int, Int>>>()
            val maxSpend = IntArray(3) { 0 }
            val parts = line.removeSuffix(".").split(": ")[1].split(". ")
            // ore, clay, obsidian, geode
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
            runBlueprint(blueprint, maxSpend, time, intArrayOf(1, 0, 0, 0), intArrayOf(0, 0, 0, 0))
        }
    }

    private fun runBlueprint(
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
                val newBots = bots.copyOf()
                val newAmount = amount.copyOf()
                for (index in bots.indices) {
                    // Every amount will increase with the bots times the waiting time
                    // (since there are no other bots being build in between)
                    newAmount[index] = amount[index] + bots[index] * (wait + 1)
                }
                // Spend the resources needed
                for ((resourceAmount, resourceType) in recipe) {
                    newAmount[resourceType] -= resourceAmount
                }
                newBots[botType] += 1

                // Optimize: look for the maximum amount of each resource to spend on robots. We never need more than this
                // amount of robots in general, since we cannot spend this amount.
                for (i in 0 until 3) {
                    newAmount[i] = minOf(newAmount[i], maxSpend[i] * remainingTime)
                }

                // Next iteration
                maxValue = maxOf(maxValue, runBlueprint(blueprint, maxSpend, remainingTime, newBots, newAmount, cache))
            }
        }

        cache[key] = maxValue
        return maxValue
    }
}
