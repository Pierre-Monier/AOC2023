class Day11 : GenericDay(11, true) {
    override fun parseInput(): List<List<String>> {
        val universe = arrayListOf(input.inputAsList.map { it.split })

        val empty_rowsIndexes = universe.mapIndexedNotNull { index, chars -> if (!chars.contains("#")) index else null }
        val emptyColumnIndexes = (0..<universe.first().size).mapNotNull { x -> universe.map { it[x] } }
            .mapIndexedNotNull { index, chars -> if (!chars.contains("#")) index else null }

        for (y in emptyColumnIndexes) {
            universe.add(y, ArrayList((0..<universe.first().size).map { "." }))
        }

        for (x in empty_rowsIndexes) {
            universe.forEach {
                it.add(x, ".")
            }
        }

        return universe
    }

    override fun solvePart1(): Int {
        val tmp = parseInput()
        return 0
    }

    override fun solvePart2(): Int {
        return 0
    }
}