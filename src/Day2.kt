class Day2(isTest: Boolean) : Day(isTest) {
    fun part1(): Long {
        val input = Helper.readAsLines(inputFile)
        var total = 0L
        input.forEach {
            var numbers = it.split(" ").map { it.toLong() }
            val validDirection = allAscending(numbers) || allDecreasing(numbers)

            if (allDecreasing(numbers)) numbers = numbers.reversed()
            val pairs = checkPairs(numbers)
            if (validDirection && pairs) total += 1
        }
        return total;
    }

    fun checkPairs(ascendingList: List<Long>): Boolean {
        ascendingList.zipWithNext().forEach{
            val diff = it.second - it.first
            if (diff >= 1 && diff <= 3) else return false
        }
        return true
    }

    fun allAscending(list: List<Long>): Boolean {
        list.zipWithNext().forEach{
            if (it.second - it.first <= 0) return false
        }
        return true
    }

    fun allDecreasing(list: List<Long>): Boolean {
        list.zipWithNext().forEach{
            if (it.second - it.first >= 0) return false
        }
        return true
    }

    fun potentialDampened(list: List<Long>): List<List<Long>> {
        val permutations = mutableListOf<List<Long>>()
        list.forEachIndexed{ idx, _ ->
            val copyList = list.toMutableList()
            copyList.removeAt(idx)
            permutations.add(copyList)
        }
        return permutations
    }


    fun part2(): Long {
        val input = Helper.readAsLines(inputFile)
        var total = 0L
        val failedList = mutableListOf<List<Long>>()
        input.forEach {
            var numbers = it.split(" ").map { it.toLong() }
            val validDirection = allAscending(numbers) || allDecreasing(numbers)

            if (allDecreasing(numbers)) numbers = numbers.reversed()
            val pairs = checkPairs(numbers)
            if (validDirection && pairs) total += 1 else failedList.add(numbers)
        }

        failedList.forEach{
            val dampenedList = potentialDampened(it)
            val resultList = dampenedList.map{ dampened ->
                var dampenedCopy = dampened
                val validDirection = allAscending(dampenedCopy) || allDecreasing(dampenedCopy)

                if (allDecreasing(dampenedCopy)) dampenedCopy = dampenedCopy.reversed()
                val pairs = checkPairs(dampenedCopy)
                if (validDirection && pairs) true else false
            }
            if (resultList.contains(true)) total += 1
        }

        return total;
    }
}