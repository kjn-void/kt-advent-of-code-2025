package aoc2025.days

import kotlin.test.Test
import kotlin.test.assertEquals

class Day05Test {

    private val exampleInput = listOf(
        "3-5",
        "10-14",
        "16-20",
        "12-18",
        "",
        "1",
        "5",
        "8",
        "11",
        "17",
        "32",
    )

    @Test
    fun part1_example() {
        val d = Day05()
        d.setInput(exampleInput)

        assertEquals("3", d.solvePart1())
    }

    @Test
    fun part2_example() {
        val d = Day05()
        d.setInput(exampleInput)

        assertEquals("14", d.solvePart2())
    }
}