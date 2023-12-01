open class GenericDay(private val day: Int, private val inTest: Boolean = false) {
    val input: InputUtil = InputUtil(day, inTest)

    open fun parseInput(): Any? {
        // Implement input parsing logic here
        return null
    }

    open fun solvePart1(): Int {
        // Implement solution logic for part 1 here
        return 0
    }

    open fun solvePart2(): Int {
        // Implement solution logic for part 2 here
        return 0
    }

    fun printSolutions() {
        println("-------------------------")
        println("         Day $day        ")
        println("Solution for puzzle one: ${solvePart1()}")
        println("Solution for puzzle two: ${solvePart2()}")
        println("\n")
    }
}
