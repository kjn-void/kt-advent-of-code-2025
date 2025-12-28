package aoc2025.days

import aoc2025.registry.DayRegistry
import aoc2025.registry.Solution
import kotlin.math.abs
import kotlin.math.max

public data class Point(val x: Int, val y: Int)

private data class Edge(
    var x1: Int,
    var y1: Int,
    var x2: Int,
    var y2: Int,
    val hor: Boolean
)

class Day09 : Solution {

    private val reds = ArrayList<Point>()
    private var edges: List<Edge>? = null
    private var vertEdges: List<Edge>? = null

    companion object {
        init {
            DayRegistry.register(9) { Day09() }
        }
    }

    override fun setInput(lines: List<String>) {
        reds.clear()
        for (line in lines) {
            if (line.isBlank()) continue
            val parts = line.split(',')
            reds += Point(parts[0].toInt(), parts[1].toInt())
        }
        edges = null
        vertEdges = null
    }

    // ----------------------------------------------------------
    // Part 1
    // ----------------------------------------------------------

    override fun solvePart1(): String {
        return maxAreaInclusive(reds).toString()
    }

    private fun maxAreaInclusive(points: List<Point>): Long {
        val n = points.size
        if (n < 2) return 0L

        var best = 0L

        for (i in 0 until n) {
            val a = points[i]
            for (j in i + 1 until n) {
                val b = points[j]
                val dx = abs(a.x - b.x) + 1
                val dy = abs(a.y - b.y) + 1
                val area = dx.toLong() * dy.toLong()
                best = max(best, area)
            }
        }
        return best
    }

    // ----------------------------------------------------------
    // Part 2
    // ----------------------------------------------------------

    override fun solvePart2(): String {
        if (reds.size < 2) return "0"
        if (edges == null) buildEdges()

        var best = 0L
        val pts = reds

        for (i in pts.indices) {
            val a = pts[i]
            for (j in i + 1 until pts.size) {
                val b = pts[j]

                val x1 = minOf(a.x, b.x)
                val x2 = maxOf(a.x, b.x)
                val y1 = minOf(a.y, b.y)
                val y2 = maxOf(a.y, b.y)

                val dx = x2 - x1 + 1
                val dy = y2 - y1 + 1
                val area = dx.toLong() * dy.toLong()

                if (area <= best) continue

                val c3 = Point(x1, y2)
                val c4 = Point(x2, y1)

                if (!pointInsideOrOn(c3) || !pointInsideOrOn(c4)) continue
                if (rectangleCutByPolygon(x1, y1, x2, y2)) continue

                best = area
            }
        }
        return best.toString()
    }

    // ----------------------------------------------------------
    // Polygon edges
    // ----------------------------------------------------------

    private fun buildEdges() {
        val n = reds.size
        val all = ArrayList<Edge>(n)
        val verts = ArrayList<Edge>(n)

        for (i in 0 until n) {
            val a = reds[i]
            val b = reds[(i + 1) % n]

            if (a.y == b.y) {
                val x1 = minOf(a.x, b.x)
                val x2 = maxOf(a.x, b.x)
                all += Edge(x1, a.y, x2, a.y, true)
            } else {
                val y1 = minOf(a.y, b.y)
                val y2 = maxOf(a.y, b.y)
                val e = Edge(a.x, y1, a.x, y2, false)
                all += e
                verts += e
            }
        }

        edges = all
        vertEdges = verts
    }

    // ----------------------------------------------------------
    // Point in polygon
    // ----------------------------------------------------------

    private fun pointInsideOrOn(p: Point): Boolean {
        for (e in edges!!) {
            if (e.hor) {
                if (p.y == e.y1 && p.x in e.x1..e.x2) return true
            } else {
                if (p.x == e.x1 && p.y in e.y1..e.y2) return true
            }
        }
        return pointInsideStrict(p)
    }

    private fun pointInsideStrict(p: Point): Boolean {
        var crossings = 0
        for (e in vertEdges!!) {
            if (e.x1 > p.x && e.y1 <= p.y && p.y < e.y2) {
                crossings++
            }
        }
        return (crossings and 1) == 1
    }

    // ----------------------------------------------------------
    // Rectangle cut test
    // ----------------------------------------------------------

    private fun rectangleCutByPolygon(x1: Int, y1: Int, x2: Int, y2: Int): Boolean {
        if (x1 == x2 || y1 == y2) return false

        for (e in edges!!) {
            if (e.hor) {
                val y = e.y1
                if (y <= y1 || y >= y2) continue
                if (maxOf(e.x1, x1) < minOf(e.x2, x2)) return true
            } else {
                val x = e.x1
                if (x <= x1 || x >= x2) continue
                if (maxOf(e.y1, y1) < minOf(e.y2, y2)) return true
            }
        }
        return false
    }
}