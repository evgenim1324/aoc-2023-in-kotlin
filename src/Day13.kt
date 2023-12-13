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

    fun List<String>.checkReflection(position: Int): Boolean {
        if (this[position - 1] != this[position]) return false

        var top = position - 2
        var bottom = position + 1
        while (top >= 0 && bottom < this.size) {
            if (this[top] != this[bottom]) return false
            top--
            bottom++
        }

        return true
    }

    fun horizontal(pattern: List<String>): Int {
        for (i in 1..< pattern.size) {
            if (pattern.checkReflection(i)) return i
        }

        return 0
    }

    fun vertical(pattern: List<String>): Int {
        val lines = buildList {
            for (j in pattern[0].indices) {
                val str = StringBuilder()
                for (i in pattern.indices) {
                    str.append(pattern[i][j])
                }
                add(str.toString())
            }
        }

        return horizontal(lines)
    }

    fun part1(input: List<String>): Int {
        return patterns(input).sumOf {
            vertical(it) + 100 * horizontal(it)
        }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    check(part1(testInput) == 405)

    val input = readInput("Day13")
    part1(input).println()
    part2(input).println()
}
