import java.util.*

class Day12(isTest: Boolean) : Day(isTest) {
    // 4133282 too high
    fun part1Fail(): Long {
        val lines = Helper.readAsLines(inputFile)
        val rows = lines.size
        val columns = lines.first().toCharArray().size
        val grid = Array(rows) { Array(columns) { '!' } }

        // Setup Grid
        lines.forEachIndexed { i, line ->
            line.toCharArray().forEachIndexed { j, c ->
                grid[i][j] = c
            }
        }

        val area = mutableMapOf<Char, Int>()
        val perim = mutableMapOf<Char, Int>()
        var shouldRemove = mutableSetOf<Char>()
        var total = 0L

        for (i in 0 until rows) {
            val row = grid[i]
            area.forEach { t, _ ->
                if (!row.contains(t)) {
                    // We can start new areas and perims
                    total += area[t]!! * perim[t]!!
                    shouldRemove.add(t)
                }
            }
            shouldRemove.forEach { t ->
                area.remove(t)
                perim.remove(t)
            }
            shouldRemove = mutableSetOf()

            for (j in 0 until columns) {
                val c = grid[i][j]
                var cPerim = 4

                // Check if it's a single square
                try {
                    if (!grid[i - 1][j].equals(c) && !grid[i + 1][j].equals(c) && !grid[i][j - 1].equals(c) && !grid[i][j + 1].equals(c)) {
                        total += 4
                        continue
                    }
                } catch (e: Exception) {
                }

                area.compute(c, { _, v -> v?.plus(1) ?: 1 })
                // Check up, down, left, right
                try {
                    val up = grid[i - 1][j]
                    if (up.equals(c)) cPerim -= 1
                } catch (e: Exception) {
                }
                try {
                    val down = grid[i + 1][j]
                    if (down.equals(c)) cPerim -= 1
                } catch (e: Exception) {
                }
                try {
                    val left = grid[i][j - 1]
                    if (left.equals(c)) cPerim -= 1
                } catch (e: Exception) {
                }
                try {
                    val right = grid[i][j + 1]
                    if (right.equals(c)) cPerim -= 1
                } catch (e: Exception) {
                }
                perim.compute(c, { _, v -> v?.plus(cPerim) ?: cPerim })
            }
        }

        area.forEach { k, v ->
            total += v * perim[k]!! //ouch !!
        }

        return total
    }

    // BFS
    fun part1(): Long {
        val lines = Helper.readAsLines(inputFile)
        val rows = lines.size
        val columns = lines.first().toCharArray().size
        val grid = Array(rows) { Array(columns) { '!' } }

        // Setup Grid
        lines.forEachIndexed { i, line ->
            line.toCharArray().forEachIndexed { j, c ->
                grid[i][j] = c
            }
        }

        val visitedSquares = mutableSetOf<Pair<Int, Int>>()
        var sum = 0L

        for (i in 0 until rows) {
            for (j in 0 until columns) {
                val point = Pair(i, j)
                if (visitedSquares.contains(point)) {
                    continue
                } else {
                    val c = grid[i][j]
                    val x = bfsRegion(point, c, grid, rows, columns)
                    visitedSquares.addAll(x.third)
                    sum += x.third.size * x.second
                }
            }
        }
        return sum
    }

    fun bfsRegion(start: Pair<Int, Int>, c: Char, grid: Array<Array<Char>>, rows: Int, columns: Int): Triple<Int, Int, List<Pair<Int, Int>>> {
        var toVisit: Queue<Pair<Int, Int>> = LinkedList()
        val visited = mutableSetOf<Pair<Int, Int>>()
        var totalPerim = 0
        var totalArea = 0
        var totalCorners = 0

        var lC = true
        var rC = true
        var uC = true
        var dC = true

        toVisit.add(start)
        visited.add(start)

        while (toVisit.isNotEmpty()) {
            val next = toVisit.remove()
            visited.add(next)
            var cPerim = 4
            // Check up, down, left, right
            try {
                val up = grid[next.first - 1][next.second]
                if (up.equals(c)) {
                    cPerim -= 1
                    if (visited.contains(Pair(next.first - 1, next.second))) {
                    } else {
                        toVisit.add(Pair(next.first - 1, next.second))
                    }
                }
            } catch (e: Exception) {
            }
            try {
                val down = grid[next.first + 1][next.second]
                if (down.equals(c)) {
                    cPerim -= 1
                    dC = false
                    if (visited.contains(Pair(next.first + 1, next.second))) {
                    } else {
                        toVisit.add(Pair(next.first + 1, next.second))
                    }
                }
            } catch (e: Exception) {
            }
            try {
                val left = grid[next.first][next.second - 1]
                if (left.equals(c)) {
                    cPerim -= 1
                    if (visited.contains(Pair(next.first, next.second - 1))) {
                    } else {
                        toVisit.add(Pair(next.first, next.second - 1))
                    }
                }
            } catch (e: Exception) {
            }
            try {
                val right = grid[next.first][next.second + 1]
                if (right.equals(c)) {
                    cPerim -= 1
                    if (visited.contains(Pair(next.first, next.second + 1))) {
                    } else {
                        toVisit.add(Pair(next.first, next.second + 1))
                    }
                }
            } catch (e: Exception) {
            }
            totalPerim += cPerim
            totalArea += 1
            toVisit = toVisit.distinct().toCollection(LinkedList())

            // Check corners 270 degree
            // TR
            var z = Pair(next.first - 1, next.second) // should be the char
            var x = Pair(next.first, next.second + 1) // should be the char
            var v = Pair(next.first - 1, next.second + 1) // should not be the char
            try {
                if (grid[z.first][z.second].equals(c) && grid[x.first][x.second].equals(c) && !grid[v.first][v.second].equals(c)) {
                    totalCorners += 1
                }
            } catch (e: Exception) {
            }
            // TL
            z = Pair(next.first - 1, next.second) // should be the char
            x = Pair(next.first, next.second - 1) // should be the char
            v = Pair(next.first - 1, next.second - 1) // should not be the char
            try {
                if (grid[z.first][z.second].equals(c) && grid[x.first][x.second].equals(c) && !grid[v.first][v.second].equals(c)) {
                    totalCorners += 1
                }
            } catch (e: Exception) {
            }
            // BL
            z = Pair(next.first + 1, next.second) // should be the char
            x = Pair(next.first, next.second - 1) // should be the char
            v = Pair(next.first + 1, next.second - 1) // should not be the char
            try {
                if (grid[z.first][z.second].equals(c) && grid[x.first][x.second].equals(c) && !grid[v.first][v.second].equals(c)) {
                    totalCorners += 1
                }
            } catch (e: Exception) {
            }
            // BR
            z = Pair(next.first + 1, next.second) // should be the char
            x = Pair(next.first, next.second + 1) // should be the char
            v = Pair(next.first + 1, next.second + 1) // should not be the char
            try {
                if (grid[z.first][z.second].equals(c) && grid[x.first][x.second].equals(c) && !grid[v.first][v.second].equals(c)) {
                    totalCorners += 1
                }
            } catch (e: Exception) {
            }

            // Check corners 90 degree
            // TL
            val tlCorner = mutableListOf<Char>()
            try {
                tlCorner.add(grid[next.first][next.second-1])
            } catch (e: Exception) { }
            try {
                tlCorner.add(grid[next.first-1][next.second])
            } catch (e: Exception) { }
            if (!tlCorner.contains(c)) {
                totalCorners += 1
            }

            // TR
            val trCorner = mutableListOf<Char>()
            try {
                trCorner.add(grid[next.first][next.second+1])
            } catch (e: Exception) { }
            try {
                trCorner.add(grid[next.first-1][next.second])
            } catch (e: Exception) { }
            if (!trCorner.contains(c)) {
                totalCorners += 1
            }

            // BL
            val blCorner = mutableListOf<Char>()
            try {
                blCorner.add(grid[next.first][next.second-1])
            } catch (e: Exception) { }
            try {
                blCorner.add(grid[next.first+1][next.second])
            } catch (e: Exception) { }
            if (!blCorner.contains(c)) {
                totalCorners += 1
            }


            // BR
            val brCorner = mutableListOf<Char>()
            try {
                brCorner.add(grid[next.first][next.second+1])
            } catch (e: Exception) { }
            try {
                brCorner.add(grid[next.first+1][next.second])
            } catch (e: Exception) { }
            if (!brCorner.contains(c)) {
                totalCorners += 1
            }
        }

        // corners (since area is == points visited), perim, points visited
        return Triple(totalCorners, totalPerim, visited.toList())
    }

    fun part2(): Long {
        val lines = Helper.readAsLines(inputFile)
        val rows = lines.size
        val columns = lines.first().toCharArray().size
        val grid = Array(rows) { Array(columns) { '!' } }

        // Setup Grid
        lines.forEachIndexed { i, line ->
            line.toCharArray().forEachIndexed { j, c ->
                grid[i][j] = c
            }
        }

        val visitedSquares = mutableSetOf<Pair<Int, Int>>()
        var sum = 0L

        for (i in 0 until rows) {
            for (j in 0 until columns) {
                val point = Pair(i, j)
                if (visitedSquares.contains(point)) {
                    continue
                } else {
                    val c = grid[i][j]
                    val x = bfsRegion(point, c, grid, rows, columns)
                    visitedSquares.addAll(x.third)

                    sum += x.first * x.third.size
                }
            }
        }
        return sum
    }
}