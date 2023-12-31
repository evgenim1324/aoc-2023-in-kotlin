
class Field(private val board: List<String>) {
    fun findTheStartingPoint(): MazePipe {
        for (i in board.indices) {
            for ((j, c) in board[i].withIndex()) {
                if (c == 'S') {
                    return when {
                        i > 0 && board[i - 1][j] in "|7F" -> PipeUp(i, j)
                        j < board[i].lastIndex && board[i][j + 1] in "-7J" -> PipeRight(i , j)
                        i < board.lastIndex && board[i + 1][j] in "|LJ" -> PipeDown(i, j)
                        j > 0 && board[i][j - 1] in "-LF" -> PipeLeft(i, j)
                        else -> throw IllegalArgumentException("Nowhere to go from the starting point !")
                    }
                }
            }
        }

        throw IllegalArgumentException("No starting point !")
    }

    fun toTop(x: Int, y: Int): MazePipe {
        return when(board[x][y]) {
            'S' -> StartingPoint(x, y)
            '|' -> PipeUp(x, y)
            '7' -> PipeLeft(x, y)
            'F' -> PipeRight(x, y)
            else -> throw IllegalArgumentException("Cannot connect ${board[x][y]}, $x $y")
        }
    }

    fun toRight(x: Int, y: Int): MazePipe {
        return when(board[x][y]) {
            'S' -> StartingPoint(x, y)
            '-' -> PipeRight(x, y)
            '7' -> PipeDown(x, y)
            'J' -> PipeUp(x, y)
            else -> throw IllegalArgumentException("Cannot connect ${board[x][y]}, $x $y")
        }
    }

    fun toBottom(x: Int, y: Int): MazePipe {
        return when(board[x][y]) {
            'S' -> StartingPoint(x, y)
            '|' -> PipeDown(x, y)
            'L' -> PipeRight(x, y)
            'J' -> PipeLeft(x, y)
            else -> throw IllegalArgumentException("Cannot connect ${board[x][y]}, $x $y")
        }
    }

    fun toLeft(x: Int, y: Int): MazePipe {
        return when(board[x][y]) {
            'S' -> StartingPoint(x, y)
            '-' -> PipeLeft(x, y)
            'L' -> PipeUp(x, y)
            'F' -> PipeDown(x, y)
            else -> throw IllegalArgumentException("Cannot connect ${board[x][y]}, $x $y")
        }
    }
}

sealed class MazePipe(val x: Int, val y: Int) {
    context(Field)
    abstract fun next(): MazePipe

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MazePipe) return false

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result
    }
}

class StartingPoint(x: Int, y: Int) : MazePipe(x, y) {
    context(Field)
    override fun next(): MazePipe {
        throw IllegalArgumentException("Reached the starting point !")
    }

}

class PipeUp(x: Int, y: Int) : MazePipe(x, y) {
    context(Field)
    override fun next() = toTop(x - 1, y)
}

class PipeRight(x: Int, y: Int) : MazePipe(x, y) {
    context(Field)
    override fun next() = toRight(x, y + 1)
}

class PipeDown(x: Int, y: Int) : MazePipe(x, y) {
    context(Field)
    override fun next() = toBottom(x + 1, y)
}

class PipeLeft(x: Int, y: Int) : MazePipe(x, y) {
    context(Field)
    override fun next() = toLeft(x, y - 1)
}


fun main() {

    fun part1(input: List<String>): Int {
        var counter = 0
        with(Field(input)) {
            val start = findTheStartingPoint()
            var pipe = start
            do {
                counter++
                pipe = pipe.next()
            } while (pipe != start)
        }

        var farthest = counter / 2
        if (counter % 2 != 0) farthest++

        return farthest
    }

    data class SearchPoint(val x: Int, val y: Int)

    fun MazePipe.toSearchPoint() = SearchPoint(x, y)

    fun addBoundary(prev: MazePipe, pipe: MazePipe, path: MutableMap<SearchPoint, Boolean>) {
        path[pipe.toSearchPoint()] = (pipe is PipeDown) ||
                (prev is PipeUp && pipe is PipeUp) ||
                (prev is PipeUp && pipe is PipeLeft) ||
                (prev is PipeUp && pipe is PipeRight)
    }

    fun part2(input: List<String>): Int {
        val path = mutableMapOf<SearchPoint, Boolean>()
        with(Field(input)) {
            val start = findTheStartingPoint()
            var prev = start
            var pipe = start.next()

            do {
                addBoundary(prev, pipe, path)
                prev = pipe
                pipe = pipe.next()
            } while (pipe != start)

            addBoundary(prev, start, path)
        }

        var counter = 0
        for (i in input.indices) {
            var bourders = 0
            for (j in input[i].indices) {
                val pipe = path[SearchPoint(i, j)]
                if (pipe == null) {
                    if (bourders % 2 != 0) counter++
                    continue
                }

                if (pipe) bourders++
            }
        }

        return counter
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 8)
    check(part2(readInput("Day10_test2")) == 10)

    val input = readInput("Day10")
    part1(input).println()
    check(part1(input) == 6812)
    part2(input).println()
}
