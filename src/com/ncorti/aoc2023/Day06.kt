package com.ncorti.aoc2023

fun main() {
    fun part1(): Int {
        val (time, distance) = getInputAsText("06") {
            split("\n").filter(String::isNotBlank).map {
                it.split(" ").filter(String::isNotBlank).drop(1).map(String::toInt)
            }
        }
        val wins = IntArray(time.size) { 0 }
        for (i in time.indices) {
            // Time 7
            val timeLimit = time[i]
            val record = distance[i]
            for (speed in 1..timeLimit) {
                if (((timeLimit - speed) * speed) > record) {
                    wins[i]++
                }
            }
        }
        return wins.fold(1) { acc, i -> acc * i }
    }

    fun part2(): Long {
        val (time, distance) = getInputAsText("06") {
            split("\n").filter(String::isNotBlank).map {
                it.split(" ").filter(String::isNotBlank).drop(1).joinToString("").toLong()
            }
        }
        var wins = 0L
        for (speed in 1..time) {
            if (((time - speed) * speed) > distance) {
                wins++
            }
        }
        return wins
    }

    println(part1())
    println(part2())
}
