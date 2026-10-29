package com.hdulam.walkingwithghosts.crash

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics

/** The only class in the app that talks to Firebase Crashlytics. */
class FirebaseCrashReporter(
    private val crashlytics: FirebaseCrashlytics = FirebaseCrashlytics.getInstance()
) : CrashReporter {

    override fun recordException(throwable: Throwable) = crashlytics.recordException(throwable)

    override fun log(message: String) = crashlytics.log(message)

    override fun setCustomKey(key: String, value: String) = crashlytics.setCustomKey(key, value)

    override fun setCustomKey(key: String, value: Int) = crashlytics.setCustomKey(key, value)

    companion object {
        /** False when the build has no google-services.json, so Firebase was never initialized. */
        fun isConfigured(context: Context): Boolean = FirebaseApp.getApps(context).isNotEmpty()
    }
}
