enum class GardenField {
    O, H, c
}

data class GardenPoint(
    val x: Int,
    val y: Int
) {
    fun step(x: Int = 0, y: Int = 0) = this.copy(x = this.x + x, y = this.y + y)
}

fun findGardenPLots(input: List<String>, maxSteps: Int): Int {
    var gardenerPoint: GardenPoint? = null
    val garden = Array(input.size) { i -> Array(input[i].length) {j ->
        when(input[i][j]) {
            '.' -> GardenField.c
            '#' -> GardenField.H
            'S' -> {
                gardenerPoint = GardenPoint(i, j)
                GardenField.c
            }
            else -> throw IllegalStateException()
        }
    }}

    checkNotNull(gardenerPoint)

    return garden.countPaths(gardenerPoint!!, maxSteps)
}

fun Array<Array<GardenField>>.countPaths(start: GardenPoint, maxSteps: Int): Int {
    val reachedPoints = mutableSetOf(start)
    val consideredPaths = mutableSetOf(start to 0)

    val stack = mutableListOf(start to 0)
    fun stepIfPossible(point: GardenPoint, stepCounter: Int) {

        if (this[point.x][point.y] == GardenField.c) {
            val counter = stepCounter + 1
            if (!consideredPaths.add(point to counter)) return

            if (counter == maxSteps)
                reachedPoints.add(point)
            else
                stack.add(point to counter)
        }
    }

    while (stack.isNotEmpty()) {
        val (point, step) = stack.removeLast()

        if (point.x > 0) stepIfPossible(point.step(x = -1), step)
        if (point.x < this.lastIndex) stepIfPossible(point.step(x = +1), step)
        if (point.y > 0) stepIfPossible(point.step(y = -1), step)
        if (point.y < this[0].lastIndex) stepIfPossible(point.step(y = +1), step)
    }

    return reachedPoints.size
}

fun main() {
    fun part1(input: List<String>, maxSteps: Int): Int {
        return findGardenPLots(input, maxSteps)
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day21_test")
    check(part1(testInput, 6) == 16)

    val input = readInput("Day21")
    part1(input, 64).println()
    part2(input).println()
}
