class Day06 : GenericDay(6, false) {
    override fun parseInput(): List<String> {
        return input.inputAsList.map { it.split(":").last() }
    }

    private fun findNumberOfWays(time: Long, distance: Long): Int {
        var numberOfWays = 0
        for (i in 0..time) {
            if (i * (time - i) > distance) {
                numberOfWays++
            }
        }

        return numberOfWays
    }

    override fun solvePart1(): Int {
        val input = parseInput().map {
            it.split(" ").filter { v ->
                v.isNotEmpty()
            }.map { v -> v.toLong() }
        }

        return input.first().zip(input.last()).map { (time, distance) ->
            findNumberOfWays(time, distance)
        }.reduce { acc, v -> acc * v }
    }

    override fun solvePart2(): Int {
        val input = parseInput().map { it.trim().filterNot { s -> s.isWhitespace() } }.map { it.toLong() }
        return findNumberOfWays(input.first(), input.last())
    }
}