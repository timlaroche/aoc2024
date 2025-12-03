package day3

import Day

class Day3(isTest: Boolean) : Day(isTest) {
    fun part1() {
        val input = Helper2025.readAsLines(inputFile)
        var res = 0L
        input.forEach { it ->
            var highestFound = 0
            val digits = it.chunked(1).map { it -> it.toInt() }

            // If I turn on this digit, what's the highest number I can make with the remaining digits
            digits.forEachIndexed { index, digit ->
                val subList = digits.subList(index + 1, digits.size)
                subList.forEach { it ->
                    val possible = "$digit$it".toInt()
                    if (possible > highestFound) highestFound = possible
                }
            }
            res += highestFound
        }
        println(res)
    }


    fun part2() {
        val input = Helper2025.readAsLines(inputFile)
        var res = 0L
        val wantedDigits = 2
        input.forEach { digitString ->
            // Go from right to left, need to keep at least 12 digits left
            // Pick the highest number that's 11 digits away
            val sb = StringBuilder()
            var digits = digitString.chunked(1).map { it -> it.toInt() }.toMutableList()
            for (i in wantedDigits - 1 downTo  0) {



                val valid = digits.dropLast(i)
                val max = valid.max()
                sb.append(max)
                // cleanup list
                val indexReached = digits.indexOf(max)
                val cleanedList = digits.subList(indexReached + 1, digits.size)
                cleanedList.remove(max)
                digits = cleanedList
            }
            res += sb.toString().toLong()
        }
        println(res)
    }
}