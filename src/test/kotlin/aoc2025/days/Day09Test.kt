package aoc2025.days

import kotlin.test.Test
import kotlin.test.assertEquals

class Day09Test {

    private val example = listOf(
        "7,1",
        "11,1",
        "11,7",
        "9,7",
        "9,5",
        "2,5",
        "2,3",
        "7,3",
    )

    @Test
    fun part1_example() {
        val d = Day09()
        d.setInput(example)
        assertEquals("50", d.solvePart1())
    }

    @Test
    fun part2_example() {
        val d = Day09()
        d.setInput(example)
        assertEquals("24", d.solvePart2())
    }
}