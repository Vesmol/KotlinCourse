package org.example.project

import kotlinx.coroutines.flow.Flow

interface IPreferencesManager {
    suspend fun saveLastPostId(id: Int)
    fun getLastPostIdFlow(): Flow<Int>
}