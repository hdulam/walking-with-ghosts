package com.hdulam.walkingwithghosts.permissions

/**
 * Android has no API that says "permanently denied". shouldShowRequestPermissionRationale()
 * is false both before the first request and after a permanent denial, so it is combined with
 * what it was before the request and with whether this permission was ever denied before.
 */
object PermissionDenialClassifier {

    fun classify(
        rationaleBeforeRequest: Boolean,
        rationaleAfterDenial: Boolean,
        deniedBefore: Boolean
    ): PermissionResult = when {
        rationaleAfterDenial -> PermissionResult.Denied
        rationaleBeforeRequest || deniedBefore -> PermissionResult.PermanentlyDenied
        else -> PermissionResult.Denied
    }
}
