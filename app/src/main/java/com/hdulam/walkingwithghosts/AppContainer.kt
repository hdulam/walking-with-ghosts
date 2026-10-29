package com.hdulam.walkingwithghosts

import android.content.Context
import android.util.Log
import com.hdulam.walkingwithghosts.crash.CrashReporter
import com.hdulam.walkingwithghosts.crash.FirebaseCrashReporter
import com.hdulam.walkingwithghosts.crash.LogcatCrashReporter
import com.hdulam.walkingwithghosts.data.local.GhostDexStore
import com.hdulam.walkingwithghosts.data.location.FusedLocationDataSource
import com.hdulam.walkingwithghosts.data.repository.DefaultGhostRepository
import com.hdulam.walkingwithghosts.data.repository.GhostRepository

/** Manual dependency injection: the single place where implementations are chosen. */
class AppContainer(context: Context) {

    private val appContext = context.applicationContext

    val crashReporter: CrashReporter =
        if (FirebaseCrashReporter.isConfigured(appContext)) {
            FirebaseCrashReporter()
        } else {
            Log.w(TAG, "Firebase is not configured: crash reports go to Logcat only")
            LogcatCrashReporter()
        }

    val ghostRepository: GhostRepository = DefaultGhostRepository(
        locationDataSource = FusedLocationDataSource(appContext),
        ghostDexStore = GhostDexStore(appContext),
        crashReporter = crashReporter
    )

    private companion object {
        const val TAG = "AppContainer"
    }
}
