package com.ncorti.aoc2023

fun main() {
    fun play(pattern: CharArray, graph: Map<String, Pair<String, String>>): Int {
        var idx = 0
        var current = "AAA"
        var steps = 0
        while (current != "ZZZ") {
            current = if (pattern[idx] == 'L') {
                graph[current]!!.first
            } else {
                graph[current]!!.second
            }
            idx = (idx + 1) % pattern.size
            steps++
        }
        return steps
    }

    fun findLoop(start: String, pattern: CharArray, graph: Map<String, Pair<String, String>>): Int {
        var idx = 0
        var current = start
        var steps = 0
        while (true) {
            current = if (pattern[idx] == 'L') {
                graph[current]!!.first
            } else {
                graph[current]!!.second
            }
            idx = (idx + 1) % pattern.size
            steps++
            if (current.endsWith("Z")) {
                break
            }
        }
        return steps
    }

    fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)

    fun lcm(a: Long, b: Long): Long = a * b / gcd(a, b)

    fun parseInput(): Pair<CharArray, Map<String, Pair<String, String>>> {
        val input = getInputAsText("08") {
            split("\n").filter(String::isNotBlank)
        }
        val pattern = input[0].toCharArray()
        val graph = input.drop(1).map { line ->
            val from = line.substringBefore(" = ")
            val (l, r) = line.substringAfter(" = ").removeSurrounding("(", ")").split(", ")
            from to (l to r)
        }.toMap()
        return pattern to graph
    }

    fun part1(): Int {
        val (pattern, graph) = parseInput()
        return play(pattern, graph)
    }

    fun part2(): Long {
        val (pattern, graph) = parseInput()
        val starts = graph.keys.filter { it.endsWith("A") }.toMutableList()
        return starts.map { findLoop(it, pattern, graph) }.fold(1L) {
            acc, i -> lcm(acc, i.toLong())
        }
    }

    println(part1())
    println(part2())
}
