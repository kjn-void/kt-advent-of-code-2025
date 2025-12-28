package aoc2025.days

import aoc2025.registry.DayRegistry
import aoc2025.registry.Solution

class Day11 : Solution {

    private var adj: Map<String, List<String>> = emptyMap()

    companion object {
        init {
            DayRegistry.register(11) { Day11() }
        }
    }

    override fun setInput(lines: List<String>) {
        val map = HashMap<String, MutableList<String>>()

        for (raw in lines) {
            val line = raw.trim()
            if (line.isEmpty()) continue

            // Format: "aaa: you hhh"
            val parts = line.split(":", limit = 2)
            val from = parts[0].trim()

            val outs = ArrayList<String>()
            if (parts.size == 2) {
                val right = parts[1].trim()
                if (right.isNotEmpty()) {
                    outs.addAll(right.split(Regex("\\s+")))
                }
            }

            map[from] = outs
        }

        adj = map
    }

    // -----------------------------------------------------------
    // Part 1 — count all paths from "you" to "out"
    // -----------------------------------------------------------

    private fun countPathsFrom(
        node: String,
        memo: MutableMap<String, Long>,
        visiting: MutableSet<String>
    ): Long {
        // Base case
        if (node == "out") {
            return 1L
        }

        memo[node]?.let { return it }

        // Cycle guard (should not occur in valid input)
        if (!visiting.add(node)) {
            return 0L
        }

        var total = 0L
        for (next in adj[node].orEmpty()) {
            total += countPathsFrom(next, memo, visiting)
        }

        visiting.remove(node)
        memo[node] = total
        return total
    }

    override fun solvePart1(): String {
        if (adj.isEmpty()) return "0"

        val memo = HashMap<String, Long>()
        val visiting = HashSet<String>()

        val total = countPathsFrom("you", memo, visiting)
        return total.toString()
    }

    // -----------------------------------------------------------
    // Part 2 — paths from "svr" to "out" that visit both
    //          "dac" and "fft"
    // -----------------------------------------------------------

    private data class State(
        val node: String,
        val mask: Int // bit 0 = dac, bit 1 = fft
    )

    private fun countPathsWithRequired(
        start: String,
        end: String,
        need1: String,
        need2: String
    ): Long {
        if (adj.isEmpty()) return 0L

        val memo = HashMap<State, Long>()

        var startMask = 0
        if (start == need1) startMask = startMask or 1
        if (start == need2) startMask = startMask or 2

        fun dfs(node: String, mask: Int): Long {
            val state = State(node, mask)
            memo[state]?.let { return it }

            if (node == end) {
                return if (mask == 3) 1L else 0L
            }

            var total = 0L
            for (next in adj[node].orEmpty()) {
                var nextMask = mask
                if (next == need1) nextMask = nextMask or 1
                if (next == need2) nextMask = nextMask or 2
                total += dfs(next, nextMask)
            }

            memo[state] = total
            return total
        }

        return dfs(start, startMask)
    }

    override fun solvePart2(): String {
        val total = countPathsWithRequired(
            start = "svr",
            end = "out",
            need1 = "dac",
            need2 = "fft"
        )
        return total.toString()
    }
}