package com.example.appmilsabores.domain.usecase

import com.example.appmilsabores.domain.repository.UserRepository

class UpdateAccountUseCase(private val repo: UserRepository) {
    suspend operator fun invoke(fullName: String, email: String, newPassword: String?) {
        repo.updateUser(fullName, email, newPassword)
    }
}
