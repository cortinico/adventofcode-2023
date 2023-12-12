package com.ncorti.aoc2023

fun main() {
    fun part(part: Int = 1): Int =
        getInputAsText("09") {
            split("\n").filter(String::isNotBlank).map { it.split(" ").map(String::toInt) }
        }.sumOf { values ->
            val array = values.toIntArray()
            var idx = 0
            var limit = array.size
            var allZero = false
            val firstElements = mutableListOf<Int>()
            while (!allZero) {
                allZero = true
                limit--
                firstElements.add(array[0])
                while (idx < limit) {
                    array[idx] = array[idx + 1] - array[idx]
                    if (array[idx] != 0) {
                        allZero = false
                    }
                    idx++
                }
                idx = 0
            }
            firstElements.add(0)
            for (i in firstElements.lastIndex - 1 downTo 0) {
                firstElements[i] = firstElements[i] - firstElements[i + 1]
            }

            if (part == 1) {
                firstElements.first()
            } else {
                array.last()
            }
        }

    println(part(1))
    println(part(2))
}
