package com.example.appmilsabores.presentation.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable


private val AppColorScheme = lightColorScheme(
    primary = PrimaryPurple,       // Usará el nuevo valor #B08D57
    secondary = CardBackgroundColor, // Usará el nuevo valor #FFFFFF (o el secundario que definas)
    background = PureBlackBackground,  // Usará el nuevo valor #FFFFFF
    surface = CardBackgroundColor,     // Usará el nuevo valor #FFFFFF


    onPrimary = WebFondoClaro,      // Texto blanco sobre el botón de acento
    onSecondary = MainTextColor,
    onBackground = MainTextColor,   // Texto oscuro sobre el fondo blanco
    onSurface = MainTextColor       // Texto oscuro sobre las tarjetas blancas
)

@Composable
fun AppMilSaboresTheme(
    // Usamos tema claro por defecto ya que el diseño web usa fondo blanco
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = AppColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
