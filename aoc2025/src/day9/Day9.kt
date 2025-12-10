package day9

import Day
import kotlin.collections.mutableMapOf

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
        val grid = MutableList(boundsRow) { MutableList(boundsColumn) { false } }

        // Parse the points into the grid
        points.forEach { p -> grid[p.y.toInt()][p.x.toInt()] = true }
        // Scan horizontally
        for (i in 0..boundsRow - 1) {
            val horizontalIndexes = grid.get(i).mapIndexed { idx, it -> if (it == true) idx else -1 }.filter { it != -1 }
            val lines = horizontalIndexes.zipWithNext()
            lines.forEach {
                for (j in it.first..it.second) {
                    grid[i][j] = true
                }
            }
        }
        // Scan vertically
        for (j in 0..boundsColumn - 1) {
            val verticalIndexes = mutableListOf<Int>()
            for (i in 0..boundsRow - 1) {
                if (grid[i][j] == true) {
                    verticalIndexes.add(i)
                }
            }
            val lines = verticalIndexes.zipWithNext()
            lines.forEach {
                for (i in it.first..it.second) {
                    grid[i][j] = true
                }
            }
        }
        // At this point grid is a mask of 1s
        println(grid.size)
        // For each point, create a grid filling in 1s in the area
        var biggest = 0
        points.forEach { p1 ->
            points.forEach { p2 ->
                val areaMask = MutableList(boundsRow) { MutableList(boundsColumn) { false } }
                val startingX = Math.min(p1.x, p2.x).toInt()
                val startingY = Math.min(p1.y, p2.y).toInt()

                val dx = Math.abs(p2.x - p1.x).toInt()
                val dy = Math.abs(p2.y - p1.y).toInt()

                for (i in startingX..startingX + dx) {
                    for (j in startingY..startingY + dy) {
                        areaMask[j][i] = true
                    }
                }

//                println("======&&=====")
                val valid = validMask(areaMask, grid)
//                println(valid)
//                printGridAndMask(grid, areaMask)
//                println("=======")


                val areaOfMask = areaMask.filter { it.contains(true) }.sumOf { it.count { it -> it == true } }
                if (valid && areaOfMask > biggest) {
//                    println("New biggest found: $areaOfMask")
//                    println("Corners: $p1 to $p2")
                    biggest = areaOfMask
                }
            }
        }
        println(biggest)
    }

    data class Point2(val row: Int, val column: Int)

    fun part2_map() {
        // Work out green spots
        val input = Helper2025.readAsLines(inputFile)
        val points = input.map {
            val split = it.split(",")
            Point2(split[0].toInt(), split[1].toInt())
        }

        val boundsRow = points.maxOf { it.column } + 3
        val boundsColumn = points.maxOf { it.row } + 3

        // Initialize grid
        val grid = mutableMapOf<Point2, Boolean>()

        // Parse the points into the grid
        points.forEach { p -> grid[Point2(p.column, p.row)] = true }

        // Scan horizontally
        for (i in 0..boundsRow) {
            for (j in 0..boundsColumn) {
                val p = Point2(i, j)
                // If the grid has this point, then we are on a corner
                if (grid.contains(p)) {
                    val sameRow = grid.keys.filter { it.row == p.row }.sortedBy { it.column }
                    val pairs = sameRow.zipWithNext()
                    pairs.forEach { pair ->
                        for (k in pair.first.column..pair.second.column) {
                            grid[Point2(p.row, k)] = true
                        }
                    }
                }
            }
        }

        // Scan vertically
        for (j in 0..boundsColumn) {
            for (i in 0..boundsRow) {
                val p = Point2(i, j)
                // If the grid has this point, then we are on a corner
                if (grid.contains(p)) {
                    val sameColumn = grid.keys.filter { it.column == p.column }.sortedBy { it.row }
                    val pairs = sameColumn.zipWithNext()
                    pairs.forEach { pair ->
                        for (k in pair.first.row..pair.second.row) {
                            grid[Point2(k, p.column)] = true
                        }
                    }
                }
            }
        }

        // At this point grid is a mask of 1s
        // Create area masks for each pair of points
        var res = 0
        points.forEach { p1 ->
            points.forEach { p2 ->
                val areaMask = mutableMapOf<Point2, Boolean>()
                val startingX = Math.min(p1.row, p2.row)
                val startingY = Math.min(p1.column, p2.column)

                val dx = Math.abs(p2.row - p1.row)
                val dy = Math.abs(p2.column - p1.column)

                for (i in startingX..startingX + dx) {
                    for (j in startingY..startingY + dy) {
                        val p = Point2(i, j)
                        areaMask[Point2(p.column, p.row)] = true
                    }
                }

                val valid = validMaskMap(grid, areaMask)
                if (valid == true) {
                    if (areaMask.size > res) {
                        res = areaMask.size
                    }
                }
            }
        }
        println(res)
    }

    fun validMaskMap(grid: Map<Point2, Boolean>, mask: Map<Point2, Boolean>): Boolean {
        // TODO: should I check these are the same size?
        mask.forEach { entry ->
            if (!grid.contains(entry.key)) return false
        }
        return true
    }

    fun validMask(m1: MutableList<MutableList<Boolean>>, m2: MutableList<MutableList<Boolean>>): Boolean {
        val rowSize = m1.size
        val columnSize = m1[0].size

        for (i in 0..rowSize - 1) {
            for (j in 0..columnSize - 1) {
                if (m1[i][j] == true && m2[i][j] != true) {
                    return false
                }
            }
        }
        return true
    }

    fun printGridMap(map: Map<Point2, Boolean>) {
        val rowSize = map.keys.maxOf { it.row } + 1
        val columnSize = map.keys.maxOf { it.column } + 1

        for (i in 0..rowSize - 1) {
            for (j in 0..columnSize - 1) {
                val p = Point2(i, j)
                print(if (map.containsKey(p) && map[p] == true) "#" else ".")
            }
            println()
        }
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