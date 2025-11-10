package com.example.appmilsabores.domain.repository

import com.example.appmilsabores.domain.model.NotificationSettings
import kotlinx.coroutines.flow.Flow

interface NotificationPreferencesRepository {
    fun observeSettings(): Flow<NotificationSettings>
    suspend fun getSettings(): NotificationSettings
    suspend fun saveSettings(settings: NotificationSettings)
    suspend fun updateSettings(transform: (NotificationSettings) -> NotificationSettings)
}
