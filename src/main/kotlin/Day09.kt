class Day09 : GenericDay(9, false) {
    override fun parseInput(): List<List<Int>> {
        return input.inputAsList.map { it.split(" ").filterNot { v -> v.isEmpty() }.map { v -> v.toInt() } }
    }
    
    override fun solvePart1(): Int {
        val input = parseInput()
        val nextValues = arrayListOf<Int>()
        for (history in input) {
            nextValues.add(calculateNextValue(history))
        }

        return nextValues.sum()
    }

    private fun calculateNextValue(history: List<Int>): Int {
        val diffSequences = getDiffSequences(history)

        val nextValues = arrayListOf(0)
        for (i in 1..<diffSequences.size) {
            nextValues.add(nextValues.last() + diffSequences.reversed()[i].last())
        }

        return nextValues.last()
    }

    private fun calculatePreviousValue(history: List<Int>): Int {
        val diffSequences = getDiffSequences(history)

        val previousValues = arrayListOf(0)
        for (i in 1..<diffSequences.size) {
            previousValues.add(diffSequences.reversed()[i].first() - previousValues.last())
        }

        return previousValues.last()
    }

    private fun getDiffSequences(history: List<Int>): ArrayList<List<Int>> {
        val historyPairs = getPairs(history)
        val diffSequences = arrayListOf(history, getDiffSequence(historyPairs))

        while (diffSequences.firstOrNull { it.all { v -> v == 0 } } == null) {
            diffSequences.add(getDiffSequence(getPairs(diffSequences.last())))
        }

        return diffSequences
    }

    private fun getDiffSequence(pairs: List<Pair<Int, Int>>): List<Int> {
        return pairs.map { it.second - it.first }
    }

    private fun getPairs(list: List<Int>): List<Pair<Int, Int>> {
        val listPairs = arrayListOf<Pair<Int, Int>>()
        for (i in 1..<list.size) {
            listPairs.add(Pair(list[i-1], list[i]))
        }

        return listPairs
    }
    
    override fun solvePart2(): Int {
        val input = parseInput()
        val nextValues = arrayListOf<Int>()
        for (history in input) {
            nextValues.add(calculatePreviousValue(history))
        }

        return nextValues.sum()
    }
}