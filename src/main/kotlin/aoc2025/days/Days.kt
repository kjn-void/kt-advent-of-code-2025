package aoc2025

import aoc2025.days.Day01
import aoc2025.days.Day02
import aoc2025.days.Day03
import aoc2025.days.Day04
import aoc2025.days.Day05
import aoc2025.days.Day06
import aoc2025.days.Day07
import aoc2025.days.Day08
import aoc2025.days.Day09
import aoc2025.days.Day10
import aoc2025.days.Day11
import aoc2025.days.Day12

fun registerAllDays() {
    // Touch the classes so their init blocks run
    Class.forName(Day01::class.java.name)
    Class.forName(Day02::class.java.name)
    Class.forName(Day03::class.java.name)
    Class.forName(Day04::class.java.name)
    Class.forName(Day05::class.java.name)
    Class.forName(Day06::class.java.name)
    Class.forName(Day07::class.java.name)
    Class.forName(Day08::class.java.name)
    Class.forName(Day09::class.java.name)
    Class.forName(Day10::class.java.name)
    Class.forName(Day11::class.java.name)
    Class.forName(Day12::class.java.name)
}