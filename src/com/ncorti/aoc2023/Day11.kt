package com.ncorti.aoc2023

import kotlin.math.abs
import kotlin.math.min

fun main() {
    fun part(factor : Long = 2): Long {
        val map = getInputAsText("11") {
            split("\n").filter(String::isNotBlank).map { it.toCharArray().toMutableList() }
        }.toMutableList()
        var i = 0
        while (i < map.size) {
            if (map[i].all { it == '.' }) {
                map.removeAt(i)
                map.add(i, map[i].toList().map { '$' }.toMutableList())
            }
            i++
        }
        i = 0
        while (i < map[0].size) {
            if (map.all { it[i] == '.' || it[i] == '$' }) {
                map.forEach {
                    it.removeAt(i)
                    it.add(i, '$')
                }
            }
            i++
        }
        val galaxies = mutableListOf<Pair<Int, Int>>()
        map.forEachIndexed { i, line ->
            line.forEachIndexed { j, char ->
                if (char == '#') {
                    galaxies.add(i to j)
                }
            }
        }
        var sum = 0L
        val seen = mutableSetOf<Pair<Pair<Int,Int>,Pair<Int,Int>>>()
        for (i in galaxies.indices) {
            for (j in i .. galaxies.lastIndex) {
                if (galaxies[i] to galaxies[j] !in seen && galaxies[j] to galaxies[i] !in seen && i != j) {
                    seen.add(galaxies[i] to galaxies[j])
                    var localSum = 1L
                    val upMost = min(galaxies[i].first, galaxies[j].first)
                    val leftMost = min(galaxies[i].second, galaxies[j].second)
                    for(col in upMost+1  until upMost+abs(galaxies[i].first - galaxies[j].first)) {
                        localSum += if (map[col][galaxies[i].second] == '$') {
                            factor
                        } else {
                            1L
                        }
                    }
                    for(row in leftMost+1 until leftMost+abs(galaxies[i].second - galaxies[j].second)) {
                        localSum += if (map[galaxies[i].first][row] == '$') {
                            factor
                        } else {
                            1L
                        }
                    }
                    if (galaxies[i].first != galaxies[j].first && galaxies[i].second != galaxies[j].second) {
                        localSum++
                    }
                    sum += localSum
                }
            }
        }
        return sum
    }

    println(part(factor = 2L))
    println(part(factor = 1_000_000L))
}
