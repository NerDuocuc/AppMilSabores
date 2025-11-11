package com.example.appmilsabores.presentation.ui.screens

import com.example.appmilsabores.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.appmilsabores.presentation.ui.theme.WebFondoClaro
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.appmilsabores.presentation.viewmodel.SplashDestination
import com.example.appmilsabores.presentation.viewmodel.SplashViewModel

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: SplashViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.destination) {
        when (state.destination) {
            SplashDestination.LOGIN -> {
                navController.navigate("login") {
                    popUpTo("splash") { inclusive = true }
                }
                viewModel.onNavigated()
            }
            SplashDestination.HOME -> {
                navController.navigate("landing_page") {
                    popUpTo("splash") { inclusive = true }
                }
                viewModel.onNavigated()
            }
            SplashDestination.NONE -> Unit
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WebFondoClaro),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = "Logo Pasteleria Mil Sabores"
        )
    }
}
