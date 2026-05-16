package com.wintam.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.wintam.data.TokenManager
import com.wintam.data.repository.AuthRepository
import com.wintam.ui.screens.LoginScreen
import com.wintam.ui.screens.SplashScreen
import com.wintam.viewmodel.AuthViewModel
import com.wintam.viewmodel.AuthViewModelFactory

@Composable
fun AppNavigation(){
    val navController= rememberNavController()
    val context= LocalContext.current
    val tokenManager= TokenManager(context)
    val repository= AuthRepository(tokenManager)
    val authViewModel: AuthViewModel= viewModel(
        factory = AuthViewModelFactory(repository)
    )

    NavHost(
        navController= navController,
        startDestination = "splash"
    ){
        composable("splash"){
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate("login"){
                        popUpTo("splash") { inclusive= true}
                    }
                }
            )
        }

        composable("login"){
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate("feed") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate("register")
                },
                onNavigateToRecoverPassword = {
                    navController.navigate("recoverPassword")
                }
            )
        }
    }
}