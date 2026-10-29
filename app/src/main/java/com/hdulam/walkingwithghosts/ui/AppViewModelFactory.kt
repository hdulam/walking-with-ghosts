package com.hdulam.walkingwithghosts.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.hdulam.walkingwithghosts.WalkingWithGhostsApplication
import com.hdulam.walkingwithghosts.ui.capture.CaptureViewModel
import com.hdulam.walkingwithghosts.ui.ghostdex.GhostDexViewModel
import com.hdulam.walkingwithghosts.ui.radar.RadarViewModel

/** Builds every ViewModel with its dependencies from the AppContainer. */
val AppViewModelFactory = viewModelFactory {
    initializer {
        val container = (this[APPLICATION_KEY] as WalkingWithGhostsApplication).container
        RadarViewModel(container.ghostRepository, container.crashReporter)
    }
    initializer {
        val container = (this[APPLICATION_KEY] as WalkingWithGhostsApplication).container
        CaptureViewModel(createSavedStateHandle(), container.ghostRepository, container.crashReporter)
    }
    initializer {
        val container = (this[APPLICATION_KEY] as WalkingWithGhostsApplication).container
        GhostDexViewModel(container.ghostRepository, container.crashReporter)
    }
}
