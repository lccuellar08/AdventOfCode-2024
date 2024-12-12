fun main() {

    fun parseStones(input: List<String>) = input[0].split(" ").map{it.toLong()}

    val seenStonesCount = mutableMapOf<Pair<Long, Int>, Long>()
    fun getFinalNumberOfStones(stone: Long, count: Int): Long {
        if(count == 0) {
            return 1L
        }

        if(seenStonesCount.containsKey(Pair(stone, count))) {
            return seenStonesCount[Pair(stone, count)]!!
        }

        if(stone == 0L) {
            val finalCount = getFinalNumberOfStones(1L, count -1)
            seenStonesCount[Pair(stone, count)] = finalCount
            return finalCount
        }
        val stoneString = "$stone"
        if(stoneString.length % 2 == 0) {
            val firstHalf = stoneString.slice(0..<stoneString.length / 2)
            val secondHalf = stoneString.slice(stoneString.length / 2..<stoneString.length)

            val finalCount = getFinalNumberOfStones(firstHalf.toLong(), count -1) + getFinalNumberOfStones(secondHalf.toLong(), count - 1)
            seenStonesCount[Pair(stone, count)] = finalCount
            return finalCount
        }

        val finalCount = getFinalNumberOfStones(stone * 2024, count -1)
        seenStonesCount[Pair(stone, count)] = finalCount
        return finalCount
    }

    fun getNewStones(stone: Long): List<Long> {
        if(stone == 0L) {
            return listOf(1L)
        }
        val stoneString = "$stone"
        if(stoneString.length % 2 == 0) {
            val firstHalf = stoneString.slice(0..<stoneString.length / 2)
            val secondHalf = stoneString.slice(stoneString.length / 2..<stoneString.length)
            return listOf(firstHalf.toLong(), secondHalf.toLong())
        }
        return listOf(stone * 2024)
    }

    fun part1(input: List<String>): Int {
        var stones = parseStones(input)

        for(i in 0..<25) {
            stones = stones.map{getNewStones(it)}.flatten()
        }

        return stones.size
    }

    fun part2(input: List<String>): Long {
        val stones = parseStones(input)

        return stones.sumOf { stone ->
            getFinalNumberOfStones(stone, 75)
        }
    }

    val input = readInput("Day11")

    println(part1(input))
    println(part2(input))
}
