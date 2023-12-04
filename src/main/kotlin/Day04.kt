class Day04 : GenericDay(4, false) {
    override fun parseInput(): List<Card> {
        return input.inputAsList.map { it.split(":").last().split("|") }
            .mapIndexed { i, it ->
                Card(
                    it.first().split(" ").filter { v -> v.isNotEmpty() }
                        .map { v -> v.toInt() },
                    it.last().split(" ").filter { v -> v.isNotEmpty() }.map { v -> v.toInt() },
                    i + 1
                )
            }
    }

    override fun solvePart1(): Int {
        return parseInput().sumOf { it.getPoints() }
    }

    override fun solvePart2(): Int {
        return Game(ArrayList(parseInput())).getPoints()
    }

    class Game(private val cards: ArrayList<Card>) {
        fun getPoints(): Int {
            var x = 0
            val dummyCards = ArrayList( cards.map { DummyCard(it.id, it.getNumberOfWinningCard()) })

            while (x < dummyCards.size) {
                val card = dummyCards[x]

                val points = card.winningNumbers
                for (i in (card.id + 1)..(card.id + points)) {
                    val copied = dummyCards.firstOrNull { it.id == i } ?: continue
                    dummyCards.add(copied)
                }

                x += 1
            }

            return dummyCards.size
        }
    }

    data class DummyCard(val id: Int, val winningNumbers: Int)

    data class Card(
        val winningNumbers: List<Int>,
        val yourNumbers: List<Int>,
        val id: Int,
        var processed: Boolean = false
    ) {
        fun getNumberOfWinningCard(): Int {
            processed = true
            val intersect = winningNumbers.intersect(yourNumbers.toSet())

            return intersect.size
        }

        fun getPoints(): Int {
            val numberOfWinningCard = getNumberOfWinningCard()

            fun calculatePoints(x: Int): Int {
                return when (x) {
                    0 -> 0
                    1 -> 1
                    else -> calculatePoints(x - 1) * 2
                }
            }

            return calculatePoints(numberOfWinningCard)
        }
    }
}