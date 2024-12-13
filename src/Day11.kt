class Day11(isTest: Boolean) : Day(isTest) {
    fun part1(): Long {
        val input = Helper.readAsLines(inputFile).get(0).split(" ")
        var stones = ArrayDeque<String>()
        input.forEach {
            stones.add(it)
        }
        for (i in 0 until 25) {
            stones = blink(stones)
        }
        return stones.size.toLong()
    }

    fun part2(): Long {
        val input = Helper.readAsLines(inputFile).get(0).split(" ")
        val stones = mutableListOf<String>()
        input.forEach {
            stones.add(it)
        }
        var stoneMap = stones.groupingBy { it }.eachCount().toMap().map { (k, v) -> k to v.toLong() }.toMap() // So many lambdas/helper functions ;_;

        for (i in 0 until 75) {
            stoneMap = mapBlink(stoneMap)
        }

        return stoneMap.values.sum().toLong()
    }

    // For each stone in map, blink multiply each result by number of the original stone, add to map
    fun mapBlink(stones: Map<String, Long>): Map<String, Long> {
        val newStones = HashMap<String, Long>()
        stones.forEach{k, v ->
            if (k.equals("0")) {
                newStones.compute("1", {_, v1 -> if (v1 == null) 1 * v else v1 + v})
            }
            else if (k.length % 2 == 0) {
                val left = k.subSequence(0, k.length / 2).toString().toLong().toString() // To strip leading zeroes
                val right = k.subSequence(k.length / 2, k.length).toString().toLong().toString()
                newStones.compute(left, {_, v1 -> if (v1 == null) 1 * v else v1 + v})
                newStones.compute(right, {_, v1 -> if (v1 == null) 1 * v else v1 + v})
            } else {
                newStones.compute((k.toLong() * 2024).toString(), {_, v1 -> if (v1 == null) 1 * v else v1 + v})
            }
        }
        return newStones
    }

    fun blink(stones: MutableList<String>): ArrayDeque<String> {
        val newStones = ArrayDeque<String>()
        stones.forEach {
            if (it.equals("0")) newStones.add("1")
            else if (it.length % 2 == 0) {
                val left = it.subSequence(0, it.length / 2).toString().toLong().toString() // To strip leading zeroes
                val right = it.subSequence(it.length / 2, it.length).toString().toLong().toString()
                newStones.add(left)
                newStones.add(right)
            } else {
                newStones.add((it.toLong() * 2024).toString())
            }
        }
        return newStones
    }
}