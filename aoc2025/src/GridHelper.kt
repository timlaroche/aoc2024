import java.util.function.Predicate

class GridHelper {
    // Takes a Grid: <List<List<Char>> and returns a map of all the points and how many adjacent square match the predicate
    fun checkImmediateAdjacency(grid: List<List<Char>>, predicate: Predicate<Char>): Map<Pair<Int, Int>, Int> {
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

    // TODO: Untested, but copied and altered from last year
    fun checkNAwayAdjacency(grid: List<List<Char>>, predicate: Predicate<Char>, length: Int): Map<Pair<Int, Int>, Int> {
        var countMap = mutableMapOf<Pair<Int, Int>, Int>()
        var columnSize = grid[0].size - 1
        var rowSize = grid.size - 1

        grid.forEachIndexed { i, row ->
            row.forEachIndexed { j, columnValue ->
                var count = 0
                for (add in 0..length) {
                    // N
                    if (i - add == 0) {
                        // skip
                    } else {
                        if (predicate.test(grid[i-1][j])) count += 1
                    }
                    // NE
                    if (i - add == 0 || j + add == columnSize) {
                        // skip
                    } else {
                        if (predicate.test(grid[i-1][j+1])) count += 1
                    }
                    // E
                    if (j + add == columnSize) {
                        // skip
                    } else {
                        if (predicate.test(grid[i][j+1])) count += 1
                    }
                    // SE
                    if (i + add == rowSize || j + add == columnSize) {
                        // skip
                    } else {
                        if (predicate.test(grid[i+1][j+1])) count += 1
                    }
                    // S
                    if (i + add == rowSize) {
                        // skip
                    } else {
                        if (predicate.test(grid[i+1][j])) count += 1
                    }
                    // SW
                    if (i + add  == rowSize || j - add == 0) {
                        // skip
                    } else {
                        if (predicate.test(grid[i+1][j-1])) count += 1
                    }
                    // W
                    if (j - add == 0) {
                        // skip
                    } else {
                        if (predicate.test(grid[i][j-1])) count += 1
                    }
                    // NW
                    if (j - add == 0 || i - add == 0) {
                        // skip
                    } else {
                        if (predicate.test(grid[i-1][j-1])) count += 1
                    }
                }
                countMap.put(Pair(i, j), count)
            }
        }
        return countMap
    }
}