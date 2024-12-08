class Day7(isTest: Boolean) : Day(isTest) {
    fun part1(): Long {
        val input = Helper.readAsLines(inputFile)
        var count = 0L
        input.forEach{
            val split = it.split(":")
            val target = split[0].toLong()
            val values = split[1].trimStart().trimEnd().split(" ").map{ it.toLong()}
            if (solve(values, target)) count += target
            println()
        }
        return count.toLong()
    }

    // 3267: 81 40 27
    // 3267: (81+40) 27 -> 3267: 121 27
    // 3267: (81*40) 27 -> 3267: 3240 27

    fun solve(list: List<Long>, target: Long): Boolean {
        val results = mutableListOf<Boolean>()
        if (list.size == 2) {
            if (target == (list[0] + list[1]) || target == (list[0] * list[1])) return true
        } else {
            results.add(solve(listOf(list[0] + list[1]) + list.drop(2), target))
            results.add(solve(listOf(list[0] * list[1]) + list.drop(2), target))
        }
        return results.any { it == true }
    }

    fun solve2(list: List<String>, target: Long): Boolean {
        val results = mutableListOf<Boolean>()
        if (list.size == 2) {
            if (
                target == (list[0].toLong() + list[1].toLong()) ||
                target == (list[0].toLong() * list[1].toLong()) ||
                target.toString() == ((list[0] + list[1]))
                ) return true
        } else {


            results.add(solve2(listOf((list[0].toLong() + list[1].toLong()).toString()) + list.drop(2), target))
            results.add(solve2(listOf((list[0].toLong() * list[1].toLong()).toString()) + list.drop(2), target))
            results.add(solve2(listOf((list[0] + list[1])) + list.drop(2), target))
        }
        return results.any { it == true }
    }

    fun part2(): Long {
        val input = Helper.readAsLines(inputFile)
        var count = 0L
        input.forEach{
            val split = it.split(":")
            val target = split[0].toLong()
            val values = split[1].trimStart().trimEnd().split(" ")
            if (solve2(values, target)) count += target
        }
        return count.toLong()
    }
}