package aoc2025.bench

import aoc2025.days.Day11
import aoc2025.net.AocNet
import org.openjdk.jmh.annotations.*

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(java.util.concurrent.TimeUnit.MICROSECONDS)
open class Day11Bench {

    private lateinit var input: List<String>
    private lateinit var solution: Day11

    @Setup(Level.Trial)
    fun setup() {
        input = AocNet.loadDay(11)
        solution = Day11()
    }

    @Benchmark
    fun solveDay11() {
        solution.setInput(input)
        solution.solvePart1()
        solution.solvePart2()
    }
}