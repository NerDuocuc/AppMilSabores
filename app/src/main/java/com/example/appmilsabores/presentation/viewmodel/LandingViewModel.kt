package com.example.appmilsabores.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appmilsabores.data.repository.LandingRepositoryImpl
import com.example.appmilsabores.data.repository.SessionRepositoryImpl
import com.example.appmilsabores.domain.model.*
import com.example.appmilsabores.domain.repository.SessionRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LandingUiState(
    val promotions: List<Promotion> = emptyList(),
    val categories: List<Category> = emptyList(),
    val featured: List<ProductSummary> = emptyList(),
    val newProducts: List<ProductSummary> = emptyList(),
    val currentPage: Int = 0
)

class LandingViewModel(
    private val repo: LandingRepositoryImpl = LandingRepositoryImpl(),
    private val sessionRepository: SessionRepository = SessionRepositoryImpl()
) : ViewModel() {

    private val _uiState = MutableStateFlow(LandingUiState())
    val uiState: StateFlow<LandingUiState> = _uiState

    init {
        loadLandingContent()
        autoScrollCarousel()
    }

    private fun loadLandingContent() {
        _uiState.update {
            it.copy(
                promotions = repo.getPromotions(),
                categories = repo.getCategories(),
                featured = repo.getFeaturedProducts(),
                newProducts = repo.getNewProducts()
            )
        }
    }

    private fun autoScrollCarousel() {
        viewModelScope.launch {
            while (true) {
                delay(5000)
                _uiState.update { current ->
                    if (current.promotions.isEmpty()) {
                        current
                    } else {
                        val next = (current.currentPage + 1) % current.promotions.size
                        current.copy(currentPage = next)
                    }
                }
            }
        }
    }

    suspend fun logout() {
        sessionRepository.clearSession()
    }
}
