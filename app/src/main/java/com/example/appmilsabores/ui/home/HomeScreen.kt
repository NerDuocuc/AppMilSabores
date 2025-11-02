// En el archivo ui/home/HomeScreen.kt
package com.example.appmilsabores.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun HomeScreen(homeViewModel: HomeViewModel = viewModel()) {
    // 'collectAsState' observa el StateFlow del ViewModel.
    // Cada vez que 'productos' cambie en el ViewModel, esta variable 'listaProductos'
    // se actualizará y la UI se "recompondrá" (se redibujará) automáticamente.
    val listaProductos by homeViewModel.productos.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Text("¡Bienvenido a Mil Sabores!", fontSize = 24.sp)

        // Mostramos los nombres de los productos de la lista
        listaProductos.forEach { producto ->
            Text("${producto.nombre} - $${producto.precio}", modifier = Modifier.padding(top = 8.dp))
        }
    }
}
    