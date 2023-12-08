class Day05 : GenericDay(5, false) {
    override fun parseInput(): Almanac {
        return Almanac(input.inputAsList)
    }

    override fun solvePart1(): Int {
        val almanac = parseInput()
        return almanac.findNearestSeed().toInt()
    }

    override fun solvePart2(): Int {
        val almanac = parseInput()
        return almanac.findNearestSeedInRange().toInt()
    }

    class Almanac(rawData: List<String>) {
        private val seeds: List<Long>

        private val convertersData: ArrayList<ArrayList<List<Long>>>

        init {
            seeds = rawData.first().split(":").last().split(" ").filter { it.isNotEmpty() }.map { it.toLong() }

            val convertersRawData = rawData.drop(1)
            convertersData = arrayListOf()
            var shouldCreateNewArray = true
            for (line in convertersRawData) {
                shouldCreateNewArray = if (line.isNotEmpty() && line.first().isDigit()) {
                    val value = line.split(" ").filter { it.isNotEmpty() }.map { it.toLong() }
                    if (shouldCreateNewArray) {
                        convertersData.add(arrayListOf(value))
                    } else {
                        convertersData.last().add(value)
                    }

                    false
                } else {
                    true
                }
            }
        }

        fun findNearestSeedInRange(): Long {
            val seedsRange = seeds.chunked(2).map { it.first()..<(it.first()+it.last()) }
            var i: Long = 0
            while (true) {
                val seed = findSeed(i)
                if (seedsRange.any { it.contains(seed) }) {
                    break
                }

                i++
            }

            return i
        }

        fun findNearestSeed(): Long {
            return seeds.minOf {
                findLocation(it)
            }
        }

        private fun findLocation(seed: Long, reversed: Boolean = false): Long {
            var output = seed

            for (converterData in (if (reversed) convertersData.reversed() else convertersData)) {
                for (converter in converterData) {
                    val range = converter[2]
                    if (output >= converter[1] && output < (converter[1] + range)) {
                        val step = output - converter[1]
                        val destination = converter[0] + step
                        output = destination
                        break
                    }
                }

            }

            return output
        }

        private fun findSeed(location: Long): Long {
            var output = location

            for (converterData in convertersData.reversed()) {
                for (converter in converterData) {
                    val range = converter[2]
                    if (output >= converter[0] && output < (converter[0] + range)) {
                        val step = output - converter[0]
                        val destination = converter[1] + step
                        output = destination
                        break
                    }
                }

            }

            return output
        }
    }
}