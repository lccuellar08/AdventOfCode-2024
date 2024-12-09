import kotlin.math.abs

data class Point(val row: Int, val col: Int)
data class Antenna(val point: Point, val name: Char)

fun main() {

    fun getGroupsOfAntennas(input: List<String>): Map<Char, List<Antenna>> {
        val antennaMap = mutableMapOf<Char, MutableList<Antenna>>()

        input.forEachIndexed { rowIndex, row ->
            row.forEachIndexed{colIndex, c ->
                if(c != '.') {
                    antennaMap.computeIfAbsent(c) {mutableListOf()}.add(Antenna(Point(rowIndex, colIndex), c))
                }
            }
        }

        return antennaMap
    }

    fun getAntinodes(antennas: List<Antenna>, maxRow: Int, maxCol: Int): Set<Point> {
        val antinodes = mutableListOf<Point>()
        val combinations = mutableListOf<Pair<Antenna, Antenna>>()

        for (i in antennas.indices) {
            for (j in i + 1 until antennas.size) {
                combinations.add(Pair(antennas[i], antennas[j]))
            }
        }

        combinations.forEach {(antenna1, antenna2) ->
            // Get distance
            val dRow = abs(antenna2.point.row - antenna1.point.row)
            val dCol = abs(antenna2.point.col - antenna1.point.col)

            val leftMost = if(antenna1.point.col < antenna2.point.col) antenna1 else antenna2
            val rightMost = if(leftMost == antenna1) antenna2 else antenna1

            val leftIsLower = leftMost.point.row < rightMost.point.row

            val leftAntinode = Point(
                leftMost.point.row + (if(leftIsLower) -1 else 1) * dRow,
                leftMost.point.col - dCol
            )
            val rightAntinode = Point(
                rightMost.point.row + (if(leftIsLower) 1 else -1) * dRow,
                rightMost.point.col + dCol
            )

            antinodes.add(leftAntinode)
            antinodes.add(rightAntinode)
        }


        return antinodes.filter{ it.row in 0..<maxRow && it.col in 0..<maxCol}.toSet()
    }

    fun getResonantAntinodes(antennas: List<Antenna>, maxRow: Int, maxCol: Int): Set<Point> {
        val antinodes = mutableListOf<Point>()
        val combinations = mutableListOf<Pair<Antenna, Antenna>>()

        for (i in antennas.indices) {
            for (j in i + 1 until antennas.size) {
                combinations.add(Pair(antennas[i], antennas[j]))
            }
        }

        combinations.forEach {(antenna1, antenna2) ->
            // Get distance
            val dRow = abs(antenna2.point.row - antenna1.point.row)
            val dCol = abs(antenna2.point.col - antenna1.point.col)

            val leftMost = if(antenna1.point.col < antenna2.point.col) antenna1 else antenna2
            val rightMost = if(leftMost == antenna1) antenna2 else antenna1

            val leftIsLower = leftMost.point.row < rightMost.point.row

            // left antinodes
            var leftAntinodeCount = 1
            var newLeftAntinode = Point(
                leftMost.point.row + (if(leftIsLower) -leftAntinodeCount else leftAntinodeCount) * dRow,
                leftMost.point.col - dCol
            )
            do {
                antinodes.add(newLeftAntinode)
                leftAntinodeCount++

                newLeftAntinode = Point(
                    leftMost.point.row + (if(leftIsLower) -leftAntinodeCount else leftAntinodeCount) * dRow,
                    leftMost.point.col - dCol * leftAntinodeCount
                )

            } while(newLeftAntinode.row in 0..<maxRow && newLeftAntinode.col in 0..<maxCol)

            // Right antinodes
            var rightAntinodeCount = 1
            var newRightAntinode = Point(
                rightMost.point.row + (if(leftIsLower) rightAntinodeCount else -rightAntinodeCount) * dRow,
                rightMost.point.col + dCol
            )
            do {
                antinodes.add(newRightAntinode)
                rightAntinodeCount++

                newRightAntinode = Point(
                    rightMost.point.row + (if(leftIsLower) rightAntinodeCount else -rightAntinodeCount) * dRow,
                    rightMost.point.col + dCol * rightAntinodeCount
                )

            } while(newRightAntinode.row in 0..<maxRow && newRightAntinode.col in 0..<maxCol)

        }

        antinodes.addAll(antennas.map{it.point})

        return antinodes.filter{ it.row in 0..<maxRow && it.col in 0..<maxCol}.toSet()
    }

    fun part1(input: List<String>): Int {

        val antennaGroups = getGroupsOfAntennas(input)

        val allAntinodes = antennaGroups.map {(_, antennas) ->
            getAntinodes(antennas, input.size, input[0].length)
        }.flatten().toSet()

        return allAntinodes.size
    }

    fun part2(input: List<String>): Int {
        val antennaGroups = getGroupsOfAntennas(input)

        val allAntinodes = antennaGroups.map {(_, antennas) ->
            getResonantAntinodes(antennas, input.size, input[0].length)
        }.flatten().toSet()

        input.forEachIndexed{rowIndex, row ->
            row.forEachIndexed{colIndex, c ->
                if(c != '.') {
                    print(c)
                } else {
                    if(allAntinodes.contains(Point(rowIndex, colIndex))) {
                       print('#')
                    }
                    else {
                        print('.')
                    }
                }
            }
            println()
        }

        return allAntinodes.size
    }

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}
