
fun main() {

    fun getBlockString(diskMap: String): String {
        var blockString = ""
        var currentID = 0

        diskMap.forEachIndexed{index, c ->
            if(index % 2 == 0) {
                // File Length
                blockString += Array<String>(c.digitToInt()) {"$currentID"}.joinToString("")
                currentID++
            } else {
                // Free space
                blockString += Array<String>(c.digitToInt()) {"."}.joinToString("")
            }
        }

        return blockString
    }

    fun getSizes(diskMap: String): Pair<Int, Int> {
        return diskMap.mapIndexed{index, c ->
            if(index % 2 == 0) {
                // File Length
                Pair(c.digitToInt(),0)
            } else {
                // Free space
                Pair(0,c.digitToInt())
            }
        }.fold(Pair(0,0)) {acc, pair ->
            Pair(acc.first + pair.first, acc.second + pair.second)
        }
    }

    fun getCheckSum(diskMap: String): Long {
        val (totalBlocks, totalEmpty) = getSizes(diskMap)


        var leftIndex = 0
        var rightIndex = if(diskMap.length % 2 == 0) diskMap.length - 2 else diskMap.length - 1

        var checkSum = 0L
        var leftID = 0
        var rightID = diskMap.length / 2
        var fillBuffer = diskMap[rightIndex].digitToInt()

        var currPos = 0

        while(leftIndex < diskMap.length) {
            val currNum = diskMap[leftIndex].digitToInt()
            if(rightIndex >= leftIndex) {
//                println("Curr Num: $currNum; left index: $leftIndex; right index: $rightIndex; check sum: $checkSum")
                if (leftIndex % 2 == 0) {
                    if(rightIndex == leftIndex) {
                        for (i in 0..<fillBuffer) {
//                            println("L: Adding $checkSum + $currPos * $leftID")
                            checkSum += currPos * leftID
                            currPos++
                        }
                    } else {
                        for (i in 0..<currNum) {
//                            println("L: Adding $checkSum + $currPos * $leftID")
                            checkSum += currPos * leftID
                            currPos++
                        }
                    }
                    leftID++
                } else {
                    if (fillBuffer > 0) {
                        for (i in 0..<currNum) {
//                            println("R: Adding $checkSum + $currPos * $rightID")
                            checkSum += currPos * rightID
                            currPos++

                            fillBuffer--
                            if (fillBuffer == 0) {
                                rightIndex -= 2
                                rightID--
                                if (rightIndex > leftIndex) {
                                    fillBuffer = diskMap[rightIndex].digitToInt()
                                } else {
                                    break
                                }
                            }
                        }
                    }
                }
            }
            leftIndex++
        }

        return checkSum
    }

    fun getCorrectedCheckSum(diskMap: String): Long {
        val (totalBlocks, totalEmpty) = getSizes(diskMap)

        var leftIndex = 0
        var rightIndex = if(diskMap.length % 2 == 0) diskMap.length - 2 else diskMap.length - 1

        var leftID = 0
        var checkSum = 0L
        var currPos = 0

        // 2333133121414131402
        // listOf<CurrPos, Size>
        val emptySpaces = mutableListOf<Pair<Int, Int>>()

        // listOf<CurrPos, ID>
        val occupiedSpaces = mutableListOf<Pair<Int, Int>>()

        while(leftIndex < diskMap.length) {
            val currNum = diskMap[leftIndex].digitToInt()
            if (leftIndex % 2 == 0) {
                occupiedSpaces.add(Pair(currPos, leftID))
                currPos += currNum
                leftID++
            } else {
                emptySpaces.add(Pair(currPos, currNum))
                currPos += currNum
            }
            leftIndex++
        }

        // Just used for debugging
        val outputArray = Array<String>(totalBlocks + totalEmpty) {"."}

        while(rightIndex >= 0) {
            val rightSize = diskMap[rightIndex].digitToInt()
            val (rightPos, rightID) = occupiedSpaces.removeLast()

            val emptySpaceToRemove: Pair<Int,Int>? = emptySpaces.find{(pos, size) ->
                size >= rightSize && pos < rightPos
            }

            if(emptySpaceToRemove !== null) {
                for(i in emptySpaceToRemove.first ..< emptySpaceToRemove.first + rightSize) {
//                    println("Empty Space: adding $checkSum + $i * $rightID")
                    outputArray[i] = "$rightID"
                    checkSum += i * rightID
                }

                if(emptySpaceToRemove.second > rightSize) {
                    val emptySpaceIndex = emptySpaces.indexOf(emptySpaceToRemove)
                    emptySpaces[emptySpaceIndex] = Pair(emptySpaceToRemove.first + rightSize, emptySpaceToRemove.second - rightSize)
                } else {
                    emptySpaces.remove(emptySpaceToRemove)
                }
            } else {
                for(i in rightPos..< rightPos + rightSize) {
//                    println("Normal Space: adding $checkSum + $i * $rightID")
                    outputArray[i] = "$rightID"
                    checkSum += i * rightID
                }
            }
            rightIndex -= 2
        }

        return checkSum
    }

    fun part1(input: List<String>): Long {
        return getCheckSum(input[0])
    }

    fun part2(input: List<String>): Long {

        return getCorrectedCheckSum(input[0])
    }

    val input = readInput("Day09")

    println(part1(input))
    println(part2(input))
}
