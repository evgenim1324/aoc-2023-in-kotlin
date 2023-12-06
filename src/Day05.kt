fun main() {

    class AlmanacRecord(
        val source: Long,
        val destination: Long,
        val length: Long
    ) {
        fun inRange(value: Long) = value in source ..< source + length

        fun inRange(value: LongRange): Boolean {
            val range = source ..< source + length
            return value.first in range || value.last in range
        }

        fun map(point: Long) = destination + (point - source)

        fun project(point: LongRange): LongRange  {
            val start = if (point.first < source) source else point.first
            val end = if (point.last < source + length) point.last else source + length - 1

            return start .. end
        }

        fun map(point: LongRange): LongRange {
            return map(point.first) .. map(point.last)
        }

        fun map(): LongRange {
            return map(destination) .. map(destination + length - 1)
        }
    }

    class AlmanacSection(
        val title: String,
        val records: List<AlmanacRecord>
    ) {

        fun mapToDestination(source: Long): Long {
            val searchResult = records.binarySearchBy(key = source, selector = AlmanacRecord::source)
            if (searchResult >= 0) return records[searchResult].map(source)
            if (searchResult == -1) return source
            val closestRecord = records[-(searchResult + 2)]
            if (closestRecord.inRange(source)) return closestRecord.map(source)

            return source
        }

        fun mapToDestination(source: LongRange): List<LongRange> {
            val searchResult = records.binarySearchBy(key = source.first, selector = AlmanacRecord::source)
            val applicableRules = buildList {
                var ruleIndex = records.let {
                    if (searchResult >= 0) return@let searchResult
                    if (searchResult == -1) return@let 0
                    return@let -(searchResult + 2)
                }

                while (ruleIndex < records.size) {
                    val record = records[ruleIndex]
                    if (!records[ruleIndex].inRange(source)) break

                    add(record)

                    ruleIndex++
                }
            }

            if (applicableRules.isEmpty()) return listOf(source)

            return buildList {
                var prevRule = applicableRules[0]

                if (source.first < prevRule.source) {
                    add(source.first ..< prevRule.source)
                }

                for (i in 1..applicableRules.lastIndex) {
                    add(prevRule.map())

                    val rule = applicableRules[i]
                    if (rule.source - prevRule.source + prevRule.length > 0) {
                        add((prevRule.source + prevRule.length)..<rule.source)
                    }

                    prevRule = rule
                }

                add(prevRule.map(prevRule.project(source)))

                val prevLast = prevRule.source + prevRule.length - 1
                if (source.last > prevLast) {
                    add(prevLast .. source.last)
                }
            }
        }
    }

    fun Array<AlmanacSection>.mapFromSeedToLocation(seed: Long): Long {
        var location = seed
        for (section in this) {
            location = section.mapToDestination(location)
        }

        return location
    }

    fun Array<AlmanacSection>.mapFromSeedToLocation(seed: LongRange): List<LongRange> {
        var location = listOf(seed)
        for (section in this) {
            location = location.flatMap { section.mapToDestination(it) }
        }

        return location
    }

    fun readAlmanac(input: List<String>): Array<AlmanacSection> {
        var linePointer = 2
        fun checkHeader(title: String) {
            if (linePointer >= input.size) throw IllegalArgumentException("Title $title not found !")
            check(input[linePointer] == title)
            linePointer++
        }

        fun readTableLines(): List<AlmanacRecord> {
            return buildList {
                var line = input[linePointer]
                while (line.isNotBlank()) {
                    val numbers = line.readNumbers().toList()
                    add(AlmanacRecord(numbers[1], numbers[0], numbers[2]))

                    linePointer++
                    if (linePointer >= input.size) break

                    line = input[linePointer]
                }
            }.sortedBy(AlmanacRecord::source).also {
                linePointer++
            }
        }

        val maps = arrayOf(
            "seed-to-soil map:",
            "soil-to-fertilizer map:",
            "fertilizer-to-water map:",
            "water-to-light map:",
            "light-to-temperature map:",
            "temperature-to-humidity map:",
            "humidity-to-location map:",
        )

        return Array(maps.size) {
            val title = maps[it]
            checkHeader(title)
            AlmanacSection(title.split(' ')[0], readTableLines())
        }
    }


    fun part1(input: List<String>): Int {
        val seeds = input[0].substring(6).readNumbers()
        check(input[1].isBlank())

        val maps = readAlmanac(input)

        return seeds.map { maps.mapFromSeedToLocation(it) }.min().toInt()
    }

    fun part2(input: List<String>): Int {
        val seeds = input[0].substring(6).readNumbers().windowed(2, 2)
        check(input[1].isBlank())

        val maps = readAlmanac(input)

        return seeds
            .map {
                maps.mapFromSeedToLocation(it[0] ..< (it[0] + it[1])).asSequence().map { it.first }.min()
            }.min().toInt()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 35)
    check(part2(testInput) == 46)


    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
