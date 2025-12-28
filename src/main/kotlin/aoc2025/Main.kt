package aoc2025

import aoc2025.net.AocNet
import aoc2025.registry.DayRegistry

fun main(args: Array<String>) {
    registerAllDays()

    val days = if (args.isEmpty()) {
        DayRegistry.availableDays()
    } else {
        args.map { it.toInt() }.sorted()
    }

    for (day in days) {
        val solution = DayRegistry.create(day)
        val input = AocNet.loadDay(day)

        solution.setInput(input)

        val part1 = solution.solvePart1()
        val part2 = solution.solvePart2()

        println("Day $day")
        println("  Part 1: $part1")
        println("  Part 2: $part2")
    }
}