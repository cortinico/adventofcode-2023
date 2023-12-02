package com.ncorti.aoc2023

import kotlin.math.max

data class Game(val id: Int, val sets : List<Triple<Int, Int, Int>>) {
    companion object {
        fun from(input: String): Game {
            val gameId = input.substringBefore(":").substringAfter(" ").toInt()
            val sets = input.substringAfter(":").split(";")
                .map { set ->
                    val red = if ("red" in set) {
                        set.substringBefore(" red").substringAfterLast(" ").toInt()
                    } else {
                        0
                    }
                    val green = if ("green" in set) {
                        set.substringBefore(" green").substringAfterLast(" ").toInt()
                    } else {
                        0
                    }
                    val blue = if ("blue" in set) {
                        set.substringBefore(" blue").substringAfterLast(" ").toInt()
                    } else {
                        0
                    }
                    Triple(red, green, blue)
                }
            return Game(gameId, sets)
        }
    }
}

fun main() {
    fun part1() = getInputAsText("02") {
        split("\n").filter(String::isNotBlank).map { Game.from(it) }
    }.sumOf { game ->
        if (game.sets.any { (red, green, blue) -> red > 12 || green > 13 || blue > 14 }) {
            0
        } else {
            game.id
        }
    }

    fun part2() = getInputAsText("02") {
        split("\n").filter(String::isNotBlank).map { Game.from(it) }
    }.sumOf { it ->
        val maxRed = it.sets.maxBy { (red, _, _) -> red }.first.toLong()
        val maxGreen = it.sets.maxBy { (_, green, _) -> green }.second.toLong()
        val maxBlue = it.sets.maxBy { (_, _, blue) -> blue }.third.toLong()
        maxRed * maxGreen * maxBlue
    }

    println(part1())
    println(part2())
}
