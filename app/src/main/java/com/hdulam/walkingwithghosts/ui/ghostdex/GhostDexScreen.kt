package com.hdulam.walkingwithghosts.ui.ghostdex

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hdulam.walkingwithghosts.BuildConfig
import com.hdulam.walkingwithghosts.R
import com.hdulam.walkingwithghosts.domain.model.Ghost
import com.hdulam.walkingwithghosts.ui.AppViewModelFactory
import com.hdulam.walkingwithghosts.ui.components.GhostAvatar
import com.hdulam.walkingwithghosts.ui.components.GhostScaffold
import com.hdulam.walkingwithghosts.ui.components.RarityBadge

@Composable
fun GhostDexScreen(
    onBack: () -> Unit,
    viewModel: GhostDexViewModel = viewModel(factory = AppViewModelFactory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    GhostScaffold(title = stringResource(R.string.ghostdex_title)) {
        when (val state = uiState) {
            GhostDexUiState.Loading -> CircularProgressIndicator()
            GhostDexUiState.Error -> Text(
                stringResource(R.string.ghostdex_error),
                color = MaterialTheme.colorScheme.error
            )
            is GhostDexUiState.Loaded -> {
                Text(
                    stringResource(R.string.ghostdex_total, state.capturedCount, state.ghosts.size),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                // Demo-only entry point used in the Crashlytics activity; hidden in release builds.
                if (BuildConfig.DEBUG) {
                    TextButton(onClick = viewModel::onFeaturedGhostClicked) {
                        Text(stringResource(R.string.action_featured_ghost_demo))
                    }
                }
                state.featuredGhost?.let { featured ->
                    Text(
                        stringResource(R.string.ghostdex_featured, featured.name),
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                if (state.isFeaturedUnavailable) {
                    Text(
                        stringResource(R.string.ghostdex_featured_empty),
                        color = MaterialTheme.colorScheme.error
                    )
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.ghosts, key = { it.id }) { ghost -> GhostDexEntry(ghost) }
                }
            }
        }
        OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.action_back))
        }
    }
}

@Composable
private fun GhostDexEntry(ghost: Ghost) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            GhostAvatar(ghost, hidden = !ghost.isCaptured)
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    if (ghost.isCaptured) ghost.name else stringResource(R.string.ghostdex_unknown),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    if (ghost.isCaptured) stringResource(R.string.badge_captured)
                    else stringResource(R.string.ghostdex_not_captured),
                    color = if (ghost.isCaptured) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            RarityBadge(ghost.rarity)
        }
    }
}
