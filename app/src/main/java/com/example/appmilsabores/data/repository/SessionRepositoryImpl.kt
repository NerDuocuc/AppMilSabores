package com.example.appmilsabores.data.repository

import com.example.appmilsabores.AppMilSaboresApplication
import com.example.appmilsabores.data.prefs.SessionPreferencesDataSource
import com.example.appmilsabores.domain.model.SessionState
import com.example.appmilsabores.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class SessionRepositoryImpl(
    private val prefs: SessionPreferencesDataSource = AppMilSaboresApplication.sessionPreferences
) : SessionRepository {

    override fun observeSession(): Flow<SessionState> = prefs.sessionFlow

    override suspend fun getSession(): SessionState = prefs.sessionFlow.first()

    override suspend fun saveSession(state: SessionState) {
        prefs.saveSession(state)
    }

    override suspend fun updateSession(transform: (SessionState) -> SessionState) {
        prefs.updateSession(transform)
    }

    override suspend fun clearSession() {
        prefs.clearSession()
    }
}
