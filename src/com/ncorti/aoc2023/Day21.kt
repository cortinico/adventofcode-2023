package com.ncorti.aoc2023

fun main() {

    fun parseInput(): Array<CharArray> {
        return getInputAsText("21") {
            split("\n").filter(String::isNotBlank).map {
                it.toCharArray()
            }.toTypedArray()
        }
    }

    fun parseInputTimes5(): Array<CharArray> {
        val world = parseInput()
        val newWorld = Array(world.size * 5) { CharArray(world[0].size * 5) }
        val startx = world.indexOfFirst { it.contains('S') }
        val starty = world[startx].indexOf('S')
        world[startx][starty] = '.'
        for (i in world.indices) {
            for (j in world[i].indices) {
                for (k in 0 until 5) {
                    for (l in 0 until 5) {
                        newWorld[i + (world.size * k)][j + (world[i].size * l)] = world[i][j]
                    }
                }
            }
        }
        newWorld[startx + (world.size * 2)][starty + (world[startx].size * 2)] = 'S'
        return newWorld
    }

    val neighbors = listOf(-1 to 0, 0 to -1, 0 to 1, 1 to 0)

    fun iterateOnWorldAndCompute(world: Array<CharArray>, iterations: Long): Int {
        val startx = world.indexOfFirst { it.contains('S') }
        val starty = world[startx].indexOf('S')
        world[startx][starty] = 'O'
        repeat(iterations.toInt()) {
            val toFlip = mutableListOf<Pair<Int, Int>>()
            for (i in world.indices) {
                for (j in world[i].indices) {
                    if (world[i][j] == 'O') {
                        toFlip.add(i to j)
                        neighbors.map { (di, dj) ->
                            i + di to j + dj
                        }.filter { (ni, nj) ->
                            ni in world.indices && nj in world[i].indices && world[ni][nj] == '.'
                        }.apply { toFlip.addAll(this) }
                    }
                }
            }
            toFlip.distinct().forEach { (i, j) ->
                if (world[i][j] == '.') {
                    world[i][j] = 'O'
                } else {
                    world[i][j] = '.'
                }
            }
        }
        return world.sumOf { it.count { it == 'O' } }
    }

    fun part1(): Int = iterateOnWorldAndCompute(parseInput(), 64)

    fun part2(): Long {
        val size = parseInput().size.toLong()
        val requestedIterations = 26501365
        val div = (requestedIterations / size)
        val rem = (requestedIterations % size)

        val y0 = iterateOnWorldAndCompute(parseInputTimes5(), rem)
        val y1 = iterateOnWorldAndCompute(parseInputTimes5(), rem + size)
        val y2 = iterateOnWorldAndCompute(parseInputTimes5(), rem + (size * 2))

        val c = y0
        val b = (y2 - y0 - (4 * y1) + (4 * y0)) / -2
        val a = y1 - y0 - b
        return (a * div * div) + (b * div) + c
    }

    println(part1())
    println(part2())
}

