package com.example.appmilsabores

import android.app.Application
import com.example.appmilsabores.data.local.AppDatabase
import com.example.appmilsabores.data.prefs.NotificationPreferencesDataSource
import com.example.appmilsabores.data.prefs.SessionPreferencesDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class AppMilSaboresApplication : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    val appDatabase: AppDatabase by lazy { AppDatabase.build(this) }
    val sessionPreferencesDataSource: SessionPreferencesDataSource by lazy { SessionPreferencesDataSource(this) }
    val notificationPreferencesDataSource: NotificationPreferencesDataSource by lazy { NotificationPreferencesDataSource(this) }

    override fun onCreate() {
        super.onCreate()
        instance = this
        val database = appDatabase
        val sessionPrefs = sessionPreferencesDataSource
        applicationScope.launch {
            database.seed()
            sessionPrefs.seedSuperAdminIfNeeded()
        }
    }

    companion object {
        lateinit var instance: AppMilSaboresApplication
            private set

        val database: AppDatabase
            get() = instance.appDatabase

        val sessionPreferences: SessionPreferencesDataSource
            get() = instance.sessionPreferencesDataSource

        val notificationPreferences: NotificationPreferencesDataSource
            get() = instance.notificationPreferencesDataSource
    }
}