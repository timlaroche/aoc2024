package day9

import Day
import day8.Day8

class Day9(isTest: Boolean) : Day(isTest) {
    data class Point(val x: Long, val y: Long)
    fun part1() {
        val input = Helper2025.readAsLines(inputFile)
        val points = input.map {
            val split = it.split(",")
            Point(split[0].toLong(), split[1].toLong())
        }

        val areas = mutableListOf<Long>()
        points.forEach { p1 ->
            points.forEach { p2 ->
                areas.add(area(p1, p2))
            }
        }

        val res = areas.max()
        println(res)
    }

    fun area(p1: Point, p2: Point): Long {
        val dx = Math.abs(p2.x - p1.x) + 1
        val dy = Math.abs(p2.y - p1.y) + 1
        return dx * dy
    }

    fun part2() {
        // Work out green spots
        val input = Helper2025.readAsLines(inputFile)]
        val points = input.map {
            val split = it.split(",")
            Point(split[0].toLong(), split[1].toLong())
        }
        // Scan horizontally
        // For each row, if there's two points, we have a line between them
        //
    }
}