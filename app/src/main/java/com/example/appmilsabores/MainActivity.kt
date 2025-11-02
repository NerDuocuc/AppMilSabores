// En el archivo MainActivity.kt
package com.example.appmilsabores

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.appmilsabores.ui.home.HomeScreen // Importa tu nueva pantalla
import com.example.appmilsabores.ui.theme.AppMilSaboresTheme // El tema de tu app

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppMilSaboresTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Aquí llamamos a nuestra pantalla principal
                    HomeScreen()
                }
            }
        }
    }
}
    