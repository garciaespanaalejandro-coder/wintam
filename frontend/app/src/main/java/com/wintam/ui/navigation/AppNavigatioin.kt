package com.wintam.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.wintam.data.TokenManager
import com.wintam.data.repository.AuthRepository
import com.wintam.ui.screens.SplashScreen
import com.wintam.viewmodel.AuthViewModel
import com.wintam.viewmodel.AuthViewModelFactory

