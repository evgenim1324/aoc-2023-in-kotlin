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

    class LavaMachineBox(
        private val lenses: MutableMap<String, Int> = mutableMapOf(),
    ) {
        fun remove(len: String) {
            lenses.remove(len)
        }

        fun addOrUpdate(len: String, focus: Int) {
            lenses[len] = focus
        }

        fun focalFactor(): Long {
            var factor = 0L

            var counter = 1
            for (focal in lenses.values) {
                factor += counter * focal

                counter++
            }

            return factor
        }
    }

    fun part2(input: List<String>): Long {
        val operations = input[0].split(',')

        val boxes = mutableMapOf<Long, LavaMachineBox>()
        for (op in operations) {
            if (op.last() == '-') {
                val len = op.substring(0, op.length - 1)
                val boxNumber = hashValue(len)

                val box = boxes.getOrPut(boxNumber) { LavaMachineBox() }
                box.remove(len)
            } else {
                val (len, focus) = op.split('=')
                val boxNumber = hashValue(len)

                val box = boxes.getOrPut(boxNumber) { LavaMachineBox() }
                box.addOrUpdate(len, focus.toInt())
            }
        }

        var result = 0L
        for ((key, value) in boxes) {
            result+= (key.toInt() + 1) * value.focalFactor()
        }

        return result
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    check(part1(testInput) == 1320L)
    check(part2(testInput) == 145L)

    val input = readInput("Day15")
    check(part1(input) == 509784L)
    check(part2(input) == 230197L)
    part1(input).println()
    part2(input).println()
}
