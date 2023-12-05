import java.util.TreeMap

fun main() {

    fun part1(input: List<String>): Int {
        val seeds = input[0].substring(6).readNumbers()
        check(input[1].isBlank())

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

        fun TreeMap<Long, Pair<Long, Long>>.mapItem(item: Long): Long {
            val (source, destinationData) = this.floorEntry(item)?: return item
            val (destination, length) = destinationData
            if (item < source + length) {
                return destination + (item - source)
            }

            return item
        }

        fun Array<Pair<String, TreeMap<Long, Pair<Long, Long>>>>.mapFromSeedToLocation(seed: Long): Long {
            var item = seed
            for ((_, map) in this) {
                item = map.mapItem(item)
            }

            return item
        }


        return seeds.map { maps.mapFromSeedToLocation(it) }.min().toInt()
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 35)


    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
