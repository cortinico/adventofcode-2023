package com.ncorti.aoc2023

import com.ncorti.aoc2023.PipeState.*

enum class PipeState {
    NONE, FROM_TOP, FROM_BOTTOM
}

fun main() {
    fun part(part: Int = 1): Int {
        val map = getInputAsText("10") {
            split("\n").filter(String::isNotBlank).map { it.toCharArray() }
        }.toTypedArray()
        val startRow = map.first { 'S' in it }
        var y = startRow.indexOf('S')
        var x = map.indexOf(startRow)
        val startx = x
        val starty = y
        val seen = mutableListOf<Pair<Int, Int>>()
        var found = false

        while (!found) {
            seen.add(x to y)
            if (seen.size > 2) {
                seen.remove(startx to starty)
            }
            when {
                x - 1 >= 0 &&
                    (map[x - 1][y] == '|' || map[x - 1][y] == 'F' || map[x - 1][y] == '7' || map[x - 1][y] == 'S') &&
                    (x - 1 to y !in seen) &&
                    (map[x][y] == 'S' || map[x][y] == '|' || map[x][y] == 'J' || map[x][y] == 'L') -> {
                    // move up
                    x -= 1
                }

                x + 1 < map.size &&
                    (map[x + 1][y] == '|' || map[x + 1][y] == 'J' || map[x + 1][y] == 'L' || map[x + 1][y] == 'S') &&
                    (x + 1 to y !in seen) &&
                    (map[x][y] == 'S' || map[x][y] == '|' || map[x][y] == 'F' || map[x][y] == '7') -> {
                    // move down
                    x += 1
                }

                y - 1 >= 0 &&
                    (map[x][y - 1] == '-' || map[x][y - 1] == 'F' || map[x][y - 1] == 'L' || map[x][y - 1] == 'S') &&
                    (x to y - 1 !in seen) &&
                    (map[x][y] == 'S' || map[x][y] == '-' || map[x][y] == 'J' || map[x][y] == '7') -> {
                    // move left
                    y -= 1
                }

                y + 1 < map[0].size &&
                    (map[x][y + 1] == '-' || map[x][y + 1] == 'J' || map[x][y + 1] == '7' || map[x][y + 1] == 'S') &&
                    (x to y + 1 !in seen) &&
                    (map[x][y] == 'S' || map[x][y] == '-' || map[x][y] == 'F' || map[x][y] == 'L') -> {
                    // move right
                    y += 1
                }
            }
            if (x == startx && y == starty) {
                found = true
            }
        }
        seen.add(0, startx to starty)
        if (part == 1) {
            return seen.size / 2
        }
        var count = 0
        var isInside: Boolean
        var pipeState = NONE
        for (i in map.indices) {
            isInside = false
            for (j in map[i].indices) {
                if (i to j !in seen && !isInside) {
                    continue
                } else if (i to j !in seen && isInside) {
                    count++
                    continue
                }
                when {
                    map[i][j] == '|' -> isInside = !isInside
                    map[i][j] == '.' && isInside -> {
                        count++
                    }

                    map[i][j] == '.' && !isInside -> { // no-op as outside
                    }

                    map[i][j] == '-' -> { // no-op as a pipe
                    }

                    map[i][j] == 'L' && pipeState == NONE -> pipeState = FROM_TOP
                    map[i][j] == 'F' && pipeState == NONE -> pipeState = FROM_BOTTOM
                    map[i][j] == 'J' && pipeState == FROM_TOP -> pipeState = NONE
                    map[i][j] == 'L' && pipeState != NONE -> {}
                    map[i][j] == '7' && pipeState == NONE -> {}
                    map[i][j] == 'J' && pipeState == NONE -> {}
                    map[i][j] == 'F' && pipeState != NONE -> {}
                    map[i][j] == 'J' && pipeState == FROM_BOTTOM -> {
                        pipeState = NONE
                        isInside = !isInside
                    }

                    map[i][j] == '7' && pipeState == FROM_BOTTOM -> pipeState = NONE
                    map[i][j] == '7' && pipeState == FROM_TOP -> {
                        pipeState = NONE
                        isInside = !isInside
                    }

                    map[i][j] == 'S' -> {
                        val extremes = setOf(seen[1], seen.last())
                        if (i - 1 to j in extremes && i + 1 to j in extremes) {
                            // is a |
                            isInside = !isInside
                        } else if (i to j - 1 in extremes && i to j + 1 in extremes) {
                            // is a -
                        } else if (i - 1 to j in extremes && i to j - 1 in extremes) {
                            // is a J
                            if (pipeState == FROM_TOP) {
                                pipeState = NONE
                                isInside = !isInside
                            }
                        } else if (i - 1 to j in extremes && i to j + 1 in extremes) {
                            // is a L
                            if (pipeState == NONE) {
                                pipeState = FROM_TOP
                            }
                        } else if (i + 1 to j in extremes && i to j - 1 in extremes) {
                            // is a 7
                            if (pipeState == FROM_TOP) {
                                pipeState = NONE
                                isInside = !isInside
                            } else if (pipeState == FROM_BOTTOM) {
                                pipeState = NONE
                            }
                        } else if (i + 1 to j in extremes && i to j + 1 in extremes) {
                            // is a F
                            if (pipeState == NONE) {
                                pipeState = FROM_BOTTOM
                            }
                        }
                    }
                }
            }
        }
        return count
    }

    println(part(1))
    println(part(2))
}
