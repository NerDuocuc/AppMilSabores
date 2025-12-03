package com.example.appmilsabores.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appmilsabores.data.repository.LandingRepositoryImpl
import com.example.appmilsabores.data.repository.ProductRepositoryImpl
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
    val desserts: List<ProductSummary> = emptyList(),
    val currentPage: Int = 0
)

class LandingViewModel(
    private val repo: LandingRepositoryImpl = LandingRepositoryImpl(),
    private val sessionRepository: SessionRepository = SessionRepositoryImpl()
) : ViewModel() {

    // Product repository provides observable products from Room (keeps UI in sync with remote sync)
    private val productRepo = com.example.appmilsabores.data.AppDependencyContainer.createProductRepository()

    private val _uiState = MutableStateFlow(LandingUiState())
    val uiState: StateFlow<LandingUiState> = _uiState

    init {
        loadLandingContent()
        observeProducts()
        autoScrollCarousel()
    }

    private fun loadLandingContent() {
        _uiState.update {
            it.copy(
                promotions = repo.getPromotions(),
                categories = repo.getCategories(),
                // product lists will be populated by observeProducts() when DB data is available
                featured = emptyList(),
                newProducts = emptyList(),
                desserts = emptyList()
            )
        }
    }

    private fun observeProducts() {
        viewModelScope.launch {
            productRepo.observeProducts().collect { products ->
                // Map domain Product -> ProductSummary and pick sections
                val summaries = products.map { p ->
                    ProductSummary(
                        id = p.id,
                        name = p.name,
                        price = formatPrice(p.price),
                        imageRes = p.imageRes,
                        imageUrl = p.imageUrl
                    )
                }

                val featured = summaries.take(3)
                val newProducts = summaries.drop(3).take(3).ifEmpty { summaries.take(3) }
                val desserts = summaries.filter { it.name.contains("Brownie", ignoreCase = true) || it.name.contains("Postre", ignoreCase = true) }.take(3)

                _uiState.update {
                    it.copy(
                        featured = featured,
                        newProducts = newProducts,
                        desserts = if (desserts.isEmpty()) summaries.take(3) else desserts
                    )
                }
            }
        }
    }

    private fun formatPrice(value: Double): String {
        // Simple thousands separator with dot and leading $
        val intPart = value.toLong()
        val formatted = java.text.NumberFormat.getInstance(java.util.Locale.GERMANY).format(intPart)
        return "\$$formatted"
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
