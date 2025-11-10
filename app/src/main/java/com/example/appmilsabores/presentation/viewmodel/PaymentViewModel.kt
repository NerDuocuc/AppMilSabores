package com.example.appmilsabores.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appmilsabores.data.repository.PaymentRepositoryImpl
import com.example.appmilsabores.domain.model.PaymentMethod
import com.example.appmilsabores.domain.usecase.DeletePaymentMethodUseCase
import com.example.appmilsabores.domain.usecase.GetPaymentMethodsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PaymentUiState(
    val methods: List<PaymentMethod> = emptyList(),
    val isLoading: Boolean = true
)

class PaymentViewModel(
    private val repo: PaymentRepositoryImpl = PaymentRepositoryImpl()
) : ViewModel() {

    private val getMethods = GetPaymentMethodsUseCase(repo)
    private val deleteMethod = DeletePaymentMethodUseCase(repo)

    private val _uiState = MutableStateFlow(PaymentUiState())
    val uiState: StateFlow<PaymentUiState> = _uiState

    init {
        loadMethods()
    }

    fun loadMethods() {
        viewModelScope.launch {
            _uiState.update { it.copy(methods = getMethods(), isLoading = false) }
        }
    }

    fun deleteMethod(id: Int) {
        viewModelScope.launch {
            deleteMethod.invoke(id)
            loadMethods()
        }
    }

    fun markAsDefault(id: Int) {
        viewModelScope.launch {
            repo.setDefaultPaymentMethod(id)
            loadMethods()
        }
    }
}
