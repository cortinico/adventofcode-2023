package com.ncorti.aoc2023

fun main() {

    fun parseInput() = getInputAsText("14") {
        split("\n").filter(String::isNotBlank).map {
            it.toCharArray()
        }.toTypedArray()
    }

    fun computeWeight(input: Array<CharArray>): Long {
        var total = 0L
        for (i in input.indices) {
            for (j in input[i].indices) {
                if (input[i][j] == 'O') {
                    total += input.size - i
                }
            }
        }
        return total
    }

    fun moveNorth(input: Array<CharArray>) {
        for (i in 1..<input.size) {
            for (j in input[i].indices) {
                if (input[i][j] == 'O') {
                    // Move up
                    for (k in i - 1 downTo 0) {
                        if (input[k][j] == '.') {
                            input[k][j] = 'O'
                            input[k + 1][j] = '.'
                        } else {
                            break
                        }
                    }
                }
            }
        }
    }

    fun moveSouth(input: Array<CharArray>) {
        for (i in input.size - 1 downTo 0) {
            for (j in input[i].size - 1 downTo 0) {
                if (input[i][j] == 'O') {
                    for (k in i + 1 until input.size) {
                        if (input[k][j] == '.') {
                            input[k][j] = 'O'
                            input[k - 1][j] = '.'
                        } else {
                            break
                        }
                    }
                }
            }
        }
    }

    fun moveWest(input: Array<CharArray>) {
        for (i in 0..<input[0].size) {
            for (j in input.indices) {
                if (input[i][j] == 'O') {
                    // Move left
                    for (k in j - 1 downTo 0) {
                        if (input[i][k] == '.') {
                            input[i][k] = 'O'
                            input[i][k + 1] = '.'
                        } else {
                            break
                        }
                    }
                }
            }
        }
    }

    fun moveEast(input: Array<CharArray>) {
        for (i in input[0].size - 1 downTo 0) {
            for (j in input.size - 1 downTo 0) {
                if (input[i][j] == 'O') {
                    // Move right
                    for (k in j + 1 until input[0].size) {
                        if (input[i][k] == '.') {
                            input[i][k] = 'O'
                            input[i][k - 1] = '.'
                        } else {
                            break
                        }
                    }
                }
            }
        }
    }
    fun part1(): Long {
        val input = parseInput()
        moveNorth(input)
        return computeWeight(input)
    }

    fun part2(): Long {
        val input = parseInput()
        var step = 0
        val seen = mutableMapOf<String, Int>()
        var loopSize = -1
        while (true) {
            moveNorth(input)
            moveWest(input)
            moveSouth(input)
            moveEast(input)
            val mapString = input.joinToString("") { it.joinToString("") }
            if (mapString !in seen) {
                seen[mapString] = step
            } else {
                loopSize = step - seen[mapString]!!
                break
            }
            step++
        }
        val loopingTimes = (1000000000 / loopSize) - 10
        step += ((loopingTimes) * (loopSize))
        while (step < 1000000000 - 1) {
            moveNorth(input)
            moveWest(input)
            moveSouth(input)
            moveEast(input)
            step++
        }
        return computeWeight(input)
    }

    println(part1())
    println(part2())
}
