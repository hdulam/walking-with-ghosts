package com.hdulam.walkingwithghosts.data.repository

/** Expected failures of a ghost search, returned inside a failed Result. */
sealed class GhostSearchException(message: String, cause: Throwable? = null) : Exception(message, cause) {
    class LocationDisabled : GhostSearchException("Location services are disabled")
    class LocationUnavailable : GhostSearchException("No location available")
    class PermissionMissing(cause: Throwable) : GhostSearchException("Location permission missing", cause)
    class Unexpected(cause: Throwable) : GhostSearchException("Unexpected location error", cause)
}
