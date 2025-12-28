package aoc2025.days

import aoc2025.registry.DayRegistry
import aoc2025.registry.Solution
import kotlin.math.max

class Day06 : Solution {

    private val grid = ArrayList<String>()
    private var R = 0
    private var C = 0

    companion object {
        init {
            DayRegistry.register(6) { Day06() }
        }
    }

    // -----------------------------------------------------------
    // Parsing
    // -----------------------------------------------------------

    override fun setInput(lines: List<String>) {
        grid.clear()
        grid.addAll(lines)

        // Normalize row widths
        var maxC = 0
        for (row in grid) {
            if (row.length > maxC) {
                maxC = row.length
            }
        }

        for (i in grid.indices) {
            if (grid[i].length < maxC) {
                grid[i] = grid[i] + " ".repeat(maxC - grid[i].length)
            }
        }

        R = grid.size
        C = maxC
    }

    // -----------------------------------------------------------
    // Helpers
    // -----------------------------------------------------------

    private data class Block(val start: Int, val end: Int)

    /** Find contiguous column blocks separated by blank columns */
    private fun findBlocks(): List<Block> {
        val isBlank = BooleanArray(C)

        for (c in 0 until C) {
            var allSpace = true
            for (r in 0 until R) {
                if (grid[r][c] != ' ') {
                    allSpace = false
                    break
                }
            }
            isBlank[c] = allSpace
        }

        val blocks = ArrayList<Block>(64)
        var inBlock = false
        var start = 0

        for (c in 0 until C) {
            if (!isBlank[c]) {
                if (!inBlock) {
                    inBlock = true
                    start = c
                }
            } else {
                if (inBlock) {
                    inBlock = false
                    blocks.add(Block(start, c - 1))
                }
            }
        }

        if (inBlock) {
            blocks.add(Block(start, C - 1))
        }

        return blocks
    }

    /** Read operator from last row inside block */
    private fun getOperator(b: Block): Char {
        val row = grid[R - 1]
        for (c in b.start..b.end) {
            val ch = row[c]
            if (ch == '+' || ch == '*') {
                return ch
            }
        }
        return '*' // AoC guarantees this won't happen
    }

    // -----------------------------------------------------------
    // Number extractors
    // -----------------------------------------------------------

    /** Part 1: each row forms a number */
    private fun extractNumbersPart1(b: Block): LongArray {
        val nums = LongArray(R - 1)

        for (r in 0 until R - 1) {
            val s = grid[r].substring(b.start, b.end + 1).trim()
            nums[r] = s.toLong()
        }

        return nums
    }

    /** Part 2: each column forms a number */
    private fun extractNumbersPart2(b: Block): LongArray {
        val width = b.end - b.start + 1
        val nums = LongArray(width)

        for (c in b.start..b.end) {
            var value = 0L
            for (r in 0 until R - 1) {
                val ch = grid[r][c]
                if (ch != ' ') {
                    value = value * 10 + (ch - '0')
                }
            }
            nums[c - b.start] = value
        }

        return nums
    }

    // -----------------------------------------------------------
    // Shared evaluation
    // -----------------------------------------------------------

    private fun evaluateBlocks(
        extractor: (Block) -> LongArray
    ): Long {
        val blocks = findBlocks()
        var total = 0L

        for (b in blocks) {
            val nums = extractor(b)
            val op = getOperator(b)
            total += evalNumbers(nums, op)
        }

        return total
    }

    private fun evalNumbers(nums: LongArray, op: Char): Long {
        return if (op == '+') {
            var sum = 0L
            for (n in nums) {
                sum += n
            }
            sum
        } else {
            var prod = 1L
            for (n in nums) {
                prod *= n
            }
            prod
        }
    }

    // -----------------------------------------------------------
    // Part 1
    // -----------------------------------------------------------

    override fun solvePart1(): String {
        return evaluateBlocks(::extractNumbersPart1).toString()
    }

    // -----------------------------------------------------------
    // Part 2
    // -----------------------------------------------------------

    override fun solvePart2(): String {
        return evaluateBlocks(::extractNumbersPart2).toString()
    }
}