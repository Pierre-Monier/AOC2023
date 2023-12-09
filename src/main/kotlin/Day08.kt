import kotlin.math.max
import kotlin.math.min

class Day08 : GenericDay(8, false) {
    override fun parseInput(): Maps {
        val rawData = input.inputAsList
        return Maps(rawData.first(), rawData.drop(2))
    }

    override fun solvePart1(): Int {
        val maps = parseInput()
        return maps.findNumberOfStep()
    }

    override fun solvePart2(): Int {
        val maps = parseInput()
        val result = maps.findNumberOfGhostStep()
        // Converting to Int break the result
        println("actual result : $result")
        return 0
    }

    class Maps(private val instructions: String, rawPaths: List<String>) {
        private val paths: Map<String, Pair<String, String>> = rawPaths.map { it.split("=").map { v -> v.trim() } }
            .associate { it.first().take(3) to Pair(it.last().drop(1).take(3), it.last().drop(6).take(3)) }

        private fun getNumberOfSteps(numberOfSteps: Int, numberOfInstructionLoop: Int): Int {
            return numberOfSteps + (numberOfInstructionLoop * (instructions.length))
        }

        fun findNumberOfStep(): Int {
            var numberOfInstructionLoop = 0
            var numberOfSteps = 0
            var node = paths.keys.first { it == "AAA" }


            while (node != "ZZZ") {
                val nodeToExplore = paths[node]!!

                node = if (instructions[numberOfSteps] == 'L')
                    nodeToExplore.first
                else
                    nodeToExplore.second

                if (numberOfSteps >= instructions.length - 1) {
                    numberOfSteps = 0
                    numberOfInstructionLoop++
                } else {
                    numberOfSteps++
                }
            }

            return getNumberOfSteps(numberOfSteps, numberOfInstructionLoop)
        }

        fun findNumberOfGhostStep(): Long {
            var numberOfInstructionLoop = 0
            var numberOfSteps = 0
            var nodes = paths.keys.filter { it.last() == 'A' }
            val distances = arrayListOf<Long>()

            while (nodes.isNotEmpty()) {
                val nodesToExplore = paths.filter { nodes.contains(it.key) }.map { it.value }

                val newNodes = arrayListOf<String>()
                for (nodeToExplore in nodesToExplore) {
                    newNodes.add(if (instructions[numberOfSteps] == 'L') nodeToExplore.first else nodeToExplore.second)
                }
                nodes = newNodes.filterNot { it.last() == 'Z' }

                if (numberOfSteps >= instructions.length - 1) {
                    numberOfSteps = 0
                    numberOfInstructionLoop++
                } else {
                    numberOfSteps++
                }

                val endedNodes = newNodes.filter { it.last() == 'Z' }
                endedNodes.forEach { _ ->
                    distances.add(getNumberOfSteps(numberOfSteps, numberOfInstructionLoop).toLong())
                }
            }

            return getLCMs(distances)
        }

        private fun getLCMs(distances: List<Long>): Long {
            return distances.reduce { acc, i -> getLCM(acc, i) }
        }

        private fun getLCM(a: Long, b: Long): Long {
            return ((a*b)/getGCD(a,b))
        }

        private fun getGCD(a: Long, b: Long): Long {
            if (a == 0L || b == 0L) {
                return a+b
            }

            val biggerValue = max(a, b)
            val smallerValue = min(a,b)

            return getGCD(biggerValue % smallerValue, smallerValue)
        }
    }
}