package com.hdulam.walkingwithghosts.data.location

/** A coarse location. It must never be logged or sent to crash reports. */
data class ApproximateLocation(val latitude: Double, val longitude: Double)
