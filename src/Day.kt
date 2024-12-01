abstract class Day(isTest: Boolean) {
    val inputFile: String
    init {
        val fileName = "${javaClass.simpleName.lowercase()}${if (isTest) "_test" else ""}" // Kind of reads weirdly, maybe regular string concat is better
        inputFile = "$fileName.txt"
    }
}