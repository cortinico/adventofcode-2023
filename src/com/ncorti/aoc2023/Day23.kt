package com.ncorti.aoc2023

fun main() {

    fun parseInput(): Array<CharArray> {
        return getInputAsText("23") {
            split("\n").filter(String::isNotBlank).map { it.toCharArray() }
        }.toTypedArray()
    }

    val neighbours = listOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1)

    fun computeMaxPath(map: Array<CharArray>, x: Int, y: Int, fromX: Int, fromY: Int, count: Int, seen: String): Int {
        if (x == map.size - 1 && y == map[0].size - 2) {
            return count
        }
        val next = neighbours.map { (dx, dy) ->
            x + dx to y + dy
        }.filter { it ->
            it.first in map.indices && it.second in map[0].indices && (it != fromX to fromY) && map[it.first][it.second] != '#'
        }.filter {
            "#${it.first}-${it.second}#" !in seen
        }.filter { (nx, ny) ->
            if (nx == x + 1 && map[nx][ny] == '^') {
                false
            } else if (nx == x - 1 && map[nx][ny] == 'v') {
                false
            } else if (ny == y + 1 && map[nx][ny] == '<') {
                false
            } else if (ny == y - 1 && map[nx][ny] == '>') {
                false
            } else {
                true
            }
        }

        return if (next.isEmpty()) {
            0
        } else {
            next.maxOf { (nx, ny) ->
                computeMaxPath(map, nx, ny, x, y, count + 1, "$seen,#$nx-$ny#")
            }
        }
    }

    fun findConjunctions(map: Array<CharArray>): MutableMap<Pair<Int, Int>, MutableList<Triple<Int, Int, Int>>> {
        val result = mutableMapOf<Pair<Int, Int>, MutableList<Triple<Int, Int, Int>>>()
        for (i in 1..<map.lastIndex) {
            for (j in 1..<map[i].lastIndex) {
                val hashCount = neighbours.count { (di, dj) ->
                    map[i + di][j + dj] == '#'
                }
                if (map[i][j] == '.' && (hashCount == 1 || hashCount == 0)) {
                    result[i to j] = mutableListOf()
                }
            }
        }
        return result
    }

    fun computeConjunctionGraph(
        map: Array<CharArray>, conjunctions: MutableMap<Pair<Int, Int>, MutableList<Triple<Int, Int, Int>>>
    ) {
        conjunctions.forEach { (start, list) ->
            val seen = mutableSetOf<Pair<Int, Int>>()
            val queue = mutableListOf<Triple<Int, Int, Int>>()
            neighbours.forEach { (dx, dy) ->
                if (start.first + dx in map.indices && start.second + dy in map[0].indices && map[start.first + dx][start.second + dy] != '#') {
                    queue.add(Triple(start.first + dx, start.second + dy, 1))
                }
            }
            while (queue.isNotEmpty()) {
                val (x, y, dist) = queue.removeAt(0)
                seen.add(x to y)
                if (x to y in conjunctions && x to y != start) {
                    list.add(Triple(x, y, dist))
                } else {
                    neighbours.forEach { (dx, dy) ->
                        if (map[x + dx][y + dy] != '#' && x + dx to x + dy != start && (x + dx to y + dy !in seen)) {
                            queue.add(Triple(x + dx, y + dy, dist + 1))
                        }
                    }
                }
            }
        }
    }

    class Status(val point: Pair<Int, Int>, val dist: Int, val seen: Set<Pair<Int, Int>>)

    fun computeLongestPath(
        conjunctions: MutableMap<Pair<Int, Int>, MutableList<Triple<Int, Int, Int>>>,
        start: Pair<Int, Int>,
        end: Pair<Int, Int>
    ): Int {
        var max = 0
        val queue = mutableListOf(Status(start, 0, setOf(start)))
        while (queue.isNotEmpty()) {
            val current = queue.removeLast()
            val (x, y) = current.point
            val dist = current.dist
            val seen = current.seen.toMutableSet().toSet()
            val next = conjunctions[x to y]!!
            next.forEach { (nx, ny, ndist) ->
                if (nx to ny == end) {
                    if (dist + ndist > max) {
                        max = dist + ndist
                    }
                } else if (nx to ny !in seen) {
                    queue.add(Status(nx to ny, dist + ndist, seen + (nx to ny)))
                }
            }
        }
        return max
    }

    fun part1(): Int {
        val map = parseInput()
        return computeMaxPath(map, 0, 1, 0, 0, 0, "#0-1#")
    }

    fun part2(): Int {
        val map = parseInput()
        val conjunctions = findConjunctions(map)
        conjunctions[0 to 1] = mutableListOf()
        conjunctions[map.size - 1 to map[0].size - 2] = mutableListOf()
        computeConjunctionGraph(map, conjunctions)
        return computeLongestPath(conjunctions, 0 to 1, map.size - 1 to map[0].size - 2)
    }

    println(part1())
    println(part2())
}
