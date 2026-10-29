package com.hdulam.walkingwithghosts.permissions

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.launch

/**
 * Generic runtime-permission component. It follows Android's request workflow:
 * check -> (rationale) -> system dialog -> result. It works with any dangerous permission.
 * It lives in the UI layer because the launcher is tied to the Activity lifecycle.
 */
@Stable
class PermissionRequester internal constructor(
    val permission: String,
    private val activity: Activity,
    private val onRequest: () -> Unit,
    private val onLaunchSystemDialog: () -> Unit
) {
    fun isGranted(): Boolean =
        ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED

    /** Reports [PermissionResult.Granted] or [PermissionResult.ShowRationale], or shows the system dialog. */
    fun request() = onRequest()

    /** Shows the system dialog directly; call it after the user has read the rationale. */
    fun launchSystemDialog() = onLaunchSystemDialog()

    /** Opens this app's settings page. Only call it from an explicit user action. */
    fun openAppSettings() {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", activity.packageName, null)
        )
        activity.startActivity(intent)
    }
}

@Composable
fun rememberPermissionRequester(
    permission: String,
    onResult: (PermissionResult) -> Unit
): PermissionRequester {
    val context = LocalContext.current
    val activity = remember(context) { context.findActivity() }
    val historyStore = remember(context) { PermissionHistoryStore(context) }
    val scope = rememberCoroutineScope()
    val currentOnResult by rememberUpdatedState(onResult)
    // Saved so the value survives an Activity recreation while the system dialog is open.
    val rationaleBeforeRequest = rememberSaveable(permission) { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            currentOnResult(PermissionResult.Granted)
        } else {
            val rationaleAfterDenial = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
            scope.launch {
                val deniedBefore = historyStore.wasDenied(permission)
                historyStore.markDenied(permission)
                currentOnResult(
                    PermissionDenialClassifier.classify(
                        rationaleBeforeRequest = rationaleBeforeRequest.value,
                        rationaleAfterDenial = rationaleAfterDenial,
                        deniedBefore = deniedBefore
                    )
                )
            }
        }
    }

    return remember(permission, activity, launcher) {
        lateinit var requester: PermissionRequester
        val launchSystemDialog = {
            rationaleBeforeRequest.value = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
            launcher.launch(permission)
        }
        requester = PermissionRequester(
            permission = permission,
            activity = activity,
            onRequest = {
                when {
                    requester.isGranted() -> currentOnResult(PermissionResult.Granted)
                    ActivityCompat.shouldShowRequestPermissionRationale(activity, permission) ->
                        currentOnResult(PermissionResult.ShowRationale)
                    else -> launchSystemDialog()
                }
            },
            onLaunchSystemDialog = launchSystemDialog
        )
        requester
    }
}

private fun Context.findActivity(): Activity {
    var current: Context = this
    while (current is ContextWrapper) {
        if (current is Activity) return current
        current = current.baseContext
    }
    error("PermissionRequester must be used inside an Activity")
}
