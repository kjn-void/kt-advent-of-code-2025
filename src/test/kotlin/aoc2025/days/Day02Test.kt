package aoc2025.days

import kotlin.test.Test
import kotlin.test.assertEquals

class Day02Test {

    private val exampleInput = listOf(
        "11-22,95-115,998-1012,1188511880-1188511890,222220-222224," +
        "1698522-1698528,446443-446449,38593856-38593862,565653-565659," +
        "824824821-824824827,2121212118-2121212124"
    )

    @Test
    fun part1_example() {
        val d = Day02()
        d.setInput(exampleInput)
        assertEquals("1227775554", d.solvePart1())
    }

    @Test
    fun part2_example() {
        val d = Day02()
        d.setInput(exampleInput)
        assertEquals("4174379265", d.solvePart2())
    }
}