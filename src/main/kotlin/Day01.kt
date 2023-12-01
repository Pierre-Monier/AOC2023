class Day01 : GenericDay(1, false) {
    override fun parseInput(): List<String> {
        return this.input.inputAsList
    }

    override fun solvePart1(): Int {
        return parseInput().map { "${it.first { c -> c.isDigit() }}${it.last { c -> c.isDigit() }}" }
            .sumOf { it.toInt() }
    }

    override fun solvePart2(): Int {
        return parseInput().map {
            Regex("\\d|twone|oneight|threeight|fiveight|nineight|eightwo|eighthree|one|two|three|four|five|six|seven|eight|nine").findAll(
                it
            )
        }.map { it.mapIndexedNotNull { i, v -> getCurrentNumber(v.value, i == 0)?.value } }
            .sumOf { "${it.first()}${it.last()}".toInt() }

    }

    private fun getCurrentNumber(value: String, isFirst: Boolean): Number? {
        if (value.isEmpty()) {
            return null
        }

        if (Regex("\\d").matches(value)) {
            return Number.entries.first { it.value == value.toInt() }
        }

        val number =  Number.entries.firstOrNull { it.name == value }
        if (number != null) {
            return number
        }

        return handleEdgeCases(value, isFirst)
    }

    // Not proud of this but I have better things to do
    private fun handleEdgeCases(value: String, isFirst: Boolean): Number? {
        return when (value) {
            "twone" -> if(isFirst) Number.two else Number.one
            "oneight" -> if(isFirst) Number.one else Number.eight
            "threeight" -> if(isFirst) Number.three else Number.eight
            "fiveight" -> if(isFirst) Number.five else Number.eight
            "nineight" -> if(isFirst) Number.nine else Number.eight
            "eightwo" -> if(isFirst) Number.eight else Number.two
            "eighthree" -> if(isFirst) Number.eight else Number.three
            else -> return null
        }

    }

    private enum class Number(val value: Int) {
        one(1),
        two(2),
        three(3),
        four(4),
        five(5),
        six(6),
        seven(7),
        eight(8),
        nine(9)
    }

}
