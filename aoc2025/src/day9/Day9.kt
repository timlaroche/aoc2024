package day9

import Day
import java.io.File
import kotlin.collections.mutableMapOf

// pt2 378779816 too low
// 433059134 too low
// 4728026430 too high
// 4602484064 not correct
// 4781377701 wrong

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
    fun areapt2(p1: Point2, p2: Point2): Long {
        val dx = Math.abs(p2.row - p1.row).toLong() + 1
        val dy = Math.abs(p2.column - p1.column).toLong() + 1
        return dx * dy
    }

    data class Point2(val row: Int, val column: Int)
    data class Rectangle(val topLeft: Point2, val bottomRight: Point2, val area: Long)

    fun part2() {
        // Work out green spots
        val input = Helper2025.readAsLines(inputFile)
        val points = input.map {
            val split = it.split(",")
            Point2(split[0].toInt(), split[1].toInt())
        }

        // Part 1 work out biggest rectangles
        var areas = mutableListOf<Rectangle>()
        points.forEach { p1 ->
            points.forEach { p2 ->
                areas.add(Rectangle(p1, p2, areapt2(p1, p2)))
            }
        }
        areas.sortByDescending { it.area }

        // Initialize grid
        val grid = mutableMapOf<Point2, Boolean>()

        // Parse the points into the grid
        points.forEach { p -> grid[Point2(p.row, p.column)] = true }

        // Scan horizontally
        val fillHorizontal = grid.keys.mapIndexed { i, p ->
            val sameRow = grid.keys.filter { it.column == p.column }.sortedBy { it.row }
            val pairs = sameRow.chunked(2).map { Pair(it[0], it[1]) }
            val res = mutableListOf<Point2>()
            pairs.forEach { pair ->
                for (k in pair.first.row..pair.second.row) {
                    res.add(Point2(k, p.column))
                }
            }
            res
        }.flatten().toSet()

        // Scan vertically
        val fillVertical = grid.keys.mapIndexed { i, p ->
            val sameColumn = grid.keys.filter { it.row == p.row }.sortedBy { it.column }
            val pairs = sameColumn.chunked(2).map { Pair(it[0], it[1]) }
            val res = mutableListOf<Point2>()
            pairs.forEach { pair ->
                for (k in pair.first.column..pair.second.column) {
                    res.add(Point2(p.row, k))
                }
            }
            res
        }.flatten().toSet()
        printGridMapToFile(grid)

        // This is the perimeter of the filled area
        fillHorizontal.forEach { p -> grid[p] = true }
        fillVertical.forEach { p -> grid[p] = true }
        printGridMapToFile(grid)
        //val compressedGrid = compressGrid(grid)
        //printGridMapToFile(grid)

        // Create area masks for each pair of points
        var res = 0L
        println("tocheck: ${points.size * points.size}")
        areas.forEachIndexed { i, area ->
            val start = System.nanoTime()
            println("$i: Checking points ${area.topLeft} x ${area.bottomRight} with area ${area.area}")

            val perimMask = mutableMapOf<Point2, Boolean>()
            val startingX = Math.min(area.topLeft.row, area.bottomRight.row)
            val startingY = Math.min(area.topLeft.column, area.bottomRight.column)

            val dx = Math.abs(area.bottomRight.row - area.topLeft.row)
            val dy = Math.abs(area.bottomRight.column - area.topLeft.column)

            // Make rectangle
            // top
            val perimMaskList = mutableListOf<Point2>()
            for (i in startingX..startingX + dx) {
                perimMaskList.add(Point2(i, startingY))
            }
            // bottom
            for (i in startingX..startingX + dx) {
                perimMaskList.add(Point2(i, startingY + dy))
            }
            // left
            for (i in startingY..startingY + dy) {
                perimMaskList.add(Point2(startingX, i))
            }
            // right
            for (i in startingY..startingY + dy) {
                perimMaskList.add(Point2(startingX + dx, i))
            }
            val c2 = System.nanoTime()
            println("Created perimeter in ${(start - c2) / 1_000_000}ms")
            perimMaskList.distinct().forEach { p -> perimMask[p] = true }
            val c3 = System.nanoTime()
            println("Created perimeter map in ${(c3 - c2) / 1_000_000}ms")

            val pointsToCheck = perimMask.keys.filter { p -> !grid.contains(p) }
            val a = pointsToCheck.distinctBy { it.row }
            val b = pointsToCheck.distinctBy { it.column }
            val amax = a.maxByOrNull { it.row }
            val amin = a.minByOrNull { it.row }
            val bmax = b.maxByOrNull { it.column }
            val bmin = b.minByOrNull { it.column }
            val cornerChecks = setOfNotNull(amin, amax, bmin, bmax)
            val invalid = cornerChecks.any { p -> checkPointHitsEdgeInEveryDirection(grid, p) == false }
            val c4 = System.nanoTime()
            println("Checked validity in ${(c4 - c3) / 1_000_000}ms")

            if(!invalid) {
                println("VALID AREA FOUND: ${area.topLeft}, ${area.bottomRight} with area ${area.area}")
                val newArea = area.area
                if (newArea > res) {
                    res = newArea
                    println("CURRENTRES: $res via ${area.topLeft}, ${area.bottomRight}")
                }
            }
            val end = System.nanoTime()
            println("Checked point in ${(end - start) / 1_000_000}ms")
        }
        println("FINALRES: $res")
    }

    fun checkPointHitsEdgeInEveryDirection(grid: Map<Point2, Boolean>, p: Point2): Boolean {
        // Column check
        val sameX = grid.keys.filter { it.row == p.row }.sortedBy { it.column }
        if (sameX.isEmpty()) return false
        val minY = sameX.minBy { it.column }
        val maxY = sameX.maxBy { it.column }
        val inBoundsRow = minY.column <= p.column && p.column <= maxY.column
        if (!inBoundsRow) return false

        // Row check
        val sameY = grid.keys.filter { it.column == p.column }.sortedBy { it.row }
        if (sameY.isEmpty()) return false
        val minColumn = sameY.minBy { it.row }
        val maxColumn = sameY.maxBy { it.row }
        val inBoundsColumn = minColumn.row <= p.row && p.row <= maxColumn.row
        if (!inBoundsColumn) return false

        return true
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
        val rowSize = map.keys.maxOf { it.row } + 10
        val columnSize = map.keys.maxOf { it.column } + 10

        for (i in 0..rowSize - 1) {
            for (j in 0..columnSize - 1) {
                val p = Point2(j, i)
                print(if (map.containsKey(p) && map[p] == true) "#" else ".")
            }
            println()
        }
    }

    fun printGridMapToFile(map: Map<Point2, Boolean>) {
        val rowSize = 30
        val columnSize = 30

        val lines = mutableListOf<String>()
        for (i in 0..rowSize - 1) {
            val lineBuilder = StringBuilder()
            for (j in 0..columnSize - 1) {
                val p = Point2(j, i)
                lineBuilder.append(if (map.containsKey(p) && map[p] == true) "#" else ".")
            }
            lines.add(lineBuilder.toString())
        }
        File("gridoutput.txt").printWriter().use { out ->
            lines.forEach {
                out.println(it)
            }
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