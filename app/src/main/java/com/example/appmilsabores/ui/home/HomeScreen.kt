// En ui/home/HomeScreen.kt
package com.example.appmilsabores.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.appmilsabores.ui.navigation.AppScreens

// Función composable para la pantalla de inicio

@Composable
fun HomeScreen(navController: NavController) {
    // Usamos una columna para apilar los elementos verticalmente
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp) // Espacio entre los botones
    ) {
        // Título de bienvenida
        Text(
            text = "Bienvenido a Mil Sabores",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Botón para Productos
        CategoryButton(
            text = "Productos",
            onClick = { navController.navigate(AppScreens.ProductsScreen.route) }
        )

        // Botón para Blog
        CategoryButton(
            text = "Blog",
            onClick = { navController.navigate(AppScreens.BlogScreen.route) }
        )

        // Botón para Contacto
        CategoryButton(
            text = "Contacto",
            onClick = { navController.navigate(AppScreens.ContactScreen.route) }
        )
    }
}

@Composable
fun CategoryButton(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(20.dp)) // Bordes redondeados
            .background(MaterialTheme.colorScheme.primary) // Color de fondo del tema
            .clickable(onClick = onClick), // Hace que sea clickeable
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
