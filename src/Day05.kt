import java.util.TreeMap

fun main() {

    fun readAlmanac(input: List<String>): Array<Pair<String, TreeMap<Long, Pair<Long, Long>>>> {
        var linePointer = 2
        fun checkHeader(title: String) {
            if (linePointer >= input.size) throw IllegalArgumentException("Title $title not found !")
            check(input[linePointer] == title)
            linePointer++
        }

        fun TreeMap<Long, Pair<Long, Long>>.readTableLines() {
            var line = input[linePointer]
            while (line.isNotBlank()) {
                val numbers = line.readNumbers().toList()
                this[numbers[1]] = numbers[0] to numbers[2]

                linePointer++
                if (linePointer >= input.size) return

                line = input[linePointer]
            }

            linePointer++
        }

        val maps = arrayOf(
            "seed-to-soil map:" to TreeMap<Long, Pair<Long, Long>>(),
            "soil-to-fertilizer map:" to TreeMap<Long, Pair<Long, Long>>(),
            "fertilizer-to-water map:" to TreeMap<Long, Pair<Long, Long>>(),
            "water-to-light map:" to TreeMap<Long, Pair<Long, Long>>(),
            "light-to-temperature map:" to TreeMap<Long, Pair<Long, Long>>(),
            "temperature-to-humidity map:" to TreeMap<Long, Pair<Long, Long>>(),
            "humidity-to-location map:" to TreeMap<Long, Pair<Long, Long>>(),
        )

        for ((title, map) in maps) {
            checkHeader(title)
            map.readTableLines()
        }

        return maps
    }

    fun Map.Entry<Long, Pair<Long, Long>>.sourceRange(): LongRange {
        val (source, destinationData) = this
        val (_, length) = destinationData

        return source ..< source + length
    }

    fun LongRange.translateSeedRange(entry: Map.Entry<Long, Pair<Long, Long>>): LongRange {
        val (source, destinationData) = entry
        val (destination, _) = destinationData

        val firstInDestination = destination + (this.first - source)

        return firstInDestination.. firstInDestination + (this.last - this.first)
    }

    fun TreeMap<Long, Pair<Long, Long>>.mapItem(item: LongRange): List<LongRange> {
        val points = mutableListOf<LongRange>()
        var range: LongRange? = item
        while (range != null) {
            val floorEntry = this.floorEntry(range.first)
            val floorRange = floorEntry?.sourceRange()
            if (floorRange == null || !floorRange.contains(range.first)) {
                val ceilingEntry = this.ceilingEntry(range.first)
                if (ceilingEntry == null) {
                    points.add(range)
                    range = null
                    continue
                }

                val ceilingRange = ceilingEntry.sourceRange()
                if (!ceilingRange.contains(range.last)) {
                    points.add(range)
                    range = null
                    continue
                }

                points.add(range.first..< ceilingRange.first)
                range = ceilingRange.first .. range.last
                continue
            }

            if (floorRange.contains(range.last)) {
                points.add(range.translateSeedRange(floorEntry))
                range = null
                continue
            }

            val ceilingEntry = this.ceilingEntry(range.first)
            if (ceilingEntry == null)  {
                points.add((range.first  .. floorRange.last).translateSeedRange(floorEntry))
                range = floorRange.last + 1 .. range.last
                continue
            }

            val ceilingRange = ceilingEntry.sourceRange()
            if (ceilingRange.contains(range.last)) {
                points.add((range.first  ..< ceilingRange.first).translateSeedRange(floorEntry))
                range = ceilingRange.first .. range.last
                continue
            }

            points.add((range.first  .. floorRange.last).translateSeedRange(floorEntry))
            range = floorRange.last + 1 .. range.last
        }

        return points
    }

    fun Array<Pair<String, TreeMap<Long, Pair<Long, Long>>>>.mapFromSeedToLocation(seed: LongRange): List<LongRange> {
        var item = listOf(seed)
        for ((_, map) in this) {
            item = item.flatMap { map.mapItem(it) }.toList()
        }

        return item
    }

    fun part1(input: List<String>): Int {
        val seeds = input[0].substring(6).readNumbers()
        check(input[1].isBlank())

        val maps = readAlmanac(input)

        return seeds.map { maps.mapFromSeedToLocation(it .. it).asSequence().map { it.first }.min() }.min().toInt()
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
