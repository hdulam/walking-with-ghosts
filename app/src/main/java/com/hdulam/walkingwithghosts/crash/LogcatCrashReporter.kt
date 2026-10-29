package com.hdulam.walkingwithghosts.crash

import android.util.Log

/**
 * Used only when Firebase is not configured (no google-services.json).
 * It writes to Logcat so nothing is silently lost; it does NOT send anything to Crashlytics.
 */
class LogcatCrashReporter : CrashReporter {

    override fun recordException(throwable: Throwable) {
        Log.e(TAG, "Non-fatal exception (Crashlytics not configured)", throwable)
    }

    override fun log(message: String) {
        Log.i(TAG, message)
    }

    override fun setCustomKey(key: String, value: String) {
        Log.d(TAG, "key $key=$value")
    }

    override fun setCustomKey(key: String, value: Int) {
        Log.d(TAG, "key $key=$value")
    }

    private companion object {
        const val TAG = "CrashReporter"
    }
}
