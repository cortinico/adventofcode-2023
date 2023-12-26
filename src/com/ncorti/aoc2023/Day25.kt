package com.ncorti.aoc2023

fun main() {

    fun part1(): Int {
        val graph = getInputAsText("25") {
            split("\n").filter(String::isNotBlank).map {
                it.split(": ", " ")
            }.associate {
                it[0] to it.drop(1).toMutableList()
            }
        }
        val nodes = (graph.keys.toMutableSet() + graph.values.flatten().toMutableSet()).distinct()
        val size = nodes.size
        println("""
            digraph Day25Graph {
                overlap = true;
                layout = neato;
        """.trimIndent())
        nodes.forEach { node ->
            println("    $node;")
        }
        println()
        graph.forEach { (node, edges) ->
            edges.forEach { next ->
                println("    $node -> $next;")
            }
        }
        println("}")
        // Run graphviz -Tsvg -osolution.svg input.dot
        graph["sdv"]!!.remove("mxv")
        graph["klj"]!!.remove("scr")
        graph["gqr"]!!.remove("vbk")

        val toVisit = mutableListOf("sdv")
        val seen = mutableSetOf<String>()
        while (toVisit.isNotEmpty()) {
            val current = toVisit.removeAt(0)
            seen.add(current)
            toVisit.addAll(graph[current]?.filter { it !in seen } ?: emptyList())
            graph.forEach { (start, edge) ->
                if (current in edge && start !in seen) {
                    toVisit.add(start)
                }
            }
        }
        return seen.size * (size - seen.size)
    }
    println(part1())
}
