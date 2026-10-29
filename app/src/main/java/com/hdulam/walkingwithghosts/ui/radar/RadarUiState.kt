package com.hdulam.walkingwithghosts.ui.radar

import com.hdulam.walkingwithghosts.domain.model.Ghost

sealed interface RadarUiState {
    data object Idle : RadarUiState
    data object PermissionRationale : RadarUiState
    data object PermissionDenied : RadarUiState
    data object PermissionPermanentlyDenied : RadarUiState
    data object Searching : RadarUiState
    data class GhostsFound(val ghosts: List<Ghost>) : RadarUiState
    data object LocationDisabled : RadarUiState
    data object LocationUnavailable : RadarUiState
    data object Error : RadarUiState
}
