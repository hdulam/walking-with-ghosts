package com.hdulam.walkingwithghosts.crash

/**
 * Isolates crash reporting from the rest of the app.
 * Never pass coordinates or personal data to any of these methods.
 */
interface CrashReporter {
    fun recordException(throwable: Throwable)
    fun log(message: String)
    fun setCustomKey(key: String, value: String)
    fun setCustomKey(key: String, value: Int)
}
