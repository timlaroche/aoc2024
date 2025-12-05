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
    // 342492218176632 - incorrect
    fun part2() {
        val input = Helper2025.readAsLines(inputFile)
        val parsedInput = parseInput(input)
        var ranges = parsedInput.first
    }
}

fun mergeRange(range: LongRange, incoming: LongRange): LongRange? {
    if (range == incoming) return null
    else if (incoming.first == incoming.endInclusive && (range.first == incoming.first || range.endInclusive >= incoming.first)) {
        return LongRange(range.first, range.last)
    }
    // 1 self contaiend
    else if (incoming.first >= range.first && incoming.endInclusive < range.endInclusive) {
        return LongRange(range.first, range.endInclusive)
    }
    // 2 contained extending past
    else if (incoming.first > range.first && range.endInclusive > incoming.first && incoming.endInclusive > range.endInclusive) {
        return LongRange(range.first, incoming.endInclusive)
    }
    // 3 contaiend extending before
    else if (incoming.first < range.first && range.first < incoming.endInclusive && incoming.endInclusive > range.first) {
        return LongRange(incoming.first, range.endInclusive)
    } else {
        return null
    }
}