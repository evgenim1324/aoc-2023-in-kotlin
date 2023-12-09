fun main() {

    fun String.readNavigation(): List<Int> {
        val characters = mapOf('L' to 0, 'R' to 1)
        return this.map { characters[it]!! }
    }

    fun String.readInstruction() : Pair<String, Array<String>> {
        val (key, value1, value2) = this.split('=', ',')
        return key.trim() to arrayOf(value1.trim(' ', '('), value2.trim(' ', ')'))
    }

    fun part1(input: List<String>): Int {
        val navigation = input[0].readNavigation()
        var navigationPointer = 0

        fun nextMove(): Int {
            if (navigationPointer >= navigation.size) navigationPointer = 0

            return navigation[navigationPointer]
                .also { navigationPointer++ }
        }

        val rules = input.asSequence().drop(2).map { it.readInstruction() }.associate { it }

        var currentRule = "AAA"

        var counter = 0

        while (currentRule != "ZZZ") {
            currentRule = rules[currentRule]!![nextMove()]
            counter++
        }

        return counter
    }

    fun part2(input: List<String>): Long {
        val navigation = input[0].readNavigation()
        var navigationPointer = 0

        fun nextMove(): Int {
            if (navigationPointer >= navigation.size) navigationPointer = 0

            return navigation[navigationPointer]
                .also { navigationPointer++ }
        }

        val rulesParsed = input.asSequence().drop(2).map { it.readInstruction() }.toList()

        val rules = rulesParsed.associate { it }

        var currentRule = rulesParsed.filter { it.first[2] == 'A'}.map { it.first }.toList()

        fun getMovesForRule(start: String): Long {
            navigationPointer = 0

            var currentRule = start

            var counter = 0L

            while (currentRule[2] != 'Z') {
                currentRule = rules[currentRule]!![nextMove()]

                counter++
            }

//            println(counter)
            return counter
        }

        return currentRule.map { getMovesForRule(it).toBigInteger() }.reduce { acc, c -> acc * c / acc.gcd(c) }.toLong()
    }

    // test if implementation meets criteria from the description, like:
    check(part1(readInput("Day08_test")) == 6)
    check(part2(readInput("Day08_test2")) == 6L)

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}
