package com.hdulam.walkingwithghosts.ui.capture

import com.hdulam.walkingwithghosts.domain.model.Ghost

sealed interface CaptureUiState {
    data object Loading : CaptureUiState
    data object NotFound : CaptureUiState
    data class Ready(val ghost: Ghost, val phase: CapturePhase = CapturePhase.READY) : CaptureUiState
}
