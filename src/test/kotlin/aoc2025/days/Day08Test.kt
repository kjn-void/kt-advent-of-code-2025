package aoc2025.days

import kotlin.test.Test
import kotlin.test.assertEquals

class Day08Test {

    private val example = listOf(
        "162,817,812",
        "57,618,57",
        "906,360,560",
        "592,479,940",
        "352,342,300",
        "466,668,158",
        "542,29,236",
        "431,825,988",
        "739,650,466",
        "52,470,668",
        "216,146,977",
        "819,987,18",
        "117,168,530",
        "805,96,715",
        "346,949,466",
        "970,615,88",
        "941,993,340",
        "862,61,35",
        "984,92,344",
        "425,690,689",
    )

    @Test
    fun part1_example() {
        val d = Day08()
        d.setInput(example)

        val got = d.solvePart1()
        val want = "0"

        assertEquals(want, got)
    }

    @Test
    fun part2_example() {
        val d = Day08()
        d.setInput(example)

        val got = d.solvePart2()
        val want = "25272"

        assertEquals(want, got)
    }
}