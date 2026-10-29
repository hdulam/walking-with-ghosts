package com.hdulam.walkingwithghosts.permissions

import org.junit.Assert.assertEquals
import org.junit.Test

class PermissionDenialClassifierTest {

    @Test
    fun firstDenial_withRationaleAfterwards_isDenied() {
        val result = PermissionDenialClassifier.classify(
            rationaleBeforeRequest = false,
            rationaleAfterDenial = true,
            deniedBefore = false
        )
        assertEquals(PermissionResult.Denied, result)
    }

    @Test
    fun firstDialogDismissed_withoutHistory_isDenied() {
        val result = PermissionDenialClassifier.classify(
            rationaleBeforeRequest = false,
            rationaleAfterDenial = false,
            deniedBefore = false
        )
        assertEquals(PermissionResult.Denied, result)
    }

    @Test
    fun secondDenial_rationaleTurnsOff_isPermanentlyDenied() {
        val result = PermissionDenialClassifier.classify(
            rationaleBeforeRequest = true,
            rationaleAfterDenial = false,
            deniedBefore = true
        )
        assertEquals(PermissionResult.PermanentlyDenied, result)
    }

    @Test
    fun requestWhenDialogNoLongerShown_isPermanentlyDenied() {
        val result = PermissionDenialClassifier.classify(
            rationaleBeforeRequest = false,
            rationaleAfterDenial = false,
            deniedBefore = true
        )
        assertEquals(PermissionResult.PermanentlyDenied, result)
    }
}
