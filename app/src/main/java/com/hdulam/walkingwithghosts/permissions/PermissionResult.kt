package com.hdulam.walkingwithghosts.permissions

sealed interface PermissionResult {
    /** The permission is granted; the feature can run. */
    data object Granted : PermissionResult

    /** Android recommends explaining the permission before asking again. */
    data object ShowRationale : PermissionResult

    /** The user said no (or dismissed the dialog); asking again is still possible. */
    data object Denied : PermissionResult

    /** The system will not show the dialog again; only app settings can change it. */
    data object PermanentlyDenied : PermissionResult
}
