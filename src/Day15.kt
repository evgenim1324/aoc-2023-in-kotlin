import java.lang.Long.parseLong
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

fun main() {

    /*
    Determine the ASCII code for the current character of the string.
    Increase the current value by the ASCII code you just determined.
    Set the current value to itself multiplied by 17.
    Set the current value to the remainder of dividing itself by 256.
     */
    fun hashValue(str: String): Long {
        var currentValue = 0L
        for (c in str) {
            currentValue += c.code
            currentValue *= 17
            currentValue %= 256
        }

        return currentValue
    }

    fun part1(input: List<String>): Long {
        return input[0].split(',').sumOf { hashValue(it) }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    check(part1(testInput) == 1320L)

    val input = readInput("Day15")
    part1(input).println()
    part2(input).println()
}
