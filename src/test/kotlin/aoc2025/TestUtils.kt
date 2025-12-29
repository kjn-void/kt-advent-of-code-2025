package aoc2025

fun splitLines(s: String): List<String> =
    s.trimIndent()
        .lines()
        .map { it.trimEnd() }
        .filter { it.isNotEmpty() }