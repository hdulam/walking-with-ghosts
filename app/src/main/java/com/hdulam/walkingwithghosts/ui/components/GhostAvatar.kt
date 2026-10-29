package com.hdulam.walkingwithghosts.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hdulam.walkingwithghosts.domain.model.Ghost

/** The ghost's glyph floating on a glow of its aura color. [hidden] draws a silhouette. */
@Composable
fun GhostAvatar(ghost: Ghost, modifier: Modifier = Modifier, size: Dp = 56.dp, hidden: Boolean = false) {
    val aura = if (hidden) Color.Gray else Color(ghost.auraColorArgb)
    Box(
        modifier = modifier
            .size(size)
            .background(
                brush = Brush.radialGradient(listOf(aura.copy(alpha = 0.7f), aura.copy(alpha = 0.05f))),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (hidden) "?" else ghost.glyph,
            fontSize = (size.value * 0.45f).sp,
            modifier = Modifier.alpha(if (hidden) 0.6f else 1f)
        )
    }
}
