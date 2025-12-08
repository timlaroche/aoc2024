import day6.Day6
import day8.Day8

fun main() {
    val day = Day6(false)
    val day_test = Day6(true)

    println("======test_input======")
    println(day_test.part1())
    println(day_test.part2())
    println("======real_input======")
    println(day.part1())
    println(day.part2())
}