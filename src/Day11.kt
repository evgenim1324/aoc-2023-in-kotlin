import kotlin.math.abs

class Galaxy(var x: Long, var y: Long) {
    fun distance(galaxy: Galaxy) = abs(this.x - galaxy.x) + abs(this.y - galaxy.y)
}

fun main() {
    fun LongArray.expand(expansion: Int) {
        var counter = 0L
        for ((index, value) in this.withIndex()) {
            counter += if (value == 0L) expansion else 1

            this[index] = counter
        }
    }

    fun theUniverse(input: List<String>, expansion: Int): MutableList<Galaxy> {
        val columns = LongArray(input[0].length)
        val lines = LongArray(input.size)

        val galaxies = mutableListOf<Galaxy>()

        for (i in input.indices) {
            val row = input[i]
            for (j in row.indices) {
                if (row[j] == '#') {
                    galaxies.add(Galaxy(i.toLong(), j.toLong()))
                    lines[i]++
                    columns[j]++
                }
            }
        }

        columns.expand(expansion)
        lines.expand(expansion)

        for (galaxy in galaxies) {
            galaxy.x = lines[galaxy.x.toInt()]
            galaxy.y = columns[galaxy.y.toInt()]
        }
        return galaxies
    }

    fun partOneAndTwo(input: List<String>, expansion: Int = 2): Long {
        val galaxies = theUniverse(input, expansion)

        var totalSum = 0L
        while (galaxies.isNotEmpty()) {
            val galaxy = galaxies.removeLast()
            totalSum+= galaxies.sumOf { it.distance(galaxy) }
        }

        return totalSum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(partOneAndTwo(testInput) == 374L)
    check(partOneAndTwo(testInput, 10) == 1030L)
    check(partOneAndTwo(testInput, 100) == 8410L)

    val input = readInput("Day11")
    partOneAndTwo(input).println()
    partOneAndTwo(input, 1_000_000).println()
}
