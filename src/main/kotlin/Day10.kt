import kotlin.coroutines.suspendCoroutine

class Day10 : GenericDay(10, false) {
    override fun parseInput(): List<List<Pipes>> {
        return input.inputAsList.map { it.toCharArray().map { c -> Pipes.fromChar(c) } }
    }

    override fun solvePart1(): Int {
        val input = parseInput()
        val yStartIndex = input.indexOfFirst { it.contains(Pipes.S) }
        val xStartIndex = input[yStartIndex].indexOf(Pipes.S)
        val start = Pair(yStartIndex, xStartIndex)
        val distances = arrayListOf<List<Pair<Int, Int>>>()

        distances.addAll(listOf(explore('N', start, input)))
        distances.addAll(listOf(explore('S', start, input)))
        distances.addAll(listOf(explore('E', start, input)))
        distances.addAll(listOf(explore('W', start, input)))

        val positions =
            distances.filter { it.isNotEmpty() }.map { pairs -> pairs.mapIndexed { i, v -> v to i }.toMap() }
        val farthestPositions = mutableMapOf<Pair<Int, Int>, Int>()
        positions.forEach {
            farthestPositions.putAllIfLess(it)
        }

        return farthestPositions.values.max() + 1
    }

    private fun explore(
        direction: Char,
        position: Pair<Int, Int>,
        input: List<List<Pipes>>,
    ): List<Pair<Int, Int>> {
        var previousDirection = direction
        var previousPosition = position
        val distances = arrayListOf<Pair<Int, Int>>()

        do {
            val newPosition = when (previousDirection) {
                'N' -> Pair(previousPosition.first - 1, previousPosition.second)
                'S' -> Pair(previousPosition.first + 1, previousPosition.second)
                'W' -> Pair(previousPosition.first, previousPosition.second - 1)
                else -> Pair(previousPosition.first, previousPosition.second + 1)
            }

            val newPositionIsOutOfBound =
                newPosition.first < 0 || newPosition.first > input.size || newPosition.second < 0 || newPosition.second > input.first().size

            if (newPositionIsOutOfBound) {
                break
            }

            val pipe = input[newPosition.first][newPosition.second]

            val nextDirection = getNextDirection(previousDirection, pipe)
            if (nextDirection == null || distances.contains(newPosition)) {
                break
            }

            distances.add(newPosition)
            previousDirection = nextDirection
            previousPosition = newPosition
        } while (true)

        return distances
    }


    private fun getNextDirection(previousDirection: Char, pipe: Pipes): Char? {
        val reverseDirection = when (previousDirection) {
            'N' -> 'S'
            'S' -> 'N'
            'E' -> 'W'
            else -> 'E'
        }
        if (pipe == Pipes.GROUND) {
            return null
        }

        val nextDirections = pipe.getDirections().filterNot { it == reverseDirection }
        if (nextDirections.size > 1) {
            return null
        }

        return nextDirections.firstOrNull()
    }

    override fun solvePart2(): Int {
        val input = parseInput()
        val yStartIndex = input.indexOfFirst { it.contains(Pipes.S) }
        val xStartIndex = input[yStartIndex].indexOf(Pipes.S)
        val start = Pair(yStartIndex, xStartIndex)
        val distances = arrayListOf<List<Pair<Int, Int>>>()

        distances.addAll(listOf(explore('N', start, input)))
        distances.addAll(listOf(explore('S', start, input)))
        distances.addAll(listOf(explore('E', start, input)))
        distances.addAll(listOf(explore('W', start, input)))

        var loop = distances.first() { it.isNotEmpty() }
        loop = listOf(start) + loop
        val otherTiles =
            input.flatMapIndexed { index, v -> List(v.size) { i -> Pair(index, i) } }
                .filterNot { loop.contains(it) || it == start }


        val nestedTiles = findNestedTiles(loop, otherTiles, input,input.size - 1, input.first().size - 1)

        for (y in input.indices) {
            val chars = arrayListOf<String>()
            for (x in 0..<input.first().size) {
                val char = if (loop.any { it == Pair(y, x) }) loop.indexOf(Pair(y, x)).toString().padStart(3, '0') else if (nestedTiles.any { it == Pair(y,x) }) "iii" else  "ooo"
                chars.add(char)
            }

            println(chars.joinToString())
        }

        return nestedTiles.size
    }

    private fun findNestedTiles(
        loop: List<Pair<Int, Int>>,
        otherTiles: List<Pair<Int, Int>>,
        input: List<List<Pipes>>,
        maxY: Int,
        maxX: Int
    ): List<Pair<Int, Int>> {
        fun rayCast(tile: Pair<Int, Int>): Int {
            var numberOfCrossings = 0
            val countingChars = listOf(Pipes.SN, Pipes.NE, Pipes.NW)
            for (i in tile.second..maxX) {
                val exploringTile = Pair(tile.first, i)
                if (loop.contains(exploringTile) &&  countingChars.contains(input[exploringTile.first][exploringTile.second])) {
                    numberOfCrossings++
                }
            }

            return numberOfCrossings
        }

        val toProcessedTiles = arrayListOf<Pair<Int, Int>>()
        toProcessedTiles.addAll(otherTiles)

        val nestedTiles = arrayListOf<Pair<Int, Int>>()

        do {
            val tile = toProcessedTiles.first()
            val tilesNeighbors = findTileNeighbors(tile, loop, maxY, maxX)

            toProcessedTiles.removeIf { tilesNeighbors.contains(it) }

            if (rayCast(tilesNeighbors.first()) % 2 != 0) {
                nestedTiles.addAll(tilesNeighbors)
            }
        } while (toProcessedTiles.isNotEmpty())

        return nestedTiles
    }

    private fun findTileNeighbors(
        tile: Pair<Int, Int>,
        loop: List<Pair<Int, Int>>,
        maxY: Int,
        maxX: Int,
    ): List<Pair<Int, Int>> {
        val neighbors = arrayListOf(tile)
        val processedTiles = arrayListOf(tile)
        var currentTile = tile

        fun isOutBorder(tile: Pair<Int, Int>): Boolean {
            return tile.first < 0 || tile.first > maxY || tile.second < 0 || tile.second > maxX
        }

        do {
            val newNeighbors = listOf(
                Pair(currentTile.first - 1, currentTile.second - 1),
                Pair(currentTile.first - 1, currentTile.second),
                Pair(currentTile.first - 1, currentTile.second + 1),
                Pair(currentTile.first, currentTile.second - 1),
                Pair(currentTile.first, currentTile.second + 1),
                Pair(currentTile.first + 1, currentTile.second - 1),
                Pair(currentTile.first + 1, currentTile.second),
                Pair(currentTile.first + 1, currentTile.second + 1)
            ).filterNot { loop.contains(it) || neighbors.contains(it) || isOutBorder(it) }

            neighbors.addAll(newNeighbors)
            currentTile = neighbors.firstOrNull { !processedTiles.contains(it) } ?: break
            processedTiles.add(currentTile)
        } while (true)

        return neighbors
    }


    enum class Pipes() {
        SN,
        EW,
        NE,
        NW,
        SE,
        SW,
        GROUND,
        S;

        fun getDirections(): CharArray {
            if (this == GROUND || this == S) {
                return charArrayOf()
            }

            return this.name.toCharArray()
        }

        companion object {
            fun fromChar(c: Char): Pipes {
                return when (c) {
                    '|' -> SN
                    '-' -> EW
                    'L' -> NE
                    'J' -> NW
                    '7' -> SW
                    'F' -> SE
                    'S' -> S
                    else -> GROUND
                }
            }
        }
    }
}

fun <K, V : Comparable<V>> MutableMap<K, V>.putAllIfLess(other: Map<K, V>) {
    val filteredEntries = other.filter { !contains(it.key) || (get(it.key)!! > it.value) }
    putAll(filteredEntries)
}