package aoc2025.days

import aoc2025.registry.DayRegistry
import aoc2025.registry.Solution
import kotlin.math.abs

class Day01 : Solution {

    private val moves = ArrayList<Int>()

    companion object {
        init {
            DayRegistry.register(1) { Day01() }
        }
    }

    override fun setInput(lines: List<String>) {
        moves.clear()

        for (raw in lines) {
            val line = raw.trim()
            if (line.isEmpty()) continue

            val dir = line[0]
            val value = line.substring(1).toInt()

            moves += if (dir == 'L') -value else value
        }
    }

    private fun mod100(n: Int): Int {
        val r = n % 100
        return if (r < 0) r + 100 else r
    }

    override fun solvePart1(): String {
        var pos = 50
        var countZero = 0

        for (delta in moves) {
            pos = mod100(pos + delta)
            if (pos == 0) countZero++
        }

        return countZero.toString()
    }

    override fun solvePart2(): String {
        var pos = 50
        var countZero = 0

        for (delta in moves) {
            val step = if (delta < 0) -1 else 1
            repeat(abs(delta)) {
                pos += step
                if (pos < 0) pos += 100
                else if (pos >= 100) pos -= 100
                if (pos == 0) countZero++
            }
        }

        return countZero.toString()
    }
}