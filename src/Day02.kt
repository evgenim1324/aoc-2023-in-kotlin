import kotlin.math.max

fun parseSubset(colorsString: String): IntArray {
    var red = 0
    var green = 0
    var blue = 0
    for (colorString in colorsString.split(',')) {
        if (colorString.endsWith("red")) {
            red += Integer.parseInt(colorString.substring(1, colorString.length - 3).trim())
        }

        if (colorString.endsWith("green")) {
            green += Integer.parseInt(colorString.substring(1, colorString.length - 5).trim())
        }

        if (colorString.endsWith("blue")) {
            blue += Integer.parseInt(colorString.substring(1, colorString.length - 4).trim())
        }
    }

    return intArrayOf(red, green, blue)
}

fun main() {

    fun part1(input: List<String>): Int {
        fun isGameValid(subsets: String): Boolean {
            for (subset in subsets.split(';')) {
                val colors = parseSubset(subset)
                if (colors[0] > 12 || colors[1] > 13 || colors[2] > 14) return false
            }

            return true
        }

        var sum = 0
        for (gameString in input) {
            val gameNumberEnd = gameString.indexOfFirst { it == ':' }
            val gameNumber = Integer.parseInt(gameString.substring(5, gameNumberEnd))

            if (isGameValid(gameString.substring(gameNumberEnd + 1))) {
                sum+= gameNumber
            }
        }

        return sum
    }

    fun part2(input: List<String>): Int {
        fun getMinimalSetPower(subsets: String): Int {
            var minRed = 0
            var minGreen = 0
            var minBlue = 0

            for (subset in subsets.split(';')) {
                val colors = parseSubset(subset)
                minRed = max(minRed, colors[0])
                minGreen = max(minGreen, colors[1])
                minBlue = max(minBlue, colors[2])
            }

            return minRed * minGreen * minBlue
        }

        var sum = 0
        for (gameString in input) {
            val gameNumberEnd = gameString.indexOfFirst { it == ':' }
            sum+= getMinimalSetPower(gameString.substring(gameNumberEnd + 1))
        }

        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
//    check(part1(testInput) == 8)
    check(part2(testInput) == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
