enum class PlatformField {
    O, H, c
}

fun main() {

    fun List<String>.toPlatformFields(): Array<Array<PlatformField>> {
        val allFields = Array(this[0].length) { Array(this.size) { PlatformField.c } }

        for (i in this[0].indices) {
            for (j in this.lastIndex downTo 0) {
                when(this[j][i]) {
                    'O' -> allFields[i][this.lastIndex - j] = PlatformField.O
                    '#' -> allFields[i][this.lastIndex - j] = PlatformField.H
                }
            }
        }

        return allFields
    }

    fun part1(input: List<String>): Int {
        val fields = input.toPlatformFields()

        var score = 0
        for (i in fields.indices) {
            val vertical = fields[i]
            var bottom = vertical.lastIndex
            for (j in vertical.lastIndex downTo 0) {
                val field = vertical[j]
                if (field == PlatformField.H) {
                    bottom = j - 1
                    continue
                }

                if (field == PlatformField.O) {
                    if (j == bottom) {
                        score += j + 1
                        bottom = j - 1
                        continue
                    }

                    vertical[bottom] = PlatformField.O
                    score += bottom + 1
                    vertical[j] = PlatformField.c
                    bottom--
                }
            }
        }

        return score
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    check(part1(testInput) == 136)

    val input = readInput("Day14")
    part1(input).println()
    part2(input).println()
}
