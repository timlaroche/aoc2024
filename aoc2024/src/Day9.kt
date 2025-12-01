class Day9(isTest: Boolean) : Day(isTest) {
//    fun part1(): Long {
//        // Hashmap for id to block location
//        var isFile = true
//        val fileSize = .Helper.readAsLines(inputFile).get(0).length
//        var fileId = 0
//        var blockId = 0
//        val fileMap = mutableMapOf<Int, MutableList<Int>>()
//        .Helper.readAsLines(inputFile).get(0).forEach {
//            val size = it.toString().toInt()
//            val blockIds = mutableListOf<Int>()
//            if (isFile) {
//                for (i in 0 until  size) {
//                    blockIds.add(blockId)
//                    blockId += 1
//                }
//                fileMap.put(fileId, blockIds)
//                fileId += 1
//                isFile = !isFile
//            } else {
//                for (i in 0 until  size) {
//                    blockIds.add(blockId)
//                    blockId += 1
//                }
//                fileMap.compute(-1, { k, v ->
//                    val currList = fileMap[k] ?: mutableListOf()
//                    currList.addAll(blockIds)
//                    currList
//                })
//                isFile = !isFile
//            }
//        }
//
//        // Remap
//        var freeSpaces = fileMap.get(-1) ?: mutableListOf()
//        val filesInReverseOrder = fileMap.keys.sorted().reversed().dropLast(1)
//        filesInReverseOrder.forEach {
//            if (freeSpaces.isNotEmpty()) {
//                val fileBlockIds = fileMap[it] ?: mutableListOf() // The ids that the block used to be
//                val remappedFileBlockIds = freeSpaces.take(fileBlockIds.size) // The ids that they will be now
//                val finalRemap = fileBlockIds.zip(remappedFileBlockIds).map { min(it.first, it.second) }
//                fileMap[it] = finalRemap.toMutableList()
//                freeSpaces = freeSpaces.drop(fileBlockIds.size).toMutableList()
//            }
//        }
//
//        var sum = 0
//        fileMap.put(-1, mutableListOf()) // Lol
//        for (i in 0..fileSize*2) {
//            val id = try {fileMap.filterValues { it.contains(i) }.keys.first()} catch (Exception: NoSuchElementException) { -1 }
//            val location = i
//            sum += id * location
//        }
//
//        return sum.toLong()
//    }

    fun part1(): Long {
        val input = Helper.readAsLines(inputFile).get(0)
        var fileId = 0
        var forward = 0
        var forwardFile = true

        var size = 0
        input.forEach { size += it.toString().toInt() }
        val fileBlocks = Array(size) { -1 }

        input.forEach {
            val size = it.toString().toInt()
            if (forwardFile) {
                for (i in 0 until size) {
                    fileBlocks[forward] = fileId
                    forward += 1
                }
                fileId += 1
                forwardFile = !forwardFile
            } else {
                for (i in 0 until size) {
                    forward += 1
                }
                forwardFile = !forwardFile
            }
        }

        for (i in fileBlocks.size - 1 downTo 0) {
            val firstEmpty = fileBlocks.indexOfFirst { it == -1 }
            if (firstEmpty == -1) {
                break
            }
            val lastNonEmpty = fileBlocks.indexOfLast { it != -1 }
            if (fileBlocks[i] == -1 || firstEmpty - 1 == lastNonEmpty) {
            } else {
                fileBlocks[firstEmpty] = fileBlocks[i]
                fileBlocks[i] = -1
            }
        }

        var sum = 0L
        val firstEmpty = fileBlocks.indexOfFirst { it == -1 }
        if (firstEmpty == -1) {
            fileBlocks.forEachIndexed { idx, value ->
                if (value != -1) sum += (value * idx)
            }
        } else {
            val x = fileBlocks.take(firstEmpty)
            x.forEachIndexed { idx, value ->
                if (value != -1) sum += (value * idx)
            }
        }
        return sum //1401820230 too low 1019650424 too low <--- omfg this was integer overflow / max ; long immediately fixed it ffs
    }

    fun part2(): Long {
        val input = Helper.readAsLines(inputFile).get(0)
        var fileId = 0
        var forward = 0
        var forwardFile = true
        val fileSizes = mutableMapOf<Int, Int>()

        var size = 0
        input.forEach { size += it.toString().toInt() }
        val fileBlocks = Array(size) { -1 }

        input.forEach {
            val size = it.toString().toInt()
            if (forwardFile) {
                for (i in 0 until size) {
                    fileBlocks[forward] = fileId
                    forward += 1
                }
                fileSizes.put(fileId, size)
                fileId += 1
                forwardFile = !forwardFile
            } else {
                for (i in 0 until size) {
                    forward += 1
                }
                forwardFile = !forwardFile
            }
        }

        // all empty blocks
        fun emptyBlocks(): MutableMap<Int, Int> {
            var empty = false
            var starting = 0
            var started = false
            val emptyMap = mutableMapOf<Int, Int>()
            for (i in 0 until fileBlocks.size) {
                if (fileBlocks[i] == -1 && !started) {
                    starting = i
                    emptyMap.put(starting, 1)
                    started = true
                } else if (fileBlocks[i] == -1 && started) {
                    emptyMap.compute(starting, { k, v -> v?.plus(1) ?: 1 })
                } else if (fileBlocks[i] != -1 && started) {
                    started = false
                }
            }
            return emptyMap
        }

        var emptyMap = emptyBlocks()
        // Rearranging
        val hasTriedFile = mutableSetOf<Int>()
        for (i in fileBlocks.size - 1 downTo 0) {
            val fileSize = fileSizes.get(fileBlocks[i]) ?: 0
            // Check contiguous space
            val fileId = fileBlocks[i]
            if (fileId == -1) continue
            val contiguousBlocksThatFit = emptyMap.filterValues { it >= fileSize }.keys
            if (contiguousBlocksThatFit.isNotEmpty() && !hasTriedFile.contains(fileId)) {
                val contiguous = contiguousBlocksThatFit.first()
                if(contiguous < i){
                    for (j in 0 until fileSize) {
                        fileBlocks[contiguous + j] = fileId
                        fileBlocks[i - j] = -1
                    }
                    emptyMap.remove(contiguous)
                    emptyMap = emptyBlocks()
                }
            }
            hasTriedFile.add(fileId)
        }


        // Summing
        var sum = 0L
        fileBlocks.forEachIndexed { index, i -> if(i != -1) sum += (i * index) }
        return sum
    }

}