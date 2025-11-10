package com.example.appmilsabores.domain.usecase

import com.example.appmilsabores.domain.model.SessionState
import com.example.appmilsabores.domain.repository.SessionRepository

class UpdateSessionUseCase(private val repository: SessionRepository) {
    suspend operator fun invoke(transform: (SessionState) -> SessionState) {
        repository.updateSession(transform)
    }
}
