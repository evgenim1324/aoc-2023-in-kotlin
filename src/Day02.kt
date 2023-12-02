fun main() {

    fun part1(input: List<String>): Int {
        fun isGameValid(subsets: String): Boolean {
            for (subset in subsets.split(';')) {
                var red = 0
                var green = 0
                var blue = 0
                for (colorString in subset.split(',')) {
                    if (colorString.endsWith("red")) {
                        red+= Integer.parseInt(colorString.substring(1, colorString.length - 3).trim())
                    }

                    if (colorString.endsWith("green")) {
                        green+= Integer.parseInt(colorString.substring(1, colorString.length - 5).trim())
                    }

                    if (colorString.endsWith("blue")) {
                        blue+= Integer.parseInt(colorString.substring(1, colorString.length - 4).trim())
                    }
                }

                if (red > 12 || green > 13 || blue > 14) return false
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
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
