package aoc2025.days

import kotlin.test.Test
import kotlin.test.assertEquals

class Day01Test {

    private val example = listOf(
        "L68", "L30", "R48", "L5", "R60",
        "L55", "L1", "L99", "R14", "L82"
    )

    @Test
    fun part1_example() {
        val d = Day01()
        d.setInput(example)
        assertEquals("3", d.solvePart1())
    }

    @Test
    fun part2_example() {
        val d = Day01()
        d.setInput(example)
        assertEquals("6", d.solvePart2())
    }
}