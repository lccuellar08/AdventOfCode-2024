import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {

    fun getHashMap(input: List<String>, inverse: Boolean = false): Map<Int, List<Int>> {
        val hashMap = mutableMapOf<Int, MutableList<Int>>()

        input.forEach{it ->
            val tokens = it.split("|").map{it.toInt()}
            if(inverse) {
                hashMap.computeIfAbsent(tokens[0]) { mutableListOf() }.add(tokens[1])
            } else {
                hashMap.computeIfAbsent(tokens[1]) { mutableListOf() }.add(tokens[0])
            }
        }

        return hashMap
    }

    fun getRulesAndUpdates(input: List<String>, inverse: Boolean = false): Pair<Map<Int, List<Int>>, List<List<Int>>> {

        val rulesInput = mutableListOf<String>()
        val updatesInput = mutableListOf<String>()

        var readingRulesFlag = true

        input.forEach{line ->
            if(readingRulesFlag) {
                if(line.isNotEmpty()) {
                    rulesInput.add(line)
                } else {
                    readingRulesFlag = false
                }
            } else {
                updatesInput.add(line)
            }
        }

        return Pair(
            getHashMap(rulesInput, inverse),
            updatesInput.map{it.split(",").map{it.toInt()}}
        )
    }

    fun checkIfUpdatesAreValid(updates: List<Int>, rules: Map<Int, List<Int>>): Pair<Boolean, Int> {
        updates.forEachIndexed{index, update ->
            val numbersBefore = updates.slice(0 until index)
            if(numbersBefore.isNotEmpty()) {
                val updateRules = rules[update] ?: return Pair(false, index)
                if(!updateRules.containsAll(numbersBefore)) {
                    return Pair(false, index)
                }
            }
        }
        return Pair(true, -1)
    }

    fun part1(input: List<String>): Int {
        val (rules, updatesList) = getRulesAndUpdates(input)

        val middleUpdates = mutableListOf<Int>()
        updatesList.forEach{updates ->
            if(checkIfUpdatesAreValid(updates, rules).first) {
                middleUpdates.add(updates[updates.size / 2])
            }
        }

        return middleUpdates.sum()
    }

    fun part2(input: List<String>): Int {
        val (rules, updatesList) = getRulesAndUpdates(input)
        val (inverseRules, _) = getRulesAndUpdates(input, true)

        val middleUpdates = mutableListOf<Int>()
        updatesList.forEach{updates ->
            var (valid, invalidIndex) = checkIfUpdatesAreValid(updates, rules)
            if(valid) return@forEach

            val newUpdates = updates.toMutableList()

            do {
                val invalidUpdate = newUpdates[invalidIndex]
                val invalidUpdateRules = inverseRules.getOrDefault(invalidUpdate, emptyList())
                val otherUpdate = newUpdates.slice(0 until invalidIndex).find{
                    invalidUpdateRules.contains(it)
                }
                if(otherUpdate != null) {
                    val otherIndex = newUpdates.indexOf(otherUpdate)
                    newUpdates[invalidIndex] = otherUpdate
                    newUpdates[otherIndex] = invalidUpdate
                }

                val (newValid, newInvalidIndex) = checkIfUpdatesAreValid(newUpdates, rules)
                valid = newValid
                invalidIndex = newInvalidIndex
            } while(!valid)

            middleUpdates.add(newUpdates[newUpdates.size / 2])
        }

        return middleUpdates.sum()
    }

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}
