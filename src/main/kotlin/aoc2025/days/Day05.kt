package aoc2025.days

import aoc2025.registry.DayRegistry
import aoc2025.registry.Solution
import kotlin.math.max

class Day05 : Solution {

    private val ranges = ArrayList<LongArray>() // each: [start, end]
    private val ids = ArrayList<Long>()

    companion object {
        init {
            DayRegistry.register(5) { Day05() }
        }
    }

    override fun setInput(lines: List<String>) {
        ranges.clear()
        ids.clear()

        var section = 0

        for (raw in lines) {
            val s = raw.trim()
            if (s.isEmpty()) {
                section++
                continue
            }

            if (section == 0) {
                val dash = s.indexOf('-')
                val start = s.substring(0, dash).toLong()
                val end = s.substring(dash + 1).toLong()
                ranges.add(longArrayOf(start, end))
            } else {
                ids.add(s.toLong())
            }
        }

        if (ranges.isEmpty()) {
            return
        }

        // Sort by start
        ranges.sortWith(compareBy { it[0] })

        // Merge overlapping ranges
        val merged = ArrayList<LongArray>(ranges.size)

        var curStart = ranges[0][0]
        var curEnd = ranges[0][1]

        for (i in 1 until ranges.size) {
            val s = ranges[i][0]
            val e = ranges[i][1]

            if (s <= curEnd) {
                if (e > curEnd) {
                    curEnd = e
                }
            } else {
                merged.add(longArrayOf(curStart, curEnd))
                curStart = s
                curEnd = e
            }
        }

        merged.add(longArrayOf(curStart, curEnd))

        ranges.clear()
        ranges.addAll(merged)
    }

    // -------------------------------------------------------------------------
    // Part 1
    // -------------------------------------------------------------------------

    override fun solvePart1(): String {
        var count = 0
        for (id in ids) {
            if (isFresh(id)) {
                count++
            }
        }
        return count.toString()
    }

    private fun isFresh(id: Long): Boolean {
        var lo = 0
        var hi = ranges.size - 1

        while (lo <= hi) {
            val mid = (lo + hi) ushr 1
            val r = ranges[mid]

            when {
                id < r[0] -> hi = mid - 1
                id > r[1] -> lo = mid + 1
                else -> return true
            }
        }
        return false
    }

    // -------------------------------------------------------------------------
    // Part 2
    // -------------------------------------------------------------------------

    override fun solvePart2(): String {
        var total = 0L
        for (r in ranges) {
            total += (r[1] - r[0] + 1)
        }
        return total.toString()
    }
}