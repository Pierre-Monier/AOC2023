class Day02 : GenericDay(2, false) {
    override fun parseInput(): List<List<List<CubeDraw>>> {
        return input.inputAsList.map { it.split(":").last() }.map { it.split(";") }.map { parseGame(it) }
    }

    private fun parseGame(data: List<String>): List<List<CubeDraw>> {
        return data.map { it.split(",").map { set -> CubeDraw(set.split(" ")[1].toInt(), set.split(" ").last()) } }
    }

    private fun findValidGames(id: Int, configuration: Map<String, Int>, game: List<List<CubeDraw>>): Int {
        game.firstOrNull { it.any { cubeDraw -> configuration[cubeDraw.color]!! < cubeDraw.number } }
            ?: return id

        return 0
    }
    
    override fun solvePart1(): Int {
    val configuration = mapOf("red" to 12, "green" to 13, "blue" to 14)
    return parseInput().mapIndexed { i, g -> findValidGames(i + 1, configuration, g) }.sum()
    }

    private fun finMaxCubeColor(game: List<List<CubeDraw>>): Int {
        val maxValue = arrayListOf<CubeDraw>()
        for (set in game) {
            for (play in set) {
                val minCube = maxValue.firstOrNull { it.color == play.color  }
                if (minCube != null && minCube.number > play.number) continue

                if (minCube != null) {
                    maxValue.remove(minCube)
                }
                maxValue.add(play)
            }
        }

        return maxValue.map { it.number }.reduce { acc, v -> acc*v }
    }
    
    override fun solvePart2(): Int {
      return parseInput().sumOf { finMaxCubeColor(it) }
    }

    data class CubeDraw(val number: Int, val color: String)
}