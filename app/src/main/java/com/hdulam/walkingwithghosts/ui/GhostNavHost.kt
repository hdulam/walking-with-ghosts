package com.hdulam.walkingwithghosts.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.hdulam.walkingwithghosts.ui.capture.CaptureScreen
import com.hdulam.walkingwithghosts.ui.capture.CaptureViewModel
import com.hdulam.walkingwithghosts.ui.ghostdex.GhostDexScreen
import com.hdulam.walkingwithghosts.ui.radar.RadarScreen

private object Routes {
    const val RADAR = "radar"
    const val GHOST_DEX = "ghostdex"
    const val CAPTURE_ARG_GHOST_ID = CaptureViewModel.GHOST_ID_ARG
    const val CAPTURE = "capture/{$CAPTURE_ARG_GHOST_ID}"

    fun capture(ghostId: String) = "capture/$ghostId"
}

@Composable
fun GhostNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.RADAR) {
        composable(Routes.RADAR) {
            RadarScreen(
                onOpenGhostDex = { navController.navigate(Routes.GHOST_DEX) },
                onGhostSelected = { ghostId -> navController.navigate(Routes.capture(ghostId)) }
            )
        }
        composable(
            route = Routes.CAPTURE,
            arguments = listOf(navArgument(Routes.CAPTURE_ARG_GHOST_ID) { type = NavType.StringType })
        ) {
            CaptureScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.GHOST_DEX) {
            GhostDexScreen(onBack = { navController.popBackStack() })
        }
    }
}
