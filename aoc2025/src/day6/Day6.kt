package day6

import Day
import java.io.File
import java.util.function.BinaryOperator
import java.util.function.Predicate

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

    fun part2() {}
}