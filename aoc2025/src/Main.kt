import day2.Day2

fun main() {
    val day = Day2(false)
    val day_test = Day2(true)

    println("======test_input======")
    println(day_test.part1())
    println(day_test.part2())
    println("======real_input======")
    println(day.part1())
    println(day.part2())
}