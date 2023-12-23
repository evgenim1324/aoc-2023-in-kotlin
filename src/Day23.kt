import kotlin.math.abs
import kotlin.math.max

fun main() {
    data class Point(val x: Int, val y: Int)

    data class Path(
        val points: MutableSet<Point>,
        val last: Point
    )

    fun String.findPoint(): Int? {
        return this.withIndex().find { (i, v )-> v == '.' }?.index
    }

    fun part1(input: List<String>): Int {
        val start = Point(0, input[0].findPoint()!!)
        val end = Point(input.lastIndex, input.last().findPoint()!!)

        fun Point.distance() = abs(end.x - x) + abs(end.y - y)

        fun Point.adjacent() = sequenceOf(
            this.copy(y = y + 1).takeIf { it.y < input[0].length  && input[it.x][it.y] != '<' },
            this.copy(y = y - 1).takeIf { it.y >= 0 && input[it.x][it.y] != '>' },
            this.copy(x = x + 1).takeIf { it.x < input.size && input[it.x][it.y] != '^' },
            this.copy(x = x - 1).takeIf { it.x >= 0 && input[it.x][it.y] != 'v' })
            .filterNotNull()
            .filter { input[it.x][it.y] != '#'}

        fun Point.slide(): Point? {
            val field = input[x][y]
            return when(field) {
                '>' -> this.copy(y = y + 1)
                '<' -> this.copy(y = y - 1)
                'v' -> this.copy(x = x + 1)
                '^' -> this.copy(x = x - 1)
                else -> null
            }
        }

        val stack = mutableListOf(Path(mutableSetOf(), start))

        var maxLength = 0

        while (stack.isNotEmpty()) {
            var (visited, point) = stack.removeLast()

            while (point != end) {
                val slide = point.slide()
                if (slide != null && !visited.contains(slide)) {
                    visited.add(point)
                    point = slide
                    continue
                }

                val nextPoints = point.adjacent().filter{ !visited.contains(it)}.sortedBy { it.distance() }.toList()
                if (nextPoints.isEmpty()) continue

                if (nextPoints.size > 1) {
                    for (i in 0 ..< nextPoints.lastIndex) {
                        stack.add(Path(HashSet(visited).also { it.add(point) }, nextPoints[i]))
                    }
                }

                visited.add(point)
                point = nextPoints.last()
            }

            maxLength = max(maxLength, visited.size)
        }

        return maxLength
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day23_test")
    check(part1(testInput) == 94)

    val input = readInput("Day23")
    part1(input).println()
    part2(input).println()
}
