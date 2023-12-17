package com.ncorti.aoc2023

import com.ncorti.aoc2023.Direction.*
import java.util.*

fun main() {

    fun parseInput() = getInputAsText("17") {
        split("\n").filter(String::isNotBlank).map {
            it.toCharArray().map { it.digitToInt() }.toIntArray()
        }
    }.toTypedArray()

    data class Item(
        val x: Int, val y: Int, val score: Int, val disallowedDirection: Direction
    )

    val directions = listOf(Triple(1, 0, DOWN), Triple(0, 1, RIGHT), Triple(-1, 0, UP), Triple(0, -1, LEFT))

    fun part(min: Int, max: Int): Int {
        val input = parseInput()
        val distances = Array(input.size) {
            IntArray(input[0].size) { Int.MAX_VALUE }
        }
        val seen = mutableSetOf<Triple<Int, Int, Direction>>()
        distances[0][0] = 0
        val queue = PriorityQueue<Item> { o1, o2 ->
            o1.score - o2.score
        }
        queue.add(Item(0, 0, 0, UP))
        while (queue.isNotEmpty()) {
            val (x, y, currentScore, disallowedDirection) = queue.remove()
            val seenEntry = Triple(x, y, disallowedDirection)
            if (seenEntry in seen) {
                continue
            }
            seen.add(seenEntry)
            directions.forEach { (dx, dy, direction) ->
                var (nx, ny) = x to y
                var updatedScore = currentScore
                if (x == 0 && y == 0 || direction != disallowedDirection && direction != disallowedDirection.opposite()) {
                    for (i in 1..max) {
                        nx += dx
                        ny += dy
                        if (nx >= 0 && ny >= 0 && nx < input.size && ny < input[0].size) {
                            updatedScore += input[nx][ny]
                            if (i !in min..max) {
                                continue
                            }
                            if (updatedScore <= distances[nx][ny]) {
                                distances[nx][ny] = updatedScore
                            }
                            queue.add(Item(nx, ny, updatedScore, direction))
                        }
                    }
                }
            }
        }
        return distances[input.size - 1][input[0].size - 1]
    }
    println(part(min = 1, max = 3))
    println(part(min = 4, max = 10))
}

