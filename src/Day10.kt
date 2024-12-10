import java.util.*

class Day10(isTest: Boolean) : Day(isTest) {
    fun part1(): Long {
        val lines = Helper.readAsLines(inputFile)
        val rows = lines.size
        val columns = lines.first().toCharArray().size
        val grid: Array<Array<Char>> = Array(rows) { Array(columns) { '!' } }

        // Setup Grid
        lines.forEachIndexed { i, line ->
            line.toCharArray().forEachIndexed { j, c ->
                grid[i][j] = c
            }
        }

        // Find all start points
        val startPoints = mutableListOf<Pair<Int, Int>>()
        for (i in 0 until rows) {
            for ( j in 0 until columns) {
                if (grid[i][j] == '0') startPoints.add(Pair(i, j))
            }
        }

        var count = 0
        startPoints.forEach {
            val toVisit: Queue<Pair<Int,Int>> = LinkedList()
            val visited = mutableListOf<Pair<Int, Int>>()

            toVisit.add(it)
            visited.add(it)

            while (toVisit.isNotEmpty()) {
                val next = toVisit.remove()
                val currentLevel = grid[next.first][next.second].toString().toInt()
                if (currentLevel == 9) {
                    count += 1
                }
                try {
                    val nextLevel = grid[next.first][next.second + 1]
                    if (nextLevel.toString().toInt() == currentLevel + 1 && !visited.contains(Pair(next.first, next.second + 1))) {
                        val nextPair = Pair(next.first, next.second + 1)
                        visited.add(nextPair)
                        toVisit.add(nextPair) // Right
                    }
                } catch (e: Exception) {}
                try {
                    val nextLevel = grid[next.first][next.second - 1]
                    if (nextLevel.toString().toInt() == currentLevel + 1 && !visited.contains(Pair(next.first, next.second - 1))) {
                        val nextPair = Pair(next.first, next.second - 1)
                        visited.add(nextPair)
                        val add = toVisit.add(nextPair) // Left
                    }
                } catch (e: Exception) {}
                try {
                    val nextLevel = grid[next.first + 1][next.second]
                    if (nextLevel.toString().toInt() == currentLevel + 1 && !visited.contains(Pair(next.first + 1, next.second))) {
                        val nextPair = Pair(next.first + 1, next.second)
                        visited.add(nextPair)
                        toVisit.add(nextPair) // Down
                    }
                } catch (e: Exception) {}
                try {
                    val nextLevel = grid[next.first - 1][next.second]
                    if (nextLevel.toString().toInt() == currentLevel + 1 && !visited.contains(Pair(next.first - 1, next.second))) {
                        val nextPair = Pair(next.first - 1, next.second)
                        visited.add(nextPair)
                        toVisit.add(nextPair) // Up
                    }
                } catch (e: Exception) {}
            }
        }
        return count.toLong()
    }

    fun part2(): Long {
        val lines = Helper.readAsLines(inputFile)
        val rows = lines.size
        val columns = lines.first().toCharArray().size
        val grid: Array<Array<Char>> = Array(rows) { Array(columns) { '!' } }

        // Setup Grid
        lines.forEachIndexed { i, line ->
            line.toCharArray().forEachIndexed { j, c ->
                grid[i][j] = c
            }
        }

        // Find all start points
        val startPoints = mutableListOf<Pair<Int, Int>>()
        for (i in 0 until rows) {
            for ( j in 0 until columns) {
                if (grid[i][j] == '0') startPoints.add(Pair(i, j))
            }
        }

        var count = 0
        startPoints.forEach {
            val toVisit: Queue<Pair<Int,Int>> = LinkedList()
            val visited = mutableListOf<Pair<Int, Int>>()

            toVisit.add(it)
            visited.add(it)

            while (toVisit.isNotEmpty()) {
                val next = toVisit.remove()
                val currentLevel = grid[next.first][next.second].toString().toInt()
                if (currentLevel == 9) {
                    count += 1
                }
                try {
                    val nextLevel = grid[next.first][next.second + 1]
                    if (nextLevel.toString().toInt() == currentLevel + 1) {
                        val nextPair = Pair(next.first, next.second + 1)
                        visited.add(nextPair)
                        toVisit.add(nextPair) // Right
                    }
                } catch (e: Exception) {}
                try {
                    val nextLevel = grid[next.first][next.second - 1]
                    if (nextLevel.toString().toInt() == currentLevel + 1) {
                        val nextPair = Pair(next.first, next.second - 1)
                        visited.add(nextPair)
                        val add = toVisit.add(nextPair) // Left
                    }
                } catch (e: Exception) {}
                try {
                    val nextLevel = grid[next.first + 1][next.second]
                    if (nextLevel.toString().toInt() == currentLevel + 1) {
                        val nextPair = Pair(next.first + 1, next.second)
                        visited.add(nextPair)
                        toVisit.add(nextPair) // Down
                    }
                } catch (e: Exception) {}
                try {
                    val nextLevel = grid[next.first - 1][next.second]
                    if (nextLevel.toString().toInt() == currentLevel + 1) {
                        val nextPair = Pair(next.first - 1, next.second)
                        visited.add(nextPair)
                        toVisit.add(nextPair) // Up
                    }
                } catch (e: Exception) {}
            }
        }
        return count.toLong()
    }
}