package com.hdulam.walkingwithghosts.data.repository

import com.hdulam.walkingwithghosts.domain.model.Ghost
import kotlin.random.Random

/**
 * Picks fictional ghosts for a map cell. The same cell and time window always give the same
 * ghosts, so two players in the same zone see the same encounters.
 */
class GhostSpawner(private val catalog: List<Ghost>) {

    fun spawn(cellLatitude: Int, cellLongitude: Int, timeWindow: Long, count: Int = DEFAULT_COUNT): List<Ghost> {
        val seed = 31 * (31 * cellLatitude + cellLongitude) + timeWindow.hashCode()
        val random = Random(seed)
        val remaining = catalog.toMutableList()
        val spawned = mutableListOf<Ghost>()
        while (spawned.size < count && remaining.isNotEmpty()) {
            val totalWeight = remaining.sumOf { it.rarity.spawnWeight }
            var roll = random.nextInt(totalWeight)
            val picked = remaining.first { ghost ->
                roll -= ghost.rarity.spawnWeight
                roll < 0
            }
            spawned += picked
            remaining -= picked
        }
        return spawned
    }

    private companion object {
        const val DEFAULT_COUNT = 3
    }
}
