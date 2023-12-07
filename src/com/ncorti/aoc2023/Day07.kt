package com.ncorti.aoc2023

class CardComparator(val withJoker: Boolean) : Comparator<Pair<String, Int>> {
    private val cardRank = arrayOf('2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A')
    private val cardRankWithJoker = arrayOf('J', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'Q', 'K', 'A')

    private fun compareCard(card1: Char, card2: Char): Int =
        if (withJoker) cardRankWithJoker.indexOf(card1) - cardRankWithJoker.indexOf(card2)
        else cardRank.indexOf(card1) - cardRank.indexOf(card2)

    private fun handScore(hand: String): Int {
        val cards = hand.toCharArray()
        val grouping = cards.groupBy { it }.mapValues { it.value.size }.toMutableMap()

        if (withJoker && 'J' in grouping) {
            val jokerInstances = grouping['J']!!
            grouping.remove('J')
            if (jokerInstances == 5) {
                grouping['A'] = 5
            } else {
                val smartSortedEntries = grouping.entries.sortedWith { (key1, value1), (key2, value2) ->
                    when {
                        value1 > value2 -> -1
                        value1 < value2 -> 1
                        else -> compareCard(key1, key2)
                    }
                }
                val (firstEntry, firstValue) = smartSortedEntries[0]
                grouping[firstEntry] = firstValue + jokerInstances
            }
        }

        return when {
            grouping.values.any { it == 5 } -> 7
            grouping.values.any { it == 4 } -> 6
            grouping.values.any { it == 3 } && grouping.values.any { it == 2 } -> 5
            grouping.values.any { it == 3 } -> 4
            grouping.values.count { it == 2 } == 2 -> 3
            grouping.values.any { it == 2 } -> 2
            else -> 1
        }
    }

    override fun compare(o1: Pair<String, Int>, o2: Pair<String, Int>): Int {
        val (hand1, _) = o1
        val (hand2, _) = o2
        val score1 = handScore(hand1)
        val score2 = handScore(hand2)
        return when {
            score1 > score2 -> 1
            score1 < score2 -> -1
            else -> {
                for (i in hand1.indices) {
                    val card1 = hand1[i]
                    val card2 = hand2[i]
                    val cardComparison = compareCard(card1, card2)
                    if (cardComparison != 0) {
                        return cardComparison
                    }
                }
                0
            }
        }
    }
}

fun main() {
    fun part(withJoker: Boolean): Int {
        val hands = getInputAsText("07") {
            split("\n").filter(String::isNotBlank).map {
                val (card, rank) = it.split(" ")
                card to rank.toInt()
            }
        }
        return hands.sortedWith(CardComparator(withJoker = withJoker)).foldIndexed(0) { index, acc, (card, rank) ->
            acc + (rank * (index + 1))
        }
    }

    println(part(withJoker = false))
    println(part(withJoker = true))
}
