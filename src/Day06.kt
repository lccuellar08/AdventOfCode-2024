import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

typealias Grid = Array<BooleanArray>
typealias Position = Pair<Int, Int>

enum class Direction {
    UP, DOWN, LEFT, RIGHT
}
fun main() {


    fun getGridAndStartPos(input: List<String>): Pair<Grid, Position> {
        val grid = Array(input.size) {
            BooleanArray(input[0].length) {false}
        }

        var posRow = 0
        var posCol = 0

        input.forEachIndexed{rowIndex, row ->
            row.forEachIndexed{colIndex, c ->
                grid[rowIndex][colIndex] = c == '#'
                if(c == '^') {
                    posRow = rowIndex
                    posCol = colIndex
                }
            }
        }

        return Pair(grid, Pair(posRow, posCol))
    }

    fun getTransversalLength(grid: Grid, position: Position): Int {
        val visitedPositions = mutableSetOf<Position>()

        var dY = -1
        var dX = 0

        var oldPosition = position
        var newPosition = Pair(position.first + dY, position.second + dX)

        do {

//            println("Position $oldPosition; forward: $newPosition, dY: $dY; dX: $dX")
            if(grid[newPosition.first][newPosition.second]) {
//                println("Turning")
                if(dY != 0) {
                    dX = dY * -1
                    dY = 0
                } else {
                    dY = dX * 1
                    dX = 0
                }
                newPosition = Pair(oldPosition.first + dY, oldPosition.second + dX)
            } else {
                visitedPositions.add(oldPosition)
                newPosition = Pair(newPosition.first + dY, newPosition.second + dX)
                oldPosition = Pair(oldPosition.first + dY, oldPosition.second + dX)
            }

        } while(newPosition.first >= 0 && newPosition.first < grid.size &&
            newPosition.second >= 0 && newPosition.second < grid[0].size)

        return visitedPositions.size + 1
    }

    fun getDirection(dY: Int, dX: Int): Direction {
        if(dY == -1) return Direction.UP
        if(dY == 1) return Direction.DOWN
        if(dX == 1) return Direction.RIGHT
        return Direction.LEFT
    }

    fun detectCycle(grid: Grid, position: Position, startDY: Int, startDX: Int, oldVisitedPositionsAndDirections: MutableSet<
            Pair<Position, Direction>>, newObstacle: Position): Position? {
        val cycles = listOf(
            Pair(6, 3), Pair(7, 6), Pair(8, 3), Pair(8, 1), Pair(7, 7), Pair(9, 7)
        )
        val doPrint = false
//        if(doPrint) {println("Hypothetical for $newObstacle")}

        var dY = startDY
        var dX = startDX
        val visitedPositionsAndDirections = oldVisitedPositionsAndDirections.toMutableSet()

        var oldPosition = position
        var newPosition = Pair(position.first + dY, position.second + dX)

        while(newPosition.first >= 0 && newPosition.first < grid.size &&
            newPosition.second >= 0 && newPosition.second < grid[0].size) {

//            if(doPrint) {println("\t\tPosition $oldPosition; forward: $newPosition, ${getDirection(dY, dX)}")}
            if(grid[newPosition.first][newPosition.second] || newPosition == newObstacle) {
//                if(doPrint) {println("\t\tTurning")}
                if(dY != 0) {
                    dX = dY * -1
                    dY = 0
                } else {
                    dY = dX * 1
                    dX = 0
                }
                newPosition = Pair(oldPosition.first + dY, oldPosition.second + dX)
            } else {
                oldPosition = Pair(oldPosition.first + dY, oldPosition.second + dX)
                if(visitedPositionsAndDirections.contains(Pair(oldPosition, getDirection(dY, dX)))) {
//                    if(doPrint) {println("\t\tHave visited $newPosition while moving ${getDirection(dY, dX)}")}
//                    if(doPrint) {println("Obstacle in $newObstacle")}
                    return newObstacle
                }

                visitedPositionsAndDirections.add(Pair(oldPosition, getDirection(dY, dX)))
                newPosition = Pair(newPosition.first + dY, newPosition.second + dX)
            }
        }

        return null
    }

    fun getNumOfCycles(grid: Grid, position: Position): Int {

        val allCycles = mutableListOf<Position>()

        val dY = -1
        val dX = 0

        grid.forEachIndexed { rowIndex, row ->
            row.forEachIndexed{ colIndex, c ->
                if(!c && Pair(rowIndex, colIndex) !== position) {
                    val cycle = detectCycle(grid, position, dY, dX, mutableSetOf(), Pair(rowIndex, colIndex))
                    if (cycle != null) {
                        allCycles.add(cycle)
                    }
                }
            }
        }

        return allCycles.distinct().size
    }


    fun part1(input: List<String>): Int {
        val (grid, position) = getGridAndStartPos(input)

        println(position)
        val traversalLength = getTransversalLength(grid, position)

        return traversalLength
    }

    fun part2(input: List<String>): Int {
        val (grid, position) = getGridAndStartPos(input)

//        println(position)
        val cycles = getNumOfCycles(grid, position)

        return cycles
    }

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}
