package com.ncorti.aoc2023

fun main() {

    fun parseInput(): Pair<Map<String, List<String>>, List<Map<String, Int>>> {
        val (workflows, parts) = getInputAsText("19") {
            split("\n\n").filter(String::isNotBlank).map {
                it.split("\n").filter(String::isNotBlank)
            }
        }
        val workflowMap = workflows.associate {
            val key = it.substringBefore("{").trim()
            val value = it.substringBefore("}").substringAfter("{").split(",")
            key to value
        }
        val partsList = parts.map {
            it.removeSurrounding("{", "}").split(",").map {
                it.split("=")
            }.associate { (key, value) ->
                key to value.toInt()
            }
        }
        return workflowMap to partsList
    }

    fun computeNewIntervals(
        greaterThan: Boolean, threshold: Int, letter: String, target: String, input: IntRange
    ): Pair<IntRange, IntRange> = when {
        target != letter -> input to input
        greaterThan -> (threshold + 1)..input.last to input.first..threshold
        else -> input.first..<threshold to (threshold)..input.last
    }

    fun IntRange.size(): Long = (this.last - this.first + 1).toLong()

    fun computeCombination(
        workflows: Map<String, List<String>>,
        current: List<String>,
        idx: Int,
        rangeX: IntRange,
        rangeM: IntRange,
        rangeA: IntRange,
        rangeS: IntRange
    ): Long {
        val step = current[idx]
        return when {
            "<" in step || ">" in step -> {
                val (condition, consequence) = step.split(":")
                val letter = condition.substringBefore(">").substringBefore("<")
                val threshold = condition.substringAfter(">").substringAfter("<").toInt()
                val greaterThan = ">" in condition
                val (newX, oldX) = computeNewIntervals(greaterThan, threshold, letter, "x", rangeX)
                val (newM, oldM) = computeNewIntervals(greaterThan, threshold, letter, "m", rangeM)
                val (newA, oldA) = computeNewIntervals(greaterThan, threshold, letter, "a", rangeA)
                val (newS, oldS) = computeNewIntervals(greaterThan, threshold, letter, "s", rangeS)
                val newCombinationResult = when {
                    "R" in consequence -> 0
                    "A" in consequence -> newX.size() * newM.size() * newA.size() * newS.size()
                    else -> computeCombination(workflows, workflows[consequence]!!, 0, newX, newM, newA, newS)
                }
                newCombinationResult + computeCombination(workflows, current, idx + 1, oldX, oldM, oldA, oldS)
            }

            "A" in step -> rangeX.size() * rangeM.size() * rangeA.size() * rangeS.size()
            "R" in step -> 0
            else -> computeCombination(workflows, workflows[step]!!, 0, rangeX, rangeM, rangeA, rangeS)
        }
    }


    fun part1(): Int {
        val (workflows, parts) = parseInput()
        return parts.sumOf { map ->
            var currentWorkflow = workflows["in"]!!
            var idx = 0
            var accepted = false
            while (true) {
                val step = currentWorkflow[idx]
                val (condition, consequence) = if (":" in step) {
                    step.split(":")
                } else {
                    listOf("", step)
                }
                val letter = condition.substringBefore(">").substringBefore("<")
                val threshold = condition.substringAfter(">").substringAfter("<")
                if ((">" !in condition && "<" !in condition) || (">" in condition && map[letter]!! > threshold.toInt()) || ("<" in condition && map[letter]!! < threshold.toInt())) {
                    if (consequence == "A") {
                        accepted = true
                        break
                    } else if (consequence == "R") {
                        accepted = false
                        break
                    }
                    currentWorkflow = workflows[consequence]!!
                    idx = 0
                    continue
                }
                idx++
            }
            if (accepted) {
                map["x"]!! + map["m"]!! + map["a"]!! + map["s"]!!
            } else {
                0
            }
        }
    }

    fun part2(): Long {
        val (workflows, _) = parseInput()
        return computeCombination(
            workflows = workflows,
            current = workflows["in"]!!,
            idx = 0,
            rangeX = 1..4000,
            rangeM = 1..4000,
            rangeA = 1..4000,
            rangeS = 1..4000
        )
    }

    println(part1())
    println(part2())
}

