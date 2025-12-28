package aoc2025.days

import aoc2025.registry.DayRegistry
import aoc2025.registry.Solution

class Day03 : Solution {

    private val banks = ArrayList<IntArray>()

    companion object {
        init {
            DayRegistry.register(3) { Day03() }
        }
    }

    override fun setInput(lines: List<String>) {
        banks.clear()

        for (line in lines) {
            val n = line.length
            val digits = IntArray(n)
            for (i in 0 until n) {
                digits[i] = line[i] - '0'
            }
            banks += digits
        }
    }

    override fun solvePart1(): String =
        maxJoltage(2)

    override fun solvePart2(): String =
        maxJoltage(12)

    private fun maxJoltage(pick: Int): String {
        var total = 0L

        for (bank in banks) {
            val n = bank.size
            var need = pick

            // manual stack (faster than MutableList)
            val stack = IntArray(pick)
            var top = 0

            for (i in 0 until n) {
                val dig = bank[i]
                val remaining = n - i

                while (
                    top > 0 &&
                    remaining > need &&
                    stack[top - 1] < dig
                ) {
                    top--
                    need++
                }

                if (need > 0) {
                    stack[top++] = dig
                    need--
                }
            }

            total += stackToNumber(stack, top)
        }

        return total.toString()
    }

    private fun stackToNumber(stack: IntArray, size: Int): Long {
        var value = 0L
        for (i in 0 until size) {
            value = value * 10 + stack[i]
        }
        return value
    }
}