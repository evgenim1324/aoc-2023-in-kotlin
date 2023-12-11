import kotlin.math.abs

class Galaxy(var x: Int, var y: Int) {
    fun distance(galaxy: Galaxy) = abs(this.x - galaxy.x) + abs(this.y - galaxy.y)
}
fun main() {
    fun IntArray.expand() {
        var counter = 0
        for ((index, value) in this.withIndex()) {
            if (value == 0) counter++
            this[index] = counter

            counter++
        }
    }

    fun part1(input: List<String>): Int {
        val columns = IntArray(input[0].length)
        val lines = IntArray(input.size)

        val galaxies = mutableListOf<Galaxy>()

        for (i in input.indices) {
            val row = input[i]
            for (j in row.indices) {
                if (row[j] == '#') {
                    galaxies.add(Galaxy(i, j))
                    lines[i]++
                    columns[j]++
                }
            }
        }

        columns.expand()
        lines.expand()

        for (galaxy in galaxies) {
            galaxy.x = lines[galaxy.x]
            galaxy.y = columns[galaxy.y]
        }

        var totalSum = 0
        while (galaxies.isNotEmpty()) {
            val galaxy = galaxies.removeLast()
            totalSum+= galaxies.map { it.distance(galaxy) }.sum()
        }

        return totalSum
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 374)

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}
