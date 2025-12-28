package aoc2025.days

import kotlin.test.Test
import kotlin.test.assertEquals

class Day04Test {

    private val exampleInput = listOf(
        "..@@.@@@@.",
        "@@@.@.@.@@",
        "@@@@@.@.@@",
        "@.@@@@..@.",
        "@@.@@@@.@@",
        ".@@@@@@@.@",
        ".@.@.@.@@@",
        "@.@@@.@@@@",
        ".@@@@@@@@.",
        "@.@.@@@.@."
    )

    @Test
    fun part1_example() {
        val d = Day04()
        d.setInput(exampleInput)

        assertEquals("13", d.solvePart1())
    }

    @Test
    fun part2_example() {
        val d = Day04()
        d.setInput(exampleInput)

        assertEquals("43", d.solvePart2())
    }
}