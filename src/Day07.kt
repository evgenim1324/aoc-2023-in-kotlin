enum class Type {
    FIVE_OF_A_KIND,
    FOUR_OF_A_KIND,
    FULL_HOUSE,
    THREE_OF_A_KIND,
    TWO_PAIR,
    ONE_PAIR,
    HIGH_CARD,
}

fun main() {
    val cards = arrayOf('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2').asSequence().withIndex()
        .associate { it.value to it.index }

    fun getType(hand: List<Int>): Type {
        val elements = hand.groupingBy { it }.eachCount()
        if (elements.size == 1) return Type.FIVE_OF_A_KIND
        if (elements.size == 2) {
            val first = elements.iterator().next().value
            if (first == 4 || first == 1) return Type.FOUR_OF_A_KIND
            if (first == 3 || first == 2) return Type.FULL_HOUSE
        }
        if (elements.size == 3) {
            if (elements.containsValue(3)) return Type.THREE_OF_A_KIND
            return Type.TWO_PAIR
        }

        if (elements.size == 4) return Type.ONE_PAIR

        return Type.HIGH_CARD
    }

    class Hand(
        val cards: List<Int>,
        val bid: Int,
    ) {
        val type: Type = getType(this.cards)
    }

    fun String.parseHand(): Hand {
        return Hand(
            this.substring(0, 5).asSequence().map { cards[it]!! }.toList(),
            this.substring(5).trim().toInt()
        )
    }

    fun part1(input: List<String>): Int {
        val hands = input.asSequence().map { it.parseHand() }.sortedWith { v1, v2 ->
            val typeComparison = v2.type.compareTo(v1.type)
            if (typeComparison == 0) {
                val pair =v2.cards.asSequence().zip(v1.cards.asSequence()).first { it.first != it.second }
                return@sortedWith pair.first.compareTo(pair.second)
            }

            typeComparison
        }.withIndex().toList()

        return hands.asSequence().map { (it.index + 1) * it.value.bid }.sum()
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 6440)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}
