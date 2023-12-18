import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min

enum class LavaTrenchDirection {
    U, D, L, R
}

fun main() {

    fun getPath(input: List<String>) = input.map {
        val (direction, number) = it.split(" ")
        LavaTrenchDirection.valueOf(direction) to number.toInt()
    }

    fun List<Pair<LavaTrenchDirection, Int>>.getArea(): Long {
        var maxWidth = 0L
        var minWidth = 0L
        var maxHight = 0L
        var minHight = 0L

        var width = 0L
        var hight = 0L
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

        var s = 0L
        var h = hight
        var l = 0L
        for (i in this.indices) {
            val (d, n) = this[i]
            when (d) {
                LavaTrenchDirection.R -> {
                    s += n * h
                }

                LavaTrenchDirection.L -> {
                    s -= n * h
                }

                LavaTrenchDirection.U -> {
                    h += n
                }

                LavaTrenchDirection.D -> {
                    h -= n
                }
            }

            l += n
        }


        return s.absoluteValue + l / 2 + 1
    }


    fun part1(input: List<String>): Long {
        val path = getPath(input)
        return path.getArea()
    }

    fun getPath2(input: List<String>) = input.map {
        val (_, number) = it.split('#')
        val n = Integer.parseInt(number.substring(0, 5), 16)
        // dig: 0 means R, 1 means D, 2 means L, and 3 means U.
        val d = when (number.substring(5, 6)) {
            "0" -> LavaTrenchDirection.R
            "1" -> LavaTrenchDirection.D
            "2" -> LavaTrenchDirection.L
            "3" -> LavaTrenchDirection.U
            else -> throw IllegalArgumentException()
        }
        d to n
    }

    fun part2(input: List<String>): Long {
        val path = getPath2(input)
        return path.getArea()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day18_test")
    check(part1(testInput) == 62L)
    check(part2(testInput) == 952408144115)

    val input = readInput("Day18")
    part1(input).println()
    part2(input).println()
}
