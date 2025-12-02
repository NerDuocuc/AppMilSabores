package com.example.appmilsabores.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.graphics.Color
import com.example.appmilsabores.presentation.ui.theme.TopBarAndDrawerColor
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.appmilsabores.presentation.viewmodel.AdminProductsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminProductsScreen(
    navController: NavController,
    viewModel: AdminProductsViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val pending = remember { mutableStateOf<Pair<Int, Int>?>(null) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Administrar Productos") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = TopBarAndDrawerColor,
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(padding)) {
            items(state.products, key = { it.id }) { product ->
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)) {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp), verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text(product.name)
                            Text("Stock: ${product.stock}")
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { pending.value = product.id to -1 }) {
                                Icon(Icons.Default.Remove, contentDescription = "-")
                            }
                            IconButton(onClick = { pending.value = product.id to 1 }) {
                                Icon(Icons.Default.Add, contentDescription = "+")
                            }
                        }
                    }
                }
            }
        }
    }

    // Confirmation dialog for admin screen
    val pendingPair = pending.value
    if (pendingPair != null) {
        val (productId, delta) = pendingPair
        val product = state.products.firstOrNull { it.id == productId }
        if (product != null) {
            AlertDialog(
                onDismissRequest = { pending.value = null },
                confirmButton = {
                    TextButton(onClick = {
                        if (delta > 0) viewModel.incrementStock(productId) else viewModel.decrementStock(productId)
                        pending.value = null
                    }) { Text("Confirmar") }
                },
                dismissButton = {
                    TextButton(onClick = { pending.value = null }) { Text("Cancelar") }
                },
                title = { Text(if (delta > 0) "Aumentar stock" else "Disminuir stock") },
                text = { Text("¿Deseas ${if (delta > 0) "aumentar" else "disminuir"} el stock de '${product.name}' en ${kotlin.math.abs(delta)} unidad(es)?\nStock actual: ${product.stock}") }
            )
        } else {
            pending.value = null
        }
    }
}
