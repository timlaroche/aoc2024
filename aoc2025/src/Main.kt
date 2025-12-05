import day5.Day5

fun main() {
    val day = Day5(false)
    val day_test = Day5(true)

    println("======test_input======")
    println(day_test.part1())
    println(day_test.part2())
    println("======real_input======")
    println(day.part1())
    println(day.part2())
}