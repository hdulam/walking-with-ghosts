package com.hdulam.walkingwithghosts.ui.ghostdex

import com.hdulam.walkingwithghosts.domain.model.Ghost

sealed interface GhostDexUiState {
    data object Loading : GhostDexUiState
    data object Error : GhostDexUiState
    data class Loaded(
        val ghosts: List<Ghost>,
        val featuredGhost: Ghost? = null,
        val isFeaturedUnavailable: Boolean = false
    ) : GhostDexUiState {
        val capturedCount: Int get() = ghosts.count { it.isCaptured }
    }
}
