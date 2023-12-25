package com.ncorti.aoc2023

data class Line(
    val x1: Double,
    val y1: Double,
    val z1: Double,
    val vx: Double,
    val vy: Double,
    val vz: Double,
    val x2: Double = x1 + vx,
    val y2: Double = y1 + vy,
    val z2: Double = z1 + vz,
    val slope: Double = (y1 - y2) / (x1 - x2),
    val intercept: Double = y1 - (slope * x1),
)

fun main() {

    fun parseInput() = getInputAsText("24") {
        split("\n").filter(String::isNotBlank).map {
            val line = it.split(", ", " @ ").map { it.trim() }
            Line(
                line[0].toDouble(),
                line[1].toDouble(),
                line[2].toDouble(),
                line[3].toDouble(),
                line[4].toDouble(),
                line[5].toDouble()
            )
        }
    }

    fun isInTheFuture(p1: Double, vp: Double, intercept: Double) = if (vp > 0) {
        intercept > p1
    } else {
        intercept < p1
    }

    fun part1(): Int {
        val points = parseInput()
        var count = 0
        for (i in points.indices) {
            for (j in i + 1..points.lastIndex) {
                if (points[i].slope == points[j].slope) {
                    continue
                }
                val interceptX = (points[i].intercept - points[j].intercept) / (points[j].slope - points[i].slope)
                val interceptY = points[i].slope * interceptX + points[i].intercept
                if (interceptX in 200000000000000.0..400000000000000.0 && interceptY in 200000000000000.0..400000000000000.0 && isInTheFuture(
                        points[i].x1, points[i].vx, interceptX
                    ) && isInTheFuture(points[i].y1, points[i].vy, interceptY) && isInTheFuture(
                        points[j].x1, points[j].vx, interceptX
                    ) && isInTheFuture(points[j].y1, points[j].vy, interceptY)
                ) {
                    count++
                }
            }
        }
        return count
    }

    fun part2(): Long {
        val points = parseInput()
        var foundVx = setOf<Long>()
        var foundVy = setOf<Long>()
        var foundVz = setOf<Long>()
        for (i in points.indices) {
            for (j in i + 1..points.lastIndex) {
                if (points[i].vx == points[j].vx) {
                    val localFound = mutableSetOf<Long>()
                    for (v in -300L..300L) {
                        if (v.toDouble() != points[i].vx && (points[j].x1 - points[i].x1) % (v - points[i].vx) == 0.0) {
                            localFound.add(v)
                        }
                    }
                    foundVx = if (foundVx.isEmpty()) localFound.toSet() else foundVx.intersect(localFound)
                }
                if (points[i].vy == points[j].vy) {
                    val localFound = mutableSetOf<Long>()
                    for (v in -300L..300L) {
                        if (v.toDouble() != points[i].vy && (points[j].y1 - points[i].y1) % (v - points[i].vy) == 0.0) {
                            localFound.add(v)
                        }
                    }
                    foundVy = if (foundVy.isEmpty()) localFound.toSet() else foundVy.intersect(localFound)
                }
                if (points[i].vz == points[j].vz) {
                    val localFound = mutableSetOf<Long>()
                    for (v in -300L..300L) {
                        if (v.toDouble() != points[i].vz && (points[j].z1 - points[i].z1) % (v - points[i].vz) == 0.0) {
                            localFound.add(v)
                        }
                    }
                    foundVz = if (foundVz.isEmpty()) localFound.toSet() else foundVz.intersect(localFound)
                }
            }
        }

        val rvx = foundVx.first().toDouble()
        val rvy = foundVy.first().toDouble()
        val rvz = foundVz.first().toDouble()
        val (apx, apy, apz, avx, avy, avz) = points[0]
        val (bpx, bpy, _, bvx, bvy, _) = points[1]
        val ma = (avy - rvy) / (avx - rvx)
        val mb = (bvy - rvy) / (bvx - rvx)
        val ca = apy - (ma * apx)
        val cb = bpy - (mb * bpx)
        val x = ((cb - ca) / (ma - mb))
        val y = ((ma * x) + ca)
        val tim = (x - apx) / (avx - rvx)
        val z = apz + (avz - rvz) * tim
        return (x + y + z).toLong()
    }

    println(part1())
    println(part2())
}
