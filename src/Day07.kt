
data class Equation(val result: Long, val operands: List<Long>)

fun main() {

    fun isSolution(equation: Equation): Long {

        // DFS
        val stack = mutableListOf<Pair<Int, Long>>()
        stack.add(Pair(0, equation.operands.first()))

        while(stack.isNotEmpty()) {
            val (index, sum) = stack.removeLast()
            if(index < equation.operands.size - 1) {
                val nextOperand = equation.operands[index + 1]
                val mulResult = sum * nextOperand
                val sumResult = sum + nextOperand
                if(mulResult == equation.result || sumResult == equation.result) {
                    return equation.result
                }

                stack.add(Pair(index + 1, mulResult))
                stack.add(Pair(index + 1,sumResult))
            }
        }

        return 0L

    }

    fun triSolution(equation: Equation): Long {

        // DFS
        val stack = mutableListOf<Pair<Int, Long>>()
        stack.add(Pair(0, equation.operands.first()))

        while(stack.isNotEmpty()) {
            val (index, sum) = stack.removeLast()
            if(index < equation.operands.size - 1) {
                val nextOperand = equation.operands[index + 1]
                val mulResult = sum * nextOperand
                val sumResult = sum + nextOperand
                val concatResult = "$sum$nextOperand".toLong()
                if(mulResult == equation.result || sumResult == equation.result || concatResult == equation.result) {
                    if(index == equation.operands.size - 2) {
                        return equation.result
                    }
                }

                if(mulResult <= equation.result) {
                    stack.add(Pair(index + 1, mulResult))
                }
                if(sumResult <= equation.result) {
                    stack.add(Pair(index + 1, sumResult))
                }
                if(concatResult <= equation.result) {
                    stack.add(Pair(index + 1, concatResult))
                }
            }
        }

        return 0L

    }

    fun getEquation(input: String): Equation {
        val tokens = input.split(":")
        val result = tokens[0].toLong()
        val operands = tokens[1].trim().split(" ").map{it.toLong()}

        return Equation(result, operands)
    }

    fun part1(input: List<String>): Long {

        val equations = input.map{getEquation(it)}

        val totalSolutions = equations.sumOf { isSolution(it) }


        return totalSolutions
    }

    fun part2(input: List<String>): Long {
        val equations = input.map{getEquation(it)}

        val totalSolutions = equations.sumOf { triSolution(it) }

        return totalSolutions
    }

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}
