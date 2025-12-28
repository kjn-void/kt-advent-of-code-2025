package aoc2025.days

import aoc2025.registry.DayRegistry
import aoc2025.registry.Solution
import java.util.Arrays
import kotlin.math.abs

class Day08 : Solution {

    private val points = ArrayList<Vec3>()
    private var edges: Array<Edge>? = null

    companion object {
        init {
            DayRegistry.register(8) { Day08() }
        }
    }

    // ------------------------------------------------------------
    // Parsing
    // ------------------------------------------------------------

    override fun setInput(lines: List<String>) {
        points.clear()

        for (line in lines) {
            val s = line.trim()
            if (s.isEmpty()) continue
            points += parseVec3(s)
        }

        edges = buildSortedEdges(points)
    }

    private fun parseVec3(line: String): Vec3 {
        val parts = line.split(',')
        return Vec3(
            parts[0].toLong(),
            parts[1].toLong(),
            parts[2].toLong()
        )
    }

    // ------------------------------------------------------------
    // Solve Part 1 & Part 2
    // ------------------------------------------------------------

    override fun solvePart1(): String {
        val e = edges ?: return "0"
        val sizes = runConnections(points.size, e, 1000)
        if (sizes.size < 3) return "0"
        val result = sizes[0] * sizes[1] * sizes[2]
        return result.toString()
    }

    override fun solvePart2(): String {
        val e = edges ?: return "0"
        if (points.size < 2) return "0"

        val (i, j) = runUntilSingleCircuit(points.size, e)
        return (points[i].x * points[j].x).toString()
    }

    // ------------------------------------------------------------
    // Core helpers
    // ------------------------------------------------------------

    private fun runConnections(
        n: Int,
        edges: Array<Edge>,
        k: Int
    ): IntArray {
        if (n == 0) return IntArray(0)

        val uf = DSU(n)
        val limit = minOf(k, edges.size)

        for (i in 0 until limit) {
            val e = edges[i]
            uf.union(e.i, e.j)
        }

        val seen = HashMap<Int, Int>(n)
        for (i in 0 until n) {
            val r = uf.find(i)
            seen[r] = uf.size[r]
        }

        val sizes = IntArray(seen.size)
        var idx = 0
        for (v in seen.values) sizes[idx++] = v

        Arrays.sort(sizes)
        sizes.reverse()
        return sizes
    }

    private fun runUntilSingleCircuit(
        n: Int,
        edges: Array<Edge>
    ): Pair<Int, Int> {
        if (n <= 1) return 0 to 0

        val uf = DSU(n)
        var components = n
        var lastI = 0
        var lastJ = 0

        for (e in edges) {
            if (uf.union(e.i, e.j)) {
                components--
                lastI = e.i
                lastJ = e.j
                if (components == 1) break
            }
        }
        return lastI to lastJ
    }

    // ------------------------------------------------------------
    // Data + helpers
    // ------------------------------------------------------------

    private data class Vec3(val x: Long, val y: Long, val z: Long)

    private data class Edge(
        val dist2: Long,
        val i: Int,
        val j: Int
    )

    private fun squaredDist(a: Vec3, b: Vec3): Long {
        val dx = a.x - b.x
        val dy = a.y - b.y
        val dz = a.z - b.z
        return dx * dx + dy * dy + dz * dz
    }

    private fun buildSortedEdges(points: List<Vec3>): Array<Edge> {
        val n = points.size
        if (n < 2) return emptyArray()

        val total = n * (n - 1) / 2
        val list = ArrayList<Edge>(total)

        for (i in 0 until n) {
            val pi = points[i]
            for (j in i + 1 until n) {
                val pj = points[j]
                list += Edge(squaredDist(pi, pj), i, j)
            }
        }

        list.sortBy { it.dist2 }
        return list.toTypedArray()
    }

    // ------------------------------------------------------------
    // DSU
    // ------------------------------------------------------------

    private class DSU(n: Int) {
        private val parent = IntArray(n) { it }
        val size = IntArray(n) { 1 }

        fun find(x: Int): Int {
            var v = x
            while (parent[v] != v) {
                parent[v] = parent[parent[v]]
                v = parent[v]
            }
            return v
        }

        fun union(a: Int, b: Int): Boolean {
            var ra = find(a)
            var rb = find(b)
            if (ra == rb) return false

            if (size[ra] < size[rb]) {
                val t = ra; ra = rb; rb = t
            }
            parent[rb] = ra
            size[ra] += size[rb]
            return true
        }
    }
}