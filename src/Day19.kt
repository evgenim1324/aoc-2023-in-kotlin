import InputRule.Companion.readInputRule
import Part.Companion.readPart
import Rule.Companion.readRule
import java.util.*
import kotlin.math.max
import kotlin.math.min

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

data class PartsRange(val min: Int = 1, val max: Int = 4000) {
    fun gt(value: Int) = if (min > value) this else this.copy(min = value + 1)
    fun gte(value: Int) = if (min >= value) this else this.copy(min = value)
    fun lt(value: Int) = if (max < value) this else this.copy(max = value - 1)
    fun lte(value: Int) = if (max <= value) this else this.copy(max = value)

    fun merge(range2: PartsRange): PartsRange {
        return PartsRange(min(this.min, range2.min), max(this.max, range2.max))
    }

    val length = max - min + 1L

    companion object {
        val operators = buildMap {
            put(">", PartsRange::gt)
            put(">=", PartsRange::gte)
            put("<", PartsRange::lt)
            put("<=", PartsRange::lte)
        }

        val invertedOperators = buildMap {
            put(">", "<=")
            put("<", ">=")
        }
    }
}

data class InputRule(
    val conditions: List<Pair<String, Triple<String, String, Int>>>,
    val default: String
) {
    companion object {
        fun String.readInputRule(): Pair<String, InputRule> {
            val (name, conditionString) = this.split('{', '}')
            val cndSplit = conditionString.split(',')

            val conditions = buildList {
                for (i in (0 ..< cndSplit.lastIndex)) {
                    val (cnd, nextState) = cndSplit[i].split(':')
                    add(nextState to Triple(cnd.substring(0, 1), cnd.substring(1, 2), cnd.substring(2).toInt()))
                }
            }

            check(conditions.size <= 4)

            return name to InputRule(conditions, cndSplit.last())
        }
    }
}
data class RangeRule(
    val x: PartsRange = PartsRange(),
    val m: PartsRange = PartsRange(),
    val a: PartsRange = PartsRange(),
    val s: PartsRange = PartsRange()
) {
    val totalCombination: Long get() = x.length * m.length * a.length * s.length

    fun merge(rule2: RangeRule): RangeRule {
        return RangeRule(x.merge(rule2.x), m.merge(rule2.m), a.merge(rule2.a), s.merge(rule2.s))
    }

    fun apply(condition: Triple<String, String, Int>): RangeRule {
        return updateParameter(condition.first) {
            PartsRange.operators[condition.second]!!.invoke(
                it,
                condition.third
            )
        }
    }

    fun applyNegative(condition: Triple<String, String, Int>): RangeRule {
        return updateParameter(condition.first) {
            PartsRange.operators[PartsRange.invertedOperators[condition.second]]!!.invoke(
                it,
                condition.third
            )
        }
    }

    private fun updateParameter(p: String, callback: Function1<PartsRange, PartsRange>): RangeRule {
        return when (p) {
            "x" -> this.copy(x = callback(this.x))
            "m" -> this.copy(m = callback(this.m))
            "a" -> this.copy(a = callback(this.a))
            "s" -> this.copy(s = callback(this.s))

            else -> throw IllegalArgumentException()
        }
    }
}

fun Map<String, InputRule>.computeAcceptationRanges(): List<RangeRule> {
    val inputRules = this

    val combndRule = mutableListOf<RangeRule>()
    fun buildRange(rule: InputRule, range: RangeRule) {

        var combined = range
        rule.conditions.forEach {
//            println(rule to newRange)
            if (it.first == "A")
                combndRule.add(combined.apply(it.second))
            else if (it.first != "R")
                buildRange(inputRules[it.first]!!, combined.apply(it.second))

            combined = combined.applyNegative(it.second)
        }

//        var combined = range
//        for (cnd in rule.conditions) combined = combined.applyNegative(cnd.second)

//        println(rule.default to combined)

        if (rule.default == "A")
            combndRule.add(combined)
        else if (rule.default != "R")
            buildRange(inputRules[rule.default]!!, combined)
    }

    buildRange(this["in"]!!, RangeRule())

    return combndRule
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


    fun part2(input: List<String>): Long {
        val rules = input.asSequence().takeWhile { it.isNotBlank() }.associate { it.readInputRule() }
        val finalRules = rules.computeAcceptationRanges()
        val fold = finalRules.sumOf { it.totalCombination  }
        return fold
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day19_test")
    check(part1(testInput) == 19114)
    check(part2(testInput) == 167409079868000)

    val input = readInput("Day19")
    part1(input).println()
    part2(input).println()
}
