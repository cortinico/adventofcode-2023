package com.ncorti.aoc2023

typealias CharCube = Array<Array<Array<Char>>>

data class Brick(
    val name: Char,
    val start: Triple<Int, Int, Int>,
    val end: Triple<Int, Int, Int>,
    var position: MutableList<Triple<Int, Int, Int>> = mutableListOf()
) {
    fun getBricksAbove(world: CharCube, bricks: List<Brick>): MutableList<Brick> {
        val result = mutableSetOf<Brick>()
        position.forEach { (z, x, y) ->
            if (z < world.size - 1 && world[z + 1][x][y] != name && world[z + 1][x][y] != '.') {
                result.add(bricks.first { brick -> brick.position.contains(Triple(z + 1, x, y)) })
            }
        }
        return result.toMutableList()
    }
}

fun main() {

    fun parseInput(): Pair<CharCube, List<Brick>> {
        var name = ('a'.code - 1).toChar()
        val bricks = getInputAsText("22") {
            split("\n").filter(String::isNotBlank).map {
                val points = it.split('~', ',')
                name = (name.code + 1).toChar()
                Brick(
                    name,
                    Triple(points[0].toInt(), points[1].toInt(), points[2].toInt()),
                    Triple(points[3].toInt(), points[4].toInt(), points[5].toInt())
                )
            }
        }
        val max = bricks.maxOf {
            listOf(it.start.first, it.start.second, it.start.third, it.end.first, it.end.second, it.end.third).max()
        }
        val world = Array(max + 1) { Array(max + 1) { Array(max + 1) { '.' } } }
        bricks.forEach {
            for (x in it.start.first..it.end.first) {
                for (y in it.start.second..it.end.second) {
                    for (z in it.start.third..it.end.third) {
                        world[z][x][y] = it.name
                        it.position.add(Triple(z, x, y))
                    }
                }
            }
        }
        return world to bricks
    }

    fun letBrickFall(world: CharCube, bricks: List<Brick>) {
        val brickToFall = mutableListOf<Brick>()
        while (true) {
            brickToFall.clear()
            bricks.forEach {
                if (it.position.all { (z, x, y) -> z > 0 && (world[z - 1][x][y] == '.' || world[z - 1][x][y] == it.name) }) {
                    brickToFall.add(it)
                }
            }
            if (brickToFall.isEmpty()) {
                break
            } else {
                brickToFall.forEach {
                    it.position.forEach { (z, x, y) ->
                        world[z][x][y] = '.'
                        world[z - 1][x][y] = it.name
                    }
                    it.position = it.position.map { (z, x, y) -> Triple(z - 1, x, y) }.toMutableList()
                }
            }
        }
    }

    fun computeSustainingBricks(world: CharCube, bricks: List<Brick>) = bricks.count { candidate ->
        val bricksAbove = candidate.getBricksAbove(world, bricks)
        candidate.position.forEach { (z, x, y) ->
            if (z < world.size - 1 && world[z + 1][x][y] != candidate.name && world[z + 1][x][y] != '.') {
                bricksAbove.add(bricks.first { brick -> brick.position.contains(Triple(z + 1, x, y)) })
            }
        }
        bricksAbove.none { brick ->
            brick.position.all { (z, x, y) ->
                z == 0 || (world[z - 1][x][y] == '.' || world[z - 1][x][y] == candidate.name || world[z - 1][x][y] == brick.name)
            }
        }
    }

    fun computeChainReaction(world: CharCube, bricks: List<Brick>, beginning: Brick): Int {
        val wouldFall = mutableListOf(beginning)
        var count = 0
        val fallingBricksName = mutableSetOf<Char>()
        while (wouldFall.isNotEmpty()) {
            val next = wouldFall.removeAt(0)
            fallingBricksName.add(next.name)
            var bricksAbove = next.getBricksAbove(world, bricks)
            bricksAbove = bricksAbove.distinct().toMutableList().filter { brick ->
                brick.position.all { (z, x, y) ->
                    z == 0 || (world[z - 1][x][y] == '.' || world[z - 1][x][y] == next.name || world[z - 1][x][y] == brick.name || world[z - 1][x][y] in fallingBricksName)
                }
            }.toMutableList()
            bricksAbove.distinct().forEach {
                if (it !in wouldFall) {
                    wouldFall.add(it)
                    fallingBricksName.add(it.name)
                    count++
                }
            }
        }
        return count
    }

    fun part1(): Int = parseInput().let { (world, bricks) ->
        letBrickFall(world, bricks)
        computeSustainingBricks(world, bricks)
    }

    fun part2(): Int = parseInput().let { (world, bricks) ->
        letBrickFall(world, bricks)
        bricks.sumOf {
            computeChainReaction(world, bricks, it)
        }
    }

    println(part1())
    println(part2())
}

