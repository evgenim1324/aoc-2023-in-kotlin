fun main() {

    tailrec fun Sequence<Long>.getNextValue(): Long {
        val nextSequence = this.windowed(2).map { it[1] - it[0] }
        if (nextSequence.all { it == 0L }) {
            return this.first()
        }

        return this.last() + nextSequence.getNextValue()
    }

    tailrec fun Sequence<Long>.getPreviousValue(): Long {
        val nextSequence = this.windowed(2).map { it[1] - it[0] }
        if (nextSequence.all { it == 0L }) {
            return this.first()
        }

        return this.first() - nextSequence.getPreviousValue()
    }

    fun part1(input: List<String>): Int {
        return input.asSequence().map { it.readNumbers().getNextValue().toInt() }.sum()
    }

    fun part2(input: List<String>): Int {
        return input.asSequence().map { it.readNumbers().getPreviousValue().toInt()}.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 114)
    check(part2(testInput) == 2)

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}
