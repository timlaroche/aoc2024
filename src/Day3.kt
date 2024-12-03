class Day3(isTest: Boolean) : Day(isTest) {
    fun part1(): Long {
        val lines = Helper.readAsLines(inputFile)
        var total = 0L
        var stack = ArrayDeque<Char>(emptyList())
        lines.forEach {
            val chars = it.toCharArray()
            var count = 0
            // Build up stack
            chars.forEach { c ->
                if (c.equals('m') && stack.isEmpty()) {
                    stack.addLast(c)
                    count++
                } else if (stack.isNotEmpty()) {
                    if (c.equals('u') && stack.last().equals('m')) {
                        stack.addLast(c)
                        count++
                    } else if (c.equals('l') && stack.last().equals('u')) {
                        stack.addLast(c)
                        count++
                    } else if (c.equals('(') && stack.last().equals('l')) {
                        stack.addLast(c)
                        count++
                    } else if (c.isDigit() && stack.last().equals('(')) {
                        stack.addLast(c)
                        count++
                    } else if (c.isDigit() && stack.last().isDigit()) {
                        stack.addLast(c)
                        count++
                    } else if (c.equals(',') && stack.last().isDigit()) {
                        stack.addLast(c)
                        count++
                    } else if (c.isDigit() && stack.last().equals(',')) {
                        stack.addLast(c)
                        count++
                    } else if (c.equals(')') && stack.last().isDigit()) {
                        stack.addLast(c)
                        count++
                        // retrieve and clean up
                        val operationStack = stack.joinToString("")
                        val operation = stack.joinToString("").split(",")
                        val a = operation[0].removePrefix("mul(").toInt()
                        val b = operation[1].removeSuffix(")").toInt()
                        val res = a * b
                        total += res
                        stack = ArrayDeque<Char>(emptyList())
                    } else {
                        stack = ArrayDeque<Char>(emptyList())
                    }
                }
            }
        }
        return total
    }

    fun part2(): Long {
        val lines = Helper.readAsLines(inputFile)
        var total = 0L
        var stack = ArrayDeque<Char>(emptyList())
        var doit = true
        lines.forEach {
            val chars = it.toCharArray()
            var count = 0 // don't think I need this but just in case
            // Build up stack - this time looking for do and don't
            chars.forEach { c ->
                if (c.equals('m') && stack.isEmpty()) {
                    stack.addLast(c)
                    count++
                }
                else if (c.equals('d') && stack.isEmpty()) {
                    stack.addLast(c)
                    count++
                } else if (stack.isNotEmpty()) {
                    // DO
                    if (c.equals('o') && stack.last().equals('d')) stack.addLast(c)
                    else if (c.equals('(') && stack.last().equals('o')) stack.addLast(c)

                    // DON'T
                    else if (c.equals('n') && stack.last().equals('o')) stack.addLast(c)
                    else if (c.equals('\'') && stack.last().equals('n')) stack.addLast(c)
                    else if (c.equals('t') && stack.last().equals('\'')) stack.addLast(c)
                    else if (c.equals('(') && stack.last().equals('t')) stack.addLast(c)
                    else if (c.equals(')') && stack.last().equals('(')) {
                        stack.addLast(c)
                        val operationStack = stack.joinToString("")
                        if(operationStack.equals("don't()")) doit = false
                        else if(operationStack.equals("do()")) doit = true
                        stack = ArrayDeque<Char>(emptyList())
                    }

                    // MUL
                    else if (c.equals('u') && stack.last().equals('m')) {
                        stack.addLast(c)
                        count++
                    } else if (c.equals('l') && stack.last().equals('u')) {
                        stack.addLast(c)
                        count++
                    } else if (c.equals('(') && stack.last().equals('l')) {
                        stack.addLast(c)
                        count++
                    } else if (c.isDigit() && stack.last().equals('(')) {
                        stack.addLast(c)
                        count++
                    } else if (c.isDigit() && stack.last().isDigit()) {
                        stack.addLast(c)
                        count++
                    } else if (c.equals(',') && stack.last().isDigit()) {
                        stack.addLast(c)
                        count++
                    } else if (c.isDigit() && stack.last().equals(',')) {
                        stack.addLast(c)
                        count++
                    } else if (c.equals(')') && stack.last().isDigit()) {
                        stack.addLast(c)
                        count++
                        // retrieve and clean up
                        val operationStack = stack.joinToString("")
                        val operation = stack.joinToString("").split(",")
                        val a = operation[0].removePrefix("mul(").toInt()
                        val b = operation[1].removeSuffix(")").toInt()
                        val res = a * b
                        if (doit) total += res
                        stack = ArrayDeque<Char>(emptyList())
                    } else {
                        stack = ArrayDeque<Char>(emptyList())
                    }
                }
            }
        }
        return total
    }
}