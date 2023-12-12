fun main() {
    fun arrangements(spring: String, mask: String): Long {
        val cache = mutableMapOf<String, Long>()

        fun withCaching(arg: String, perform: () -> Long): Long {
            return cache.getOrPut(arg) {
                perform()
            }
        }

        fun getValidArrangements(current: Char, springNextPosition: Int, maskPosition: Int): Long = withCaching("call $current $springNextPosition $maskPosition") {
//            println("call $current $springNextPosition $maskPosition")
            if (current == '?') {
                val result1 = getValidArrangements('#', springNextPosition, maskPosition)
                val result2 = getValidArrangements('.', springNextPosition, maskPosition)

                return@withCaching result1 + result2
            }

            val nextChar = if (springNextPosition >= spring.length) null else spring[springNextPosition]
            val maskItem = if (maskPosition >= mask.length) null else mask[maskPosition]

            if (current == '.') {
                if (maskItem == null) {
                    if (nextChar == null) return@withCaching 1

                    return@withCaching getValidArrangements(nextChar, springNextPosition + 1, maskPosition)
                }

                if (maskItem == '.') {
                    if (nextChar == null) return@withCaching 0

                    return@withCaching getValidArrangements(nextChar, springNextPosition + 1, maskPosition + 1)
                }

                if (maskItem == '#') {
                    if (nextChar == null) return@withCaching 0

                    if (maskPosition == 0 || mask[maskPosition - 1] == '.' ) {
                        return@withCaching getValidArrangements(nextChar, springNextPosition + 1, maskPosition)
                    }

                    return@withCaching 0
                }

                throw IllegalArgumentException()
            }

            if (current == '#') {
                if (maskItem == null || maskItem == '.') return@withCaching 0
                if (maskItem == '#') {
                    if (nextChar == null) {
                        if (maskPosition == mask.lastIndex) return@withCaching 1

                        return@withCaching 0
                    }

                    return@withCaching getValidArrangements(nextChar, springNextPosition + 1, maskPosition + 1)
                }

                throw IllegalArgumentException()
            }

            throw IllegalArgumentException("Unexpected character $current : $springNextPosition")
        }

        return getValidArrangements(spring[0], 1, 0)
    }


    fun String.mask() = this.split(',').asSequence().map { it.toInt() }
            .map { (1..it).fold(StringBuilder()) { acc, _ ->  acc.append('#') }.toString() }
            .joinToString(".")

    fun part1(input: List<String>): Long {
        return input.sumOf {
            val (spring, condition) = it.split(' ')
            var arr = arrangements(spring, condition.mask())
            arr
        }
    }

    fun String.unfold(separator: String) = (1..5).asSequence().map { this }.joinToString(separator)

    fun part2(input: List<String>): Long {
        return input.sumOf {
            val (spring, condition) = it.split(' ')
            arrangements(spring.unfold("?"), condition.unfold(",").mask())
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")

    check(part1(testInput) == 21L)
    check(part2(testInput) == 525152L)

    val input = readInput("Day12")
    part1(input).println()
    part2(input).println()
}
