package aoc2025.bench

import aoc2025.days.Day01
import aoc2025.net.AocNet
import org.openjdk.jmh.annotations.*
import java.util.concurrent.TimeUnit

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
open class Day01Bench {

    private lateinit var input: List<String>
    private lateinit var day: Day01

    @Setup(Level.Trial)
    fun setup() {
        input = AocNet.loadDay(1)
        solution = Day01()
    }

    @Benchmark
    fun solveDay01() {
        solution.setInput(input)
        solution.solvePart1()
        solution.solvePart2()
    }
}