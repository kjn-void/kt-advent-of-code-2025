package aoc2025.days

import aoc2025.registry.DayRegistry
import aoc2025.registry.Solution
import kotlin.math.max
import kotlin.math.min

class Day02 : Solution {

    private val ranges = ArrayList<Pair<Long, Long>>()

    companion object {
        private val p10: LongArray = LongArray(20).also {
            var x = 1L
            for (i in it.indices) {
                it[i] = x
                x *= 10L
            }
        }

        init {
            DayRegistry.register(2) { Day02() }
        }
    }

    override fun setInput(lines: List<String>) {
        ranges.clear()
        if (lines.isEmpty()) return

        val parts = lines[0].trim().split(',')
        for (part in parts) {
            if (part.isEmpty()) continue
            val dash = part.indexOf('-')
            val lo = part.substring(0, dash).toLong()
            val hi = part.substring(dash + 1).toLong()
            ranges.add(lo to hi)
        }
    }

    // smallest repeating block size of numeric string s
    private fun smallestBlock(s: String): Int {
        val n = s.length
        for (k in 1..n / 2) {
            if (n % k != 0) continue
            val block = s.substring(0, k)
            var ok = true
            var i = k
            while (i < n) {
                if (s.substring(i, i + k) != block) {
                    ok = false
                    break
                }
                i += k
            }
            if (ok) return k
        }
        return n
    }

    // ---------------- Part 1 ----------------

    override fun solvePart1(): String {
        var sum = 0L

        for ((L, R) in ranges) {
            val maxDigits = R.toString().length

            for (k in 1..maxDigits / 2) {
                val base = p10[k]
                val repFactor = base + 1

                val dLo = p10[k - 1]
                val dHi = base - 1

                var candMin = (L + repFactor - 1) / repFactor
                var candMax = R / repFactor

                candMin = max(candMin, dLo)
                candMax = min(candMax, dHi)

                if (candMin > candMax) continue

                var dd = candMin
                while (dd <= candMax) {
                    sum += dd * repFactor
                    dd++
                }
            }
        }

        return sum.toString()
    }

    // ---------------- Part 2 ----------------

    override fun solvePart2(): String {
        var total = 0L

        for ((L, R) in ranges) {
            val maxDigits = R.toString().length

            for (totalDigits in 2..maxDigits) {
                val tenLen = p10[totalDigits]

                for (m in 2..totalDigits) {
                    if (totalDigits % m != 0) continue

                    val k = totalDigits / m
                    val baseK = p10[k]
                    val repFactor = (tenLen - 1) / (baseK - 1)

                    val dLo = p10[k - 1]
                    val dHi = baseK - 1

                    var candMin = (L + repFactor - 1) / repFactor
                    var candMax = R / repFactor

                    candMin = max(candMin, dLo)
                    candMax = min(candMax, dHi)

                    if (candMin > candMax) continue

                    var dd = candMin
                    while (dd <= candMax) {
                        val ds = dd.toString()
                        if (smallestBlock(ds) == ds.length) {
                            total += dd * repFactor
                        }
                        dd++
                    }
                }
            }
        }

        return total.toString()
    }
}