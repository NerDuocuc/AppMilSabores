package com.example.appmilsabores.presentation.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme // Puedes cambiarlo a lightColorScheme si lo prefieres
import androidx.compose.runtime.Composable


private val AppColorScheme = darkColorScheme( // Cambiado de 'DarkColorScheme' para evitar confusión
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
    //  Tema "oscuro" por simplicidad, aunque los colores son claros
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = AppColorScheme // Usamos nuestro esquema de colores actualizado

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
