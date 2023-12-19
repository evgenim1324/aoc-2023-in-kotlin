import Part.Companion.readPart
import Rule.Companion.readRule
import java.lang.Long.parseLong
import java.util.Objects
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

data class Part(
    val x: Int,
    val m: Int,
    val a: Int,
    val s: Int
) {
    companion object {
        fun String.readPart(): Part {
            val (x, m, a, s) = this.substring(1, this.lastIndex).split(",").map { it.split('=')[1].toInt() }
            return Part(x, m, a, s)
        }
    }
}

class Rule(
    val conditions: List<Function1<Part, String?>>,
    val default: String
) {
    fun apply(part: Part): String {
        return conditions.asSequence().map { it.invoke(part) }.find(Objects::nonNull)?: default
    }

    companion object {
        val terminalRules = listOf("R", "A")

        fun String.readRule(): Pair<String, Rule> {
            val (name, conditionString) = this.split('{', '}')
            val cndSplit = conditionString.split(',')

            val conditions = buildList {
                for (i in (0 ..< cndSplit.lastIndex)) {
                    val (cnd, nextState) = cndSplit[i].split(':')
                    val parameter = cnd.substring(0, 2)
                    val n = cnd.substring(2).toInt()

                    add(readCondition(parameter, n, nextState))
                }
            }

            return name to Rule(conditions, cndSplit.last())
        }

        private fun readCondition(cnd: String, n: Int, state: String): Function1<Part, String?> {
            return when(cnd) {
                "x>" -> { part: Part -> if (part.x > n) state else null }
                "x<" -> { part: Part -> if (part.x < n) state else null }

                "m>" -> { part: Part -> if (part.m > n) state else null }
                "m<" -> { part: Part -> if (part.m < n) state else null }

                "a>" -> { part: Part -> if (part.a > n) state else null }
                "a<" -> { part: Part -> if (part.a < n) state else null }

                "s>" -> { part: Part -> if (part.s > n) state else null }
                "s<" -> { part: Part -> if (part.s < n) state else null }

                else -> throw IllegalArgumentException()
            }
        }
    }
}

fun main() {

    fun part1(input: List<String>): Int {
        var i = 0

        val conditions = buildMap {
            while (i < input.size) {
                val str = input[i]
                if (str.isBlank()) {
                    i++
                    break
                }

                val (key, value) = str.readRule()
                put(key, value)
                i++
            }
        }

        val parts = buildList {
            while (i < input.size) {
                add(input[i].readPart())
                i++
            }
        }

        fun applyRules(part: Part): String {
            var next = "in"
            do {
                next = conditions[next]!!.apply(part)
            } while (next !in Rule.terminalRules)

            return next
        }

        return parts.stream().parallel().filter { applyRules(it) == "A" }.mapToInt { it.x + it.a + it.m + it.s }.sum()
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day19_test")
    check(part1(testInput) == 19114)

    val input = readInput("Day19")
    part1(input).println()
    part2(input).println()
}
