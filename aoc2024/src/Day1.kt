import kotlin.math.abs

class Day1(isTest: Boolean) : Day(isTest) {
    fun part1(): Long {
        val input = Helper.readAsPairs(inputFile)

        // I know we can traverse the list once, but getting used to Kotlin syntax so traversing twice
        val leftSorted = input.map {
            it.first.toLong()
        }.sorted()
        val rightSorted = input.map {
            it.second.toLong()
        }.sorted()
        val sortedZip = leftSorted.zip(rightSorted)

        val finalList = sortedZip.map {
            abs(it.first - it.second)
        }

        var sum = 0L
        finalList.forEach {
            sum = sum + it
        }
        return sum
    }

    fun part2(): Long {
        val input = Helper.readAsPairs(inputFile)
        // eachCount is a Kotlin feature, otherwise store in a hashmap as we traverse the list
        val rightFreq: Map<Long, Int> = input.map { it.second.toLong() }.groupingBy { it }.eachCount()
        var sum = 0L
        input.forEach {
            sum = sum + (it.first.toLong() * (rightFreq[it.first.toLong()] ?: 0))
        }
        return sum
    }
}