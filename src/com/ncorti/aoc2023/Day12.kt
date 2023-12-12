package com.ncorti.aoc2023

fun main() {

    val memo = mutableMapOf<Triple<Int, Int, Int>, Long>()

    fun score(pattern: CharArray, numberList: List<Int>, idxPattern: Int, idxNumber: Int, bangCount: Int): Long {
        val input = Triple(idxPattern, idxNumber, bangCount)
        if (input in memo) {
            return memo[input]!!
        }
        if (idxPattern == pattern.size) {
            return when {
                idxNumber == numberList.size && bangCount == 0 -> 1
                idxNumber == numberList.size - 1 && numberList[idxNumber] == bangCount -> 1
                else -> 0
            }
        }
        var result = 0L
        for (char in listOf('.', '#')) {
            if (pattern[idxPattern] == char || pattern[idxPattern] == '?') {
                if (char == '.' && bangCount == 0) {
                    result += score(pattern, numberList, idxPattern + 1, idxNumber, bangCount)
                } else if (char == '.' && bangCount > 0 && idxNumber < numberList.size && numberList[idxNumber] == bangCount) {
                    result += score(pattern, numberList, idxPattern + 1, idxNumber + 1, 0)
                } else if (char == '#') {
                    result += score(pattern, numberList, idxPattern + 1, idxNumber, bangCount + 1)
                }
            }
        }
        memo[input] = result
        return result
    }

    fun parseInput() = getInputAsText("12") {
        split("\n").filter(String::isNotBlank).map { it.split(" ") }.map {
            val (pattern, numbers) = it
            pattern to numbers.split(",").map(String::toInt)
        }
    }

    fun part1(): Long =
        parseInput().sumOf { sample ->
            memo.clear()
            score(sample.first.toCharArray(), sample.second, 0, 0, 0)
        }

    fun part2(): Long =
        parseInput().sumOf { sample ->
            memo.clear()
            val pattern =
                "${sample.first}?${sample.first}?${sample.first}?${sample.first}?${sample.first}".toCharArray()
            val numberList = sample.second + sample.second + sample.second + sample.second + sample.second
            score(pattern, numberList, 0, 0, 0)
        }

    println(part1())
    println(part2())
}
