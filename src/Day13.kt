fun main() {

    fun patterns(input: List<String>) = sequence<List<String>> {
        var pattern = mutableListOf<String>()
        for (line in input) {
            if (line.isBlank()) {
                yield(pattern)
                pattern = mutableListOf()
                continue
            }

            pattern.add(line)
        }

        yield(pattern)
    }

    fun List<String>.checkReflection(position: Int, smudge: Int = 0): Boolean {
        var smudgesAllowed = smudge

        fun compare(str1: String, str2: String): Boolean {
            if (str1.length != str2.length) return false

            for (i in str1.indices) {
                val equals = str1[i] == str2[i]
                if (!equals) {
                    if (smudgesAllowed == 0) return false

                    smudgesAllowed--
                }
            }

            return true
        }

        if (!compare(this[position - 1], this[position])) return false

        var top = position - 2
        var bottom = position + 1
        while (top >= 0 && bottom < this.size) {
            if (!compare(this[top], this[bottom])) return false
            top--
            bottom++
        }

        if (smudgesAllowed == 0) return true

        return false
    }

    fun horizontal(pattern: List<String>, smudge: Int = 0): Int {
        for (i in 1..< pattern.size) {
            if (pattern.checkReflection(i, smudge)) return i
        }

        return 0
    }

    fun vertical(pattern: List<String>, smudge: Int = 0): Int {
        val lines = buildList {
            for (j in pattern[0].indices) {
                val str = StringBuilder()
                for (i in pattern.indices) {
                    str.append(pattern[i][j])
                }
                add(str.toString())
            }
        }

        return horizontal(lines, smudge)
    }

    fun part1(input: List<String>): Int {
        return patterns(input).sumOf {
            vertical(it, 0) + 100 * horizontal(it, 0)
        }
    }

    fun part2(input: List<String>): Int {
        return patterns(input).sumOf {
            vertical(it, 1) + 100 * horizontal(it, 1)
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    check(part1(testInput) == 405)
    check(part2(testInput) == 400)

    val input = readInput("Day13")
    part1(input).println()
    part2(input).println()
}
