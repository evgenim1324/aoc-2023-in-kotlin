
data class Signal(
    val from: String,
    val to: String,
    val value: Boolean
)

data class TotalSignals (
    val low: Long,
    val high: Long
)

class Machine(
    val devices: Map<String, Device>
) {
    internal val signalQueue = ArrayDeque<Signal>()

    fun pushButton(): TotalSignals {
        signalQueue.add(Signal("button", "broadcaster", false))
        var lowSignals = 0L
        var highSignals = 0L
        while (signalQueue.isNotEmpty()) {
            val signal = signalQueue.removeFirst()
            if (signal.value)
                highSignals++
            else
                lowSignals++

            println("${signal.from} -${if (signal.value) "high" else "low"}-> ${signal.to}")
            val device = devices[signal.to] ?: continue // output
            when(device) {
                is Device.Broadcaster -> device.receive(signal.value)
                is Device.FlipFlop -> device.receive(signal.value)
                is Device.Conjunction -> device.receive(signal.from, signal.value)
            }
        }

        return TotalSignals(lowSignals, highSignals)
    }
}

sealed interface Device {
    val name: String

    context(Machine)
    fun sendSignal(to: String, value: Boolean) {
        signalQueue.add(Signal(name, to, value))
    }

    class Broadcaster(
        override val name: String = "broadcaster",
        private val destinations: List<String>
    ) : Device {

        context(Machine)
        fun receive(signal: Boolean) {
            destinations.forEach { sendSignal(it, signal) }
        }
    }

    class FlipFlop(
        override val name: String,
        private val connections: List<String>,
        private var state: Boolean = false
    ) : Device {

        context(Machine)
        fun receive(signal: Boolean) {
            if (signal) return

            state = !state
            connections.forEach { sendSignal(it, state) }
        }
    }

    class Conjunction(
        override val name: String,
        private val connections: List<String>,
        input: List<String>
    ) : Device {
        private val memory = input.associateWithTo(mutableMapOf()) { false }

        context(Machine)
        fun receive(input: String, signal: Boolean) {
            require(memory[input] != null)

            memory[input] = signal

            val signalToSend = !memory.all { (_, v) -> v }
            connections.forEach { sendSignal(it, signalToSend) }
        }
    }
}

fun buildMachine(input: List<String>): Machine {
    fun String.toConnections() = this.split(",").asSequence().map(String::trim).toList()

    val inputConnections = mutableMapOf<String, MutableList<String>>()
    val conjunctionDevices = mutableListOf<Pair<String, List<String>>>()

    val devices = mutableMapOf<String, Device>()

    var broadcaster: Device.Broadcaster? = null

    for (str in input) {
        val pair = str.split("->")
        val key = pair[0].trim()
        val connections = pair[1].toConnections()

        if (key == "broadcaster") {
            check(broadcaster == null)
            broadcaster = Device.Broadcaster(destinations = connections)

            connections.forEach {
                inputConnections.getOrPut(it) { mutableListOf() }.add("broadcaster")
            }

            continue
        }

        if (key[0] == '%') {
            val name = key.substring(1)
            devices[name] = Device.FlipFlop(name, connections)

            connections.forEach {
                inputConnections.getOrPut(it) { mutableListOf() }.add(name)
            }

            continue
        }

        if (key[0] == '&') {
            val name = key.substring(1)
            conjunctionDevices.add(name to connections)

            connections.forEach {
                inputConnections.getOrPut(it) { mutableListOf() }.add(name)
            }

            continue
        }
    }

    check(broadcaster != null)
    devices["broadcaster"] = broadcaster

    for (conjunction in conjunctionDevices) {
        devices[conjunction.first] = Device.Conjunction(conjunction.first, conjunction.second, inputConnections[conjunction.first]!!)
    }

    return Machine(devices)
}

fun main() {

    fun part1(input: List<String>): Long {
        val machine = buildMachine(input)

        var lowTotal = 0L
        var highTotal = 0L
        repeat(1000) {
            val (low, high) = machine.pushButton()
            lowTotal+=low
            highTotal+=high
        }

        return lowTotal * highTotal
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day20_test")
    check(part1(testInput) == 32000000L)

    val input = readInput("Day20")
    part1(input).println()
    part2(input).println()
}
