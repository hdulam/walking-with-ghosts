package com.hdulam.walkingwithghosts.data.repository

import com.hdulam.walkingwithghosts.domain.model.GhostCatalog
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GhostSpawnerTest {

    private val spawner = GhostSpawner(GhostCatalog.ghosts)

    @Test
    fun sameCellAndWindow_giveSameGhosts() {
        val first = spawner.spawn(cellLatitude = 1443, cellLongitude = -8721, timeWindow = 42)
        val second = spawner.spawn(cellLatitude = 1443, cellLongitude = -8721, timeWindow = 42)
        assertEquals(first, second)
    }

    @Test
    fun spawn_returnsRequestedCountWithoutDuplicates() {
        val ghosts = spawner.spawn(cellLatitude = 0, cellLongitude = 0, timeWindow = 0, count = 3)
        assertEquals(3, ghosts.size)
        assertEquals(3, ghosts.map { it.id }.toSet().size)
    }

    @Test
    fun spawn_neverExceedsCatalogSize() {
        val ghosts = spawner.spawn(cellLatitude = 7, cellLongitude = 7, timeWindow = 7, count = 99)
        assertEquals(GhostCatalog.ghosts.size, ghosts.size)
        assertTrue(ghosts.containsAll(GhostCatalog.ghosts))
    }
}
