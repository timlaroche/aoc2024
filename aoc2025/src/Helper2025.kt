import java.io.File

class Helper2025 {
    companion object {
        private val inputSrc = "/aoc2025/inputs/"
        fun readString(path: String): String {
            // TODO wtf fix this hack
            val x = File("").absolutePath
            val realPath = x + inputSrc + path

            val bufferedReader = File(realPath).bufferedReader()
            val inputString = bufferedReader.use { it.readText() }
            return inputString
        }

        fun readAsPairs(path: String): List<Pair<String, String>> {
            val x = File("").absolutePath
            val realPath = x + inputSrc + path

            var listOfPairs = mutableListOf<Pair<String, String>>()

            val bufferedReader = File(realPath).bufferedReader()
            bufferedReader.forEachLine {
                val split = it.split(" ")
                listOfPairs.add(Pair(split.first(), split.last()))
            }
            return listOfPairs;
        }

        fun readAsLines(path: String): List<String> {
            val x = File("").absolutePath
            val realPath = x + inputSrc + path

            var listOfString = mutableListOf<String>()

            val bufferedReader = File(realPath).bufferedReader()
            bufferedReader.forEachLine {
                listOfString.add(it)
            }
            return listOfString;
        }

        fun readAsCharGrid(path: String): MutableList<MutableList<Char>> {
            val x = File("").absolutePath
            val realPath = x + inputSrc + path

            val grid = mutableListOf(mutableListOf<Char>()).toMutableList()
            val bufferedReader = File(realPath).bufferedReader()
            grid.removeFirst() // hack wtf
            bufferedReader.forEachLine {
                grid.add(
                    it.toCharArray().map {
                        it
                    }.toMutableList()
                )
            }
            return grid
        }
    }
}