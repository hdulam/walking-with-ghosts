package com.hdulam.walkingwithghosts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.hdulam.walkingwithghosts.ui.GhostNavHost
import com.hdulam.walkingwithghosts.ui.theme.WalkingWithGhostsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WalkingWithGhostsTheme {
                GhostNavHost()
            }
        }
    }
}
