import day6.Day6
import day8.Day8
import day9.Day9

fun main() {
    val day = Day9(false)
    val day_test = Day9(true)
//    println("======test_input======")
    println(day_test.part1())
    println(day_test.part2_map())
    println(day_test.part2())
    println("======real_input======")
    println(day.part1())
    println(day.part2_map())
    println(day.part2())
}