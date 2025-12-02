package com.example.appmilsabores.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.appmilsabores.presentation.ui.components.EmptyProductView
import com.example.appmilsabores.presentation.ui.components.ProductFilterSheet
import com.example.appmilsabores.presentation.ui.components.ProductListItem
import com.example.appmilsabores.presentation.ui.theme.PrimaryPurple
import com.example.appmilsabores.presentation.ui.theme.PureBlackBackground
import com.example.appmilsabores.presentation.ui.theme.TopBarAndDrawerColor
import com.example.appmilsabores.presentation.viewmodel.ProductListEvent
import com.example.appmilsabores.presentation.viewmodel.ProductListViewModel
import com.example.appmilsabores.presentation.viewmodel.ProductListViewModelFactory
import kotlinx.coroutines.flow.collect
import com.example.appmilsabores.AppMilSaboresApplication
import com.example.appmilsabores.domain.model.SessionState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    navController: NavController,
    categoryName: String,
    viewModel: ProductListViewModel = viewModel(factory = ProductListViewModelFactory())
) {
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    // collect session to determine admin privileges
    val session = AppMilSaboresApplication.sessionPreferences.sessionFlow.collectAsState(initial = SessionState()).value
    val isAdmin = session.email == "system@milsabores.local"
    var showFilterSheet by remember { mutableStateOf(false) }

    LaunchedEffect(categoryName) {
        viewModel.loadProducts(categoryName)
    }

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            if (event is ProductListEvent.ItemAdded) {
                snackbarHostState.showSnackbar("Producto agregado al carrito")
            }
        }
    }

    // pending stock change: Pair(productId, delta)
    val pendingStockChange = remember { mutableStateOf<Pair<Int, Int>?>(null) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(categoryName, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = TopBarAndDrawerColor,
                    titleContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver", tint = Color.White)
                    }
                }
            )
        },
        containerColor = PureBlackBackground
    ) { paddingValues ->
        when {
            state.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PrimaryPurple)
            }

            state.errorMessage != null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Error: ${state.errorMessage}", color = Color.Red)
            }

            state.products.isEmpty() -> EmptyProductView(categoryName)

            else -> Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                FilterSummaryBar(
                    total = state.products.size,
                    activeFilters = state.filters.activeFiltersCount(),
                    onOpenFilters = { showFilterSheet = true }
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(state.products, key = { it.id }) { product ->
                        ProductListItem(
                            product = product,
                            onAddToCart = { viewModel.addProductToCart(product.id) },
                            navController = navController,
                            isAdmin = isAdmin,
                            onIncreaseStock = { id -> pendingStockChange.value = id to 1 },
                            onDecreaseStock = { id -> pendingStockChange.value = id to -1 }
                        )
                    }
                }
            }
        }
    }

    if (showFilterSheet) {
        ProductFilterSheet(
            currentFilters = state.filters,
            availablePriceRange = state.availablePriceRange,
            onApply = viewModel::applyFilters,
            onReset = viewModel::resetFilters,
            onDismiss = { showFilterSheet = false }
        )
    }

    // Confirmation dialog for stock changes
    val pending = pendingStockChange.value
    if (pending != null) {
        val (productId, delta) = pending
        val product = state.products.firstOrNull { it.id == productId }
        if (product != null) {
            AlertDialog(
                onDismissRequest = { pendingStockChange.value = null },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.updateStock(productId, delta)
                        pendingStockChange.value = null
                    }) { Text("Confirmar") }
                },
                dismissButton = {
                    TextButton(onClick = { pendingStockChange.value = null }) { Text("Cancelar") }
                },
                title = { Text(if (delta > 0) "Aumentar stock" else "Disminuir stock") },
                text = { Text("¿Deseas ${if (delta > 0) "aumentar" else "disminuir"} el stock de '${product.name}' en ${kotlin.math.abs(delta)} unidad(es)?\nStock actual: ${product.stock}") }
            )
        } else {
            // product not found -> clear
            pendingStockChange.value = null
        }
    }
}

@Composable
private fun FilterSummaryBar(total: Int, activeFilters: Int, onOpenFilters: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$total productos",
            color = Color.LightGray,
            style = MaterialTheme.typography.bodyMedium
        )

        AssistChip(
            onClick = onOpenFilters,
            label = {
                Text(
                    text = if (activeFilters > 0) "Filtros ($activeFilters)" else "Filtros",
                    color = Color.White
                )
            },
            leadingIcon = {
                Icon(Icons.Filled.FilterList, contentDescription = null, tint = Color.White)
            },
            colors = AssistChipDefaults.assistChipColors(
                containerColor = Color.DarkGray.copy(alpha = 0.4f)
            )
        )
    }
}
