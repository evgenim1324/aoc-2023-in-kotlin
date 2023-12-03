
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
        fun String.getDigit(pointer: Int): Int {
            var start = pointer - 1
            while (start >= 0) {
                val c = this[start]
                if (!c.isDigit()) break

                start--
            }

            var end = pointer + 1
            while (end < this.length) {
                val c = this[end]
                if (!c.isDigit()) break

                end++
            }

            return Integer.parseInt(this.substring(start + 1, end))
        }

        fun String.checkLeftRight(lineNumber: Int, starPosition: Int, digitPositions: ArrayList<Pair<Int, Int>>) {
            if (starPosition > 0) {
                val position = starPosition - 1
                if (this[position].isDigit()) digitPositions.add(lineNumber to position)
            }

            if (starPosition < this.lastIndex) {
                val position = starPosition + 1
                if (this[position].isDigit()) digitPositions.add(lineNumber to position)
            }
        }

        fun String.checkLine(lineNumber: Int, position: Int, digitPositions: ArrayList<Pair<Int, Int>>) {
            if (this[position].isDigit()) {
                digitPositions.add(lineNumber to position)
            } else {
                this.checkLeftRight(lineNumber, position, digitPositions)
            }
        }

        fun List<String>.getGearRation(lineNumber: Int, starPosition: Int): Int? {
            val line = this[lineNumber]
            val digitPositions = ArrayList<Pair<Int, Int>>()

            line.checkLeftRight(lineNumber, starPosition, digitPositions)
            if (lineNumber != 0) {
                val lineToCheck = lineNumber - 1
                this[lineToCheck].checkLine(lineToCheck, starPosition, digitPositions)
            }
            if (lineNumber != this.lastIndex) {
                val lineToCheck = lineNumber + 1
                this[lineToCheck].checkLine(lineToCheck, starPosition, digitPositions)
            }

            if (digitPositions.size != 2) return null

            val digit1 = digitPositions[0]
            val digit2 = digitPositions[1]

            return this[digit1.first].getDigit(digit1.second) * this[digit2.first].getDigit(digit2.second)
        }

        var sum = 0
        for (i in input.indices) {
            val line = input[i]
            for (j in line.indices) {
                if (line[j] == '*') {
                    val ration = input.getGearRation(i, j)
                    if (ration != null) {
                        sum += ration
                    }
                }
            }
        }

        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
//    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
