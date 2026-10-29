package com.hdulam.walkingwithghosts.ui.ghostdex

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hdulam.walkingwithghosts.crash.CrashReporter
import com.hdulam.walkingwithghosts.data.repository.GhostRepository
import com.hdulam.walkingwithghosts.domain.model.Ghost
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GhostDexViewModel(
    private val repository: GhostRepository,
    private val crashReporter: CrashReporter
) : ViewModel() {

    private val featuredGhost = MutableStateFlow<Result<Ghost>?>(null)

    val uiState: StateFlow<GhostDexUiState> =
        combine(repository.ghostDex, featuredGhost) { ghosts, featured ->
            GhostDexUiState.Loaded(
                ghosts = ghosts,
                featuredGhost = featured?.getOrNull(),
                isFeaturedUnavailable = featured?.isFailure == true
            ) as GhostDexUiState
        }
            .catch { emit(GhostDexUiState.Error) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS), GhostDexUiState.Loading)

    fun onFeaturedGhostClicked() {
        crashReporter.setCustomKey(KEY_SCREEN_NAME, SCREEN_NAME)
        viewModelScope.launch {
            featuredGhost.value = repository.getFeaturedGhost()
        }
    }

    private companion object {
        const val STOP_TIMEOUT_MS = 5_000L
        const val KEY_SCREEN_NAME = "screen_name"
        const val SCREEN_NAME = "ghostdex"
    }
}
