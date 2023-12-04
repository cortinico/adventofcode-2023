package com.ncorti.aoc2023

import kotlin.math.pow

fun main() {

    fun part1(): Long {
        val matrix = getInputAsText("03") {
            split("\n").filter(String::isNotBlank).map { it.toCharArray() }
        }.toTypedArray()
        var result = 0L
        var foundNumberList = mutableListOf<Pair<Int, Int>>()
        for (i in matrix.indices) {
            for (j in matrix[i].indices) {
                if (matrix[i][j].isDigit() && foundNumberList.isEmpty()) {
                    foundNumberList = scanNumber(matrix, i, j)
                    if (foundNumberList.isNeighbourOfSymbol(matrix)) {
                        result += foundNumberList.toNumber(matrix)
                    }
                    foundNumberList.remove(i to j)
                } else if (i to j in foundNumberList) {
                    foundNumberList.remove(i to j)
                }
            }
        }
        return result
    }

    fun part2(): Long {
        val matrix =
            getInputAsText("03") {
                split("\n").filter(String::isNotBlank).map { it.toCharArray() }
            }.toTypedArray()

        var result = 0L
        for (i in matrix.indices) {
            for (j in matrix[i].indices) {
                if (matrix[i][j] != '*') continue
                val adjacentNumbersList = mutableListOf<MutableList<Pair<Int, Int>>>()
                if (matrix[i][j - 1].isDigit()) {
                    adjacentNumbersList.add(scanNumber(matrix, i, j - 1))
                }
                if (matrix[i][j + 1].isDigit()) {
                    adjacentNumbersList.add(scanNumber(matrix, i, j + 1))
                }
                adjacentNumbersList.addAll(findAdjacentNumbersInRow(matrix, i - 1, j))
                adjacentNumbersList.addAll(findAdjacentNumbersInRow(matrix, i + 1, j))
                if (adjacentNumbersList.size == 2) {
                    result += adjacentNumbersList[0].toNumber(matrix) * adjacentNumbersList[1].toNumber(matrix)
                }
            }
        }
        return result.toLong()
    }

    println(part1())
    println(part2())
}

private val neighbours = listOf(
    -1 to -1,
    -1 to 0,
    -1 to 1,
    0 to -1,
    0 to 1,
    1 to -1,
    1 to 0,
    1 to 1,
)

private fun scanNumber(matrix: Array<CharArray>, i: Int, inputj: Int): MutableList<Pair<Int, Int>> {
    var j = inputj
    while (j - 1 >= 0 && matrix[i][j - 1].isDigit()) {
        j--
    }
    val result = mutableListOf(i to j)
    while (j + 1 < matrix[i].size && matrix[i][j + 1].isDigit()) {
        j++
        result.add(i to j)
    }
    return result
}

private fun findAdjacentNumbersInRow(
    matrix: Array<CharArray>,
    i: Int,
    j: Int
): MutableList<MutableList<Pair<Int, Int>>> {
    val row = matrix[i]
    return when {
        row[j - 1].isDigit() && !row[j].isDigit() && row[j + 1].isDigit() -> mutableListOf(
            scanNumber(matrix, i, j - 1),
            scanNumber(matrix, i, j + 1)
        )

        row[j - 1].isDigit() -> mutableListOf(scanNumber(matrix, i, j - 1))
        row[j].isDigit() -> mutableListOf(scanNumber(matrix, i, j))
        row[j + 1].isDigit() -> mutableListOf(scanNumber(matrix, i, j + 1))
        else -> mutableListOf()
    }
}

private fun List<Pair<Int, Int>>.isNeighbourOfSymbol(matrix: Array<CharArray>): Boolean {
    return this.any { (i, j) ->
        neighbours.any { (di, dj) ->
            i + di >= 0 &&
                i + di < matrix.size &&
                j + dj >= 0 &&
                j + dj < matrix[i].size &&
                !matrix[i + di][j + dj].isDigit() &&
                matrix[i + di][j + dj] != '.'
        }
    }
}

private fun List<Pair<Int, Int>>.toNumber(matrix: Array<CharArray>): Long {
    var powindex = 0.0
    var number = 0L
    for (i in this.size - 1 downTo 0) {
        val (itemi, itemj) = this[i]
        number += matrix[itemi][itemj].digitToInt() * (10.0.pow(powindex)).toLong()
        powindex++
    }
    return number
}
