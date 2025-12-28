package aoc2025.bench

import aoc2025.days.Day10
import aoc2025.net.AocNet
import org.openjdk.jmh.annotations.*

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(java.util.concurrent.TimeUnit.MICROSECONDS)
open class Day10Bench {

    private lateinit var input: List<String>
    private lateinit var solution: Day10

    @Setup(Level.Trial)
    fun setup() {
        input = AocNet.loadDay(10)
        solution = Day10()
    }

    @Benchmark
    fun solveDay10() {
        solution.setInput(input)
        solution.solvePart1()
        solution.solvePart2()
    }
}