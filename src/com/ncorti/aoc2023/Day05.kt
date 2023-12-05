package com.ncorti.aoc2023

data class Almanac(val seeds: List<Long>, val maps: List<List<Triple<Long, Long, Long>>>) {
    companion object {
        fun from(input: List<String>): Almanac {
            val seeds = input[0].substringAfter("seeds: ")
                .split(" ")
                .filter(String::isNotBlank)
                .map(String::toLong)
            val sublist = input.subList(1, input.size)
            val maps = mutableListOf<List<Triple<Long, Long, Long>>>()
            var submap = mutableListOf<Triple<Long, Long, Long>>()
            for (i in sublist.indices) {
                if ("map:" in sublist[i]) {
                    if (submap.isNotEmpty()) maps.add(submap)
                    submap = mutableListOf()
                } else {
                    submap.add(
                        Triple(
                            sublist[i].substringBefore(" ").toLong(),
                            sublist[i].substringAfter(" ").substringBefore(" ").toLong(),
                            sublist[i].substringAfterLast(" ").toLong()
                        )
                    )
                }
            }
            if (submap.isNotEmpty()) maps.add(submap)
            return Almanac(seeds, maps)
        }
    }

    private fun computeLocation(seed: Long): Long {
        var current = seed
        for (element in maps) {
            for (singleMapEntry in element) {
                val (destStart, sourceStart, size) = singleMapEntry
                if (current in sourceStart..<sourceStart + size) {
                    val delta = current - sourceStart
                    current = destStart + (delta)
                    break
                }
            }
        }
        return current
    }

    fun seedsToLocation(): List<Long> = seeds.map { computeLocation(it) }

    fun seedRangesToLowestLocation(): Long {
        var lowest = Long.MAX_VALUE
        val ranges = seeds.chunked(2).map { it[0]..<it[0] + it[1] }
        for (range in ranges) {
            for (i in range.first..range.last) {
                val location = computeLocation(i)
                if (location < lowest) {
                    lowest = location
                }
            }
        }
        return lowest
    }
}

fun main() {
    fun part1(): Long = getInputAsText("05") {
        Almanac.from(split("\n").filter(String::isNotBlank))
    }.seedsToLocation().min()

    fun part2(): Long = getInputAsText("05") {
        Almanac.from(split("\n").filter(String::isNotBlank))
    }.seedRangesToLowestLocation()

    println(part1())
    println(part2())
}
