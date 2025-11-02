// En el archivo ui/home/HomeViewModel.kt
package com.example.appmilsabores.ui.home

import androidx.lifecycle.ViewModel
import com.example.appmilsabores.data.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel : ViewModel() {
    // StateFlow es un flujo de datos que emite el estado actual a sus observadores.
    // Es ideal para que la UI reaccione a los cambios de datos.
    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos: StateFlow<List<Producto>> = _productos

    init {
        // Este bloque se ejecuta cuando se crea el ViewModel por primera vez.
        // Aquí cargaremos los datos iniciales (por ahora, serán datos de ejemplo).
        cargarProductosDeEjemplo()
    }

    private fun cargarProductosDeEjemplo() {
        val listaDeEjemplo = listOf(
            Producto("1", "Torta de Chocolate", "Deliciosa torta con cobertura de chocolate.", 25.50, ""),
            Producto("2", "Pie de Limón", "Clásico pie con merengue.", 18.00, ""),
            Producto("3", "Cheesecake de Fresa", "Cremoso cheesecake con mermelada de fresa.", 22.00, "")
        )
        _productos.value = listaDeEjemplo // Actualizamos el valor del StateFlow
    }
}
    