import day3.Day3

fun main() {
    val day = Day3(false)
    val day_test = Day3(true)

    println("======test_input======")
    println(day_test.part1())
    println(day_test.part2())
    println("======real_input======")
    println(day.part1())
    println(day.part2())
}