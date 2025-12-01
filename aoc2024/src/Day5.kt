class Day5(isTest: Boolean) : Day(isTest) {
    fun part1(): Long {
        val input = Helper.readAsLines(inputFile)
        val splitIndex = input.indexOf("")
        val rules = input.subList(0, splitIndex)
        val pages = input.subList(splitIndex+1, input.size).map { it.split(",").map { it.toLong() } }

        // Map rules
        val rulePairs: List<Pair<Long, Long>> = rules.map { rule ->
            val splitRule = rule.split("|")
            Pair(splitRule[0].toLong(), splitRule[1].toLong())
        }
        // Pages
        val goodPages = mutableListOf<List<Pair<Long, Long>>>()
        pages.forEach{
            val pagePairs = it.zipWithNext()
            if (rulePairs.containsAll(pagePairs)) goodPages.add(pagePairs)
        }

        var total = 0L
        val goodPagesAsList = goodPages.map { it.flatMap { listOf(it.first, it.second) }.distinct() } // Must be a nicer way?
        goodPagesAsList.forEach{
            val middle = it.get((it.size)/2) // Hopefully all list sizes are odd so there's a true middle
            total += middle
        }

        return total
    }

    fun part2(): Long {
        val input = Helper.readAsLines(inputFile)
        val splitIndex = input.indexOf("")
        val rules = input.subList(0, splitIndex)
        val pages = input.subList(splitIndex+1, input.size).map { it.split(",").map { it.toLong() } }

        // Map rules
        val rulePairs = rules.map { rule ->
            val splitRule = rule.split("|")
            Pair(splitRule[0].toLong(), splitRule[1].toLong())
        }

        // Pages
        val badPages = mutableListOf<List<Pair<Long, Long>>>()
        pages.forEach{
            val pagePairs = it.zipWithNext()
            if (rulePairs.containsAll(pagePairs)) else badPages.add(pagePairs)
        }
        val badPagesAsList: List<List<Long>> = badPages.map { it.flatMap { listOf(it.first, it.second) }.distinct() } // Must be a nicer way?
        val fixedList = badPagesAsList.map { iterateAndSwap(it.toMutableList(), rulePairs) }
        var total = 0L
        fixedList.forEach{
            val middle = it.get((it.size)/2) // Hopefully all list sizes are odd so there's a true middle
            total += middle
        }

        return total
    }

    fun iterateAndSwap(list: MutableList<Long>, goodRules: List<Pair<Long, Long>>): List<Long> {
        var count = 0
        // "Recursion"
        while (count != list.size - 1) {
            count = 0
            for (i in 0 until list.size - 1) {
                val first = list[i]
                val second = list[i + 1]
                val pair = Pair(first, second)
                if (goodRules.contains(pair)) {
                    count++ // "Recursion"
                } else {
                    list[i] = second
                    list[i + 1] = first
                }
            }
        }
        return list
    }

}