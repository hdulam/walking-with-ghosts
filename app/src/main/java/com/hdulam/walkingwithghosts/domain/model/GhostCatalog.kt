package com.hdulam.walkingwithghosts.domain.model

/** Every ghost that exists in the game. All of them are original and fictional. */
object GhostCatalog {
    val ghosts: List<Ghost> = listOf(
        Ghost(
            id = "lost_echo",
            name = "Eco Perdido",
            description = "Repite susurros que nadie recuerda haber dicho.",
            rarity = Rarity.COMMON,
            captureChance = 0.75f,
            glyph = "👻",
            auraColorArgb = 0xFF6EF3FF
        ),
        Ghost(
            id = "wandering_shade",
            name = "Sombra Errante",
            description = "Camina por los pasillos buscando una puerta que ya no existe.",
            rarity = Rarity.COMMON,
            captureChance = 0.7f,
            glyph = "🌑",
            auraColorArgb = 0xFF9AA3C7
        ),
        Ghost(
            id = "forest_soul",
            name = "Alma del Bosque",
            description = "Brilla entre las hojas y huye cuando alguien pisa una rama.",
            rarity = Rarity.RARE,
            captureChance = 0.5f,
            glyph = "🍃",
            auraColorArgb = 0xFF39FF88
        ),
        Ghost(
            id = "night_watcher",
            name = "Vigilante Nocturno",
            description = "Observa desde los tejados y nunca parpadea.",
            rarity = Rarity.RARE,
            captureChance = 0.45f,
            glyph = "🦉",
            auraColorArgb = 0xFFB266FF
        ),
        Ghost(
            id = "crimson_specter",
            name = "Espectro Carmesí",
            description = "Deja un rastro rojo que se desvanece al amanecer.",
            rarity = Rarity.EPIC,
            captureChance = 0.3f,
            glyph = "🩸",
            auraColorArgb = 0xFFFF5470
        ),
        Ghost(
            id = "threshold_guardian",
            name = "Guardián del Umbral",
            description = "Custodia el límite entre los dos mundos. Pocos lo han visto.",
            rarity = Rarity.LEGENDARY,
            captureChance = 0.15f,
            glyph = "🗝️",
            auraColorArgb = 0xFFFFD166
        )
    )

    fun findById(id: String): Ghost? = ghosts.firstOrNull { it.id == id }
}
