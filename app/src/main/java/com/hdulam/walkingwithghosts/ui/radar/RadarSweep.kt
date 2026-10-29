package com.hdulam.walkingwithghosts.ui.radar

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import com.hdulam.walkingwithghosts.ui.theme.NeonGreen

private const val RING_COUNT = 4
private const val SWEEP_DURATION_MS = 2400

/** Purely decorative radar; [isScanning] only changes its speed and intensity. */
@Composable
fun RadarSweep(isScanning: Boolean, modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "radar")
    val angle by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = if (isScanning) SWEEP_DURATION_MS / 2 else SWEEP_DURATION_MS,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "sweepAngle"
    )
    val sweepAlpha = if (isScanning) 0.55f else 0.3f

    Canvas(modifier = modifier.aspectRatio(1f)) {
        val radius = size.minDimension / 2f
        for (ring in 1..RING_COUNT) {
            drawCircle(
                color = NeonGreen.copy(alpha = 0.18f + 0.06f * ring),
                radius = radius * ring / RING_COUNT,
                style = Stroke(width = 2f)
            )
        }
        rotate(angle) {
            drawCircle(
                brush = Brush.sweepGradient(
                    0f to Color.Transparent,
                    0.85f to Color.Transparent,
                    1f to NeonGreen.copy(alpha = sweepAlpha)
                ),
                radius = radius
            )
        }
        drawCircle(color = NeonGreen, radius = radius * 0.03f)
    }
}
