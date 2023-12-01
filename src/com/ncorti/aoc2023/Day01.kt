package com.ncorti.aoc2023

import kotlin.math.max

fun main() {
    fun part1() = getInputAsText("01") {
        split("\n")
    }
        .filterNot(String::isBlank)
        .map(String::toCharArray)
        .sumOf { array ->
            val firstDigit = array.first { it.isDigit() }.digitToInt()
            val lastDigit = array.last { it.isDigit() }.digitToInt()
            (firstDigit * 10) + lastDigit
        }

    fun part2() = getInputAsText("01") {
        split("\n")
    }
        .filterNot(String::isBlank)
        .sumOf { input ->
            val firstDigitIndex = if (input.indexOfFirst { it.isDigit() } == -1) input.length else input.indexOfFirst { it.isDigit() }
            val firstSubstring = input.substring(0, firstDigitIndex)
            val firstDigit = if (firstSubstring.containsSpelledNumber()) {
                firstSubstring.firstSpelledNumber()
            } else {
                input.first { it.isDigit() }.digitToInt()
            }
            val lastDigitIndex = max(input.indexOfLast { it.isDigit() } + 1, 0)
            val lastSubstring = input.substring(lastDigitIndex)
            val lastDigit = if (lastSubstring.containsSpelledNumber()) {
                lastSubstring.lastSpelledNumber()
            } else {
                input.last { it.isDigit() }.digitToInt()
            }
            (firstDigit * 10L) + lastDigit
        }

    println(part1())
    println(part2())
}

private fun String.firstSpelledNumber(): Int =
    mapOf(
        1 to this.indexOf("one"),
        2 to this.indexOf("two"),
        3 to this.indexOf("three"),
        4 to this.indexOf("four"),
        5 to this.indexOf("five"),
        6 to this.indexOf("six"),
        7 to this.indexOf("seven"),
        8 to this.indexOf("eight"),
        9 to this.indexOf("nine"),
    ).filter { (_, value) -> value != -1 }
        .minBy { (_, value) -> value }
        .let {(key, _) -> key}

private fun String.lastSpelledNumber(): Int =
    mapOf(
        1 to this.lastIndexOf("one"),
        2 to this.lastIndexOf("two"),
        3 to this.lastIndexOf("three"),
        4 to this.lastIndexOf("four"),
        5 to this.lastIndexOf("five"),
        6 to this.lastIndexOf("six"),
        7 to this.lastIndexOf("seven"),
        8 to this.lastIndexOf("eight"),
        9 to this.lastIndexOf("nine"),
    ).filter { (_, value) -> value != -1 }
        .maxBy { (_, value) -> value }
        .let {(key, _) -> key}

private fun String.containsSpelledNumber(): Boolean =
    "one" in this || "two" in this || "three" in this || "four" in this || "five" in this || "six" in this || "seven" in this || "eight" in this || "nine" in this
