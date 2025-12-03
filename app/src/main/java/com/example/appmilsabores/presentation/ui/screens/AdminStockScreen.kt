package com.example.appmilsabores.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.foundation.clickable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.appmilsabores.domain.model.Product
import com.example.appmilsabores.presentation.viewmodel.AdminStockViewModel
import com.example.appmilsabores.presentation.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminStockScreen(
    navController: NavController,
    viewModel: AdminStockViewModel = viewModel()
) {
    val products by viewModel.products.collectAsState(initial = emptyList())
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // local pending map of productId -> pendingStock
    val pendingStocks = remember(products) {
        mutableStateMapOf<Int, Int>().apply {
            products.forEach { put(it.id, it.stock) }
        }
    }

    var selectedProductId by remember { mutableStateOf<Int?>(null) }
    var showSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Editar Stock", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = TopBarAndDrawerColor,
                    titleContentColor = androidx.compose.ui.graphics.Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.Remove, "Volver", tint = androidx.compose.ui.graphics.Color.White)
                    }
                }
            )
        },
        containerColor = PureBlackBackground,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(12.dp)
        ) {
            items(products) { product ->
                val pending = pendingStocks[product.id] ?: product.stock
                ProductStockRowPending(
                    product = product,
                    pendingStock = pending,
                    onClick = {
                        // open the central bottom sheet to edit this product
                        selectedProductId = product.id
                        showSheet = true
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        // Bottom sheet for confirming changes
        if (showSheet && selectedProductId != null) {
            val selId = selectedProductId!!
            val selProduct = products.firstOrNull { it.id == selId }
            val pending = pendingStocks[selId] ?: selProduct?.stock ?: 0

            if (selProduct != null) {
                ModalBottomSheet(onDismissRequest = {
                    // cancel and revert pending to actual DB value
                    pendingStocks[selId] = selProduct.stock
                    showSheet = false
                    selectedProductId = null
                }, containerColor = CardBackgroundColor) {
                    Column(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(selProduct.name, color = MainTextColor, fontWeight = FontWeight.Bold)
                        Text("Stock actual: ${selProduct.stock}", color = androidx.compose.ui.graphics.Color.LightGray)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { if (pending > 0) pendingStocks[selId] = pending - 1 }) {
                                Icon(Icons.Default.Remove, contentDescription = "Disminuir", tint = PrimaryPurple)
                            }
                            Text("$pending", modifier = Modifier.width(48.dp), color = MainTextColor, fontWeight = FontWeight.Bold)
                            IconButton(onClick = { if (pending < 30) pendingStocks[selId] = pending + 1 }) {
                                Icon(Icons.Default.Add, contentDescription = "Aumentar", tint = PrimaryPurple)
                            }
                        }

                        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                            TextButton(onClick = {
                                // cancel
                                pendingStocks[selId] = selProduct.stock
                                showSheet = false
                                selectedProductId = null
                            }) { Text("Cancelar") }

                            Spacer(modifier = Modifier.width(8.dp))

                            Button(onClick = {
                                // confirm and persist
                                scope.launch {
                                    val newStock = (pendingStocks[selId] ?: selProduct.stock).coerceIn(0, 30)
                                    val success = viewModel.confirmStockChange(selId, newStock)
                                    if (success) {
                                        // read back persisted value and show
                                        viewModel.getProductByIdSync(selId) { persisted ->
                                            val persistedVal = persisted?.stock ?: -1
                                            scope.launch { snackbarHostState.showSnackbar("Stock persistido: $persistedVal") }
                                        }
                                    } else {
                                        scope.launch { snackbarHostState.showSnackbar("Error al actualizar stock") }
                                    }
                                    showSheet = false
                                    selectedProductId = null
                                }
                            }) { Text("Confirmar") }
                        }
                    }
                }
            } else {
                // If product disappeared, close sheet
                showSheet = false
                selectedProductId = null
            }
        }
    }
}

@Composable
fun ProductStockRowPending(
    product: Product,
    pendingStock: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = CardBackgroundColor),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(product.name, color = MainTextColor, fontWeight = FontWeight.Bold)
                Text("Stock actual: ${product.stock}", color = androidx.compose.ui.graphics.Color.LightGray)
                Text("Nuevo: $pendingStock", color = MainTextColor, fontWeight = FontWeight.SemiBold)
            }
            // Keep right-aligned small stock indicator
            Column(horizontalAlignment = Alignment.End) {
                Text("$pendingStock", modifier = Modifier.width(36.dp), color = MainTextColor, fontWeight = FontWeight.Bold)
            }
        }
    }
}
