// En ui/navigation/AppScreens.kt
package com.example.appmilsabores.ui.navigation

// Objeto sellado para definir las rutas de navegación de forma segura
sealed class AppScreens(val route: String) {
    object HomeScreen : AppScreens("home_screen")
    object ProductsScreen : AppScreens("products_screen")
    object BlogScreen : AppScreens("blog_screen")
    object ContactScreen : AppScreens("contact_screen")
}
