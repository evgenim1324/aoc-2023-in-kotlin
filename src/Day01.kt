fun main() {
    fun part1(input: List<String>): Int {
        return input.asSequence()
            .map { line -> "${line.find { it.isDigit() }}${line.findLast { it.isDigit() }}".toInt() }
            .sum()
    }

    fun String.findFirstDigit(): Char {
        fun isSubstring(digitAsString: String, startIndex: Int) =
            digitAsString.let { startIndex + it.length <= this.length && this.substring(startIndex, startIndex + it.length) == it }

        for (i in this.indices) {
            val c = this[i]
            if (c.isDigit()) return c

            when (c) {
                'o' -> if (isSubstring("one", i)) return '1'
                't' -> {
                    if (isSubstring("two", i)) return '2'
                    if (isSubstring("three", i)) return '3'
                }
                'f' -> {
                    if (isSubstring("four", i)) return '4'
                    if (isSubstring("five", i)) return '5'
                }
                's' -> {
                    if (isSubstring("six", i)) return '6'
                    if (isSubstring("seven", i)) return '7'
                }
                'e' -> if (isSubstring("eight", i)) return '8'
                'n' -> if (isSubstring("nine", i)) return '9'
            }
        }

        throw IllegalArgumentException()
    }

    fun String.findLastDigit(): Char {
        fun isSubstring(digitAsString: String, endIndex: Int) =
            digitAsString.let { endIndex + 1 - digitAsString.length >=0 && this.substring(endIndex + 1 - digitAsString.length, endIndex + 1) == it }

        for (i in this.lastIndex downTo 0) {
            val c = this[i]
            if (c.isDigit()) return c

            when (c) {
                'e' -> {
                    if (isSubstring("one", i)) return '1'
                    if (isSubstring("three", i)) return '3'
                    if (isSubstring("five", i)) return '5'
                    if (isSubstring("nine", i)) return '9'
                }
                'o' -> if (isSubstring("two", i)) return '2'
                'r' -> if (isSubstring("four", i)) return '4'
                'x' -> if (isSubstring("six", i)) return '6'
                'n' -> if (isSubstring("seven", i)) return '7'
                't' -> if (isSubstring("eight", i)) return '8'
            }
        }

        throw IllegalArgumentException()
    }

    fun part2(input: List<String>): Int {
        return input.asSequence()
            .map { line -> "${line.findFirstDigit()}${line.findLastDigit()}".toInt() }
            .sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
//    check(part1(testInput) == 142)
    check(part2(testInput) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
