import java.util.*
import kotlin.math.min

enum class CrucibleDirection {
    UP, DOWN, LEFT, RIGHT
}

data class PathState(
    val x: Int,
    val y: Int,
    val direction: CrucibleDirection,
    val directionSteps: Int,
    val heatLoss: Int
) {
    companion object {
        val maxSteps = 3
    }

    fun hasNoMoreSteps() = directionSteps >= maxSteps
}

fun List<String>.toIntList() = this.map { line -> line.asSequence()
    .map { Integer.parseInt(it.toString()) }.toList() }

fun List<List<Int>>.distances() :Array<IntArray> {

    val distances = Array(this.size) { IntArray(this[0].size) }


    val lastColumn = this[0].lastIndex
    val lastRow = this.lastIndex

    distances[lastRow][lastColumn] = this[lastRow][lastColumn]

    for (j in lastColumn - 1 downTo  0) distances[lastRow][j] = this[lastRow][j] + distances[lastRow][j + 1]
    for (i in lastRow - 1 downTo  0) distances[i][lastColumn] = this[i][lastColumn] + distances[i + 1][lastColumn]

    for (i in lastRow - 1 downTo  0)
        for (j in lastColumn - 1 downTo  0)
            distances[i][j] = this[i][j] + min(distances[i][j + 1], distances[i + 1][j])

    return distances
}
fun main() {

    fun part1(input: List<List<Int>>): Int {
        val lastColumn = input[0].lastIndex
        val lastRow = input.lastIndex
        var minHeatLoss = (lastRow + lastColumn) * 9

        val distances = input.distances()

        fun PathState.right(): PathState? {
            if (y == lastColumn) return null

            val steps = if (direction == CrucibleDirection.RIGHT) {
                if (hasNoMoreSteps()) return null

                directionSteps + 1
            } else 1

            if (heatLoss + distances[x][y + 1] >= minHeatLoss) return null

            return copy(
                y = y + 1,
                direction = CrucibleDirection.RIGHT,
                directionSteps = steps,
                heatLoss = heatLoss + input[x][y + 1])
        }

        fun PathState.down(): PathState? {
            if (x == lastRow) return null

            val steps = if (direction == CrucibleDirection.DOWN) {
                if (hasNoMoreSteps()) return null

                directionSteps + 1
            } else 1

            if (heatLoss + distances[x + 1][y] >= minHeatLoss) return null

            return copy(
                x = x + 1,
                direction = CrucibleDirection.DOWN,
                directionSteps = steps,
                heatLoss = heatLoss + input[x + 1][y])
        }

        fun PathState.left(): PathState? {
            if (y == 0) return null

            val steps = if (direction == CrucibleDirection.LEFT) {
                if (hasNoMoreSteps()) return null

                directionSteps + 1
            } else 1

            if (heatLoss + distances[x][y - 1] >= minHeatLoss) return null

            return copy(
                y = y - 1,
                direction = CrucibleDirection.LEFT,
                directionSteps = steps,
                heatLoss = heatLoss + input[x][y - 1])
        }

        fun PathState.up(): PathState? {
            if (x == 0) return null

            val steps = if (direction == CrucibleDirection.UP) {
                if (hasNoMoreSteps()) return null

                directionSteps + 1
            } else 1

            if (heatLoss + distances[x - 1][y] >= minHeatLoss) return null

            return copy(
                x = x - 1,
                direction = CrucibleDirection.UP,
                directionSteps = steps,
                heatLoss = heatLoss + input[x - 1][y])
        }

        data class Point(val x: Int, val y: Int, val direction: CrucibleDirection, val directionSteps: Int,)

        fun PathState.toPoint() = Point(x, y, direction, directionSteps)

//        val paths = PriorityQueue<PathState>(compareBy { it.heatLoss + distances[it.x][it.y] - input[it.x][it.y]})

        val paths = PriorityQueue<PathState>(compareBy<PathState> {
            it.x + it.y
        }.then(compareBy { it.heatLoss }))

        val passed = mutableMapOf<Point, PathState>()

        fun addSteps(steps: Sequence<PathState>) {
            steps.forEach {
                val stepToAdd = it
                val pointToAdd = stepToAdd.toPoint()

                val existingPoint = passed[pointToAdd]
                if (existingPoint == null || stepToAdd.heatLoss < existingPoint.heatLoss) {
                    passed[pointToAdd] = stepToAdd
                    paths.add(stepToAdd)
                }
            }
        }

        fun PathState.addNext() {
            addSteps(when (direction) {
                CrucibleDirection.RIGHT -> sequenceOf(down(), right(), up())
                CrucibleDirection.DOWN -> sequenceOf(right(), down(), left())
                CrucibleDirection.UP -> sequenceOf(right(), up(), left())
                CrucibleDirection.LEFT -> sequenceOf(down(), up(), left())
            }.filterNotNull().sortedBy { distances[it.x][it.y] } )
        }

        fun PathState.isLastStep() = x == lastRow && y == lastColumn

//        var nodesChecked = 0
        paths.add(PathState(0, 0, CrucibleDirection.RIGHT, 0, 0))
        do {
            var currentState: PathState? = paths.remove()

            //println(currentState)
//            nodesChecked++
            if (currentState!!.isLastStep()) {
                minHeatLoss = min(minHeatLoss, currentState.heatLoss)
            }

            currentState.addNext()
        } while (paths.isNotEmpty())

        return minHeatLoss
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day17_test")
    check(part1(testInput.toIntList()) == 102)

    val input = readInput("Day17")
    check(part1(input.toIntList()) == 907)
    part1(input.toIntList()).println()
    part2(input).println()
}
