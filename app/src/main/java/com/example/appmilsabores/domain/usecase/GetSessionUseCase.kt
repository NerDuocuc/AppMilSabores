package com.example.appmilsabores.domain.usecase

import com.example.appmilsabores.domain.model.SessionState
import com.example.appmilsabores.domain.repository.SessionRepository

class GetSessionUseCase(private val repository: SessionRepository) {
    suspend operator fun invoke(): SessionState = repository.getSession()
}
