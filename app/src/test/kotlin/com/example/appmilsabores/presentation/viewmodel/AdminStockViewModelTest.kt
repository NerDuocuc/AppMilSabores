package com.example.appmilsabores.presentation.viewmodel

import com.example.appmilsabores.domain.model.Product
import com.example.appmilsabores.domain.repository.ProductRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Test

class AdminStockViewModelTest {

    @Test
    fun `confirmStockChange delegates to repository and returns success`() = runTest {
        // provide a TestDispatcher as main for viewModelScope/stateIn
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)
        val repo = mockk<ProductRepository>()
        val product = Product(id = 1, name = "X", price = 10.0, rating = 0f, reviews = 0, imageRes = 0, imageUrl = null, stock = 5, category = "c", description = "", codigo = "1")
        coEvery { repo.getProductById(1) } returns product
        coEvery { repo.updateProductStock(1, 7) } returns true
        coEvery { repo.observeProducts() } returns flowOf(listOf(product))

        val vm = AdminStockViewModel(productRepo = repo)
        val success = vm.confirmStockChange(1, 7)
        assertTrue(success)
        coVerify { repo.updateProductStock(1, 7) }
        Dispatchers.resetMain()
    }
}
