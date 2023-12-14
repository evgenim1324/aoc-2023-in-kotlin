enum class PlatformField {
    O, H, c
}

interface BoardVertical {
    operator fun get(index: Int): PlatformField
    operator fun set(index: Int, value: PlatformField)
    val size: Int
    val indices: IntRange
        get() = IntRange(0, size - 1)
}

fun main() {

    fun List<String>.toPlatformFields(): Array<Array<PlatformField>> {
        return Array(this.size) { i -> Array(this[i].length) {j ->
            when(this[i][j]) {
                '.' -> PlatformField.c
                'O' -> PlatformField.O
                '#' -> PlatformField.H
                else -> throw IllegalStateException()
            }
        } }
    }

    fun Array<Array<PlatformField>>.north(): Sequence<BoardVertical> {
        val board = this
        return sequence {
            for (j in board[0].indices) {
                yield(object : BoardVertical {
                    override fun get(index: Int): PlatformField {
                        return board[index][j]
                    }

                    override fun set(index: Int, value: PlatformField) {
                        board[index][j] = value
                    }

                    override val size: Int
                        get() = board.size
                })
            }
        }
    }

    fun Array<Array<PlatformField>>.west(): Sequence<BoardVertical> {
        val board = this
        return sequence {
            for (i in board.indices) {
                yield(object : BoardVertical {
                    override fun get(index: Int): PlatformField {
                        return board[i][index]
                    }

                    override fun set(index: Int, value: PlatformField) {
                        board[i][index] = value
                    }

                    override val size: Int
                        get() = board[0].size
                })
            }
        }
    }

    fun Array<Array<PlatformField>>.south(): Sequence<BoardVertical> {
        val board = this
        return sequence {
            for (j in board[0].indices) {
                yield(object : BoardVertical {
                    override fun get(index: Int): PlatformField {
                        return board[board.lastIndex - index][j]
                    }

                    override fun set(index: Int, value: PlatformField) {
                        board[board.lastIndex - index][j] = value
                    }

                    override val size: Int
                        get() = board.size
                })
            }
        }
    }

    fun Array<Array<PlatformField>>.east(): Sequence<BoardVertical> {
        val board = this
        return sequence {
            for (i in board.indices) {
                yield(object : BoardVertical {
                    override fun get(index: Int): PlatformField {
                        return board[i][board[0].lastIndex - index]
                    }

                    override fun set(index: Int, value: PlatformField) {
                        board[i][board[0].lastIndex - index] = value
                    }

                    override val size: Int
                        get() = board[0].size
                })
            }
        }
    }

    fun Sequence<BoardVertical>.fall() {
        this.forEach { vertical ->
            var bottom = 0
            for (i in vertical.indices) {
                val field = vertical[i]
                if (field == PlatformField.H) {
                    bottom = i + 1
                    continue
                }

                if (field == PlatformField.O) {
                    if (i == bottom) {
                        bottom = i + 1
                        continue
                    }

                    vertical[bottom] = PlatformField.O
                    vertical[i] = PlatformField.c
                    bottom++
                }
            }
        }
    }

    fun BoardVertical.load(): Int {
        var score = 0
        for (i in this.indices) {
            if (this[i] == PlatformField.O) {
                score += this.size - i
            }
        }
        return score
    }

    fun part1(input: List<String>): Int {
        val fields = input.toPlatformFields()
        val north = fields.north()
        north.fall()

        return north.sumOf { it.load() }
    }

    data class Stone(val x: Int, val y: Int)

    fun Array<Array<PlatformField>>.getStones(): Set<Stone> {
        val field = this
        return buildSet {
            field.forEachIndexed { i, row -> row.forEachIndexed { j, value -> if (value == PlatformField.O) add(Stone(i, j)) }}
        }
    }

    fun Array<Array<PlatformField>>.apply(transition: Pair<Set<Stone>, Set<Stone>>) {
        val (from, to) = transition

        from.forEach {
            this[it.x][it.y] = PlatformField.c
        }

        to.forEach {
            this[it.x][it.y] = PlatformField.O
        }
    }

    fun part2(input: List<String>): Int {
        var score = 0

        val fields = input.toPlatformFields()

        val results = mutableMapOf<Set<Stone>, Int>()
        val loads = mutableListOf<Int>()

        for (i in 1..1000000000) {
            fields.north().fall()
            fields.west().fall()
            fields.south().fall()
            fields.east().fall()

            val stones = fields.getStones()

            val history = results[stones]
            if (history == null) {
                val load = fields.north().sumOf { it.load() }
                loads.add(load)
                results[stones] = i - 1
            } else {
                val cycleStart = history
                val cycleLength = results.size - cycleStart

                return loads[cycleStart - 1 + (1_000_000_000 - cycleStart) % cycleLength]
            }
        }

        throw IllegalArgumentException()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    check(part1(testInput) == 136)
    check(part2(testInput) == 64)

    val input = readInput("Day14")
    part1(input).println()
    part2(input).println()
}
