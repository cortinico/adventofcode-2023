package com.ncorti.aoc2023

import kotlin.math.abs

@OptIn(ExperimentalStdlibApi::class)
fun main() {

    fun parseInput() = getInputAsText("18") {
        split("\n").filter(String::isNotBlank)
    }

    fun part(hexAsInput: Boolean): Long {
        val input = parseInput()
        val vertex = mutableListOf(0 to 0)
        var (startx, starty) = vertex[0]
        var borderPoints = 0L
        input.forEach {
            val (dirStr, stepStr, hex) = it.split(" ")
            val step = if (hexAsInput) {
                hex.removeSurrounding("(", ")").drop(1).dropLast(1).hexToInt()
            } else {
                stepStr.toInt()
            }
            val direction = if (hexAsInput) {
                when {
                    hex.endsWith("0)") -> "R"
                    hex.endsWith("1)") -> "D"
                    hex.endsWith("2)") -> "L"
                    hex.endsWith("3)") -> "U"
                    else -> error("invalid input")
                }
            } else {
                dirStr
            }
            when (direction) {
                "U" -> startx -= step
                "D" -> startx += step
                "L" -> starty -= step
                "R" -> starty += step
            }
            borderPoints += step
            vertex.add(startx to starty)
        }
        var area = 0L
        for (i in vertex.indices) {
            val (j, k) = if (i < vertex.size - 1) {
                vertex[i] to vertex[i + 1]
            } else {
                vertex[i] to vertex[0]
            }
            area += ((j.first.toDouble() * k.second.toDouble()) - (k.first.toDouble() * j.second.toDouble())).toLong()
        }
        val internalPoints = abs(area / 2L) + 1L - (borderPoints / 2L)
        return (borderPoints + internalPoints)
    }

    println(part(hexAsInput = false))
    println(part(hexAsInput = true))
}

