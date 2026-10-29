package com.hdulam.walkingwithghosts.permissions

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private val Context.permissionHistoryDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "permission_history"
)

/** Remembers which permissions the user has denied at least once, across app restarts. */
class PermissionHistoryStore(context: Context) {

    private val dataStore = context.applicationContext.permissionHistoryDataStore

    suspend fun wasDenied(permission: String): Boolean =
        permission in dataStore.data.first()[DENIED_PERMISSIONS].orEmpty()

    suspend fun markDenied(permission: String) {
        dataStore.edit { preferences ->
            preferences[DENIED_PERMISSIONS] = preferences[DENIED_PERMISSIONS].orEmpty() + permission
        }
    }

    private companion object {
        val DENIED_PERMISSIONS = stringSetPreferencesKey("denied_permissions")
    }
}
