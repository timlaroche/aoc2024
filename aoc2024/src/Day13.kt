import kotlin.math.min

class Day13(isTest: Boolean) : Day(isTest) {
    data class Puzzle(val prize: Pair<Int, Int>, val a: Pair<Int, Int>, val b: Pair<Int, Int>)

    fun part1(): Long {
        val line = Helper.readAsLines(inputFile)
        val puzzles = mutableListOf<Puzzle>()
        var a: Pair<Int, Int> = Pair(0, 0)
        var b: Pair<Int, Int> = Pair(0, 0)
        var prize: Pair<Int, Int> = Pair(0, 0)
        line.forEach {
            if (it.equals("")) {
                puzzles.add(Puzzle(prize, a, b))
                a = Pair(0,0)
                b = Pair(0,0)
                prize = Pair(0,0)
            } else if (it.startsWith("Prize")) {
                val split = it.split(",")
                val x = split[0].split("=")[1].toInt()
                val y = split[1].split("=")[1].toInt()
                prize = Pair(x, y)
            } else if (it.startsWith("Button A")) {
                val split = it.split(",")
                val x = split[0].split("+")[1].toInt()
                val y = split[1].split("+")[1].toInt()
                a = Pair(x, y)
            } else if (it.startsWith("Button B")) {
                val split = it.split(",")
                val x = split[0].split("+")[1].toInt()
                val y = split[1].split("+")[1].toInt()
                b = Pair(x, y)
            }
        }

        var sum = 0L

        // 31940 too high
        // 31597 too high
        // 31237 too high

        puzzles.forEach {
           //sum += solveDP(Pair(it.a, it.b), it.prize)
            val res = solveWithCramers(Pair(it.a.first.toLong(), it.b.first.toLong()), Pair(it.a.second.toLong(), it.b.second.toLong()), Pair(it.prize.first.toLong(), it.prize.second.toLong()))
            if (res.first >= 100  || res.second >= 100 || res.first < 0 || res.second < 0){ } else {
                sum += 3*res.first + 1*res.second
            }
        }
        return sum
    }

    fun solveWithCramers(xs: Pair<Long, Long>, ys: Pair<Long, Long>, target: Pair<Long, Long>): Pair<Long, Long> {
        val a1 = xs.first
        val b1 = xs.second
        val a2 = ys.first
        val b2 = ys.second
        val d1 = target.first
        val d2 = target.second

        val denom = a1 * b2 - b1 * a2
        val determX = d1 * b2 - b1 * d2
        val determY = a1 * d2 - a2 * d1

        val solX = determX / denom
        val solY = determY / denom
        if( determX % denom == 0L && determY % denom == 0L) {
            return Pair(solX, solY)
        } else {
            return Pair(0,0)
        }

    }

    // This didn't work, heap ran out
    fun solveDP(options: Pair<Pair<Int, Int>, Pair<Int, Int>>, target: Pair<Int, Int>): Int {
        var solutions = Array(target.first + 1) { Array(target.second + 1) { 99999 } }
        solutions[0][0] = 0

        for (i in 0..target.first) {
            for (j in 0..target.second) {
                val xTarget = i
                val yTarget = j
                var hasChanged = false

                // Push button A
                val aMaybeX = xTarget - options.first.first
                val aMaybeY = yTarget - options.first.second
                if ((aMaybeX < 0 || aMaybeY < 0)) {
                    solutions[i][j] = 99999
                } else {
                    val remainder = solutions[aMaybeX][aMaybeY]
                    if (remainder == 0 && aMaybeX == 0 && aMaybeY == 0) {
                        solutions[i][j] = min(3 + remainder, solutions[i][j])
                        hasChanged = true
                    }
                    if (remainder > 0) {
                        solutions[i][j] = min(3 + remainder, solutions[i][j])
                        hasChanged = true
                    }
                }

                // Push button B
                val bMaybeX = xTarget - options.second.first
                val bMaybeY = yTarget - options.second.second
                if ((bMaybeX < 0 || bMaybeY < 0) && !hasChanged) {
                    solutions[i][j] = 99999
                } else if (bMaybeX >= 0 && bMaybeY >= 0) {
                    val remainder = solutions[bMaybeX][bMaybeY]
                    if (remainder == 0 && bMaybeX == 0 && bMaybeY == 0) {
                        solutions[i][j] = min(1 + remainder, solutions[i][j])
                        hasChanged = true
                    }
                    if (remainder > 0) {
                        solutions[i][j] = min(1 + remainder, solutions[i][j])
                    }
                }

                // If no change, then not possible
                if (solutions[i][j] == 99999) solutions[i][j] = 0
            }
        }
        val solution = solutions[target.first][target.second]
        return solution
    }

    // Still heap ran out, it's not the data structure...
    fun solveMap(options: Pair<Pair<Int, Int>, Pair<Int, Int>>, target: Pair<Int, Int>): Int {
        var solutions = mutableMapOf<Pair<Int, Int>, Int>()
        solutions.put(Pair(0,0), 0)

        for (i in 0..target.first) {
            for (j in 0..target.second) {

                val xTarget = i
                val yTarget = j
                var hasChanged = false

                // Push button A
                val aMaybeX = xTarget - options.first.first
                val aMaybeY = yTarget - options.first.second
                if ((aMaybeX < 0 || aMaybeY < 0)) {
                } else {
                    val remainder = solutions.get(Pair(aMaybeX, aMaybeY))
                    if (remainder == 0 && aMaybeX == 0 && aMaybeY == 0) {
                        solutions.put(Pair(i, j), min(3 + remainder, solutions.get(Pair(i,j)) ?: 999))
                        hasChanged = true
                    }
                    if (remainder != null) {
                        if (remainder > 0) {
                            solutions.put(Pair(i, j), min(3 + remainder, solutions.get(Pair(i,j)) ?: 999))
                            hasChanged = true
                        }
                    }
                }

                // Push button B
                val bMaybeX = xTarget - options.second.first
                val bMaybeY = yTarget - options.second.second
                if ((bMaybeX < 0 || bMaybeY < 0) && !hasChanged) {
                } else if (bMaybeX >= 0 && bMaybeY >= 0) {
                    val remainder = solutions.get(Pair(bMaybeX, bMaybeY))
                    if (remainder == 0 && bMaybeX == 0 && bMaybeY == 0) {
                        solutions.put(Pair(i, j), min(1 + remainder, solutions.get(Pair(i,j)) ?: 999))
                        hasChanged = true
                    }
                    if (remainder != null) {
                        if (remainder > 0) {
                            solutions.put(Pair(i, j), min(1 + remainder, solutions.get(Pair(i,j)) ?: 999))
                        }
                    }
                }

                // If no change, then not possible
                if (solutions.get(Pair(i,j)) == null) solutions.put(Pair(i, j), 0)
            }
        }
        val solution = solutions.get(Pair(target.first, target.second))
        return 0
    }

    fun solveRecursive(options: Pair<Pair<Int, Int>, Pair<Int, Int>>, target: Pair<Int, Int>, coinCount: Int, count: Int): Int {
        if (count == 101 || target.first < 0 || target.second < 0) {
            return -99999
        }
        if (options.first.equals(target)) return coinCount + 3
        else if (options.second.equals(target)) return coinCount + 1
        else {
            // A
            val x = solveRecursive(options, Pair(target.first - options.first.first, target.second - options.first.second), coinCount+3, count+1)
            // B
            val y = solveRecursive(options, Pair(target.first - options.second.first, target.second - options.second.second), coinCount+1, count+1)
            return min(x,y)
        }
    }

    fun part2(): Long {
        val line = Helper.readAsLines(inputFile)
        val puzzles = mutableListOf<Puzzle>()
        var a: Pair<Int, Int> = Pair(0, 0)
        var b: Pair<Int, Int> = Pair(0, 0)
        var prize: Pair<Int, Int> = Pair(0, 0)
        line.forEach {
            if (it.equals("")) {
                puzzles.add(Puzzle(prize, a, b))
                a = Pair(0,0)
                b = Pair(0,0)
                prize = Pair(0,0)
            } else if (it.startsWith("Prize")) {
                val split = it.split(",")
                val x = split[0].split("=")[1].toInt()
                val y = split[1].split("=")[1].toInt()
                prize = Pair(x, y)
            } else if (it.startsWith("Button A")) {
                val split = it.split(",")
                val x = split[0].split("+")[1].toInt()
                val y = split[1].split("+")[1].toInt()
                a = Pair(x, y)
            } else if (it.startsWith("Button B")) {
                val split = it.split(",")
                val x = split[0].split("+")[1].toInt()
                val y = split[1].split("+")[1].toInt()
                b = Pair(x, y)
            }
        }

        var sum = 0L

        // 31940 too high
        // 31597 too high
        // 31237 too high

        puzzles.forEach {
            //sum += solveDP(Pair(it.a, it.b), it.prize)
            val res = solveWithCramers(Pair(it.a.first.toLong(), it.b.first.toLong()), Pair(it.a.second.toLong(), it.b.second.toLong()), Pair(it.prize.first+10000000000000, it.prize.second+10000000000000))
            sum += 3*res.first + 1*res.second
        }
        return sum
    }

}