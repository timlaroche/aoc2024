class Day14(isTest: Boolean) : Day(isTest) {

    data class Robot(val pos: Pair<Int, Int>, val vel: Pair<Int, Int>)

    fun part1(): Long {
        val rows = 103
        val columns = 101

//        val rows = 7
//        val columns = 11

        val input = Helper.readAsLines(inputFile)

        // One day I'll regex it
        val robots = input.map {
            val lr = it.split(" ")
            val pos = lr[0].split("=")[1].split(",")
            val vel = lr[1].split("=")[1].split(",")
            Robot(Pair(pos[0].toInt(), pos[1].toInt()), Pair(vel[0].toInt(), vel[1].toInt()))
        }.toMutableList()

        for (i in 0 until 100) {
            robots.forEachIndexed { index, robot ->
                val newX = ((robot.pos.first + robot.vel.first) + columns) % columns
                val newY = ((robot.pos.second + robot.vel.second) + rows) % rows
                robots[index] = Robot(Pair(newX, newY), Pair(robots[index].vel.first, robots[index].vel.second))
            }
        }

        var tl = 0
        var tr = 0
        var bl = 0
        var br = 0
        val middleRow = (rows - 1) / 2
        val middleColumn = (columns - 1) / 2
        robots.forEach {
            if (it.pos.first == middleColumn || it.pos.second == middleRow) {
            } else if (it.pos.first < middleColumn && it.pos.second < middleRow) {
                tl += 1
            } else if (it.pos.first > middleColumn && it.pos.second < middleRow) {
                tr += 1
            } else if (it.pos.first < middleColumn && it.pos.second > middleRow) {
                bl += 1
            } else if (it.pos.first > middleColumn && it.pos.second > middleRow) {
                br += 1
            }
        }

        return (tl * tr * bl * br).toLong() // 203252196 too low, 233816660 too high, 221655456 correct!
    }

    fun part2(): Long {
        val rows = 103
        val columns = 101

        val input = Helper.readAsLines(inputFile)

        // One day I'll regex it
        val robots = input.map {
            val lr = it.split(" ")
            val pos = lr[0].split("=")[1].split(",")
            val vel = lr[1].split("=")[1].split(",")
            Robot(Pair(pos[0].toInt(), pos[1].toInt()), Pair(vel[0].toInt(), vel[1].toInt()))
        }.toMutableList()

        val securityFactors = mutableListOf<Pair<Long, Int>>() // I got given this clue!

        for (i in 0 until 10402) {
            robots.forEachIndexed { index, robot ->
                val newX = ((robot.pos.first + robot.vel.first) + columns) % columns
                val newY = ((robot.pos.second + robot.vel.second) + rows) % rows
                robots[index] = Robot(Pair(newX, newY), Pair(robots[index].vel.first, robots[index].vel.second))
            }
            var tl = 0
            var tr = 0
            var bl = 0
            var br = 0
            val middleRow = (rows - 1) / 2
            val middleColumn = (columns - 1) / 2
            robots.forEach {
                if (it.pos.first == middleColumn || it.pos.second == middleRow) { }
                else if (it.pos.first < middleColumn && it.pos.second < middleRow) {
                    tl += 1
                } else if (it.pos.first > middleColumn && it.pos.second < middleRow) {
                    tr += 1
                } else if (it.pos.first < middleColumn && it.pos.second > middleRow) {
                    bl += 1
                } else if (it.pos.first > middleColumn && it.pos.second > middleRow) {
                    br += 1
                }
            }
            securityFactors.add(Pair((tl * tr * bl * br).toLong(), i))
        }

        // 1928 too low // 7857 too low // 8166 too high
        return securityFactors.indexOfFirst { it == securityFactors.minBy { it.first } }.toLong() + 1 // plus one since we started from zero
    }

}