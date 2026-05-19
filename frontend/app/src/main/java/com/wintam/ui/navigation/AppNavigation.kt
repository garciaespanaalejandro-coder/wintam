package com.wintam.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.wintam.data.TokenManager
import com.wintam.data.repository.AuthRepository
import com.wintam.data.repository.CataRepository
import com.wintam.data.repository.InscripcionRepository
import com.wintam.ui.screens.CataDetailScreen
import com.wintam.ui.screens.CreateCataScreen
import com.wintam.ui.screens.FeedScreen
import com.wintam.ui.screens.LoginScreen
import com.wintam.ui.screens.RecoverPasswordScreen
import com.wintam.ui.screens.RegisterScreen
import com.wintam.ui.screens.ResetPasswordScreen
import com.wintam.ui.screens.SplashScreen
import com.wintam.ui.screens.StartCataScreen
import com.wintam.ui.screens.VerifyEmailScreen
import com.wintam.viewmodel.AuthViewModel
import com.wintam.viewmodel.AuthViewModelFactory
import com.wintam.viewmodel.CataViewModel
import com.wintam.viewmodel.CataViewModelFactory
import com.wintam.viewmodel.InscripcionViewModel
import com.wintam.viewmodel.InscripcionViewModelFactory

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(){
    val navController= rememberNavController()
    val context= LocalContext.current
    val tokenManager= TokenManager(context)
    val repository= AuthRepository(tokenManager)
    val authViewModel: AuthViewModel= viewModel(
        factory = AuthViewModelFactory(repository)
    )

    val cataRepository= CataRepository(tokenManager)
    val cataViewModel: CataViewModel= viewModel(
        factory = CataViewModelFactory(cataRepository)
    )

    val inscripcionRepository = InscripcionRepository(tokenManager)
    val inscripcionViewModel: InscripcionViewModel = viewModel(
        factory = InscripcionViewModelFactory(inscripcionRepository)
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

        composable("register"){
            RegisterScreen(
                viewModel= authViewModel,
                onNavigateToVerify= {
                    navController.navigate("verify"){
                        popUpTo("register") { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate("login"){
                        popUpTo("register") { inclusive = true}
                    }
                }
            )
        }

        composable("verify"){
            VerifyEmailScreen(
                viewModel = authViewModel,
                onVerifySuccess={
                    navController.navigate("login"){
                        popUpTo("verify") {inclusive = true}
                    }
                }
            )
        }

        composable("recoverPassword"){
            RecoverPasswordScreen(
                viewModel= authViewModel,
                onNavigateToResetPassword = {
                    navController.navigate("resetPassword"){
                        popUpTo("recoverPassword") {inclusive = true}
                    }
                }
            )
        }

        composable("resetPassword"){
            ResetPasswordScreen(
                viewModel= authViewModel,
                onNavigateToLogin = {
                    navController.navigate("login"){
                        popUpTo("resetPassword") {inclusive = true}
                    }
                }
            )
        }

        composable("feed"){
            FeedScreen(
                viewModel=cataViewModel,
                onNavigateToCataDetail = { id ->
                    cataViewModel.selectCata(id)
                    navController.navigate("cataDetail/$id")
                },
                onNavigateToProfile = {
                    navController.navigate("profile")
                },
                onNavigateToCreateCata = {
                    navController.navigate("createCata")
                }
            )
        }

        composable("cataDetail/{id}") {
            CataDetailScreen(
                viewModel = cataViewModel,
                inscripcionViewModel = inscripcionViewModel,
                tokenManager= tokenManager,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToStartCata = { id->
                    navController.navigate("startCata/$id")

                }
            )
        }

        composable("createCata"){
            CreateCataScreen(
                viewModel = cataViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onCataCreated = {
                    navController.navigate("feed") {
                        popUpTo("createCata") { inclusive = true }
                    }
                }
            )
        }

        composable("startCata/{id}"){
            StartCataScreen(
                viewModel = cataViewModel,
                onNavigateBack ={
                  navController.popBackStack()
              }
            )
        }
    }
}