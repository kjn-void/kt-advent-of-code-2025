package aoc2025.days

import aoc2025.registry.DayRegistry
import aoc2025.registry.Solution
import kotlin.math.max

class Day07 : Solution {

    private val grid = ArrayList<String>()
    private var rows = 0
    private var cols = 0
    private var startCol = 0

    companion object {
        init {
            DayRegistry.register(7) { Day07() }
        }
    }

    override fun setInput(lines: List<String>) {
        grid.clear()
        grid.addAll(lines)

        val maxC = grid[0].length
        for (i in grid.indices) {
            if (grid[i].length < maxC) {
                grid[i] = grid[i] + " ".repeat(maxC - grid[i].length)
            }
        }

        rows = grid.size
        cols = maxC

        // Find start column (S) in first row
        for (c in 0 until cols) {
            if (grid[0][c] == 'S') {
                startCol = c
                break
            }
        }
    }

    // ------------------------------------------------------------
    // Part 1 — count split events
    // ------------------------------------------------------------

    override fun solvePart1(): String {
        val bufA = BooleanArray(cols)
        val bufB = BooleanArray(cols)

        var active = bufA
        var next = bufB

        active[startCol] = true
        var splitCount = 0

        for (r in 1 until rows) {
            val row = grid[r]
            java.util.Arrays.fill(next, false)

            for (c in 0 until cols) {
                if (!active[c]) continue

                if (row[c] == '^') {
                    splitCount++
                    if (c > 0) next[c - 1] = true
                    if (c + 1 < cols) next[c + 1] = true
                } else {
                    next[c] = true
                }
            }

            // swap buffers
            val tmp = active
            active = next
            next = tmp
        }

        return splitCount.toString()
    }

    // ------------------------------------------------------------
    // Part 2 — count timelines
    // ------------------------------------------------------------

    override fun solvePart2(): String {
        val bufA = LongArray(cols)
        val bufB = LongArray(cols)

        var timelines = bufA
        var next = bufB

        timelines[startCol] = 1L

        for (r in 1 until rows) {
            val row = grid[r]
            java.util.Arrays.fill(next, 0L)

            for (c in 0 until cols) {
                val count = timelines[c]
                if (count == 0L) continue

                if (row[c] == '^') {
                    if (c > 0) next[c - 1] += count
                    if (c + 1 < cols) next[c + 1] += count
                } else {
                    next[c] += count
                }
            }

            // swap buffers
            val tmp = timelines
            timelines = next
            next = tmp
        }

        var total = 0L
        for (v in timelines) total += v

        return total.toString()
    }
}