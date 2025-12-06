package day5

import Day
import java.io.File
import java.util.function.Predicate

class Day5(isTest: Boolean) : Day(isTest) {
    fun parseInput(input: List<String>): Pair<List<LongRange>, List<LongRange>> {
        val input = Helper2025.readAsLines(inputFile)
        val ranges = mutableListOf<LongRange>()
        val ingredients = mutableListOf<LongRange>()
        var rangeInput = true
        input.forEach { it ->
            if (it.isEmpty()) {
                rangeInput = false
                return@forEach
            }
            if (rangeInput) {
                val split = it.split("-")
                if (split.size != 2) throw Exception("Invalid input")
                ranges.add(LongRange(split[0].toLong(), split[1].toLong()))
            } else {
                ingredients.add(LongRange(it.toLong(), it.toLong()))
            }
        }
        return Pair(ranges, ingredients)
    }

    fun part1() {
        val input = Helper2025.readAsLines(inputFile)
        val parsedInput = parseInput(input)
        val ranges = parsedInput.first
        val ingredients = parsedInput.second
        var res = 0

        ingredients.forEach { ingredient ->
            val fresh = ranges.any { it -> it.contains(ingredient.first) }
            if (fresh) res += 1
        }

        println(res)
    }

    // 360334099692323 -- too high
    // 352681648086146  -- YES
    // 352681648086160 - incorrect
    // 325062818129913 - incorrect
    // 342492218176632 - incorrect
    fun part2() {
        val input = Helper2025.readAsLines(inputFile)
        val parsedInput = parseInput(input)

        val sortedRanges = parsedInput.first.sortedBy { it.first }
        val res = mutableListOf<LongRange>()
        val finalRange = sortedRanges.reduce { sum, elem ->
            if (elem.first <= sum.endInclusive) {
                LongRange(
                    sum.first,
                    Math.max(elem.endInclusive, sum.endInclusive)
                )
            } else {
                res.add(sum)
                elem
            }
        }

        if (finalRange.first != finalRange.last) {
            res.add(finalRange)
        }
        var finalAnswer = 0L
        res.forEach { it ->
            finalAnswer += (it.endInclusive - it.first) + 1
        }
        println(finalAnswer)
    }
}