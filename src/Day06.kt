import java.lang.Long.parseLong
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

fun main() {

    fun firstBiggerThan(value: Double): Int {
        return floor(value).toInt() + 1
    }

    fun firstLowerThan(value: Double): Int {
        return ceil(value).toInt() - 1
    }

    class Race(
        val time: Long,
        val distance: Long
    ) {
        fun getNumberOfWays(): Int {
            val determinant = time * time.toDouble() - 4 * distance

            val root1 = (time - sqrt(determinant)) / 2
            val root2 = (time + sqrt(determinant)) / 2

            return firstLowerThan(root2) - firstBiggerThan(root1) + 1
        }
    }

    fun List<String>.getRaces(): Sequence<Race> {
        return this[0].removePrefix("Time:").readNumbers().zip(
            this[1].removePrefix("Distance:").readNumbers()
        ) { v1, v2 -> Race(v1, v2) }
    }

    fun List<String>.readRace(): Race {
        return Race(
            parseLong(this[0].removePrefix("Time:").filter { it != ' ' }),
            parseLong(this[1].removePrefix("Distance:").filter { it != ' ' })
        )
    }

    fun part1(input: List<String>): Int {
        return input.getRaces().map { it.getNumberOfWays() }.fold(1) { acc, value -> acc * value}
    }

    fun part2(input: List<String>): Int {
        return input.readRace().getNumberOfWays()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
//    check(part1(testInput) == 288)
    check(part2(testInput) == 71503)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
