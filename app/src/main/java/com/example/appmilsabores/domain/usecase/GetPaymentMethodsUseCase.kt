package com.example.appmilsabores.domain.usecase

import com.example.appmilsabores.domain.model.PaymentMethod
import com.example.appmilsabores.domain.repository.PaymentRepository

class GetPaymentMethodsUseCase(private val repo: PaymentRepository) {
    suspend operator fun invoke(): List<PaymentMethod> = repo.getPaymentMethods()
}
