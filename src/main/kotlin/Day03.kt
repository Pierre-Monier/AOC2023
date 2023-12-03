class Day03 : GenericDay(3, false) {
    override fun parseInput(): List<List<Char>> {
        return input.inputAsList.map { it.toList() }
    }

    private fun getXYCoordinatesOfNumbers(input: List<List<Char>>): List<NumberWithCoordinate> {
        val coordinates = arrayListOf<NumberWithCoordinate>()
        var y = 0
        var x = 0

        val coordinateBuffer = arrayListOf<Point>()

        fun flushCoordinateBuffer() {
            if (coordinateBuffer.isEmpty()) return

            val bufferValue = coordinateBuffer.map { input[it.y][it.x] }.joinToString("")

            coordinates.add(
                NumberWithCoordinate(
                    coordinateBuffer.first(),
                    coordinateBuffer.last(),
                    bufferValue.toInt(),
                    arrayListOf()
                )
            )

            coordinateBuffer.clear()
        }

        while (input.first().size >= y + 1) {
            val yv = input[y]
            while (yv.size >= x + 1) {
                if (yv[x].isDigit()) {
                    coordinateBuffer.add(Point(x, y))
                } else {
                    flushCoordinateBuffer()
                }

                x += 1
                if (yv.size <= x) {
                    flushCoordinateBuffer()
                }
            }
            x = 0
            y += 1
        }

        return coordinates
    }

    private fun isCoordinateAdjacentToASymbol(coordinate: NumberWithCoordinate, input: List<List<Char>>): Boolean {
        val adjacentCells = getAdjacentCells(coordinate, input.size - 1, input.first().size - 1)

        return adjacentCells.any { !input[it.y][it.x].isDigit() && input[it.y][it.x] != '.' }
    }

    private fun getAdjacentCells(coordinate: NumberWithCoordinate, maxY: Int, maxX: Int): List<Point> {
        val adjacentCells = arrayListOf<Point>()

        val upperX = if (coordinate.start.x > 0) coordinate.start.x - 1 else 0
        val upperY = if (coordinate.start.y > 0) coordinate.start.y - 1 else 0
        val bottomX = if (coordinate.end.x >= maxX) maxX else coordinate.end.x + 1
        val bottomY = if (coordinate.end.y >= maxY) maxY else coordinate.end.y + 1

        val upperLeft = Point(upperX, upperY)
        val bottomRight = Point(bottomX, bottomY)

        var x = upperLeft.x
        var y = upperLeft.y

        while (y <= bottomRight.y) {
            while (x <= bottomRight.x) {
                if (y != coordinate.start.y || (x < coordinate.start.x || x > coordinate.end.x)) {
                    adjacentCells.add(Point(x, y))
                }
                x += 1
            }
            y += 1
            x = upperLeft.x
        }

        return adjacentCells
    }

    private fun setGearSymbols(coordinate: NumberWithCoordinate, input: List<List<Char>>): NumberWithCoordinate {
        val adjacentCells = getAdjacentCells(coordinate, input.size - 1, input.first().size - 1)

        val gearSymbols = adjacentCells.filter { input[it.y][it.x] == '*' }
        coordinate.adjacentGearSymbols.addAll(gearSymbols)

        return coordinate
    }

    override fun solvePart1(): Int {
        val input = parseInput()
        return getXYCoordinatesOfNumbers(input).filter { isCoordinateAdjacentToASymbol(it, input) }.sumOf { it.number }
    }

    private fun getGears(partNumbers: List<NumberWithCoordinate>): List<List<Int>> {
        val gearsMap = mutableMapOf<Point, Int>()

        for (number in partNumbers) {
            for (gearSymbol in number.adjacentGearSymbols) {
                val currentValue = gearsMap[gearSymbol]
                if (currentValue != null) {
                    gearsMap[gearSymbol] = currentValue + 1
                } else {
                    gearsMap[gearSymbol] = 1
                }
            }
        }

        val validGears = gearsMap.filter { it.value == 2 }.map { it.key }
        return validGears.map { partNumbers.filter { number -> number.adjacentGearSymbols.contains(it) } }
            .map { it.map { n -> n.number } }
    }

    override fun solvePart2(): Int {
        val input = parseInput()
        return getGears(getXYCoordinatesOfNumbers(input).filter { isCoordinateAdjacentToASymbol(it, input) }
            .map { setGearSymbols(it, input) }).sumOf { it.reduce { acc, i -> acc * i } }
    }

    private data class NumberWithCoordinate(
        val start: Point,
        val end: Point,
        val number: Int,
        val adjacentGearSymbols: ArrayList<Point>
    )

    private data class Point(val x: Int, val y: Int)
}