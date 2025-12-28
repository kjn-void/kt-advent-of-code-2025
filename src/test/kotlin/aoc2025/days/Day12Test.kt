package aoc2025.days

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

private const val day12Example = """
0:
###
##.
##.

1:
###
##.
.##

2:
.##
###
##.

3:
##.
###
##.

4:
###
#..
###

5:
###
.#.
###

4x4: 0 0 0 0 2 0
12x5: 1 0 1 0 2 2
12x5: 1 0 1 0 3 2
"""

class Day12Test {
    private fun splitLines(s: String): List<String> =
        s.trimIndent()
            .lines()
            .map { it.trimEnd() }
            .filter { it.isNotEmpty() }

    @Test
    fun part1_example() {
        val d = Day12()
        d.setInput(splitLines(day12Example))
        assertEquals("2", d.solvePart1())
    }

    @Test
    fun part2_stub() {
        val d = Day12()
        d.setInput(splitLines(day12Example))
        assertEquals("0", d.solvePart2())
    }
}