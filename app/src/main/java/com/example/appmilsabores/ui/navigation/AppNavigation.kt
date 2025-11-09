// En ui/navigation/AppNavigation.kt
package com.example.appmilsabores.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appmilsabores.ui.screens.BlogScreen
import com.example.appmilsabores.ui.screens.ContactScreen
import com.example.appmilsabores.ui.screens.ProductsScreen
import com.example.appmilsabores.ui.home.HomeScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController() // Controlador que gestiona la navegación

    // NavHost es el contenedor que intercambiará las pantallas
    NavHost(
        navController = navController,
        startDestination = AppScreens.HomeScreen.route // La pantalla inicial
    ) {
        // Definimos cada pantalla de nuestra app
        composable(route = AppScreens.HomeScreen.route) {
            HomeScreen(navController) // Le pasamos el controlador para que pueda navegar
        }
        composable(route = AppScreens.ProductsScreen.route) {
            ProductsScreen(navController)
        }
        composable(route = AppScreens.BlogScreen.route) {
            BlogScreen(navController)
        }
        composable(route = AppScreens.ContactScreen.route) {
            ContactScreen(navController)
        }
    }
}
