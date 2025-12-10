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
    fun areapt2(p1: Point2, p2: Point2): Long {
        val dx = Math.abs(p2.row - p1.row).toLong() + 1
        val dy = Math.abs(p2.column - p1.column).toLong() + 1
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
    data class Rectangle(val topLeft: Point2, val bottomRight: Point2, val area: Long)

    fun part2_map() {
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
        val distinctAreas = areas.distinctBy { it.area }

        val boundsRow = points.maxOf { it.column } + 3
        val boundsColumn = points.maxOf { it.row } + 3

        // Initialize grid
        val grid = mutableMapOf<Point2, Boolean>()

        // Parse the points into the grid
        points.forEach { p -> grid[Point2(p.column, p.row)] = true }

        // Scan horizontally
        val fillHorizontal = grid.keys.mapIndexed { i, p ->
            val sameRow = grid.keys.filter { it.row == p.row }.sortedBy { it.column }
            val pairs = sameRow.zipWithNext()
            val res = mutableListOf<Point2>()
            pairs.forEach { pair ->
                for (k in pair.first.column..pair.second.column) {
                    res.add(Point2(p.row, k))
                }
            }
            res
        }
        // Scan vertically
        val fillVertical = grid.keys.mapIndexed { i, p ->
            val sameColumn = grid.keys.filter { it.column == p.column }.sortedBy { it.row }
            val pairs = sameColumn.zipWithNext()
            val res = mutableListOf<Point2>()
            pairs.forEach { pair ->
                for (k in pair.first.row..pair.second.row) {
                    res.add(Point2(k, p.column))
                }
            }
            res
        }

        // This is the perimeter of the filled area
        fillHorizontal.flatten().forEach { p -> grid[p] = true }
        fillVertical.flatten().forEach { p -> grid[p] = true }

        // Create area masks for each pair of points
        var res = 0
        println("tocheck: ${points.size * points.size}")
        points.forEachIndexed { i, p1 ->
            points.forEachIndexed { j, p2 ->
                val start = System.nanoTime()
                println("Checking points $i x $j")
                System.gc()


                val perimMask = mutableMapOf<Point2, Boolean>()
                val startingX = Math.min(p1.row, p2.row)
                val startingY = Math.min(p1.column, p2.column)

                val dx = Math.abs(p2.row - p1.row)
                val dy = Math.abs(p2.column - p1.column)

                // If dx takes you outside of the perimeter, skip this
                val endPoint1 = Point2(startingY + dy, startingX)
                val endPoint2 = Point2(startingY, startingX + dx)
                if (!checkPointHitsEdgeInEveryDirection(grid, endPoint1)) return@forEachIndexed
                if (!checkPointHitsEdgeInEveryDirection(grid, endPoint2)) return@forEachIndexed
                val c1 = System.nanoTime()
                println("Checked edge points in ${(c1 - start) / 1_000_000}ms")
                // Make rectangle
                // top
                val perimMaskList = mutableListOf<Point2>()
                for (i in startingX..startingX + dx) {
                    perimMaskList.add(Point2(startingY, i))
                }
                // bottom
                for (i in startingX..startingX + dx) {
                    perimMaskList.add(Point2(startingY + dy, i))
                }
                // left
                for (i in startingY..startingY + dy) {
                    perimMaskList.add(Point2(i, startingX))
                }
                // right
                for (i in startingY..startingY + dy) {
                    perimMaskList.add(Point2(i, startingX + dx))
                }
                val c2 = System.nanoTime()
                println("Created perimeter in ${(c2 - c1) / 1_000_000}ms")
                perimMaskList.forEach { p -> perimMask[p] = true }
                val c3 = System.nanoTime()
                println("Created perimeter map in ${(c3 - c2) / 1_000_000}ms")

                val pointsToCheck = perimMask.keys.filter { p -> !grid.contains(p) }.distinctBy { it.row }.distinctBy { it.column }
                val invalid = pointsToCheck.any { p -> checkPointHitsEdgeInEveryDirection(grid, p) == false }
                val c4 = System.nanoTime()
                println("Checked validity in ${(c4 - c3) / 1_000_000}ms")

                if(!invalid) {
                    val area = (dx + 1) * (dy + 1)
                    if (area > res) {
                        res = area
                    }
                }
                val end = System.nanoTime()
                println("Checked points $i x $j in ${(end - start) / 1_000_000}ms")
            }
        }
        println("FINALRES: $res")
    }

    fun checkPointHitsEdgeInEveryDirection(grid: Map<Point2, Boolean>, p: Point2): Boolean {
        val sameColumn = grid.keys.filter { it.column == p.column }.sortedBy { it.row }
        val minColumn = sameColumn.minBy { it.row }
        val maxColumn = sameColumn.maxBy { it.row }
        val inBoundsColumn = minColumn.row <= p.row && p.row <= maxColumn.row
        if (!inBoundsColumn) return false

        val sameRow = grid.keys.filter { it.row == p.row }.sortedBy { it.column }
        val minRow = sameRow.minBy { it.column }
        val maxRow = sameRow.maxBy { it.column }
        val inBoundsRow = minRow.column <= p.column && p.column <= maxRow.column
        if (!inBoundsRow) return false
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