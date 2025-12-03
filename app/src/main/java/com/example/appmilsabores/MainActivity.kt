// En el archivo MainActivity.kt
package com.example.appmilsabores

import android.os.Bundle
import android.graphics.Color as AndroidColor
import android.util.Log
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.example.appmilsabores.presentation.ui.theme.AppMilSaboresTheme // El tema de tu app
import com.example.appmilsabores.presentation.navigation.AppNavGraph
import com.example.appmilsabores.network.ApiClient
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Make status bar transparent and allow our Compose UI to draw behind it.
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = AndroidColor.TRANSPARENT
        // Ensure status bar icons are dark when background is light; set to false if using dark backgrounds.
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true
        setContent {
            AppMilSaboresTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavGraph()
                }
            }
        }

        // Llamada inicial para verificar que la API responde y ver los datos en Logcat
        lifecycleScope.launch {
            try {
                val productos = ApiClient.service.getProductos()
                Log.d("API", "Productos obtenidos: ${'$'}productos")
            } catch (e: Exception) {
                Log.e("API", "Error al obtener productos", e)
            }
        }
    }
}

