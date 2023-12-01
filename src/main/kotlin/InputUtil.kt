import java.io.File

/**
 * Automatically reads the contents of the input file for a given [day].
 * Note that file name and location must align.
 */
class InputUtil(private val day: Int, private val inTest: Boolean) {
    val inputAsString: String
    val inputAsList: List<String>

    init {
        inputAsString = readInputDay(day)
        inputAsList = readInputDayAsList(day)
    }

    private fun createInputPath(day: Int): String {
        val dayString = day.toString().padStart(2, '0')
        val inTestString = if (inTest) "test" else ""
        return "./input/aoc$dayString$inTestString.txt"
    }

    private fun readInputDay(day: Int): String {
        return readInput(createInputPath(day))
    }

    private fun readInput(input: String): String {
        return File(input).readText()
    }

    private fun readInputDayAsList(day: Int): List<String> {
        return readInputAsList(createInputPath(day))
    }

    private fun readInputAsList(input: String): List<String> {
        return File(input).readLines()
    }

    /**
     * Returns input as one String.
     */
    val asString: String
        get() = inputAsString

    /**
     * Reads the entire input contents as lines of text.
     */
    fun getPerLine(): List<String> = inputAsList

    /**
     * Splits the input String by `whitespace` and `newline`.
     */
    fun getPerWhitespace(): List<String> {
        return inputAsString.split(Regex("\\s"))
    }

    /**
     * Splits the input String by the given [pattern].
     */
    fun getBy(pattern: String): List<String> {
        return inputAsString.split(pattern)
    }
}
