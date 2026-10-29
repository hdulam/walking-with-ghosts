package com.hdulam.walkingwithghosts.data.repository

import com.hdulam.walkingwithghosts.domain.model.Ghost
import kotlinx.coroutines.flow.Flow

interface GhostRepository {
    /** Full catalog, each ghost marked as captured or not. */
    val ghostDex: Flow<List<Ghost>>

    /** Fails with a [GhostSearchException]. Must only be called after the permission is granted. */
    suspend fun findNearbyGhosts(): Result<List<Ghost>>

    suspend fun getGhost(ghostId: String): Ghost?

    /** Success holds true when the ghost was captured and false when it escaped. */
    suspend fun attemptCapture(ghostId: String): Result<Boolean>

    /** The rarest ghost in the player's collection; fails when the collection is empty. */
    suspend fun getFeaturedGhost(): Result<Ghost>
}
