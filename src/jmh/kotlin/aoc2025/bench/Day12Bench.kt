package aoc2025.bench

import aoc2025.days.Day12
import aoc2025.net.AocNet
import org.openjdk.jmh.annotations.*
import java.util.concurrent.TimeUnit

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
open class Day12Bench {

    private lateinit var input: List<String>
    private lateinit var solution: Day12

    @Setup(Level.Trial)
    fun setup() {
        input = AocNet.loadDay(12)
        solution = Day12()
    }

    @Benchmark
    fun solveDay12() {
        solution.setInput(input)
        solution.solvePart1()
        solution.solvePart2()
    }
}