class Day2(isTest: Boolean) : Day(isTest) {
    fun part1(): Long {
        val input = Helper2025.readString(inputFile)
        val inputSplit = input.split(',')

        val rangePair = inputSplit.map { it ->
            it.split('-').let { (start, end) -> Pair(start, end) }
        }

        var res: Long = 0
        rangePair.forEach { it ->
            for (i in it.first.toLong()..it.second.toLong()) {
                if (i.toString().length % 2 == 1) {
                    // skip uneven lengths as they can't repeat
                    continue
                }
                val str = i.toString()
                val firstHalf = str.substring(0, (str.length / 2))
                val secondHalf = str.substring((str.length / 2), str.length)
                val duplicate = firstHalf == secondHalf
                if (duplicate) res += i
            }
        }

        return res
    }

    // 65794984339 too low

    fun part2(): Long {
        val input = Helper2025.readString(inputFile)
        val inputSplit = input.split(',')

        val rangePair = inputSplit.map { it ->
            it.split('-').let { (start, end) -> Pair(start, end) }
        }

        var res: Long = 0
        rangePair.forEach { it ->
            for (i in it.first.toLong()..it.second.toLong()) {
                val str = i.toString()
                var shoudldAdd = false
                for (j in 1..str.length / 2) {
                    val chunks = str.chunked(j)
                    val distinct = chunks.distinct()
                    if (distinct.size == 1) {
                        shoudldAdd = true
                    }
                }
                res += if (shoudldAdd) i else 0
            }
        }
        return res
    }
}