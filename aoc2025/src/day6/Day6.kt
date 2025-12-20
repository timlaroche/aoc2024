package day6

import Day
import java.io.File
import java.util.function.BinaryOperator
import java.util.function.Predicate
import kotlin.math.PI

class Day6(isTest: Boolean) : Day(isTest) {
    data class ProblemInput(val inputs: List<String>)
    data class Problem(val operator: String, val inputs: List<Long>)

    fun part1() {
        val input = Helper2025.readAsStringGridIgnoringWhiteSpace(inputFile)
        val rowSize = input.size
        val columnSize = input[0].size

        val problems = mutableListOf<ProblemInput>()

        // read each column
        for (j in columnSize - 1 downTo 0) {
            val problemInput = mutableListOf<String>()
            for (i in rowSize - 1 downTo 0) {
                problemInput.add(input[i][j])
            }
            problems.add(ProblemInput(problemInput))
        }

        val parsed = problems.map { problem ->
            val operator = problem.inputs.subList(0, 1).map { it }.first()
            val inputs = problem.inputs.subList(1, problem.inputs.size).map { it.toLong() }
            Problem(operator, inputs)
        }

        val res = parsed.sumOf { problem ->
            val operationFunction: (Long, Long) -> Long = when (problem.operator) {
                "+" -> { a, b -> a + b }
                "*" -> { a, b -> a * b }
                else -> throw Exception("Unknown operator")
            }
            problem.inputs.reduce { acc, i -> operationFunction(acc, i) }
        }

        println(res)
    }

    // 2709223691871346 -- too high
    fun part2() {
        val input = Helper2025.readAsCharGrid(inputFile)
        // Make all rows the same length to avoid any out of bounds index
        val maxRowSize = input.maxBy { it.size }.size
        input.forEach {
            while (it.size < maxRowSize) {
                it.add(' ')
            }
        }

        // Read from the bottom up
        val problems = mutableListOf<Problem>()
        var problem = Problem("", listOf())
        for (i in maxRowSize - 1 downTo 0) {
            val numString = StringBuilder()
            input.forEach { row ->
                numString.append(row[i])
            }
            // If the string has a symbol - grab it and reset the current problem
            if (numString.contains("*") || numString.contains("+")) {
                val operator = if (numString.contains("*")) "*" else "+"
                problems.add(problem.copy(operator, problem.inputs + listOf(numString.split(operator)[0].trim().toLong())))
                problem = Problem("", listOf())
            }
            // Otherwise, keep building up the problem
            else if (numString.trim().isNotEmpty()) {
                problem = problem.copy(problem.operator, problem.inputs + listOf(numString.toString().trim().toLong()))
            }
        }

        val res = problems.sumOf { problem ->
            val operationFunction: (Long, Long) -> Long = when (problem.operator) {
                "+" -> { a, b -> a + b }
                "*" -> { a, b -> a * b }
                else -> throw Exception("Unknown operator")
            }
            problem.inputs.reduce { acc, i -> operationFunction(acc, i) }
        }
        println(res)
    }
}