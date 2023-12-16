package com.ncorti.aoc2023

import com.ncorti.aoc2023.Direction.*

enum class Direction {
    UP, DOWN, LEFT, RIGHT
}

fun main() {

    fun parseInput() = getInputAsText("16") {
        split("\n").filter(String::isNotBlank).map { it.toCharArray() }
    }.toTypedArray()

    fun computeEnergized(
        start: Triple<Int, Int, Direction>,
        map: Array<CharArray>,
    ): Array<IntArray> {
        val seen = Array(map.size) {
            IntArray(map[0].size) { 0 }
        }
        val toProcess = mutableListOf(start)
        val processSeen = mutableSetOf<Triple<Int, Int, Direction>>()
        while (toProcess.isNotEmpty()) {
            val nextToProcess = toProcess.removeAt(0)
            if (nextToProcess in processSeen) {
                continue
            }
            processSeen.add(nextToProcess)
            var (i, j, direction) = nextToProcess
            when (direction) {
                UP -> i--
                DOWN -> i++
                LEFT -> j--
                RIGHT -> j++
            }
            if (i < 0 || j < 0 || i >= map.size || j >= map[0].size) {
                continue
            }
            seen[i][j] = 1
            when (map[i][j]) {
                '.' -> {
                    toProcess.add(Triple(i, j, direction))
                }

                '-' -> {
                    if (direction == LEFT || direction == RIGHT) {
                        toProcess.add(Triple(i, j, direction))
                    } else {
                        toProcess.add(Triple(i, j, LEFT))
                        toProcess.add(Triple(i, j, RIGHT))
                    }
                }

                '|' -> {
                    if (direction == UP || direction == DOWN) {
                        toProcess.add(Triple(i, j, direction))
                    } else {
                        toProcess.add(Triple(i, j, UP))
                        toProcess.add(Triple(i, j, DOWN))
                    }
                }

                '/' -> {
                    when (direction) {
                        UP -> toProcess.add(Triple(i, j, RIGHT))
                        DOWN -> toProcess.add(Triple(i, j, LEFT))
                        LEFT -> toProcess.add(Triple(i, j, DOWN))
                        RIGHT -> toProcess.add(Triple(i, j, UP))
                    }
                }

                '\\' -> {
                    when (direction) {
                        UP -> toProcess.add(Triple(i, j, LEFT))
                        DOWN -> toProcess.add(Triple(i, j, RIGHT))
                        LEFT -> toProcess.add(Triple(i, j, UP))
                        RIGHT -> toProcess.add(Triple(i, j, DOWN))
                    }
                }
            }
        }
        return seen
    }

    fun part1(): Int = computeEnergized(Triple(0, -1, RIGHT), parseInput()).sumOf { it.sum() }


    fun part2(): Int {
        val map = parseInput()
        var max = 0
        val entryPoints = mutableListOf<Triple<Int, Int, Direction>>()
        for (i in map.indices) {
            entryPoints.add(Triple(i, -1, RIGHT))
            entryPoints.add(Triple(i, map[i].size, LEFT))
        }
        for (i in map[0].indices) {
            entryPoints.add(Triple(-1, i, DOWN))
            entryPoints.add(Triple(map.size, i, UP))
        }
        for (key in entryPoints) {
            val seen = computeEnergized(key, map)
            val totalSeen = seen.sumOf { it.sum() }
            if (totalSeen > max) {
                max = totalSeen
            }
        }
        return max
    }
    println(part1())
    println(part2())
}
