import java.io.File

class Helper {
    companion object {
        fun readString(path: String): String {
            // TODO wtf fix this hack
            val x = File("").absolutePath
            val realPath = x + "/src/" + path

            val bufferedReader = File(realPath).bufferedReader()
            val inputString = bufferedReader.use { it.readText() }
            return inputString
        }

        fun readAsPairs(path: String): List<Pair<String, String>> {
            val x = File("").absolutePath
            val realPath = x + "/aoc2024/src/" + path

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
            val realPath = x + "/src/" + path

            var listOfString = mutableListOf<String>()

            val bufferedReader = File(realPath).bufferedReader()
            bufferedReader.forEachLine {
                listOfString.add(it)
            }
            return listOfString;
        }
    }
}