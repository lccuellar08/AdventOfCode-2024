import kotlin.math.abs

fun main() {
    fun getBothLists(input: List<String>): Pair<List<Int>, List<Int>> {
        val list1 = mutableListOf<Int>()
        val list2 = mutableListOf<Int>()

        input.forEach {line ->
            val tokens = line.split("   ")
            list1.add((tokens[0].toInt()))
            list2.add((tokens[1].toInt()))
        }

        return Pair(list1, list2)
    }

    fun part1(input: List<String>): Int {
        val (list1, list2) = getBothLists(input)
        val sortedList1 = list1.sorted()
        val sortedList2 = list2.sorted()

        return (sortedList1 zip sortedList2).sumOf {
            abs(it.first - it.second)
        }
    }

    fun part2(input: List<String>): Int {
        val (list1, list2) = getBothLists(input)

        val countsList = list1.map{
            list2.count { itt -> itt == it }
        }

        return (list1 zip countsList).sumOf {
            it.first * it.second
        }
    }

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
