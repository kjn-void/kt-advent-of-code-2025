package aoc2025.registry

object DayRegistry {
    private val factories = mutableMapOf<Int, () -> Solution>()

    fun register(day: Int, factory: () -> Solution) {
        factories[day] = factory
    }

    fun create(day: Int): Solution =
        factories[day]?.invoke()
            ?: error("Day $day not registered")

    fun availableDays(): List<Int> =
        factories.keys.sorted()
}