import day6.Day6
import day8.Day8
import day9.Day9

fun main() {
    val day = Day8(false)
    val day_test = Day8(true)
    println("======test_input======")
    println(day_test.part1())
    println(day_test.part2())
    println("======real_input======")
    println(day.part1())
    println(day.part2())
}