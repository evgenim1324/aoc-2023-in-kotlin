
fun main() {

    fun getNumbers(numbers: String): Sequence<Int> {
        return numbers.split(' ').asSequence()
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .map { Integer.parseInt(it) }
    }

    fun getWinningNumbers(line: String): List<Int> {
        val (winningNumbersString, cardNumbersString) = line.split(':')[1].split('|')
        val winningNumbers = getNumbers(winningNumbersString).toSet()
        return getNumbers(cardNumbersString).filter { winningNumbers.contains(it) }.toList()
    }

    fun part1(input: List<String>): Int {
        return input.asSequence().map { line ->
            val cardWinningNumbers = getWinningNumbers(line)

            if (cardWinningNumbers.isEmpty()) return@map 0

            var score = 1
            for (i in 1..<cardWinningNumbers.size) {
                score*= 2
            }

            return@map score
        }.sum()
    }

    fun part2(input: List<String>): Int {
        var counter = 0
        val cards = IntArray(input.size) { 1 }
        for ((i, line) in input.withIndex()) {
            val winning = getWinningNumbers(line).size
            val numberOfCards = cards[i]
            for (j in i + 1 .. (i + winning).coerceAtMost(cards.lastIndex)) cards[j]+= numberOfCards

            counter+= numberOfCards
        }

        return counter
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
//    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
