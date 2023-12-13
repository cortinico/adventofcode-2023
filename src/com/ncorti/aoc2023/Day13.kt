package com.ncorti.aoc2023

fun main() {

    fun parseInput() = getInputAsText("13") {
        split("\n\n").filter(String::isNotBlank).map {
            it.split("\n").filter(String::isNotBlank).map { line ->
                line.toCharArray()
            }.toTypedArray()
        }
    }

    fun hasSmudgeInCol(map: Array<CharArray>, col1: Int, col2: Int): Boolean {
        var countDiffs = 0
        for (j in map.indices) {
            if (map[j][col1] != map[j][col2]) {
                countDiffs++
            }
        }
        return countDiffs == 1
    }

    fun areColEquals(map: Array<CharArray>, col1: Int, col2: Int, withSmudge: Boolean): Boolean {
        if (!withSmudge) {
            return map.all { it[col1] == it[col2] }
        } else {
            if (map.all { it[col1] == it[col2] }) {
                return true
            }
            return hasSmudgeInCol(map, col1, col2)
        }
    }

    fun hasSmudgeInRow(map: Array<CharArray>, row1: Int, row2: Int): Boolean {
        var countDiffs = 0
        for (j in map[0].indices) {
            if (map[row1][j] != map[row2][j]) {
                countDiffs++
            }
        }
        return countDiffs == 1
    }

    fun areRowEquals(map: Array<CharArray>, row1: Int, row2: Int, withSmudge: Boolean): Boolean {
        if (!withSmudge) {
            return map[row1].contentEquals(map[row2])
        } else {
            if (map[row1].contentEquals(map[row2])) {
                return true
            }
            return hasSmudgeInRow(map, row1, row2)
        }
    }

    fun findReflectionColumn(map: Array<CharArray>, withSmudge: Boolean): Int {
        for (i in 0 until map[0].size - 1) {
            if (areColEquals(map, i, i + 1, withSmudge)) {
                var smudgeFound = hasSmudgeInCol(map, i, i + 1)
                var colLeft = i - 1
                var colRight = i + 2
                val candidate = i + 1
                while (colLeft >= 0 && colRight <= map[0].size - 1) {
                    if (areColEquals(map, colLeft, colRight, withSmudge)) {
                        smudgeFound = smudgeFound || hasSmudgeInCol(map, colLeft, colRight)
                        colLeft--
                        colRight++
                    } else {
                        break
                    }
                }
                if (colLeft == -1 || colRight == map[0].size && (!withSmudge || smudgeFound)) {
                    return candidate
                }
            }
        }
        return 0
    }


    fun findReflectionRow(map: Array<CharArray>, withSmudge: Boolean): Int {
        for (i in 0 until map.size - 1) {
            if (areRowEquals(map, i, i + 1, withSmudge)) {
                var smudgeFound = hasSmudgeInRow(map, i, i + 1)
                var rowUp = i - 1
                var rowDown = i + 2
                val candidate = i + 1
                while (rowUp >= 0 && rowDown <= map.size - 1) {
                    if (areRowEquals(map, rowUp, rowDown, withSmudge)) {
                        smudgeFound = smudgeFound || hasSmudgeInRow(map, rowUp, rowDown)
                        rowUp--
                        rowDown++
                    } else {
                        break
                    }
                }
                if (rowUp == -1 || rowDown == map.size && (!withSmudge || smudgeFound)) {
                    return candidate
                }
            }
        }
        return 0
    }

    fun part(withSmudge: Boolean = false): Int {
        val input = parseInput()
        return input.sumOf { map ->
            val reflectionRow = findReflectionRow(map, false)
            val reflectionRowWithSmudge = findReflectionRow(map, true)
            val reflectionCol = findReflectionColumn(map, false)
            val reflectionColWithSmudge = findReflectionColumn(map, true)
            if (!withSmudge) {
                if (reflectionRow != 0) {
                    100 * reflectionRow
                } else {
                    reflectionCol
                }
            } else {
                if (reflectionRowWithSmudge != 0 && reflectionRow != reflectionRowWithSmudge) {
                    100 * reflectionRowWithSmudge
                } else {
                    reflectionColWithSmudge
                }
            }
        }
    }

    println(part(withSmudge = false))
    println(part(withSmudge = true))
}
