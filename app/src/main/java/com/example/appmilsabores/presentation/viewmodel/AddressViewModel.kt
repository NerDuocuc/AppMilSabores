package com.example.appmilsabores.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appmilsabores.data.repository.AddressRepositoryImpl
import com.example.appmilsabores.domain.model.Address
import com.example.appmilsabores.domain.usecase.DeleteAddressUseCase
import com.example.appmilsabores.domain.usecase.GetAddressesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import kotlinx.coroutines.launch

data class AddressUiState(
    val addresses: List<Address> = emptyList(),
    val isLoading: Boolean = true
)

class AddressViewModel(
    private val repo: AddressRepositoryImpl = AddressRepositoryImpl()
) : ViewModel() {

    private val getAddresses = GetAddressesUseCase(repo)
    private val deleteAddress = DeleteAddressUseCase(repo)

    private val _uiState = MutableStateFlow(AddressUiState())
    val uiState: StateFlow<AddressUiState> = _uiState

    init {
        loadAddresses()
    }

    fun loadAddresses() {
        viewModelScope.launch {
            val current = withContext(Dispatchers.IO) { getAddresses() }
            _uiState.update { it.copy(addresses = current, isLoading = false) }
        }
    }

    fun removeAddress(id: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) { deleteAddress(id) }
            loadAddresses()
        }
    }

    fun setDefault(id: Int) {
        viewModelScope.launch {
            repo.setDefaultAddress(id)
            loadAddresses()
        }
    }
}
