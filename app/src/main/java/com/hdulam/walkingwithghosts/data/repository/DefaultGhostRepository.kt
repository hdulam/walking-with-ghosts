package com.hdulam.walkingwithghosts.data.repository

import com.hdulam.walkingwithghosts.crash.CrashReporter
import com.hdulam.walkingwithghosts.data.local.GhostDexStore
import com.hdulam.walkingwithghosts.data.location.ApproximateLocation
import com.hdulam.walkingwithghosts.data.location.LocationDataSource
import com.hdulam.walkingwithghosts.domain.model.Ghost
import com.hdulam.walkingwithghosts.domain.model.GhostCatalog
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withTimeoutOrNull
import java.io.IOException
import kotlin.math.roundToInt
import kotlin.random.Random

class DefaultGhostRepository(
    private val locationDataSource: LocationDataSource,
    private val ghostDexStore: GhostDexStore,
    private val crashReporter: CrashReporter,
    private val spawner: GhostSpawner = GhostSpawner(GhostCatalog.ghosts),
    private val random: Random = Random.Default,
    private val clock: () -> Long = System::currentTimeMillis
) : GhostRepository {

    override val ghostDex: Flow<List<Ghost>> = ghostDexStore.capturedIds
        .map { capturedIds -> GhostCatalog.ghosts.map { it.copy(isCaptured = it.id in capturedIds) } }
        .catch { error ->
            if (error is IOException) crashReporter.recordException(error)
            throw error
        }

    override suspend fun findNearbyGhosts(): Result<List<Ghost>> {
        crashReporter.setCustomKey(KEY_OPERATION, "find_nearby_ghosts")
        crashReporter.log("Ghost search started")
        if (!locationDataSource.isLocationEnabled()) {
            crashReporter.log("Ghost search stopped: location disabled")
            return Result.failure(GhostSearchException.LocationDisabled())
        }
        val location = try {
            withTimeoutOrNull(LOCATION_TIMEOUT_MS) { locationDataSource.getCurrentApproximateLocation() }
        } catch (error: CancellationException) {
            throw error
        } catch (error: SecurityException) {
            crashReporter.recordException(error)
            return Result.failure(GhostSearchException.PermissionMissing(error))
        } catch (error: Exception) {
            crashReporter.recordException(error)
            return Result.failure(GhostSearchException.Unexpected(error))
        }
        if (location == null) {
            crashReporter.log("Ghost search stopped: no location available")
            return Result.failure(GhostSearchException.LocationUnavailable())
        }
        val capturedIds = ghostDexStore.capturedIds.first()
        val ghosts = spawnFor(location).map { it.copy(isCaptured = it.id in capturedIds) }
        crashReporter.setCustomKey(KEY_GHOST_COUNT, ghosts.size)
        crashReporter.log("Ghost search finished")
        return Result.success(ghosts)
    }

    override suspend fun getGhost(ghostId: String): Ghost? {
        val capturedIds = ghostDexStore.capturedIds.first()
        return GhostCatalog.findById(ghostId)?.let { it.copy(isCaptured = it.id in capturedIds) }
    }

    override suspend fun attemptCapture(ghostId: String): Result<Boolean> {
        crashReporter.setCustomKey(KEY_OPERATION, "attempt_capture")
        val ghost = GhostCatalog.findById(ghostId)
            ?: return Result.failure(IllegalArgumentException("Unknown ghost id: $ghostId"))
        val captured = random.nextFloat() < ghost.captureChance
        if (captured) {
            try {
                ghostDexStore.addCaptured(ghost.id)
            } catch (error: IOException) {
                crashReporter.recordException(error)
                return Result.failure(error)
            }
        }
        crashReporter.log("Capture attempt on ${ghost.id}: ${if (captured) "captured" else "escaped"}")
        return Result.success(captured)
    }

    override suspend fun getFeaturedGhost(): Result<Ghost> {
        crashReporter.setCustomKey(KEY_OPERATION, "featured_ghost")
        val capturedIds = ghostDexStore.capturedIds.first()
        crashReporter.setCustomKey(KEY_GHOST_COUNT, capturedIds.size)
        crashReporter.log("Loading featured ghost")
        return try {
            val featured = GhostCatalog.ghosts
                .filter { it.id in capturedIds }
                .sortedByDescending { it.rarity }
                .first()
            Result.success(featured.copy(isCaptured = true))
        } catch (error: NoSuchElementException) {
            // Fixed crash: an empty GhostDex is reported as non-fatal and returned as a safe failure.
            crashReporter.log("Featured ghost requested with an empty GhostDex")
            crashReporter.recordException(error)
            Result.failure(error)
        }
    }

    // Coordinates are reduced to a ~1 km cell and never leave this method.
    private fun spawnFor(location: ApproximateLocation): List<Ghost> = spawner.spawn(
        cellLatitude = (location.latitude * CELLS_PER_DEGREE).roundToInt(),
        cellLongitude = (location.longitude * CELLS_PER_DEGREE).roundToInt(),
        timeWindow = clock() / SPAWN_WINDOW_MS
    )

    private companion object {
        const val LOCATION_TIMEOUT_MS = 15_000L
        const val SPAWN_WINDOW_MS = 10 * 60 * 1000L
        const val CELLS_PER_DEGREE = 100
        const val KEY_OPERATION = "operation"
        const val KEY_GHOST_COUNT = "ghost_count"
    }
}
