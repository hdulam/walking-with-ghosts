package com.hdulam.walkingwithghosts.ui.radar

import android.Manifest
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hdulam.walkingwithghosts.R
import com.hdulam.walkingwithghosts.domain.model.Ghost
import com.hdulam.walkingwithghosts.permissions.rememberPermissionRequester
import com.hdulam.walkingwithghosts.ui.AppViewModelFactory
import com.hdulam.walkingwithghosts.ui.components.GhostAvatar
import com.hdulam.walkingwithghosts.ui.components.GhostScaffold
import com.hdulam.walkingwithghosts.ui.components.RarityBadge

@Composable
fun RadarScreen(
    onOpenGhostDex: () -> Unit,
    onGhostSelected: (String) -> Unit,
    viewModel: RadarViewModel = viewModel(factory = AppViewModelFactory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val locationPermission = rememberPermissionRequester(
        permission = Manifest.permission.ACCESS_COARSE_LOCATION,
        onResult = viewModel::onPermissionResult
    )
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.onPermissionRechecked(locationPermission.isGranted())
    }

    if (uiState is RadarUiState.PermissionRationale) {
        AlertDialog(
            onDismissRequest = viewModel::onRationaleDismissed,
            title = { Text(stringResource(R.string.rationale_title)) },
            text = { Text(stringResource(R.string.rationale_message)) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.onRationaleAccepted()
                    locationPermission.launchSystemDialog()
                }) { Text(stringResource(R.string.rationale_continue)) }
            },
            dismissButton = {
                TextButton(onClick = viewModel::onRationaleDismissed) {
                    Text(stringResource(R.string.rationale_not_now))
                }
            }
        )
    }

    GhostScaffold(title = stringResource(R.string.app_name)) {
        Text(
            text = stringResource(R.string.app_subtitle),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.secondary
        )
        val isSearching = uiState is RadarUiState.Searching
        RadarSweep(isScanning = isSearching, modifier = Modifier.fillMaxWidth(0.7f))
        RadarStatusText(uiState)

        if (isSearching) {
            OutlinedButton(onClick = viewModel::onCancelSearch, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.action_cancel_search))
            }
        } else {
            Button(onClick = locationPermission::request, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.action_search_ghosts))
            }
        }
        if (uiState is RadarUiState.PermissionPermanentlyDenied) {
            OutlinedButton(onClick = locationPermission::openAppSettings, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.action_open_settings))
            }
        }

        val found = uiState as? RadarUiState.GhostsFound
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(found?.ghosts.orEmpty(), key = { it.id }) { ghost ->
                DetectedGhostCard(ghost = ghost, onClick = { onGhostSelected(ghost.id) })
            }
        }
        OutlinedButton(onClick = onOpenGhostDex, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.action_open_ghostdex))
        }
    }
}

@Composable
private fun RadarStatusText(uiState: RadarUiState) {
    val text = when (uiState) {
        RadarUiState.Idle, RadarUiState.PermissionRationale -> stringResource(R.string.radar_status_idle)
        RadarUiState.Searching -> stringResource(R.string.radar_status_searching)
        is RadarUiState.GhostsFound -> stringResource(R.string.radar_status_found, uiState.ghosts.size)
        RadarUiState.PermissionDenied -> stringResource(R.string.radar_status_permission_denied)
        RadarUiState.PermissionPermanentlyDenied -> stringResource(R.string.radar_status_permission_permanently_denied)
        RadarUiState.LocationDisabled -> stringResource(R.string.radar_status_location_disabled)
        RadarUiState.LocationUnavailable -> stringResource(R.string.radar_status_location_unavailable)
        RadarUiState.Error -> stringResource(R.string.radar_status_error)
    }
    val isProblem = when (uiState) {
        RadarUiState.PermissionDenied,
        RadarUiState.PermissionPermanentlyDenied,
        RadarUiState.LocationDisabled,
        RadarUiState.LocationUnavailable,
        RadarUiState.Error -> true
        else -> false
    }
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        color = if (isProblem) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun DetectedGhostCard(ghost: Ghost, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            GhostAvatar(ghost)
            Column(modifier = Modifier.weight(1f)) {
                Text(ghost.name, style = MaterialTheme.typography.titleMedium)
                if (ghost.isCaptured) {
                    Text(
                        stringResource(R.string.badge_captured),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            Spacer(Modifier.padding(start = 4.dp))
            RarityBadge(ghost.rarity)
        }
    }
}
