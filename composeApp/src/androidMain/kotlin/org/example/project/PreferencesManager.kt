package org.example.project

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("app_preferences")

class PreferencesManager(context: Context) : IPreferencesManager {
    private val dataStore: DataStore<Preferences> = context.dataStore

    override suspend fun saveLastPostId(id: Int) {
        dataStore.edit { preferences ->
            preferences[intPreferencesKey("last_post_id")] = id
        }
    }

    override fun getLastPostIdFlow(): Flow<Int> = dataStore.data.map { preferences ->
        preferences[intPreferencesKey("last_post_id")] ?: 1
    }
}