package aoc2025.bench

import aoc2025.net.AocNet
import aoc2025.registry.DayRegistry
import aoc2025.registry.Solution
import org.openjdk.jmh.annotations.*

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(java.util.concurrent.TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
open class Day05Bench {

    private lateinit var input: List<String>
    private lateinit var solution: Solution

    @Setup(Level.Trial)
    fun setup() {
        input = AocNet.loadDay(5)
        solution = Day05()
    }

    @Benchmark
    fun solveDay05() {
        solution.setInput(input)
        solution.solvePart1()
        solution.solvePart2()
    }
}