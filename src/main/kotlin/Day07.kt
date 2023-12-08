class Day07 : GenericDay(7, false) {
    override fun parseInput(): List<List<String>> {
        return input.inputAsList.map { it.split(" ") }
    }

    override fun solvePart1(): Int {
        val input = parseInput()
            .map {
                Hand(
                    getTypeP1(it.first()),
                    it.last().toInt(),
                    it.first(),
                )
            }
            .sortedWith(::compareHandsP1)


        val tmp = input.mapIndexed { i, v -> v.bid * (i + 1) }
        return tmp.sum()
    }

    private fun compareHandsP1(a: Hand, b: Hand): Int {
        val typeComparison = a.type.compareTo(b.type)

        if (typeComparison != 0) {
            return typeComparison
        }

        return a.getCardsValueP1().compareTo(b.getCardsValueP1())
    }

    private fun getTypeP1(hand: String): Int {
        val handCards = hand.toCharArray().groupBy { it }
        return when (handCards.keys.size) {
            1 -> 7
            2 -> if (handCards.values.any { it.size == 4 }) 6 else 5
            3 -> if (handCards.values.any { it.size == 3 }) 4 else 3
            4 -> 2
            else -> 1
        }
    }

    private fun getTypeP2(hand: String): Int {
        var handCards = hand.toCharArray().groupBy { it }
        val mostValue = handCards.filterNot { it.key == 'J' }.maxByOrNull { it.value.size}?.key

        if (mostValue != null && handCards.contains('J')) {
            val newHandCards = hand.toCharArray().map { if (it == 'J') mostValue else it }
            handCards = newHandCards.toCharArray().groupBy { it }
        }


        return when (handCards.keys.size) {
            1 -> 7
            2 -> if (handCards.values.any { it.size == 4 }) 6 else 5
            3 -> if (handCards.values.any { it.size == 3 }) 4 else 3
            4 -> 2
            else -> 1
        }
    }

    override fun solvePart2(): Int {
        val input = parseInput()
            .map {
                Hand(
                    getTypeP2(it.first()),
                    it.last().toInt(),
                    it.first(),
                )
            }
            .sortedWith(::compareHandsP2)

        val tmp = input.mapIndexed { i, v -> v.bid * (i + 1) }
        // TOO HIGH
        return tmp.sum()
    }

    private fun compareHandsP2(a: Hand, b: Hand): Int {
        val typeComparison = a.type.compareTo(b.type)

        if (typeComparison != 0) {
            return typeComparison
        }

        return a.getCardsValueP2().compareTo(b.getCardsValueP2())
    }

    data class Hand(val type: Int, val bid: Int, val cards: String) {
        fun getCardsValueP1(): Int {
            return cards.map { if (it.isDigit()) it.toString() else getTopCardValueP1(it) }.joinToString("")
                .toInt(radix = 16)
        }

        fun getCardsValueP2(): Int {
            return cards.map { if (it.isDigit()) it.toString() else getTopCardValueP2(it) }.joinToString("")
                .toInt(radix = 16)
        }

        private fun getTopCardValueP1(c: Char): String {
            return when (c) {
                'T' -> "A"
                'J' -> "B"
                'Q' -> "C"
                'K' -> "D"
                else -> "E"
            }
        }

        private fun getTopCardValueP2(c: Char): String {
            return when (c) {
                'T' -> "A"
                'J' -> "1"
                'Q' -> "C"
                'K' -> "D"
                else -> "E"
            }
        }
    }
}