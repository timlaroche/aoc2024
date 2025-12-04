package day4

import Day
import java.util.function.Predicate

class Day4(isTest: Boolean) : Day(isTest) {
    fun part1() {
        val grid = Helper2025.readAsCharGrid(inputFile)
        val adjacency = checkAdjacency(grid, {it -> it == '@'})
        val res = adjacency.filter { it -> grid.get(it.key.first).get(it.key.second) == '@' }
        val x = res.filter { it -> it.value < 4 }
        println(x.size)
    }

    fun part2() {
        var totalRes = 0

        val grid = Helper2025.readAsCharGrid(inputFile)
        val adjacency = checkAdjacency(grid, {it -> it == '@'})
        val res = adjacency.filter { it -> grid.get(it.key.first).get(it.key.second) == '@' }
        var toRemove = res.filter { it -> it.value < 4 }
        totalRes += toRemove.size

        while (toRemove.size != 0) {
            toRemove.forEach { it ->
                grid[it.key.first][it.key.second] = '.'
            }
            val adjacency = checkAdjacency(grid, {it -> it == '@'})
            val res = adjacency.filter { it -> grid.get(it.key.first).get(it.key.second) == '@' }
            toRemove = res.filter { it -> it.value < 4 }
            totalRes += toRemove.size
        }


        println(totalRes)
    }

    fun checkAdjacency(grid: List<List<Char>>, predicate: Predicate<Char>): Map<Pair<Int, Int>, Int> {
        var countMap = mutableMapOf<Pair<Int, Int>, Int>()
        var columnSize = grid[0].size - 1
        var rowSize = grid.size - 1

        grid.forEachIndexed { i, row ->
            row.forEachIndexed { j, columnValue ->
                var count = 0
                // N
                if (i == 0) {
                    // skip
                } else {
                    if (predicate.test(grid[i-1][j])) count += 1
                }
                // NE
                if (i == 0 || j == columnSize) {
                    // skip
                } else {
                    if (predicate.test(grid[i-1][j+1])) count += 1
                }
                // E
                if (j == columnSize) {
                    // skip
                } else {
                    if (predicate.test(grid[i][j+1])) count += 1
                }
                // SE
                if (i == rowSize || j == columnSize) {
                    // skip
                } else {
                    if (predicate.test(grid[i+1][j+1])) count += 1
                }
                // S
                if (i == rowSize) {
                    // skip
                } else {
                    if (predicate.test(grid[i+1][j])) count += 1
                }
                // SW
                if (i == rowSize || j == 0) {
                    // skip
                } else {
                    if (predicate.test(grid[i+1][j-1])) count += 1
                }
                // W
                if (j == 0) {
                    // skip
                } else {
                    if (predicate.test(grid[i][j-1])) count += 1
                }
                // NW
                if (j == 0 || i == 0) {
                    // skip
                } else {
                    if (predicate.test(grid[i-1][j-1])) count += 1
                }
                countMap.put(Pair(i, j), count)
            }
        }
        return countMap
    }
}