package aoc2025.days

import kotlin.test.Test
import kotlin.test.assertEquals

class Day07Test {

    private val exampleInput = listOf(
        ".......S.......",
        "...............",
        ".......^.......",
        "...............",
        "......^.^......",
        "...............",
        ".....^.^.^.....",
        "...............",
        "....^.^...^....",
        "...............",
        "...^.^...^.^...",
        "...............",
        "..^...^.....^..",
        "...............",
        ".^.^.^.^.^...^.",
        "...............",
    )

    @Test
    fun part1_example() {
        val s = Day07()
        s.setInput(exampleInput)
        assertEquals("21", s.solvePart1())
    }

    @Test
    fun part2_example() {
        val s = Day07()
        s.setInput(exampleInput)
        assertEquals("40", s.solvePart2())
    }
}