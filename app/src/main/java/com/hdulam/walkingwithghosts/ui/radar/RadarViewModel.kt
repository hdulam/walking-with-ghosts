package com.hdulam.walkingwithghosts.ui.radar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hdulam.walkingwithghosts.crash.CrashReporter
import com.hdulam.walkingwithghosts.data.repository.GhostRepository
import com.hdulam.walkingwithghosts.data.repository.GhostSearchException
import com.hdulam.walkingwithghosts.permissions.PermissionResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RadarViewModel(
    private val repository: GhostRepository,
    private val crashReporter: CrashReporter
) : ViewModel() {

    private val _uiState = MutableStateFlow<RadarUiState>(RadarUiState.Idle)
    val uiState: StateFlow<RadarUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    fun onPermissionResult(result: PermissionResult) {
        crashReporter.log("Location permission result: ${result::class.simpleName}")
        when (result) {
            PermissionResult.Granted -> searchGhosts()
            PermissionResult.ShowRationale -> _uiState.value = RadarUiState.PermissionRationale
            PermissionResult.Denied -> _uiState.value = RadarUiState.PermissionDenied
            PermissionResult.PermanentlyDenied -> _uiState.value = RadarUiState.PermissionPermanentlyDenied
        }
    }

    /** The UI shows the system dialog right after calling this. */
    fun onRationaleAccepted() {
        _uiState.value = RadarUiState.Idle
    }

    fun onRationaleDismissed() {
        _uiState.value = RadarUiState.PermissionDenied
    }

    /** Called on every resume, e.g. when the user comes back from app settings. */
    fun onPermissionRechecked(isGranted: Boolean) {
        val state = _uiState.value
        val wasBlocked = state is RadarUiState.PermissionDenied || state is RadarUiState.PermissionPermanentlyDenied
        if (isGranted && wasBlocked) _uiState.value = RadarUiState.Idle
    }

    fun onCancelSearch() {
        searchJob?.cancel()
        crashReporter.log("Ghost search cancelled by user")
        _uiState.value = RadarUiState.Idle
    }

    private fun searchGhosts() {
        if (searchJob?.isActive == true) return
        crashReporter.setCustomKey(KEY_SCREEN_NAME, SCREEN_NAME)
        searchJob = viewModelScope.launch {
            _uiState.value = RadarUiState.Searching
            _uiState.value = repository.findNearbyGhosts().fold(
                onSuccess = { ghosts -> RadarUiState.GhostsFound(ghosts) },
                onFailure = { error ->
                    when (error) {
                        is GhostSearchException.LocationDisabled -> RadarUiState.LocationDisabled
                        is GhostSearchException.LocationUnavailable -> RadarUiState.LocationUnavailable
                        is GhostSearchException.PermissionMissing -> RadarUiState.PermissionDenied
                        else -> RadarUiState.Error
                    }
                }
            )
        }
    }

    private companion object {
        const val KEY_SCREEN_NAME = "screen_name"
        const val SCREEN_NAME = "radar"
    }
}
