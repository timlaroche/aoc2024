class Day8(isTest: Boolean) : Day(isTest) {
    // 321 too high
    fun part1(): Long {
        val lines = Helper.readAsLines(inputFile)
        val rows = lines.size
        val columns = lines.first().toCharArray().size
        val grid = Array(rows) { Array(columns) { '!' } }
        var total = 0L

        // Setup Grid
        val frequencies = mutableMapOf<Char, Int>()
        lines.forEachIndexed { i, line ->
            line.toCharArray().forEachIndexed { j, c ->
                if (!c.equals('.')) frequencies.compute(c, { _, v -> v?.plus(1) ?: 1 })
                grid[i][j] = c
            }
        }

        val antinodes = mutableSetOf<Pair<Int, Int>>()
        frequencies.filterValues { x -> x > 1 }.forEach { freq ->
            val antennas = scanGrid(grid, rows, columns, freq.key)

            for ( i in 0 until antennas.size) {
                for (j in 0 until antennas.size) {
                    val antenna1 = antennas[i]
                    val antenna2 = antennas[j]
                    val rowDistance = antenna2.first - antenna1.first
                    val columnDistance = antenna2.second - antenna1.second
                    if (rowDistance > 0 || columnDistance > 0) {
                        antinodes.add(Pair(antenna1.first - rowDistance, antenna1.second - columnDistance))
                        antinodes.add(Pair(antenna2.first + rowDistance, antenna2.second + columnDistance))
                    }
                }
            }
            antinodes.removeIf{ it.first < 0 || it.first >= rows || it.second < 0 || it.second >= columns }
        }
        return antinodes.size.toLong()
    }

    fun scanGrid(grid: Array<Array<Char>>, rows: Int, columns: Int, frequency: Char): List<Pair<Int, Int>> {
        val antennas = mutableListOf<Pair<Int, Int>>()
        for (i in 0 until rows) {
            for (j in 0 until columns) {
                if(grid[i][j].equals(frequency)) antennas.add(Pair(i,j))
            }
        }
        return antennas
    }


    // 984 too low
    fun part2(): Long {
        val lines = Helper.readAsLines(inputFile)
        val rows = lines.size
        val columns = lines.first().toCharArray().size
        val grid = Array(rows) { Array(columns) { '!' } }
        var total = 0L

        // Setup Grid
        val frequencies = mutableMapOf<Char, Int>()
        lines.forEachIndexed { i, line ->
            line.toCharArray().forEachIndexed { j, c ->
                if (!c.equals('.')) frequencies.compute(c, { _, v -> v?.plus(1) ?: 1 })
                grid[i][j] = c
            }
        }

        val antinodes = mutableSetOf<Pair<Int, Int>>()
        frequencies.filterValues { x -> x > 1 }.forEach { freq ->
            val antennas = scanGrid(grid, rows, columns, freq.key)

            for ( i in 0 until antennas.size) {
                for (j in 0 until antennas.size) {
                    val antenna1 = antennas[i]
                    val antenna2 = antennas[j]
                    val rowDistance = antenna2.first - antenna1.first
                    val columnDistance = antenna2.second - antenna1.second
                    if (rowDistance > 0 || columnDistance > 0) {
                        // Major hack?
                        for ( factor in -100..100) {
                            antinodes.add(Pair(antenna1.first - (factor * rowDistance), antenna1.second - (factor * columnDistance)))
                            antinodes.add(Pair(antenna2.first + (factor * rowDistance), antenna2.second + (factor * columnDistance)))
                        }
                    }
                }
            }
            antinodes.removeIf{ it.first < 0 || it.first >= rows || it.second < 0 || it.second >= columns }
        }
        return antinodes.size.toLong()
    }
}