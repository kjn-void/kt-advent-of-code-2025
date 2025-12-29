package aoc2025.days

import aoc2025.splitLines
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