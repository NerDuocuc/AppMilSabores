package com.example.appmilsabores.domain.repository

import com.example.appmilsabores.domain.model.PaymentMethod

interface PaymentRepository {
    suspend fun getPaymentMethods(): List<PaymentMethod>
    suspend fun deletePaymentMethod(id: Int)
    suspend fun addPaymentMethod(method: PaymentMethod): PaymentMethod
    suspend fun setDefaultPaymentMethod(id: Int)
    suspend fun clearPaymentMethods()
}
