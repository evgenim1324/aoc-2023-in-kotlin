
fun main() {

    fun getNumbers(numbers: String): Sequence<Int> {
        return numbers.split(' ').asSequence()
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .map { Integer.parseInt(it) }
    }

    fun part1(input: List<String>): Int {
        return input.asSequence().map { line ->
            val (winningNumbersString, cardNumbersString) = line.split(':')[1].split('|')
            val winningNumbers = getNumbers(winningNumbersString).toSet()
            val cardWinningNumbers = getNumbers(cardNumbersString).filter { winningNumbers.contains(it) }.toList()

            if (cardWinningNumbers.isEmpty()) return@map 0

            var score = 1
            for (i in 1..<cardWinningNumbers.size) {
                score*= 2
            }

            return@map score
        }.sum()
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
