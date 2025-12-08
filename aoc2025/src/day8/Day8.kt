package day8

import Day

class Day8(isTest: Boolean) : Day(isTest) {
    data class Coord(val x: Long, val y: Long, val z: Long)
    data class Connection(val from: Coord, val to: Coord, val distance: Double) {
        fun sorted(): Connection {
            return if (to.x < from.x) Connection(to, from, this.distance) else Connection(from, to, this.distance)
        }
    }

    fun part1() {
        val input = Helper2025.readAsLines(inputFile)
        val coords = input.map { it.split(",").map { it.toLong() } }.map { Coord(it.get(0), it.get(1), it.get(2)) }

        // Build some kind of graph
        val circuits = mutableMapOf<Coord, List<Coord>>()
        val possibleConnections = mutableListOf<Connection>()
        coords.forEach { c1 ->
            coords.forEach { c2 ->
                possibleConnections.add(Connection(c1, c2, straightLineDistance(c1, c2)))
            }
        }
    }

    fun straightLineDistance(a: Coord, b: Coord): Double {
        val dx = (b.x - a.x).toDouble()
        val dy = (b.y - a.y).toDouble()
        val dz = (b.z - a.z).toDouble()
        val sum = (dx * dx) + (dy * dy) + (dz * dz)
        return Math.sqrt(sum)
    }

    fun part2() {

    }
}