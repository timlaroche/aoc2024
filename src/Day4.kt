class Day4(isTest: Boolean) : Day(isTest) {

    fun part1(): Long {
        val lines = Helper.readAsLines(inputFile)
        val rows = lines.size
        val columns = lines.first().toCharArray().size
        val grid = Array(rows) { Array(columns) { '!' } }
        var total = 0L

        // Setup Grid
        lines.forEachIndexed { i, line ->
            line.toCharArray().forEachIndexed { j, c ->
                grid[i][j] = c
            }
        }

        // Traverse Grid
        for (i in 0 until rows) {
            for (j in 0 until columns) {
                val char = grid[i][j]
                if (char.equals('X')) {
                    total += checkXmas(i, j, grid)
                }
            }
        }
        return total
    }

    fun checkXmas(i: Int, j: Int, grid: Array<Array<Char>>): Long {
        // I apologise to the elves in advance for hard coding the i-1, i-2, i-3
        var total = 0L
        try {
            // Up
            if (grid[i - 1][j].equals('M') && grid[i - 2][j].equals('A') && grid[i - 3][j].equals('S')) total += 1
            // Down
            if (grid[i + 1][j].equals('M') && grid[i + 2][j].equals('A') && grid[i + 3][j].equals('S')) total += 1
            // Left
            if (grid[i][j - 1].equals('M') && grid[i][j - 2].equals('A') && grid[i][j - 3].equals('S')) total += 1
            // Right
            if (grid[i][j + 1].equals('M') && grid[i][j + 2].equals('A') && grid[i][j + 3].equals('S')) total += 1
            // Diagonal up left
            if (grid[i - 1][j - 1].equals('M') && grid[i - 2][j - 2].equals('A') && grid[i - 3][j - 3].equals('S')) total += 1
            // Diagonal up right
            if (grid[i - 1][j + 1].equals('M') && grid[i - 2][j + 2].equals('A') && grid[i - 3][j + 3].equals('S')) total += 1
            // Diagonal down left
            if (grid[i + 1][j - 1].equals('M') && grid[i + 2][j - 2].equals('A') && grid[i + 3][j - 3].equals('S')) total += 1
            // Diagonal down right
            if (grid[i + 1][j + 1].equals('M') && grid[i + 2][j + 2].equals('A') && grid[i + 3][j + 3].equals('S')) total += 1
        } catch (Exception: ArrayIndexOutOfBoundsException) {
            // I'm sorry
        }
        return total
    }

    fun checkMas(i: Int, j: Int, grid: Array<Array<Char>>): Long {
        var total = 0L
        try {
            if (grid[i - 1][j - 1].equals('M') && grid[i + 1][j - 1].equals('M') && grid[i - 1][j + 1].equals('S') && grid[i + 1][j + 1].equals('S')) total += 1
            if (grid[i - 1][j - 1].equals('S') && grid[i + 1][j - 1].equals('S') && grid[i - 1][j + 1].equals('M') && grid[i + 1][j + 1].equals('M')) total += 1
            if (grid[i - 1][j - 1].equals('M') && grid[i + 1][j - 1].equals('S') && grid[i - 1][j + 1].equals('M') && grid[i + 1][j + 1].equals('S')) total += 1
            if (grid[i - 1][j - 1].equals('S') && grid[i + 1][j - 1].equals('M') && grid[i - 1][j + 1].equals('S') && grid[i + 1][j + 1].equals('M')) total += 1
        } catch (Exception: ArrayIndexOutOfBoundsException) {
            // Sorry elves
        }
        return total
    }

    fun part2(): Long {
        val lines = Helper.readAsLines(inputFile)
        val rows = lines.size
        val columns = lines.first().toCharArray().size
        val grid = Array(rows) { Array(columns) { '!' } }
        var total = 0L

        // Setup Grid
        lines.forEachIndexed { i, line ->
            line.toCharArray().forEachIndexed { j, c ->
                grid[i][j] = c
            }
        }

        // Traverse Grid
        for (i in 0 until rows) {
            for (j in 0 until columns) {
                val char = grid[i][j]
                if (char.equals('A')) {
                    total += checkMas(i, j, grid)
                }
            }
        }
        return total
    }
}