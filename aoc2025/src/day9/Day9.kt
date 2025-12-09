package day9

import Day
import day8.Day8

class Day9(isTest: Boolean) : Day(isTest) {
    data class Point(val x: Long, val y: Long)

    fun part1() {
        val input = Helper2025.readAsLines(inputFile)
        val points = input.map {
            val split = it.split(",")
            Point(split[0].toLong(), split[1].toLong())
        }

        val areas = mutableListOf<Long>()
        points.forEach { p1 ->
            points.forEach { p2 ->
                areas.add(area(p1, p2))
            }
        }

        val res = areas.max()
        println(res)
    }

    fun area(p1: Point, p2: Point): Long {
        val dx = Math.abs(p2.x - p1.x) + 1
        val dy = Math.abs(p2.y - p1.y) + 1
        return dx * dy
    }

    fun part2() {
        // Work out green spots
        val input = Helper2025.readAsLines(inputFile)
        val points = input.map {
            val split = it.split(",")
            Point(split[0].toLong(), split[1].toLong())
        }

        val boundsRow = (points.maxOf { it.y } + 3).toInt()
        val boundsColumn = (points.maxOf { it.x } + 3).toInt()

        // Initialize grid
        val grid = MutableList(boundsRow) { MutableList(boundsColumn) { 0 } }

        // Parse the points into the grid
        points.forEach { p -> grid[p.y.toInt()][p.x.toInt()] = 1 }
        // Scan horizontally
        for (i in 0..boundsRow - 1) {
            val horizontalIndexes = grid.get(i).mapIndexed { idx, it -> if (it == 1) idx else -1 }.filter { it != -1 }
            val lines = horizontalIndexes.zipWithNext()
            lines.forEach {
                for (j in it.first..it.second) {
                    grid[i][j] = 1
                }
            }
        }
        // Scan vertically
        for (j in 0..boundsColumn - 1) {
            val verticalIndexes = mutableListOf<Int>()
            for (i in 0..boundsRow - 1) {
                if (grid[i][j] == 1) {
                    verticalIndexes.add(i)
                }
            }
            val lines = verticalIndexes.zipWithNext()
            lines.forEach {
                for (i in it.first..it.second) {
                    grid[i][j] = 1
                }
            }
        }
        // At this point grid is a mask of 1s
        println(grid.size)
        // For each point, create a grid filling in 1s in the area
        var biggest = 0
        points.forEach { p1 ->
            points.forEach { p2 ->
                val areaMask = MutableList(boundsRow) { MutableList(boundsColumn) { 0 } }
                val startingX = Math.min(p1.x, p2.x).toInt()
                val startingY = Math.min(p1.y, p2.y).toInt()

                val dx = Math.abs(p2.x - p1.x).toInt()
                val dy = Math.abs(p2.y - p1.y).toInt()

                for (i in startingX..startingX + dx) {
                    for (j in startingY..startingY + dy) {
                        areaMask[j][i] = 1
                    }
                }

//                println("======&&=====")
                val valid = validMask(areaMask, grid)
//                println(valid)
//                printGridAndMask(grid, areaMask)
//                println("=======")


                val areaOfMask = areaMask.filter { it.contains(1) }.sumOf { it.count { it -> it == 1 } }
                if (valid && areaOfMask > biggest) {
//                    println("New biggest found: $areaOfMask")
//                    println("Corners: $p1 to $p2")
                    biggest = areaOfMask
                }
            }
        }
        println(biggest)
    }

    fun validMask(m1: MutableList<MutableList<Int>>, m2: MutableList<MutableList<Int>>): Boolean {
        val rowSize = m1.size
        val columnSize = m1[0].size

        for (i in 0..rowSize - 1) {
            for (j in 0..columnSize - 1) {
                if (m1[i][j] == 1 && m2[i][j] != 1) {
                    return false
                }
            }
        }
        return true
    }

    fun printGrid(grid: List<List<Int>>) {
        grid.forEach { row ->
            row.forEach { cell ->
                print(if (cell == 0) "." else "#")
            }
            println()
        }
    }

    fun printGridAndMask(grid: List<List<Int>>, mask: List<List<Int>>) {
        val rowSize = grid.size
        val columnSize = grid[0].size

        for (i in 0..rowSize - 1) {
            for (j in 0..columnSize - 1) {
                if (mask[i][j] == 1 && grid[i][j] == 1) {
                    print("O")
                }
                else if (mask[i][j] == 1 && grid[i][j] == 0) {
                    print("X")
                }
                else {
                    print(if (grid[i][j] == 0) "." else "#")
                }
            }
            println()
        }
    }
}