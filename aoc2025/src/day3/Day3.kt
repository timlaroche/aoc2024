package day3

import Day

class Day3(isTest: Boolean) : Day(isTest) {
    val founds = hashMapOf<String, Long>()
    val foundspt2 = hashMapOf<String, Long>()

    fun part1() {
        val input = Helper2025.readAsLines(inputFile)
        var res = 0L
        input.forEach { it ->
            var highestFound = 0L
            val digits = it.chunked(1).map { it -> it.toInt() }

            // If I turn on this digit, what's the highest number I can make with the remaining digits
            digits.forEachIndexed { index, digit ->
                val subList = digits.subList(index + 1, digits.size)
                subList.forEach { it ->
                    val possible = "$digit$it".toLong()
                    if (possible > highestFound) highestFound = possible
                }
            }
            founds.put(it, highestFound)
            res += highestFound
        }
        println(res)
    }


    fun part2() {
        val input = Helper2025.readAsLines(inputFile)
        var res = 0L
        val wantedDigits = 12
        input.forEach { digitString ->
            val sb = StringBuilder()
            var digits = digitString.chunked(1).map { it -> it.toInt() }.toMutableList()
            for (i in wantedDigits - 1 downTo  0) {
                // Pick the highest number that's 11 digits away
                // If we only have 11 digits left we need to take all of them!
                if (digits.size == i) {
                    digits.forEach { sb.append(it) }
                    break;
                }
                var valid = digits.dropLast(i)
                val max = valid.max()
                sb.append(max)
                // cleanup list
                var indexReached = digits.indexOf(max)
                val cleanedList = digits.subList(indexReached + 1, digits.size)
                digits = cleanedList
            }
            foundspt2.put(digitString, sb.toString().toLong())
            res += sb.toString().toLong()
        }
        println(res)
    }
}