import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min

enum class LavaTrenchDirection {
    U, D, L, R
}

class LavaTrenchPoint(x: Int, y: Int)

fun main() {

    fun getPath(input: List<String>) = input.map {
        val (direction, number) = it.split(" ")
        LavaTrenchDirection.valueOf(direction) to number.toInt()
    }

    fun borderChar(prev: LavaTrenchDirection, next: LavaTrenchDirection): Char {
        return when(prev to next) {
            LavaTrenchDirection.L to LavaTrenchDirection.L -> '━'
            LavaTrenchDirection.R to LavaTrenchDirection.R -> '━'
            LavaTrenchDirection.D to LavaTrenchDirection.D -> '┃'
            LavaTrenchDirection.U to LavaTrenchDirection.U -> '┃'
            LavaTrenchDirection.U to LavaTrenchDirection.R -> '┌'
            LavaTrenchDirection.U to LavaTrenchDirection.L -> '┑'
            LavaTrenchDirection.D to LavaTrenchDirection.R -> '┕'
            LavaTrenchDirection.D to LavaTrenchDirection.L -> '┙'
            LavaTrenchDirection.L to LavaTrenchDirection.D -> '┌'
            LavaTrenchDirection.L to LavaTrenchDirection.U -> '┕'
            LavaTrenchDirection.R to LavaTrenchDirection.D -> '┑'
            LavaTrenchDirection.R to LavaTrenchDirection.U -> '┙'
            else -> throw IllegalArgumentException()
        }
    }

    fun List<Pair<LavaTrenchDirection, Int>>.getMap(): Array<CharArray> {
        var maxWidth = 0
        var minWidth = 0
        var maxHight = 0
        var minHight = 0

        var width = 0
        var hight = 0
        this.forEach { (d, n) ->
            when (d) {
                LavaTrenchDirection.R -> width+=n
                LavaTrenchDirection.L -> width-=n
                LavaTrenchDirection.U -> hight+=n
                LavaTrenchDirection.D -> hight-=n
            }

             maxWidth = max(maxWidth, width)
             minWidth = min(minWidth, width)
             maxHight = max(maxHight, hight)
             minHight = min(minHight, hight)
        }

        width = maxWidth - minWidth + 1
        hight = maxHight - minHight + 1

        val map = Array (hight) { CharArray(width) { '*' } }

        var x = hight - minHight.absoluteValue - 1
        var y = minWidth.absoluteValue

        var prev = this.last().first

        for (i in this.indices) {
            val (d, n) = this[i]
            map[x][y] = borderChar(prev, d)

            when (d) {
                LavaTrenchDirection.R -> {
                    repeat(n - 1) { map[x][++y] = '━' }; y++
                }

                LavaTrenchDirection.L -> {
                    repeat(n - 1) { map[x][--y] = '━' }; y--
                }

                LavaTrenchDirection.U -> {
                    repeat(n - 1) { map[--x][y] = '┃' }; x--
                }

                LavaTrenchDirection.D -> {
                    repeat(n - 1) { map[++x][y] = '┃' }; x++
                }
            }

            prev = d
        }

        return map
    }

    fun Array<CharArray>.countSpace(): Int {
        val bars = arrayOf('┃' , '┌', '┑')

        var counter = 0
        for (row in this) {
            var barCounter = 0
            for (cell in row) {
                if (cell != '*') {
                    if (cell in bars) barCounter++

                    counter++
                    continue
                }

                if (barCounter % 2 != 0) counter++
            }
        }

        return counter
    }


    fun part1(input: List<String>): Int {
        val path = getPath(input)
        val map = path.getMap()

        return map.countSpace()
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day18_test")
    check(part1(testInput) == 62)

    val input = readInput("Day18")
    part1(input).println()
    part2(input).println()
}
