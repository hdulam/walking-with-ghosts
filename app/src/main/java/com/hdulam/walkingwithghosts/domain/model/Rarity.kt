package com.hdulam.walkingwithghosts.domain.model

/** [spawnWeight] is the relative chance of a ghost of this rarity appearing on the radar. */
enum class Rarity(val spawnWeight: Int) {
    COMMON(spawnWeight = 50),
    RARE(spawnWeight = 30),
    EPIC(spawnWeight = 15),
    LEGENDARY(spawnWeight = 5)
}
