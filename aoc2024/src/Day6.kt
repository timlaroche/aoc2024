import kotlin.time.measureTime

class Day6(isTest: Boolean) : Day(isTest) {
    // 5176 too low, try plus one.... 5177 was correct - the first position counts! hehehehe have updated the code to add the first position to the set
    fun part1(): Long {
        val lines = Helper.readAsLines(inputFile)
        val rows = lines.size
        val columns = lines.first().toCharArray().size
        val grid: Array<Array<Char>> = Array(rows) { Array(columns) { '!' } }
        var total = 0L

        // Setup Grid
        lines.forEachIndexed { i, line ->
            line.toCharArray().forEachIndexed { j, c ->
                grid[i][j] = c
            }
        }

        // Search for starting point
        var starting = Pair(-1, -1)
        lines.forEachIndexed { i, line ->
            line.toCharArray().forEachIndexed { j, c ->
                if(grid[i][j].equals('^')) {
                    starting = Pair(i, j)
                }
            }
        }
        val x = traverse(starting, grid, rows, columns)
        return x.toLong()
    }

    fun traverse(starting: Pair<Int, Int>, grid: Array<Array<Char>>, rows: Int, columns: Int): Int{
        var walkTo = Pair(starting.first-1, starting.second)
        var orientation = 0
        var locations = mutableSetOf<Pair<Int, Int>>()
        locations.add(starting)

        while (walkTo.first < rows && walkTo.second < columns && walkTo.first >= 0 && walkTo.second >= 0) {
            // UP
            if (orientation == 0) {
                try {
                    while(!grid[walkTo.first][walkTo.second].equals('#')) {
                        locations.add(walkTo)
                        walkTo = Pair(walkTo.first-1, walkTo.second)
                    }
                } catch (Exception: ArrayIndexOutOfBoundsException) {
                    return locations.size;
                }
                orientation = (orientation + 1) % 4
                walkTo = Pair(walkTo.first+1, walkTo.second+1)
            }
            // RIGHT
            if (orientation == 1) {
                try {
                    while(!grid[walkTo.first][walkTo.second].equals('#')) {
                        locations.add(walkTo)
                        walkTo = Pair(walkTo.first, walkTo.second+1)
                    }
                } catch (Exception: ArrayIndexOutOfBoundsException) {
                    return locations.size;
                }
                orientation = (orientation + 1) % 4
                walkTo = Pair(walkTo.first+1, walkTo.second-1)
            }
            // DOWN; MOVE LEFT
            if (orientation == 2) {
                try {
                    while(!grid[walkTo.first][walkTo.second].equals('#')) {
                        locations.add(walkTo)
                        walkTo = Pair(walkTo.first+1, walkTo.second)
                    }
                } catch (Exception: ArrayIndexOutOfBoundsException) {
                    return locations.size;
                }
                orientation = (orientation + 1) % 4
                walkTo = Pair(walkTo.first-1, walkTo.second-1)
            }
            // LEFT; MOVE UP
            if (orientation == 3) {
                try {
                    while(!grid[walkTo.first][walkTo.second].equals('#')) {
                        locations.add(walkTo)
                        walkTo = Pair(walkTo.first, walkTo.second-1)
                    }
                } catch (Exception: ArrayIndexOutOfBoundsException) {
                    return locations.size;
                }
                orientation = (orientation + 1) % 4
                walkTo = Pair(walkTo.first-1, walkTo.second+1)
            }
        }
        return 0
    }

    fun traverseTrackingLoop(starting: Pair<Int, Int>, grid: Array<Array<Char>>, rows: Int, columns: Int): Boolean{
        var walkTo = Pair(starting.first-1, starting.second)
        var orientation = 0
        var locations = mutableSetOf<Pair<Int, Int>>()
        var currentLoop = mutableSetOf<Pair<Int, Int>>()
        var previousSeenLoops = mutableListOf<Set<Pair<Int, Int>>>()
        var previousLoop = mutableSetOf<Pair<Int, Int>>()
        locations.add(starting)
        currentLoop.add(starting)

        while (walkTo.first < rows && walkTo.second < columns && walkTo.first >= 0 && walkTo.second >= 0) {
            // UP
            if (orientation == 0) {
                try {
                    while(!grid[walkTo.first][walkTo.second].equals('#')) {
                        locations.add(walkTo)
                        currentLoop.add(walkTo)
                        walkTo = Pair(walkTo.first-1, walkTo.second)
                    }
                } catch (Exception: ArrayIndexOutOfBoundsException) {
                    return false
                }
                orientation = (orientation + 1) % 4
                walkTo = Pair(walkTo.first+1, walkTo.second+1)
            }
            // RIGHT
            if (orientation == 1) {
                try {
                    while(!grid[walkTo.first][walkTo.second].equals('#')) {
                        locations.add(walkTo)
                        currentLoop.add(walkTo)
                        walkTo = Pair(walkTo.first, walkTo.second+1)
                    }
                } catch (Exception: ArrayIndexOutOfBoundsException) {
                    return false
                }
                orientation = (orientation + 1) % 4
                walkTo = Pair(walkTo.first+1, walkTo.second-1)
            }
            // DOWN; MOVE LEFT
            if (orientation == 2) {
                try {
                    while(!grid[walkTo.first][walkTo.second].equals('#')) {
                        locations.add(walkTo)
                        currentLoop.add(walkTo)
                        walkTo = Pair(walkTo.first+1, walkTo.second)
                    }
                } catch (Exception: ArrayIndexOutOfBoundsException) {
                    return false
                }
                orientation = (orientation + 1) % 4
                walkTo = Pair(walkTo.first-1, walkTo.second-1)
            }
            // LEFT; MOVE UP
            if (orientation == 3) {
                try {
                    while(!grid[walkTo.first][walkTo.second].equals('#')) {
                        locations.add(walkTo)
                        currentLoop.add(walkTo)
                        walkTo = Pair(walkTo.first, walkTo.second-1)
                    }
                } catch (Exception: ArrayIndexOutOfBoundsException) {
                    return false
                }
                orientation = (orientation + 1) % 4
                walkTo = Pair(walkTo.first-1, walkTo.second+1)
                if (currentLoop.equals(previousLoop) && previousLoop.isNotEmpty() || previousSeenLoops.contains(currentLoop)) {
                    return true
                }
                previousLoop = currentLoop.toMutableSet()
                previousSeenLoops.add(previousLoop)
                currentLoop = mutableSetOf()
            }
        }
        return false
    }


    fun part2(): Long {
        val lines = Helper.readAsLines(inputFile)
        val rows = lines.size
        val columns = lines.first().toCharArray().size
        val grid: Array<Array<Char>> = Array(rows) { Array(columns) { '!' } }
        var total = 0L

        // Setup Grid
        lines.forEachIndexed { i, line ->
            line.toCharArray().forEachIndexed { j, c ->
                grid[i][j] = c
            }
        }

        // Search for starting point
        // Test starting point = (6,4)
        var starting = Pair(-1, -1)
        lines.forEachIndexed { i, line ->
            line.toCharArray().forEachIndexed { j, c ->
                if(grid[i][j].equals('^')) {
                    starting = Pair(i, j)
                }
            }
        }

//        grid[6][3] = '#'
//        traverseTrackingLoop(starting, grid, rows, columns)
//        println()

        var count = 0
        val time = measureTime {
            for (i in 0 until rows) {
                for (j in 0 until columns) {
                    // Reset grid
                    lines.forEachIndexed { i, line ->
                        line.toCharArray().forEachIndexed { j, c ->
                            grid[i][j] = c
                        }
                    }
                    if (!grid[i][j].equals('^')) grid[i][j] = '#'
                    val loop = traverseTrackingLoop(starting, grid, rows, columns)
                    if(loop) count = count + 1
                }
            }
        }
        println("Took ${time.inWholeMilliseconds}")
        return count.toLong()
    }
}