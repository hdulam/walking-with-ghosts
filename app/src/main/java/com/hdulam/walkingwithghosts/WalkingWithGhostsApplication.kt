package com.hdulam.walkingwithghosts

import android.app.Application

class WalkingWithGhostsApplication : Application() {
    val container: AppContainer by lazy { AppContainer(this) }
}
