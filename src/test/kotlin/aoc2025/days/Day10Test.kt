package aoc2025.days

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day10Test {

    private val example = listOf(
        "[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}",
        "[...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}",
        "[.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}",
    )

    @Test
    fun part1_example() {
        val d = Day10()
        d.setInput(example)
        assertEquals("7", d.solvePart1())
    }

    @Test
    fun part2_example() {
        val d = Day10()
        d.setInput(example)
        assertEquals("33", d.solvePart2())
    }
}