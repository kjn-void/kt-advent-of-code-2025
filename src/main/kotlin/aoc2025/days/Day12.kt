package aoc2025.days

import aoc2025.registry.DayRegistry
import aoc2025.registry.Solution
import kotlin.math.max
import kotlin.math.min

// --- Data types -------------------------------------------------------------

private data class Variant(
    val width: Int,
    val height: Int,
    val cells: List<Point>
)

private data class Shape(
    val area: Int,
    val variants: List<Variant>
)

private data class Region(
    val width: Int,
    val height: Int,
    val counts: IntArray
)

class Day12 : Solution {

    private var shapes: List<Shape> = emptyList()
    private var regions: List<Region> = emptyList()

    companion object {
        private const val SMALL_BOARD_MAX_AREA = 15 * 15

        init {
            DayRegistry.register(12) { Day12() }
        }
    }

    // --- Parsing ------------------------------------------------------------

    override fun setInput(lines: List<String>) {
        val shapeList = mutableListOf<Shape>()
        val regionList = mutableListOf<Region>()

        var i = 0
        while (i < lines.size && lines[i].trim().isEmpty()) i++

        // --- Shapes ---
        while (i < lines.size) {
            val line = lines[i].trim()
            if (line.isEmpty()) {
                i++
                continue
            }
            if (isRegionLine(line)) break
            if (!line.endsWith(":")) {
                i++
                continue
            }

            i++
            val rows = mutableListOf<String>()
            while (i < lines.size) {
                val s = lines[i].trimEnd('\r', '\n')
                if (s.trim().isEmpty()) {
                    i++
                    break
                }
                val t = s.trim()
                if (t.endsWith(":") || isRegionLine(t)) break
                rows.add(t)
                i++
            }
            if (rows.isNotEmpty()) {
                shapeList.add(buildShape(rows))
            }
        }

        // --- Regions ---
        while (i < lines.size) {
            val line = lines[i].trim()
            i++
            if (line.isEmpty()) continue
            if (!isRegionLine(line)) continue

            val parts = line.split(":", limit = 2)
            val dim = parts[0].trim().split("x")
            val w = dim[0].toInt()
            val h = dim[1].toInt()

            val counts = parts[1].trim()
                .split(Regex("\\s+"))
                .map { it.toInt() }
                .toIntArray()

            regionList.add(Region(w, h, counts))
        }

        shapes = shapeList
        regions = regionList
    }

    private fun isRegionLine(s: String): Boolean {
        val idx = s.indexOf(':')
        if (idx <= 0) return false
        val head = s.substring(0, idx)
        val parts = head.split("x")
        if (parts.size != 2) return false
        return parts[0].trim().toIntOrNull() != null &&
               parts[1].trim().toIntOrNull() != null
    }

    // --- Shape construction -------------------------------------------------

    private fun buildShape(rows: List<String>): Shape {
        val h0 = rows.size
        val w0 = rows.maxOf { it.length }
        val grid = Array(h0) { y ->
            BooleanArray(w0) { x ->
                x < rows[y].length && rows[y][x] == '#'
            }
        }

        val seen = HashSet<String>()
        val variants = mutableListOf<Variant>()
        var g = grid

        repeat(4) {
            repeat(2) { f ->
                val gf = if (f == 0) g else flipGridH(g)
                val v = gridToVariant(gf)
                if (v.cells.isNotEmpty()) {
                    val key = variantKey(v)
                    if (seen.add(key)) variants.add(v)
                }
            }
            g = rotateGrid(g)
        }

        val area = if (variants.isNotEmpty()) variants[0].cells.size else 0
        return Shape(area, variants)
    }

    private fun rotateGrid(grid: Array<BooleanArray>): Array<BooleanArray> {
        val h = grid.size
        val w = grid[0].size
        return Array(w) { y ->
            BooleanArray(h) { x ->
                grid[h - 1 - x][y]
            }
        }
    }

    private fun flipGridH(grid: Array<BooleanArray>): Array<BooleanArray> {
        val h = grid.size
        val w = grid[0].size
        return Array(h) { y ->
            BooleanArray(w) { x ->
                grid[y][w - 1 - x]
            }
        }
    }

    private fun gridToVariant(grid: Array<BooleanArray>): Variant {
        val h = grid.size
        val w = grid[0].size

        var minX = w
        var minY = h
        var maxX = -1
        var maxY = -1

        for (y in 0 until h)
            for (x in 0 until w)
                if (grid[y][x]) {
                    minX = min(minX, x)
                    maxX = max(maxX, x)
                    minY = min(minY, y)
                    maxY = max(maxY, y)
                }

        if (maxX < minX) return Variant(0, 0, emptyList())

        val cells = mutableListOf<Point>()
        for (y in minY..maxY)
            for (x in minX..maxX)
                if (grid[y][x])
                    cells.add(Point(x - minX, y - minY))

        return Variant(maxX - minX + 1, maxY - minY + 1, cells)
    }

    private fun variantKey(v: Variant): String =
        buildString {
            append(v.width).append('x').append(v.height).append(':')
            for (c in v.cells) append(c.x).append(',').append(c.y).append(';')
        }

    // --- Solver -------------------------------------------------------------

    override fun solvePart1(): String =
        regions.count { regionCanFit(it) }.toString()

    override fun solvePart2(): String = "0"

    private fun regionCanFit(r: Region): Boolean {
        var totalArea = 0
        for (i in r.counts.indices) {
            if (i >= shapes.size) break
            totalArea += r.counts[i] * shapes[i].area
        }
        if (totalArea > r.width * r.height) return false

        return if (r.width * r.height <= SMALL_BOARD_MAX_AREA)
            canTileRegionSmall(r)
        else
            true
    }

    // --- Exact tiling for small regions ------------------------------------

    private fun canTileRegionSmall(r: Region): Boolean {
        val w = r.width
        val h = r.height
        val board = BooleanArray(w * h)
        val counts = r.counts.clone()

        val placements = Array(shapes.size) { mutableListOf<IntArray>() }

        for (si in shapes.indices) {
            for (v in shapes[si].variants) {
                for (by in 0..h - v.height)
                    for (bx in 0..w - v.width) {
                        val cells = IntArray(v.cells.size)
                        var ok = true
                        for (i in v.cells.indices) {
                            val c = v.cells[i]
                            val idx = (by + c.y) * w + (bx + c.x)
                            cells[i] = idx
                        }
                        if (ok) placements[si].add(cells)
                    }
            }
        }

        return backtrack(board, counts, placements, w)
    }

    private fun backtrack(
        board: BooleanArray,
        counts: IntArray,
        placements: Array<MutableList<IntArray>>,
        w: Int
    ): Boolean {
        var remainingArea = 0
        var done = true
        for (i in counts.indices) {
            if (counts[i] > 0) {
                done = false
                remainingArea += counts[i] * shapes[i].area
            }
        }
        if (done) return true

        val free = board.count { !it }
        if (remainingArea > free) return false

        var bestShape = -1
        var bestCount = Int.MAX_VALUE

        for (i in counts.indices) {
            if (counts[i] <= 0) continue
            var feasible = 0
            for (pl in placements[i]) {
                if (pl.all { !board[it] }) {
                    feasible++
                    if (feasible >= bestCount) break
                }
            }
            if (feasible == 0) return false
            if (feasible < bestCount) {
                bestCount = feasible
                bestShape = i
            }
        }

        counts[bestShape]--
        for (pl in placements[bestShape]) {
            if (pl.any { board[it] }) continue
            for (idx in pl) board[idx] = true
            if (backtrack(board, counts, placements, w)) return true
            for (idx in pl) board[idx] = false
        }
        counts[bestShape]++
        return false
    }
}