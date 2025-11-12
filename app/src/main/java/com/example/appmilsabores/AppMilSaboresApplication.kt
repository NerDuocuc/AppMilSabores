package com.example.appmilsabores

import android.app.Application
import com.example.appmilsabores.data.local.AppDatabase
import com.example.appmilsabores.data.prefs.NotificationPreferencesDataSource
import com.example.appmilsabores.data.prefs.SessionPreferencesDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import android.util.Log
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter

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
            try {
                database.seed()
                sessionPrefs.seedSuperAdminIfNeeded()
            } catch (t: Throwable) {
                // Log and persist the exception to a file so we can inspect crashes that occur during initialization
                Log.e("AppInit", "Error during app initialization", t)
                try {
                    val sw = StringWriter()
                    val pw = PrintWriter(sw)
                    t.printStackTrace(pw)
                    val logFile = File(filesDir, "init_crash.log")
                    logFile.writeText(sw.toString())
                } catch (ignored: Exception) {
                    Log.e("AppInit", "Failed to write init_crash.log", ignored)
                }
            }
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