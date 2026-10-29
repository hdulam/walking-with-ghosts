package com.hdulam.walkingwithghosts.data.location

interface LocationDataSource {
    fun isLocationEnabled(): Boolean

    /**
     * Returns null when the system has no location to give right now.
     * Throws [SecurityException] if the location permission is not granted.
     */
    suspend fun getCurrentApproximateLocation(): ApproximateLocation?
}
