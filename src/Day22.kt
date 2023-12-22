import kotlin.math.max

data class BrickPoint(
    val x: Int,
    val y: Int,
    val z: Int
)

fun String.toPoint(): BrickPoint {
    val (x, y, z) = this.split(',').map { it.toInt() }
    return BrickPoint(x, y, z)
}

data class SandBrick(val bottom: BrickPoint, val top: BrickPoint)

fun String.toSandBrick(): SandBrick {
    val (bottom, top) = this.split('~').map { it.toPoint() }
    return SandBrick(bottom, top)
}

class SandPuzzle(val bricks: Array<SandBrick>, maxX: Int, maxY: Int) {
    val topLayer = Array(maxX + 1) { IntArray(maxY + 1) { -1 } }
}

fun List<String>.readPuzzle(): SandPuzzle {
    var maxX = 0
    var maxY = 0

    val bricks = Array(this.size) { i ->
        val brick = this[i].toSandBrick()
        maxX = max(maxX, brick.top.x)
        maxY = max(maxY, brick.top.y)

        brick
    }

    bricks.sortWith(compareBy { it.bottom.z })

    return SandPuzzle(bricks, maxX, maxY)
}

fun main() {


    fun part1(input: List<String>): Int {
        val puzzle = input.readPuzzle()

        val boxes = puzzle.bricks.indices.toMutableSet()

        fun SandPuzzle.fall(brickNumber: Int) {
            val brick = bricks[brickNumber]
            val rangeX = brick.bottom.x .. brick.top.x
            val rangeY = brick.bottom.y .. brick.top.y

            val bricksBeneath = mutableSetOf<Int>()
            val bricksThatHold = mutableSetOf<Int>()

            var maxZ = 0
            for (i in rangeX) for (j in rangeY) {
                val layerBrick = topLayer[i][j]
                if (layerBrick == -1) continue

                val z = bricks[layerBrick].top.z
                if (z > maxZ) {
                    bricksBeneath.addAll(bricksThatHold)
                    bricksThatHold.clear()
                    bricksThatHold.add(layerBrick)
                    maxZ = z
                } else if (z == maxZ) {
                    bricksThatHold.add(layerBrick)
                }
            }

            if (bricksThatHold.size == 1) {
                boxes.removeAll(bricksThatHold)
            }

            val brickHeight = brick.top.z - brick.bottom.z
            bricks[brickNumber] = brick.copy(bottom = brick.bottom.copy(z = maxZ + 1), top = brick.top.copy(z = maxZ + 1 + brickHeight))
            for (i in rangeX) for (j in rangeY) {
                topLayer[i][j] = brickNumber
            }
        }

        puzzle.bricks.indices.forEach { puzzle.fall(it) }

        return boxes.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day22_test")
    check(part1(testInput) == 5)

    val input = readInput("Day22")
    part1(input).println()
    part2(input).println()
}
