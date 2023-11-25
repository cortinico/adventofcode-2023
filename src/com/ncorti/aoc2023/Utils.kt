package com.ncorti.aoc2023

internal fun <R> getInputAsText(day: String, transform: String.() -> R): R =
  object {}.javaClass.getResource("input-$day.txt")?.readText()?.let { it.transform() }
    ?: error("Failed to read day $day input")
