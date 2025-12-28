package aoc2025.days

import kotlin.test.Test
import kotlin.test.assertEquals

class Day03Test {

    private val exampleInput = listOf(
        "987654321111111",
        "811111111111119",
        "234234234234278",
        "818181911112111"
    )

    @Test
    fun part1_example() {
        val d = Day03()
        d.setInput(exampleInput)

        val result = d.solvePart1()
        assertEquals("357", result)
    }

    @Test
    fun part2_example() {
        val d = Day03()
        d.setInput(exampleInput)

        val result = d.solvePart2()
        assertEquals("3121910778619", result)
    }
}