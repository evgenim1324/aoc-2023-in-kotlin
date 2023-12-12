fun main() {
    fun String.arrangements(): Int {
        val (spring, condition) = this.split(' ')
        val records = condition.split(',').map { it.toInt() }

        val permutations = mutableListOf(StringBuilder())
        var builder = permutations[0]

        fun getValidArrangements(current: Char, springNextPosition: Int, record: Int, recordNextPosition: Int): Int {
            if (current == '?') {
                val subString = builder.toString()
                val result1 = getValidArrangements('#', springNextPosition, record, recordNextPosition)

                builder = StringBuilder()
                builder.append(subString)
                permutations.add(builder)
                val result2 = getValidArrangements('.', springNextPosition, record, recordNextPosition)

                return result1 + result2
            }

            val nextChar = if (springNextPosition >= spring.length) null else spring[springNextPosition]
            if (current == '.') {
                builder.append(current)
                if (record == -1) {
                    if (nextChar == null) return 0

                    return getValidArrangements(nextChar, springNextPosition + 1, 0, recordNextPosition)
                }

                if (record != 0) return 0
                if (nextChar == null) {
                    return if (recordNextPosition >= records.size) 1.also { println(builder.toString()) } else 0
                }

                return getValidArrangements(nextChar, springNextPosition + 1, record, recordNextPosition)
            }

            if (current == '#') {
                builder.append(current)
//                if (record == 0) return 0

                if (nextChar == null ) {
                    if ((record == 1 && recordNextPosition >= records.size)) {
                        return 1
                    }

                    val nextRecord = if (recordNextPosition >= records.size) null else {
                        val stri = builder.toString()
                        if (stri.length > 1 && stri[stri.length - 2] != '.') return 0
                        records[recordNextPosition]
                    }

                    return if ((nextRecord == 1 && recordNextPosition == records.lastIndex )) 1.also { println(builder.toString()) } else 0
                }

                if (record > 0) {
                    return getValidArrangements(nextChar, springNextPosition + 1, record - 1, recordNextPosition)
                }


                val nextRecord = if (recordNextPosition >= records.size) null else {
                    val stri = builder.toString()
                    if (stri.length > 1 && stri[stri.length - 2] != '.') return 0
                    records[recordNextPosition]
                }
                if (nextRecord == null) return 0


                return getValidArrangements(nextChar, springNextPosition + 1, nextRecord - 1, recordNextPosition + 1)
            }

            throw IllegalArgumentException("Unexpected character $current : $springNextPosition")
        }

        return getValidArrangements(spring[0], 1, 0, 0)
    }

    fun part1(input: List<String>): Int {
        return input.sumOf {
            var arr = it.arrangements()
            arr
        }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part1(testInput) == 21)

    val input = readInput("Day12")
    part1(input).println()
    part2(input).println()
}
