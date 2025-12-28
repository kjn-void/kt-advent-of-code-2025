package aoc2025.days

import aoc2025.registry.DayRegistry
import aoc2025.registry.Solution
import kotlin.math.abs
import kotlin.math.round

class Day10 : Solution {

    data class MachineData(
        val targetLights: IntArray,          // 0/1
        val targetJoltage: IntArray,         // integers
        val buttons: List<IntArray>,         // each button -> affected indices
    )

    private val machines = ArrayList<MachineData>()

    companion object {
        init {
            DayRegistry.register(10) { Day10() }
        }
    }

    override fun setInput(lines: List<String>) {
        machines.clear()

        for (rawLine in lines) {
            val line = rawLine.trim()
            if (line.isEmpty()) continue

            // Format: [.##.] (3) (1,3) ... {3,5,4,7}
            val startBracket = line.indexOf('[')
            val endBracket = line.indexOf(']')
            if (startBracket == -1 || endBracket == -1 || endBracket <= startBracket) continue

            // 1) Lights
            val lightStr = line.substring(startBracket + 1, endBracket)
            val lights = IntArray(lightStr.length) { idx ->
                if (lightStr[idx] == '#') 1 else 0
            }

            // 2) Joltage { ... } optional
            val startBrace = line.indexOf('{')
            val endBrace = if (startBrace != -1) line.indexOf('}', startBrace + 1) else -1
            val joltage = if (startBrace != -1 && endBrace != -1 && endBrace > startBrace) {
                parseIntList(line.substring(startBrace, endBrace + 1)).toIntArray()
            } else {
                IntArray(0)
            }

            // 3) Buttons (...) between ']' and '{' (or end)
            val midSection = if (startBrace != -1 && startBrace > endBracket) {
                line.substring(endBracket + 1, startBrace)
            } else {
                line.substring(endBracket + 1)
            }

            val buttons = ArrayList<IntArray>()
            var cursor = 0
            while (true) {
                val pStart = midSection.indexOf('(', startIndex = cursor)
                if (pStart == -1) break
                val pEnd = midSection.indexOf(')', startIndex = pStart + 1)
                if (pEnd == -1) break

                val list = parseIntList(midSection.substring(pStart, pEnd + 1)).toIntArray()
                buttons.add(list)

                cursor = pEnd + 1
            }

            machines.add(
                MachineData(
                    targetLights = lights,
                    targetJoltage = joltage,
                    buttons = buttons,
                )
            )
        }
    }

    override fun solvePart1(): String {
        var total = 0
        for (m in machines) {
            if (m.targetLights.isEmpty() || m.buttons.isEmpty()) continue
            total += solveLights(m)
        }
        return total.toString()
    }

    override fun solvePart2(): String {
        var total = 0
        for (m in machines) {
            if (m.targetJoltage.isEmpty() || m.buttons.isEmpty()) continue
            total += solveJoltage(m)
        }
        return total.toString()
    }

    // ------------------------------------------------------------
    // Part 1: GF(2) linear system, minimal Hamming weight
    // ------------------------------------------------------------

    private fun solveLights(m: MachineData): Int {
        val n = m.targetLights.size
        val mButtons = m.buttons.size
        if (n == 0 || mButtons == 0) return 0

        // mat is N x (M+1) augmented matrix over GF(2)
        val mat = Array(n) { r ->
            IntArray(mButtons + 1).also { row ->
                row[mButtons] = m.targetLights[r]
            }
        }

        // Fill A part
        for (j in 0 until mButtons) {
            val btn = m.buttons[j]
            for (idx in btn) {
                if (idx in 0 until n) {
                    mat[idx][j] = 1
                }
            }
        }

        var pivotRow = 0
        val pivotCols = IntArray(mButtons) { -1 } // col -> row

        // Gaussian elimination over GF(2)
        for (col in 0 until mButtons) {
            if (pivotRow >= n) break

            var sel = -1
            for (r in pivotRow until n) {
                if (mat[r][col] == 1) {
                    sel = r
                    break
                }
            }
            if (sel == -1) continue

            if (sel != pivotRow) {
                val tmp = mat[pivotRow]
                mat[pivotRow] = mat[sel]
                mat[sel] = tmp
            }

            pivotCols[col] = pivotRow

            for (r in 0 until n) {
                if (r == pivotRow) continue
                if (mat[r][col] == 1) {
                    for (k in col..mButtons) {
                        mat[r][k] = mat[r][k] xor mat[pivotRow][k]
                    }
                }
            }

            pivotRow++
        }

        // Free variables
        val freeVars = ArrayList<Int>()
        for (c in 0 until mButtons) {
            if (pivotCols[c] == -1) {
                freeVars.add(c)
            }
        }

        var minPresses = mButtons + 1

        val freeCount = freeVars.size
        if (freeCount == 0) {
            // unique solution case
            val x = IntArray(mButtons)
            for (c in mButtons - 1 downTo 0) {
                val r = pivotCols[c]
                if (r != -1) {
                    var v = mat[r][mButtons]
                    for (k in c + 1 until mButtons) {
                        if (mat[r][k] == 1) v = v xor x[k]
                    }
                    x[c] = v
                }
            }
            var presses = 0
            for (v in x) presses += v
            return presses
        }

        // Brute force over free vars (typically small in AoC input)
        val count = 1L shl freeCount
        for (mask in 0L until count) {
            val x = IntArray(mButtons)

            // assign free vars
            for (i in 0 until freeCount) {
                if (((mask ushr i) and 1L) == 1L) {
                    x[freeVars[i]] = 1
                }
            }

            // back-sub pivot vars
            for (c in mButtons - 1 downTo 0) {
                val r = pivotCols[c]
                if (r != -1) {
                    var v = mat[r][mButtons]
                    for (k in c + 1 until mButtons) {
                        if (mat[r][k] == 1) v = v xor x[k]
                    }
                    x[c] = v
                }
            }

            var presses = 0
            for (v in x) presses += v
            if (presses < minPresses) {
                minPresses = presses
            }
        }

        return minPresses
    }

    // ------------------------------------------------------------
    // Part 2: RREF (Double) + integer search over free vars
    // ------------------------------------------------------------

    private fun solveJoltage(m: MachineData): Int {
        val n = m.targetJoltage.size
        val mButtons = m.buttons.size
        if (n == 0 || mButtons == 0) return 0

        val eps = 1e-9
        val epsRound = 1e-5

        // Augmented matrix N x (M+1)
        val mat = Array(n) { r ->
            DoubleArray(mButtons + 1).also { row ->
                row[mButtons] = m.targetJoltage[r].toDouble()
            }
        }

        for (j in 0 until mButtons) {
            val btn = m.buttons[j]
            for (idx in btn) {
                if (idx in 0 until n) {
                    mat[idx][j] = 1.0
                }
            }
        }

        var pivotRow = 0
        val pivotCols = IntArray(mButtons) { -1 }

        for (col in 0 until mButtons) {
            if (pivotRow >= n) break

            var sel = -1
            for (r in pivotRow until n) {
                if (abs(mat[r][col]) > eps) {
                    sel = r
                    break
                }
            }
            if (sel == -1) continue

            if (sel != pivotRow) {
                val tmp = mat[pivotRow]
                mat[pivotRow] = mat[sel]
                mat[sel] = tmp
            }

            pivotCols[col] = pivotRow

            // normalize pivot to 1
            val div = mat[pivotRow][col]
            for (k in col..mButtons) {
                mat[pivotRow][k] /= div
            }

            // eliminate from other rows
            for (r in 0 until n) {
                if (r == pivotRow) continue
                val f = mat[r][col]
                if (abs(f) < eps) continue
                for (k in col..mButtons) {
                    mat[r][k] -= f * mat[pivotRow][k]
                }
            }

            pivotRow++
        }

        // consistency check
        for (r in pivotRow until n) {
            if (abs(mat[r][mButtons]) > eps) {
                error("Day10 Part2: inconsistent real system")
            }
        }

        // free vars
        val freeVars = ArrayList<Int>()
        for (c in 0 until mButtons) {
            if (pivotCols[c] == -1) {
                freeVars.add(c)
            }
        }

        var maxTarget = 0
        for (v in m.targetJoltage) {
            if (v > maxTarget) maxTarget = v
        }
        val searchBound = maxTarget + 1

        var minTotal = Int.MAX_VALUE

        fun dfs(freeIdx: Int, x: DoubleArray, currentSum: Int) {
            if (currentSum >= minTotal) return

            if (freeIdx == freeVars.size) {
                var total = currentSum

                // compute pivot vars
                for (c in mButtons - 1 downTo 0) {
                    val r = pivotCols[c]
                    if (r == -1) continue

                    var value = mat[r][mButtons]
                    for (k in c + 1 until mButtons) {
                        val coeff = mat[r][k]
                        if (abs(coeff) > eps) {
                            value -= coeff * x[k]
                        }
                    }

                    if (value < -epsRound) return

                    val rounded = round(value)
                    if (abs(value - rounded) > epsRound) return

                    val iVal = rounded.toInt()
                    if (iVal < 0) return

                    x[c] = iVal.toDouble()
                    total += iVal

                    if (total >= minTotal) return
                }

                if (total < minTotal) {
                    minTotal = total
                }
                return
            }

            val col = freeVars[freeIdx]
            for (v in 0..searchBound) {
                val nextSum = currentSum + v
                if (nextSum >= minTotal) break
                x[col] = v.toDouble()
                dfs(freeIdx + 1, x, nextSum)
            }
        }

        val x = DoubleArray(mButtons)
        dfs(0, x, 0)

        if (minTotal == Int.MAX_VALUE) {
            error("Day10 Part2: no integer solution")
        }
        return minTotal
    }

    // ------------------------------------------------------------
    // Parsing helpers
    // ------------------------------------------------------------

    private fun parseIntList(s: String): List<Int> {
        val trimmed = s.trim()
        if (trimmed.length < 2) return emptyList()

        val inner = trimmed.substring(1, trimmed.length - 1).trim()
        if (inner.isEmpty()) return emptyList()

        return inner.split(',')
            .mapNotNull { part ->
                val p = part.trim()
                if (p.isEmpty()) null else p.toIntOrNull()
            }
    }
}