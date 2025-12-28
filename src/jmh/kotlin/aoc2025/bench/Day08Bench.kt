package aoc2025.bench

import aoc2025.days.Day08
import aoc2025.net.AocNet
import org.openjdk.jmh.annotations.*

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(java.util.concurrent.TimeUnit.MICROSECONDS)
open class Day08Bench {

    private lateinit var input: List<String>
    private lateinit var solution: Day08

    @Setup(Level.Trial)
    fun setup() {
        input = AocNet.loadDay(8)
        solution = Day08()
    }

    @Benchmark
    fun solveDay08() {
        solution.setInput(input)
        solution.solvePart1()
        solution.solvePart2()
    }
}