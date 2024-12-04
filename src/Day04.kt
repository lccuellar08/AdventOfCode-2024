import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {

    fun getGrid(input: List<String>) = input.map{it.toCharArray()}.toTypedArray()

    fun checkIfXMAS(col: Int, row: Int, dCol: Int, dRow: Int, grid: Array<CharArray>): Boolean {
        val checkWord = "XMAS"

        checkWord.forEachIndexed {index, c ->
            try {
                val currentChar = grid[col + (dCol * index)][row + (dRow * index)]
                if(currentChar != c) return false
            } catch(e: IndexOutOfBoundsException) {
                return false
            }
        }

        return true
    }

    fun checkAllDirections(col: Int, row: Int, grid: Array<CharArray>): Int {
        var totalXMases = 0
        for (dCol in -1 .. 1) {
            for (dRow in -1 .. 1) {
                totalXMases += if(checkIfXMAS(col, row, dCol, dRow, grid)) 1 else 0
            }
        }
        return totalXMases
    }

    fun part1(input: List<String>): Int {
        val grid = getGrid(input)

        return grid.mapIndexed gridMap@{ colIndex, col ->
            col.mapIndexed colMap@ {rowIndex, c ->
                if(c != 'X') {
                    return@colMap 0
                }
                else {
                    return@colMap checkAllDirections(colIndex, rowIndex, grid)
                }
            }.sum()
        }.sum()
    }

    fun checkIfMAS(subGrid: Array<Array<Char>>): Boolean {
        // Top left to bottom right
        val firstWord = "${subGrid[0][0]}${subGrid[1][1]}${subGrid[2][2]}"
        val secondWord = "${subGrid[0][2]}${subGrid[1][1]}${subGrid[2][0]}"

        if(firstWord != "MAS" && firstWord != "SAM") return false
        return firstWord == secondWord.reversed() || firstWord == secondWord
    }

    fun part2(input: List<String>): Int {
        val grid = getGrid(input)

        var total = 0

        for(rowIndex in 0 .. grid.size - 3) {
            for(colIndex in 0 .. grid[rowIndex].size - 3) {
                val subGrid = arrayOf(
                    grid[rowIndex].slice(colIndex ..colIndex + 2).toTypedArray(),
                    grid[rowIndex + 1].slice(colIndex ..colIndex + 2).toTypedArray(),
                    grid[rowIndex + 2].slice(colIndex ..colIndex + 2).toTypedArray()
                )
                total += if(checkIfMAS(subGrid)) 1 else 0
            }
        }

        return total
    }

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
