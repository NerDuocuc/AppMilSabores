// En el archivo MainActivity.kt
package com.example.appmilsabores

import android.os.Bundle
import android.graphics.Color as AndroidColor
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.appmilsabores.presentation.ui.theme.AppMilSaboresTheme // El tema de tu app
import com.example.appmilsabores.presentation.navigation.AppNavGraph


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
    }
}

