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

    class Hand(
        val cards: List<Int>,
        val bid: Int,
        val type: Type
    )

    val handComparator = object : Comparator<Hand> {
        override fun compare(v1: Hand, v2: Hand): Int {
            val typeComparison = v2.type.compareTo(v1.type)
            if (typeComparison == 0) {
                val pair =v2.cards.asSequence().zip(v1.cards.asSequence()).first { it.first != it.second }
                return pair.first.compareTo(pair.second)
            }

            return typeComparison
        }
    }

    fun part1(input: List<String>): Int {
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

        fun String.parseHand(): Hand {
            val cards = this.substring(0, 5).asSequence().map { cards[it]!! }.toList()
            return Hand(
                cards,
                this.substring(5).trim().toInt(),
                getType(cards)
            )
        }

        val hands = input.asSequence().map { it.parseHand() }.sortedWith(handComparator).withIndex().toList()

        return hands.asSequence().map { (it.index + 1) * it.value.bid }.sum()
    }

    fun part2(input: List<String>): Int {
        val cards = arrayOf('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J').asSequence().withIndex()
            .associate { it.value to it.index }

        fun getType(hand: List<Int>): Type {
            val elementsMap = hand.groupingBy { it }.eachCount()

            if (elementsMap.size == 1) return Type.FIVE_OF_A_KIND

            val jokers = elementsMap[12] ?: 0

            val elements = elementsMap.asSequence().filter { it.key != 12 }.sortedByDescending { it.value }.toList()
            if (elementsMap.size == 2) {
                val first = elements[0]
                if (first.value == 4) {
                    if (jokers >= 1) return Type.FIVE_OF_A_KIND

                    return Type.FOUR_OF_A_KIND
                }

                if (first.value == 3) {
                    if (jokers >= 2) return Type.FIVE_OF_A_KIND
                    if (jokers == 1) return Type.FOUR_OF_A_KIND

                    if (elements[1].value == 2) return Type.FULL_HOUSE

                    throw IllegalArgumentException()
                }

                if (jokers < 3) throw IllegalArgumentException()
                return Type.FIVE_OF_A_KIND
            }

            if (elementsMap.size == 3) {
                val first = elements[0]
                if (first.value == 3) {
                    if (jokers >= 2) return Type.FIVE_OF_A_KIND
                    if (jokers == 1) return Type.FOUR_OF_A_KIND

                    return Type.THREE_OF_A_KIND
                }

                if (first.value == 2) {
                    if (jokers > 2) throw IllegalArgumentException()
                    if (jokers == 2) return Type.FOUR_OF_A_KIND
                    if (jokers == 1) return Type.FULL_HOUSE

                    return Type.TWO_PAIR
                }

                if (jokers == 3) return Type.FOUR_OF_A_KIND

                throw IllegalArgumentException()
            }

            if (elementsMap.size == 4) {
                val first = elements[0]
                if (first.value == 2) {
                    if (jokers > 1) throw IllegalArgumentException()
                    if (jokers == 1) return Type.THREE_OF_A_KIND

                    return Type.ONE_PAIR
                }

                if (jokers != 2) throw IllegalArgumentException()

                return Type.THREE_OF_A_KIND
            }

            if (jokers > 1) throw IllegalArgumentException()
            if (jokers == 1) return Type.ONE_PAIR

            return Type.HIGH_CARD
        }

        fun String.parseHand(): Hand {
            val cards = this.substring(0, 5).asSequence().map { cards[it]!! }.toList()
            return Hand(
                cards,
                this.substring(5).trim().toInt(),
                getType(cards)
            )
        }

        val hands = input.asSequence().map { it.parseHand() }.sortedWith(handComparator).withIndex().toList()

        return hands.asSequence().map { (it.index + 1) * it.value.bid }.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
//    check(part1(testInput) == 6440)
    check(part2(testInput) == 5905)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}
