package day8

import Day
import Helper2025

class Day8(isTest: Boolean) : Day(isTest) {
    data class Coord(val x: Long, val y: Long, val z: Long)
    data class Connection(val from: Coord, val to: Coord, val distance: Double) {
        fun sorted(): Connection {
            return if (to.x < from.x) Connection(to, from, this.distance) else Connection(from, to, this.distance)
        }
    }
    data class DfsResult(val visited: List<Coord>)

    fun straightLineDistance(a: Coord, b: Coord): Double {
        val dx = (b.x - a.x).toDouble()
        val dy = (b.y - a.y).toDouble()
        val dz = (b.z - a.z).toDouble()
        val sum = (dx * dx) + (dy * dy) + (dz * dz)
        return Math.sqrt(sum)
    }

    fun dfs(start: Coord, graph: Map<Coord, List<Coord>>): DfsResult {
        val visited = mutableListOf<Coord>()
        val toVisit = mutableListOf<Coord>()
        toVisit.add(start)
        while (toVisit.isNotEmpty()) {
            // visit
            val pop = toVisit.removeAt(0)
            visited.add(pop)

            // visit each neighbour
            val neighbours = graph.get(pop)
            neighbours?.forEach { neighbour ->
                if (!visited.contains(neighbour)) toVisit.add(neighbour)
            }
        }
        return DfsResult(visited.toList())
    }

    fun part1() {
        val input = Helper2025.readAsLines(inputFile)
        val coords = input.map { it.split(",").map { it.toLong() } }.map { Coord(it.get(0), it.get(1), it.get(2)) }
        val circuitGraph = mutableMapOf<Coord, List<Coord>>()

        val shortest = shortestDistances(coords).map { it.sorted() }.distinct()
        shortest.forEach {
            if (circuitGraph.contains(it.to)) return@forEach
            circuitGraph.merge(it.from, listOf(it.to)) { a, b -> a + b }
            circuitGraph.merge(it.to, listOf(it.from)) { a, b -> a + b }
            println()
        }

        val res = circuitGraph.map { it -> dfs(it.key, circuitGraph) }
        println()
    }

    fun shortestDistances(coords: List<Coord>): List<Connection> {
        // Find shortest distance the 3D space
        val possibleEdges = mutableListOf<Connection>()
        coords.forEach { c1 ->
            coords.forEach { c2 ->
                if (c1 != c2) possibleEdges.add(Connection(c1, c2, straightLineDistance(c1, c2)))
            }
        }
        return possibleEdges.sortedBy { it.distance }
    }

    fun part2() {

    }
}