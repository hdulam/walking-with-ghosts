package com.hdulam.walkingwithghosts.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hdulam.walkingwithghosts.R
import com.hdulam.walkingwithghosts.domain.model.Rarity
import com.hdulam.walkingwithghosts.ui.theme.EctoplasmCyan
import com.hdulam.walkingwithghosts.ui.theme.LegendaryGold
import com.hdulam.walkingwithghosts.ui.theme.MistGrey
import com.hdulam.walkingwithghosts.ui.theme.SpectralPurple

@Composable
fun RarityBadge(rarity: Rarity, modifier: Modifier = Modifier) {
    val (labelRes, color) = when (rarity) {
        Rarity.COMMON -> R.string.rarity_common to MistGrey
        Rarity.RARE -> R.string.rarity_rare to EctoplasmCyan
        Rarity.EPIC -> R.string.rarity_epic to SpectralPurple
        Rarity.LEGENDARY -> R.string.rarity_legendary to LegendaryGold
    }
    Text(
        text = stringResource(labelRes).uppercase(),
        style = MaterialTheme.typography.labelLarge,
        color = color,
        modifier = modifier
            .border(1.dp, color, RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 2.dp)
    )
}
