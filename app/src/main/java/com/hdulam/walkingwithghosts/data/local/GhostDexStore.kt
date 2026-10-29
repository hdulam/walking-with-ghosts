package com.hdulam.walkingwithghosts.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.ghostDexDataStore: DataStore<Preferences> by preferencesDataStore(name = "ghostdex")

/** Persists the ids of captured ghosts. A set of ids is all the GhostDex needs. */
class GhostDexStore(context: Context) {

    private val dataStore = context.applicationContext.ghostDexDataStore

    val capturedIds: Flow<Set<String>> = dataStore.data.map { preferences ->
        preferences[CAPTURED_IDS].orEmpty()
    }

    suspend fun addCaptured(ghostId: String) {
        dataStore.edit { preferences ->
            preferences[CAPTURED_IDS] = preferences[CAPTURED_IDS].orEmpty() + ghostId
        }
    }

    private companion object {
        val CAPTURED_IDS = stringSetPreferencesKey("captured_ids")
    }
}
