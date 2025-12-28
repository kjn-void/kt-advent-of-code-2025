package aoc2025.days

import kotlin.test.Test
import kotlin.test.assertEquals

class Day06Test {

    private val exampleInput = listOf(
        "123 328  51 64 ",
        " 45 64  387 23 ",
        "  6 98  215 314",
        "*   +   *   +  ",
    )

    @Test
    fun part1_example() {
        val d = Day06()
        d.setInput(exampleInput)

        assertEquals("4277556", d.solvePart1())
    }

    @Test
    fun part2_example() {
        val d = Day06()
        d.setInput(exampleInput)

        assertEquals("3263827", d.solvePart2())
    }
}