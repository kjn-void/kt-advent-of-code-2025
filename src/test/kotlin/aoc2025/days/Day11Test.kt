package aoc2025.days

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day11Test {

    private fun splitLines(s: String): List<String> =
        s.trimIndent()
            .lines()
            .map { it.trimEnd() }
            .filter { it.isNotEmpty() }

    @Test
    fun part1_example() {
        val input = splitLines(
            """
            aaa: you hhh
            you: bbb ccc
            bbb: ddd eee
            ccc: ddd eee fff
            ddd: ggg
            eee: out
            fff: out
            ggg: out
            hhh: ccc fff iii
            iii: out
            """
        )

        val d = Day11()
        d.setInput(input)

        assertEquals("5", d.solvePart1())
    }

    @Test
    fun part2_example() {
        val input = splitLines(
            """
            svr: aaa bbb
            aaa: fft
            fft: ccc
            bbb: tty
            tty: ccc
            ccc: ddd eee
            ddd: hub
            hub: fff
            eee: dac
            dac: fff
            fff: ggg hhh
            ggg: out
            hhh: out
            """
        )

        val d = Day11()
        d.setInput(input)

        assertEquals("2", d.solvePart2())
    }
}