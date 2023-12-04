package com.ncorti.aoc2023

data class ScratchCard(val winningNumbers: List<Int>, val playerNumbers: List<Int>, var count: Int = 1) {
    companion object {
        fun from(input: String) = ScratchCard(
            winningNumbers = input.substringAfter(":").substringBefore("|").split(" ").filter(String::isNotBlank)
                .map(String::toInt),
            playerNumbers = input.substringAfter("|").split(" ").filter(String::isNotBlank).map(String::toInt),
        )
    }

    fun scoreV1(): Int {
        var score = 0
        playerNumbers.forEach { number ->
            if (number in winningNumbers && score == 0) {
                score = 1
            } else if (number in winningNumbers) {
                score *= 2
            }
        }
        return score
    }

    fun countMatching(): Int = playerNumbers.count { it in winningNumbers }
}

fun main() {

    fun part1(): Int {
        return getInputAsText("04") {
            split("\n").filter(String::isNotBlank).map { ScratchCard.from(it) }
        }.sumOf { it.scoreV1() }
    }

    fun part2(): Int {
        val cards = getInputAsText("04") {
            split("\n").filter(String::isNotBlank).map { ScratchCard.from(it) }
        }
        for (i in cards.indices) {
            val winnings = cards[i].countMatching()
            val originalCount = cards[i].count
            for (j in 1..winnings) {
                cards[i + j].count += originalCount
            }
        }
        return cards.sumOf { it.count }
    }

    println(part1())
    println(part2())
}
