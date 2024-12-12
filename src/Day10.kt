fun main() {

    fun getTrail(input: List<String>): Array<IntArray> = input.map{it.map{if(it == '.') -1 else it.digitToInt()}.toIntArray()}.toTypedArray()

    fun getPointsEdges(point: Point, grid: Array<IntArray>): List<Point> {
        val MAX_ROW = grid.size
        val MAX_COL = grid[0].size

        return listOf(
            Point(point.row -1, point.col), // Up
            Point(point.row + 1, point.col), // Down
            Point(point.row, point.col + 1), // Right
            Point(point.row, point.col -1) // Left
        ).filter{it ->
            it.row in 0..< MAX_ROW && it.col in 0..< MAX_COL &&
            grid[point.row][point.col] == (grid[it.row][it.col] - 1)
        }
    }

    fun getTrailHeadScore(point: Point, grid: Array<IntArray>): Int {
        val dfsStack = mutableListOf<Point>()

        val hasVisited = mutableListOf<Point>()
        val trailEnds = mutableListOf<Point>()

        dfsStack.add(point)
        while(dfsStack.isNotEmpty()) {
            val current = dfsStack.removeLast()
            if(grid[current.row][current.col] == 9) {
                trailEnds.add(current)
            }
            if(!hasVisited.contains(current)) {
                hasVisited.add(current)
                val edges = getPointsEdges(current, grid)
                dfsStack.addAll(edges)
            }

        }
        return trailEnds.toSet().size
    }

    fun getTrailHeadRating(point: Point, grid: Array<IntArray>): Int {
        val dfsStack = mutableListOf<Point>()

        val trailPaths = mutableListOf<MutableList<Point>>()

        dfsStack.add(point)
        trailPaths.add(mutableListOf(point))
        while(dfsStack.isNotEmpty()) {
            val current = dfsStack.removeLast()
            val allCurrentTrailPaths = trailPaths.filter{it.last() == current}

            val edges = getPointsEdges(current, grid)
            dfsStack.addAll(edges)
            for(edge in edges) {
                val newPaths = allCurrentTrailPaths.map{path ->
                    val newPath = path.toMutableList()
                    newPath.add(edge)
                    newPath
                }
                trailPaths.removeAll(allCurrentTrailPaths)
                trailPaths.addAll(newPaths)
            }

        }

        val trailEnds = trailPaths.filter{path ->
            val last = path.last()
            grid[last.row][last.col] == 9
        }

        return trailEnds.toSet().size
    }

    fun part1(input: List<String>): Int {
        val trail = getTrail(input)

        val totalTrailHeads = trail.mapIndexed{rowIndex, row ->
            row.mapIndexed{colIndex, v ->
                if(v == 0)
                    getTrailHeadScore(Point(rowIndex, colIndex), trail)
                else
                    0
            }.sum()
        }.sum()

        return totalTrailHeads
    }

    fun part2(input: List<String>): Int {
        val trail = getTrail(input)

        val totalTrailHeads = trail.mapIndexed{rowIndex, row ->
            row.mapIndexed{colIndex, v ->
                if(v == 0)
                    getTrailHeadRating(Point(rowIndex, colIndex), trail)
                else
                    0
            }.sum()
        }.sum()

        return totalTrailHeads
    }

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}
