package com.hdulam.walkingwithghosts.ui.capture

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hdulam.walkingwithghosts.crash.CrashReporter
import com.hdulam.walkingwithghosts.data.repository.GhostRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CaptureViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: GhostRepository,
    private val crashReporter: CrashReporter
) : ViewModel() {

    private val ghostId: String = savedStateHandle.get<String>(GHOST_ID_ARG).orEmpty()

    private val _uiState = MutableStateFlow<CaptureUiState>(CaptureUiState.Loading)
    val uiState: StateFlow<CaptureUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val ghost = repository.getGhost(ghostId)
            _uiState.value = if (ghost == null) CaptureUiState.NotFound else CaptureUiState.Ready(ghost)
        }
    }

    fun onCaptureClicked() {
        val current = _uiState.value as? CaptureUiState.Ready ?: return
        if (current.phase == CapturePhase.CAPTURING || current.phase == CapturePhase.CAUGHT) return
        crashReporter.setCustomKey(KEY_SCREEN_NAME, SCREEN_NAME)
        _uiState.value = current.copy(phase = CapturePhase.CAPTURING)
        viewModelScope.launch {
            delay(CAPTURE_ANIMATION_MS)
            val phase = repository.attemptCapture(ghostId).fold(
                onSuccess = { captured -> if (captured) CapturePhase.CAUGHT else CapturePhase.ESCAPED },
                onFailure = { CapturePhase.FAILED }
            )
            val ghost = if (phase == CapturePhase.CAUGHT) current.ghost.copy(isCaptured = true) else current.ghost
            _uiState.value = CaptureUiState.Ready(ghost, phase)
        }
    }

    companion object {
        const val GHOST_ID_ARG = "ghostId"
        private const val CAPTURE_ANIMATION_MS = 1_200L
        private const val KEY_SCREEN_NAME = "screen_name"
        private const val SCREEN_NAME = "capture"
    }
}
