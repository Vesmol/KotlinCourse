package org.example.project

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class PreferencesManagerStub : IPreferencesManager {
    override suspend fun saveLastPostId(id: Int) {
    }

    override fun getLastPostIdFlow(): Flow<Int> = flowOf(1)
}