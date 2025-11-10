package com.example.appmilsabores.data.repository

import com.example.appmilsabores.AppMilSaboresApplication
import com.example.appmilsabores.data.prefs.NotificationPreferencesDataSource
import com.example.appmilsabores.domain.model.NotificationSettings
import com.example.appmilsabores.domain.repository.NotificationPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class NotificationPreferencesRepositoryImpl(
    private val prefs: NotificationPreferencesDataSource = AppMilSaboresApplication.notificationPreferences
) : NotificationPreferencesRepository {

    override fun observeSettings(): Flow<NotificationSettings> = prefs.settingsFlow

    override suspend fun getSettings(): NotificationSettings = prefs.settingsFlow.first()

    override suspend fun saveSettings(settings: NotificationSettings) {
        prefs.save(settings)
    }

    override suspend fun updateSettings(transform: (NotificationSettings) -> NotificationSettings) {
        prefs.update(transform)
    }
}
