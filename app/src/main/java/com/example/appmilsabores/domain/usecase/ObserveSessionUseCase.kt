package com.example.appmilsabores.domain.usecase

import com.example.appmilsabores.domain.model.SessionState
import com.example.appmilsabores.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow

class ObserveSessionUseCase(private val repository: SessionRepository) {
    operator fun invoke(): Flow<SessionState> = repository.observeSession()
}
