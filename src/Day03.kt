import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {

    fun part1(input: List<String>): Int {
        val operationStrings = input.joinToString("")

        var currIndex = 0
        var sum = 0

        while(currIndex < operationStrings.length) {
            val mulIndex = operationStrings.indexOf("mul(", currIndex)
            if(mulIndex == -1) {
                break
            }

            var workingSubstring = operationStrings.substring(mulIndex, min(mulIndex + 12, operationStrings.length - 1))
            if(!workingSubstring.contains(")")) {
                currIndex = mulIndex + 4
                continue
            }

            val closeIndex = workingSubstring.indexOf(")")
            workingSubstring = workingSubstring.substring(4, closeIndex)

            if(!workingSubstring.contains(",")) {
                currIndex = mulIndex + 4
                continue
            }

            val tokens = workingSubstring.split(",")
            if(tokens.size != 2) {
                currIndex = mulIndex + 4
                continue
            }

            try {
                val numTokens = tokens.map {it.toInt()}
                sum += numTokens[0] * numTokens[1]

                currIndex = mulIndex + closeIndex + 1
            } catch (e: NumberFormatException) {
                currIndex = mulIndex + 4
            }
        }
        return sum
    }

    fun part2(input: List<String>): Int {

        val operationStrings = input.joinToString("")

        var currIndex = 0
        var enabled = true
        var sum = 0

        while(currIndex < operationStrings.length) {

            var doIndex = operationStrings.indexOf("do()", currIndex)
            var dontIndex = operationStrings.indexOf("don't()", currIndex)
            val mulIndex = operationStrings.indexOf("mul(", currIndex)

            doIndex = if(doIndex == -1) Int.MAX_VALUE else doIndex
            dontIndex = if(dontIndex == -1) Int.MAX_VALUE else dontIndex


            // 'Do' comes first
            if(doIndex < dontIndex && doIndex < mulIndex) {
                enabled = true
                currIndex = doIndex + 4
            }
            // 'Don't' comes first
            else if(dontIndex < doIndex && dontIndex < mulIndex) {
                enabled = false
                currIndex = dontIndex + 7
            } else {
                // 'mul' comes first

                if(mulIndex == -1) {
                    break
                }

                if(!enabled) {
                    currIndex = mulIndex + 4
                    continue
                }

                var workingSubstring = operationStrings.substring(mulIndex, min(mulIndex + 12, operationStrings.length - 1))
                if(!workingSubstring.contains(")")) {
                    currIndex = mulIndex + 4
                    continue
                }

                val closeIndex = workingSubstring.indexOf(")")
                workingSubstring = workingSubstring.substring(4, closeIndex)

                if(!workingSubstring.contains(",")) {
                    currIndex = mulIndex + 4
                    continue
                }

                val tokens = workingSubstring.split(",")
                if(tokens.size != 2) {
                    currIndex = mulIndex + 4
                    continue
                }

                try {
                    val numTokens = tokens.map {it.toInt()}
                    sum += numTokens[0] * numTokens[1]

                    currIndex = mulIndex + closeIndex + 1
                } catch (e: NumberFormatException) {
                    currIndex = mulIndex + 4
                }
            }
        }

        return sum
    }

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
