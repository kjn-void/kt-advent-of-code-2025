package aoc2025.days

import aoc2025.registry.DayRegistry
import aoc2025.registry.Solution
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import kotlin.math.abs
import kotlin.math.round

class Day10 : Solution {

    data class MachineData(
        val targetLights: IntArray,
        val targetJoltage: IntArray,
        val buttons: List<IntArray>,
    )

    private val machines = ArrayList<MachineData>()

    companion object {
        init {
            DayRegistry.register(10) { Day10() }
        }
    }

    // ------------------------------------------------------------
    // Parsing
    // ------------------------------------------------------------

    override fun setInput(lines: List<String>) {
        machines.clear()

        for (rawLine in lines) {
            val line = rawLine.trim()
            if (line.isEmpty()) continue

            val lb = line.indexOf('[')
            val rb = line.indexOf(']')
            if (lb == -1 || rb == -1 || rb <= lb) continue

            val lightStr = line.substring(lb + 1, rb)
            val lights = IntArray(lightStr.length) {
                if (lightStr[it] == '#') 1 else 0
            }

            val joltage =
                if ('{' in line) {
                    val s = line.indexOf('{')
                    val e = line.indexOf('}', s + 1)
                    if (e > s) parseIntList(line.substring(s, e + 1)).toIntArray()
                    else IntArray(0)
                } else IntArray(0)

            val mid = if ('{' in line) line.substring(rb + 1, line.indexOf('{')) else line.substring(rb + 1)

            val buttons = ArrayList<IntArray>()
            var pos = 0
            while (true) {
                val p1 = mid.indexOf('(', pos)
                if (p1 == -1) break
                val p2 = mid.indexOf(')', p1 + 1)
                if (p2 == -1) break
                buttons.add(parseIntList(mid.substring(p1, p2 + 1)).toIntArray())
                pos = p2 + 1
            }

            machines.add(MachineData(lights, joltage, buttons))
        }
    }

    // ------------------------------------------------------------
    // Day interface
    // ------------------------------------------------------------

    override fun solvePart1(): String {
        var total = 0
        for (m in machines) {
            if (m.targetLights.isNotEmpty() && m.buttons.isNotEmpty()) {
                total += solveLights(m)
            }
        }
        return total.toString()
    }

    override fun solvePart2(): String {
        val cpuCount = Runtime.getRuntime().availableProcessors()
        val pool = Executors.newFixedThreadPool(cpuCount)

        try {
            val futures = machines
                .filter { it.targetJoltage.isNotEmpty() && it.buttons.isNotEmpty() }
                .map { m ->
                    pool.submit(Callable { solveJoltage(m) })
                }

            var total = 0
            for (f in futures) {
                total += f.get()
            }
            return total.toString()
        } finally {
            pool.shutdown()
        }
    }

    // ------------------------------------------------------------
    // Part 1 — unchanged
    // ------------------------------------------------------------

    private fun solveLights(m: MachineData): Int {
        val n = m.targetLights.size
        val M = m.buttons.size
        val mat = Array(n) { IntArray(M + 1) }

        for (i in 0 until n) mat[i][M] = m.targetLights[i]
        for (j in 0 until M) {
            for (idx in m.buttons[j]) {
                if (idx in 0 until n) mat[idx][j] = 1
            }
        }

        val pivot = IntArray(M) { -1 }
        var pr = 0

        for (c in 0 until M) {
            if (pr >= n) break
            var sel = -1
            for (r in pr until n) if (mat[r][c] == 1) { sel = r; break }
            if (sel == -1) continue

            val tmp = mat[pr]; mat[pr] = mat[sel]; mat[sel] = tmp
            pivot[c] = pr

            for (r in 0 until n) {
                if (r != pr && mat[r][c] == 1) {
                    for (k in c..M) mat[r][k] = mat[r][k] xor mat[pr][k]
                }
            }
            pr++
        }

        val free = pivot.withIndex().filter { it.value == -1 }.map { it.index }
        var best = M + 1

        val limit = 1 shl free.size
        for (mask in 0 until limit) {
            val x = IntArray(M)
            for (i in free.indices) if ((mask shr i) and 1 == 1) x[free[i]] = 1
            for (c in M - 1 downTo 0) {
                val r = pivot[c]
                if (r != -1) {
                    var v = mat[r][M]
                    for (k in c + 1 until M) if (mat[r][k] == 1) v = v xor x[k]
                    x[c] = v
                }
            }
            best = minOf(best, x.sum())
        }
        return best
    }

    // ------------------------------------------------------------
    // Part 2 — unchanged math, parallelized externally
    // ------------------------------------------------------------

    private fun solveJoltage(m: MachineData): Int {
        val n = m.targetJoltage.size
        val M = m.buttons.size
        val eps = 1e-9
        val epsR = 1e-5

        val mat = Array(n) { DoubleArray(M + 1) }
        for (i in 0 until n) mat[i][M] = m.targetJoltage[i].toDouble()
        for (j in 0 until M)
            for (idx in m.buttons[j])
                if (idx in 0 until n) mat[idx][j] = 1.0

        val pivot = IntArray(M) { -1 }
        var pr = 0

        for (c in 0 until M) {
            if (pr >= n) break
            var sel = -1
            for (r in pr until n) if (abs(mat[r][c]) > eps) { sel = r; break }
            if (sel == -1) continue

            val tmp = mat[pr]; mat[pr] = mat[sel]; mat[sel] = tmp
            pivot[c] = pr
            val div = mat[pr][c]
            for (k in c..M) mat[pr][k] /= div
            for (r in 0 until n) {
                if (r != pr && abs(mat[r][c]) > eps) {
                    val f = mat[r][c]
                    for (k in c..M) mat[r][k] -= f * mat[pr][k]
                }
            }
            pr++
        }

        val free = pivot.withIndex().filter { it.value == -1 }.map { it.index }
        val bound = (m.targetJoltage.maxOrNull() ?: 0) + 1
        var best = Int.MAX_VALUE
        val x = DoubleArray(M)

        fun dfs(i: Int, sum: Int) {
            if (sum >= best) return
            if (i == free.size) {
                var total = sum
                for (c in M - 1 downTo 0) {
                    val r = pivot[c]
                    if (r != -1) {
                        var v = mat[r][M]
                        for (k in c + 1 until M) v -= mat[r][k] * x[k]
                        if (v < -epsR) return
                        val iv = round(v)
                        if (abs(v - iv) > epsR) return
                        total += iv.toInt()
                    }
                }
                best = minOf(best, total)
                return
            }
            val col = free[i]
            for (v in 0..bound) {
                x[col] = v.toDouble()
                dfs(i + 1, sum + v)
            }
        }

        dfs(0, 0)
        return best
    }

    private fun parseIntList(s: String): List<Int> =
        s.trim().removeSurrounding("(", ")")
            .removeSurrounding("{", "}")
            .split(',')
            .mapNotNull { it.trim().toIntOrNull() }
}