import kotlin.math.abs

fun main() {
    fun getReports(input: List<String>) = input.map{it.split(" ").map{itt -> itt.toInt()}}

    fun isValidReport(report: List<Int>): Boolean {
        val direction = if(report[0] > report[1]) -1 else 1
        var isValid = 1
        report.windowed(2).forEach {window ->
            val newDirection = if(window[0] > window[1]) -1 else 1
            val difference = abs(window[0] - window[1])
            if(direction != newDirection || (difference < 1 || difference > 3)) {
                // Not descending or ascending in same direction
                isValid = 0
            }
        }
        return isValid == 1
    }

    fun reportIsFixable(report: List<Int>): Int {
        val direction = if(report[0] > report[1]) -1 else 1
        report.windowed(2).forEachIndexed {windowIndex, window ->
            val newDirection = if(window[0] > window[1]) -1 else 1
            val difference = abs(window[0] - window[1])
            if(direction != newDirection || (difference < 1 || difference > 3)) {
                // Not descending or ascending in same direction
                val reportWithoutFirst = report.filterIndexed {index, _ -> index != windowIndex}
                val reportWithoutSecond = report.filterIndexed {index, _ -> index != windowIndex + 1}
                val reportWithoutFirstElement = report.drop(1)

                if(isValidReport(reportWithoutFirst) || isValidReport(reportWithoutSecond) || isValidReport(reportWithoutFirstElement)) {
                    return 1
                } else {
                    return 0
                }
            }
        }
        return 1
    }

    fun part1(input: List<String>): Int {
        val reports = getReports(input)

        val validReports = reports.map { isValidReport(it) }.count { it }
        return validReports
    }

    fun part2(input: List<String>): Int {
        val reports = getReports(input)

        val validReports = reports.sumOf { reportIsFixable(it) }
        return validReports
    }

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
