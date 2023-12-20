package com.ncorti.aoc2023

data class Wire(
    val name: String,
    val type: String,
    val next: List<String>,
    var status: Boolean = false,
    var memory: MutableMap<String, Boolean> = mutableMapOf(),
) {
    fun initMemory(input: Map<String, Wire>) {
        input.forEach { (key, value) ->
            if (value.next.contains(this.name)) {
                memory[key] = false
            }
        }
    }
    fun isMemoryAllHigh(): Boolean = memory.all { it.value }
}

data class Pulse(val isHigh: Boolean, val source: String, val dest: String)

fun main() {

    fun parseInput(): Map<String, Wire> {
        val input = getInputAsText("20") {
            split("\n").filter(String::isNotBlank).map {
                it.split(" -> ")
            }
        }
        val map = input.associate {
            val type = if ("%" in it[0]) {
                "%"
            } else if ("&" in it[0]) {
                "&"
            } else {
                it[0]
            }
            val name = it[0].removePrefix("%").removePrefix("&")
            name to Wire(name, type, it[1].split(", "))
        }
        map.forEach { (_, value) ->
            if (value.type == "&") {
                value.initMemory(map)
            }
        }
        return map
    }

    fun processPulse(
        map: Map<String, Wire>, queue: MutableList<Pulse>, pulse: Pulse
    ) {
        val (isHigh, source, dest) = pulse
        val value = map[dest] ?: return
        if (value.type == "broadcaster") {
            value.next.forEach { next ->
                queue.add(Pulse(isHigh, dest, next))
            }
        } else if (value.type == "%") {
            if (isHigh) {
                // no-op pulse is ignored
            } else {
                if (value.status) {
                    value.next.forEach { next ->
                        queue.add(Pulse(false, dest, next))
                    }
                } else {
                    value.next.forEach { next ->
                        queue.add(Pulse(true, dest, next))
                    }
                }
                value.status = !value.status
            }
        } else if (value.type == "&") {
            value.memory[source] = isHigh
            if (value.isMemoryAllHigh()) {
                value.next.forEach { next ->
                    queue.add(Pulse(false, dest, next))
                }
            } else {
                value.next.forEach { next ->
                    queue.add(Pulse(true, dest, next))
                }
            }
        }
    }

    fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)

    fun lcm(a: Long, b: Long): Long = a * b / gcd(a, b)

    fun part1(): Int {
        val map = parseInput()
        var (lowPulses, highPulses) = 0 to 0
        repeat(1000) {
            val queue = mutableListOf(Pulse(false, "button", "broadcaster"))
            while (queue.isNotEmpty()) {
                val pulse = queue.removeAt(0)
                if (pulse.isHigh) {
                    highPulses++
                } else {
                    lowPulses++
                }
                processPulse(map, queue, pulse)
            }
        }
        return lowPulses * highPulses
    }

    fun part2(): Long {
        val map = parseInput()
        val gate = map.filter { it.value.next.contains("rx") }.values.first()
        val conjunctions = map.filter { it.value.next.contains(gate.name) }.map {
            it.value.name to -1L
        }.associate {
            it.first to it.second
        }.toMutableMap()

        var count = 0
        while (conjunctions.any { it.value == -1L }) {
            count++
            val queue = mutableListOf(Pulse(false, "button", "broadcaster"))
            while (queue.isNotEmpty()) {
                val pulse = queue.removeAt(0)
                if (!pulse.isHigh && pulse.dest in conjunctions && conjunctions[pulse.dest]!! == -1L) {
                    conjunctions[pulse.dest] = count.toLong()
                }
                processPulse(map, queue, pulse)
            }
        }

        return conjunctions.map { it.value }.fold(1L) { acc, i ->
            lcm(acc, i)
        }
    }

    println(part1())
    println(part2())
}

