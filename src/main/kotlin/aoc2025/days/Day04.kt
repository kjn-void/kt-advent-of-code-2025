package aoc2025.days

import aoc2025.registry.DayRegistry
import aoc2025.registry.Solution

class Day04 : Solution {

    private val grid = ArrayList<String>()
    private var rows = 0
    private var cols = 0

    companion object {
        init {
            DayRegistry.register(4) { Day04() }
        }

        // 8 directions
        private val DIRS = arrayOf(
            intArrayOf(-1, -1), intArrayOf(-1, 0), intArrayOf(-1, 1),
            intArrayOf(0, -1),                    intArrayOf(0, 1),
            intArrayOf(1, -1),  intArrayOf(1, 0), intArrayOf(1, 1)
        )
    }

    override fun setInput(lines: List<String>) {
        grid.clear()
        grid.addAll(lines)

        rows = grid.size
        cols = if (rows > 0) grid[0].length else 0
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private fun makeBoolGrid(): Array<BooleanArray> {
        val out = Array(rows) { BooleanArray(cols) }
        for (r in 0 until rows) {
            val src = grid[r]
            for (c in 0 until cols) {
                out[r][c] = src[c] == '@'
            }
        }
        return out
    }

    private fun computeDegrees(on: Array<BooleanArray>): Array<IntArray> {
        val deg = Array(rows) { IntArray(cols) }

        for (r in 0 until rows) {
            for (c in 0 until cols) {
                if (!on[r][c]) continue

                var cnt = 0
                for (d in DIRS) {
                    val nr = r + d[0]
                    val nc = c + d[1]
                    if (
                        nr in 0 until rows &&
                        nc in 0 until cols &&
                        on[nr][nc]
                    ) {
                        cnt++
                    }
                }
                deg[r][c] = cnt
            }
        }
        return deg
    }

    private fun countAdjacent(r: Int, c: Int): Int {
        var count = 0
        for (d in DIRS) {
            val nr = r + d[0]
            val nc = c + d[1]
            if (
                nr in 0 until rows &&
                nc in 0 until cols &&
                grid[nr][nc] == '@'
            ) {
                count++
            }
        }
        return count
    }

    // -------------------------------------------------------------------------
    // Part 1
    // -------------------------------------------------------------------------

    override fun solvePart1(): String {
        if (rows == 0 || cols == 0) return "0"

        var total = 0

        for (r in 0 until rows) {
            for (c in 0 until cols) {
                if (grid[r][c] != '@') continue
                if (countAdjacent(r, c) < 4) {
                    total++
                }
            }
        }

        return total.toString()
    }

    // -------------------------------------------------------------------------
    // Part 2
    // -------------------------------------------------------------------------

    override fun solvePart2(): String {
        if (rows == 0 || cols == 0) return "0"

        val on = makeBoolGrid()
        val deg = computeDegrees(on)

        // Flat queue for speed
        val qr = IntArray(rows * cols)
        val qc = IntArray(rows * cols)
        var qh = 0
        var qt = 0

        for (r in 0 until rows) {
            for (c in 0 until cols) {
                if (on[r][c] && deg[r][c] < 4) {
                    qr[qt] = r
                    qc[qt] = c
                    qt++
                }
            }
        }

        var removed = 0

        while (qh < qt) {
            val r = qr[qh]
            val c = qc[qh]
            qh++

            if (!on[r][c]) continue

            on[r][c] = false
            removed++

            for (d in DIRS) {
                val nr = r + d[0]
                val nc = c + d[1]

                if (nr !in 0 until rows || nc !in 0 until cols) continue
                if (!on[nr][nc]) continue

                val newDeg = --deg[nr][nc]
                if (newDeg == 3) {
                    qr[qt] = nr
                    qc[qt] = nc
                    qt++
                }
            }
        }

        return removed.toString()
    }
}