// 6272 too high
// 5715 too low
class Day1(isTest: Boolean) : Day(isTest) {
    data class Direction(val clockwise: Boolean, val degrees: Int)

    fun part1(): Int {
        val input = Helper2025.readAsLines(inputFile);
        val parsed = input.map { line ->
            Direction(line[0] == 'R', line.substring(1).toInt())
        }

        var pos = 50;
        val modulo = 100;
        var res = 0;
        parsed.forEach { direction ->
            if (direction.clockwise) {
               pos += direction.degrees;
            } else {
                pos -= direction.degrees
                pos += modulo // to avoid negative modulo
            }
            pos = pos % modulo;
            if (pos == 0) res += 1
        }
        return res
    }

    fun part2(): Int {
        val input = Helper2025.readAsLines(inputFile);
        val parsed = input.map { line ->
            Direction(line[0] == 'R', line.substring(1).toInt())
        }

        var pos = 50;
        val modulo = 100;
        var res = 0;

        parsed.forEach { direction ->
            // Final pos
            if (direction.clockwise) {
                pos += direction.degrees;
                res += (pos / modulo)
            } else {
                // Weird logic to stop double counting
                if (pos == 0) {
                    res -= 1
                }
                pos -= direction.degrees
                while (pos < 0) {
                    pos += modulo
                    res += 1
                }
                if (pos == 0) {
                    res += 1
                }
            }
            pos = pos % modulo;
        }
        return res
    }
}