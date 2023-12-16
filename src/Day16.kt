
class EnergyBoard(
    val input: List<String>
) {
    private val energyDiagram = Array(input.size) { BooleanArray(input[it].length) { false } }
    private val maxX = input.size
    private val maxY = input[0].length

    data class MethodCall(
        val name: String,
        val x: Int, val y: Int
    )

    private val methodCalls = mutableSetOf<MethodCall>()

    private fun visited(name: String, x: Int, y: Int): Boolean {
        val methodCall = MethodCall(name, x, y)

        if (methodCalls.contains(methodCall)) return true

        methodCalls.add(methodCall)
        return false
    }

    private fun beamRight(x: Int, y: Int) {
        if (visited("beamRight", x, y)) return

        var index = y
        do {
            index++
            if (index == maxY) return
            energyDiagram[x][index] = true
        } while (input[x][index] == '.' || input[x][index] == '-')

        when (input[x][index]) {
            '|' -> {
               beamUp(x, index)
               beamDown(x, index)
            }
            '\\' -> beamDown(x, index)
            '/' -> beamUp(x, index)
            else -> throw IllegalArgumentException()
        }
    }

    private fun beamLeft(x: Int, y: Int) {
        if (visited("beamLeft", x, y)) return

        var index = y
        do {
            index--
            if (index == -1) return
            energyDiagram[x][index] = true
        } while (input[x][index] == '.' || input[x][index] == '-')

        when (input[x][index]) {
            '|' -> {
                beamUp(x, index)
                beamDown(x, index)
            }
            '\\' -> beamUp(x, index)
            '/' -> beamDown(x, index)
            else -> throw IllegalArgumentException()
        }
    }

    private fun beamDown(x: Int, y: Int) {
        if (visited("beamDown", x, y)) return

        var index = x
        do {
            index++
            if (index == maxX) return
            energyDiagram[index][y] = true
        } while (input[index][y] == '.' || input[index][y] == '|')

        when (input[index][y]) {
            '-' -> {
                beamLeft(index, y)
                beamRight(index, y)
            }
            '\\' -> beamRight(index, y)
            '/' -> beamLeft(index, y)
            else -> throw IllegalArgumentException()
        }
    }

    private fun beamUp(x: Int, y: Int) {
        if (visited("beamUp", x, y)) return

        var index = x
        do {
            index--
            if (index == -1) return
            energyDiagram[index][y] = true

            val field = input[index][y]
        } while (field == '.' || field == '|')

        when (input[index][y]) {
            '-' -> {
                beamLeft(index, y)
                beamRight(index, y)
            }
            '\\' -> beamLeft(index, y)
            '/' -> beamRight(index, y)
            else -> throw IllegalArgumentException()
        }
    }

    fun beam(): Int {
        beamRight(0, -1)

        var counter = 0
        energyDiagram.forEach { it.forEach { if (it) counter++ } }

        return counter
    }
}

fun main() {

    fun part1(input: List<String>): Int {
        val board = EnergyBoard(input)
        return board.beam()
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day16_test")
    check(part1(testInput) == 46)

    val input = readInput("Day16")
    part1(input).println()
    part2(input).println()
}
