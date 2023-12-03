
fun main() {

    class SchematicNumber(
        val start: Int,
        val end: Int // excluded
    )

    fun part1(input: List<String>): Int {
        val length = input[0].length

        fun String.nextDigit(pointer: Int): SchematicNumber? {
            var i = pointer
            while (i < this.length) {
                val c = this[i]
                if (c.isDigit()) {
                    break
                }
                i++
            }

            if (i >= this.length) return null

            val start = i

            i++
            while (i < this.length) {
                val c = this[i]
                if (!c.isDigit()) {
                    break
                }
                i++
            }

            val end = i

            return SchematicNumber(start, end)
        }

        fun String.hasAdjacentTo(word: SchematicNumber): Boolean {
            val start = (word.start - 1).coerceAtLeast(0)
            val end = (word.end + 1).coerceAtMost(this.length)

            for (i in start  ..< end) {
                val c = this[i]
                if (c != '.' && !c.isDigit()) {
                    return true
                }
            }

            return false
        }

        fun List<String>.hasAdjacentTo(lineNumber: Int, word: SchematicNumber): Boolean {
            val line = this[lineNumber]
            if (word.start > 0) {
                val c = line[word.start - 1]
                if (c != '.' && !c.isDigit()) {
                    return true
                }
            }

            if (word.end < line.length) {
                val c = line[word.end]
                if (c != '.' && !c.isDigit()) {
                    return true
                }
            }

            if (lineNumber != 0 && this[lineNumber - 1].hasAdjacentTo(word)) return true
            if (lineNumber != this.lastIndex && this[lineNumber + 1].hasAdjacentTo(word)) return true

            return false
        }

        var sum = 0
        for (i in input.indices) {
            val line = input[i]

            var j = 0
            while (j < length) {
                val digit = line.nextDigit(j) ?: break
                if (input.hasAdjacentTo(i, digit)) {
                    sum+= Integer.parseInt(line.substring(digit.start, digit.end))
                }

                j = digit.end + 1
            }
        }

        return sum
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
