package com.ncorti.aoc2023

fun main() {

    fun String.computeHash(): Int {
        var current = 0
        toCharArray().forEach {
            current += it.toInt()
            current *= 17
            current %= 256
        }
        return current
    }

    fun parseInput() = getInputAsText("15") {
        split(",").filter(String::isNotBlank).map { it.trim() }
    }

    fun computeFocalLength(boxes: MutableMap<Int, MutableList<Pair<String, Int>>>): Long {
        var total = 0L
        boxes.forEach { (index, list) ->
            list.forEachIndexed { listIndex, (label, value) ->
                total += (index + 1) * (listIndex + 1) * value
            }
        }
        return total
    }

    fun part1(): Int = parseInput().sumOf { it.computeHash() }

    fun part2(): Long {
        val input = parseInput()
        val boxes = mutableMapOf<Int, MutableList<Pair<String, Int>>>()
        repeat(256) {
            boxes[it] = mutableListOf()
        }
        input.forEach {
            val label = it.substringBefore("-").substringBefore("=")
            val box = boxes[label.computeHash()]!!
            when {
                "=" in it -> {
                    val value = it.substringAfter("=").toInt()
                    val existingLensIdx = box.indexOfFirst { it.first == label }
                    if (existingLensIdx != -1) {
                        box[existingLensIdx] = label to value
                    } else {
                        box.add(label to value)
                    }
                }

                "-" in it -> {
                    box.firstOrNull { it.first == label }?.let {
                        box.remove(it)
                    }
                }
            }
        }
        return computeFocalLength(boxes)
    }

    println(part1())
    println(part2())
}
