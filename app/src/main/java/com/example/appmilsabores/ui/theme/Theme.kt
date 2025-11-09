// Reemplaza todo el contenido de Theme.kt con este código
package com.example.appmilsabores.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

// --- AÑADIMOS ESTAS LÍNEAS DE IMPORTACIÓN ---
import com.example.appmilsabores.ui.theme.Pink40
import com.example.appmilsabores.ui.theme.Pink80
import com.example.appmilsabores.ui.theme.Purple40
import com.example.appmilsabores.ui.theme.Purple80
import com.example.appmilsabores.ui.theme.PurpleGrey40
import com.example.appmilsabores.ui.theme.PurpleGrey80
// ------------------------------------------

// Paleta de colores para el tema oscuro
private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

// Paleta de colores para el tema claro
private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

@Composable
fun AppMilSaboresTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Descomenta esto también si ya tienes el archivo Typography.kt
        content = content
    )
}
