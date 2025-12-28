package aoc2025.bench

import aoc2025.days.Day02
import aoc2025.net.AocNet
import aoc2025.registry.Solution
import org.openjdk.jmh.annotations.*

import java.nio.file.Files
import java.nio.file.Paths
import java.util.concurrent.TimeUnit

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
open class Day02Bench {

    private lateinit var input: List<String>
    private lateinit var solution: Solution

    @Setup(Level.Trial)
    fun setup() {
        input = AocNet.loadDay(2)
        solution = Day02()
    }

    @Benchmark
    fun solveDay02() {
        solution.setInput(input)
        solution.solvePart1()
        solution.solvePart2()
    }
}