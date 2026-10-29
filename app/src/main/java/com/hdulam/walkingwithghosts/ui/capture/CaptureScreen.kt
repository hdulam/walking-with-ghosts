package com.hdulam.walkingwithghosts.ui.capture

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hdulam.walkingwithghosts.R
import com.hdulam.walkingwithghosts.ui.AppViewModelFactory
import com.hdulam.walkingwithghosts.ui.components.GhostAvatar
import com.hdulam.walkingwithghosts.ui.components.GhostScaffold
import com.hdulam.walkingwithghosts.ui.components.RarityBadge
import kotlin.math.roundToInt

@Composable
fun CaptureScreen(
    onBack: () -> Unit,
    viewModel: CaptureViewModel = viewModel(factory = AppViewModelFactory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    GhostScaffold(title = stringResource(R.string.capture_title)) {
        when (val state = uiState) {
            CaptureUiState.Loading -> CircularProgressIndicator()
            CaptureUiState.NotFound -> Text(
                stringResource(R.string.capture_not_found),
                color = MaterialTheme.colorScheme.error
            )
            is CaptureUiState.Ready -> ReadyContent(state, viewModel::onCaptureClicked)
        }
        OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.action_back))
        }
    }
}

@Composable
private fun ReadyContent(state: CaptureUiState.Ready, onCapture: () -> Unit) {
    val ghost = state.ghost
    val isCapturing = state.phase == CapturePhase.CAPTURING
    val transition = rememberInfiniteTransition(label = "capture")
    val pulse by transition.animateFloat(
        initialValue = 1f,
        targetValue = if (isCapturing) 0.75f else 1.08f,
        animationSpec = infiniteRepeatable(tween(if (isCapturing) 250 else 1400), RepeatMode.Reverse),
        label = "pulse"
    )
    GhostAvatar(ghost, size = 160.dp, modifier = Modifier.scale(pulse))
    Text(ghost.name, style = MaterialTheme.typography.titleLarge)
    RarityBadge(ghost.rarity)
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            ghost.description,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyLarge
        )
    }
    Text(
        stringResource(R.string.capture_chance, (ghost.captureChance * 100).roundToInt()),
        color = MaterialTheme.colorScheme.tertiary
    )
    val (message, color) = when (state.phase) {
        CapturePhase.READY -> null to MaterialTheme.colorScheme.onSurface
        CapturePhase.CAPTURING -> stringResource(R.string.capture_in_progress) to MaterialTheme.colorScheme.secondary
        CapturePhase.CAUGHT -> stringResource(R.string.capture_success, ghost.name) to MaterialTheme.colorScheme.primary
        CapturePhase.ESCAPED -> stringResource(R.string.capture_escaped, ghost.name) to MaterialTheme.colorScheme.error
        CapturePhase.FAILED -> stringResource(R.string.capture_error) to MaterialTheme.colorScheme.error
    }
    if (message != null) Text(message, color = color, textAlign = TextAlign.Center)
    if (state.phase != CapturePhase.CAUGHT) {
        Button(onClick = onCapture, enabled = !isCapturing, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.action_capture))
        }
    }
}
