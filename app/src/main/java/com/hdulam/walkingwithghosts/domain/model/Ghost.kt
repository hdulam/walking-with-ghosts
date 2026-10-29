package com.hdulam.walkingwithghosts.domain.model

/**
 * A fictional ghost. [captureChance] is between 0 and 1.
 * [glyph] and [auraColorArgb] are the visual resource; the color is a plain ARGB value
 * so the domain layer does not depend on Compose.
 */
data class Ghost(
    val id: String,
    val name: String,
    val description: String,
    val rarity: Rarity,
    val captureChance: Float,
    val glyph: String,
    val auraColorArgb: Long,
    val isCaptured: Boolean = false
)
