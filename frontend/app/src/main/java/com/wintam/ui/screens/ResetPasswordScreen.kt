package com.wintam.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wintam.R
import com.wintam.data.remote.dto.ResetPasswordRequest
import com.wintam.ui.components.WintamTextField
import com.wintam.ui.theme.Border
import com.wintam.ui.theme.Burgundy
import com.wintam.ui.theme.BurgundySoft
import com.wintam.ui.theme.Cream
import com.wintam.ui.theme.DMSans
import com.wintam.ui.theme.Error
import com.wintam.ui.theme.PlayfairDisplay
import com.wintam.ui.theme.Surface
import com.wintam.ui.theme.TextPrimary
import com.wintam.ui.theme.TextSecondary
import com.wintam.viewmodel.AuthUiState
import com.wintam.viewmodel.AuthViewModel

@Composable
fun ResetPasswordScreen(
    viewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val pendingEmail by viewModel.pendingEmail.collectAsState()
    var code by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPassword by remember { mutableStateOf("") }
    val passwordsMatch = newPassword == confirmPassword
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Success) {
            viewModel.resetState()
            onNavigateToLogin()
        }
        if (uiState is AuthUiState.Error) {
            snackbarHostState.showSnackbar((uiState as AuthUiState.Error).message)
            viewModel.resetState()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Surface)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(64.dp))
            Image(painter = painterResource(id = R.drawable.logo_burgundy), contentDescription = "Wintam logo", modifier = Modifier.size(72.dp))
            Spacer(modifier = Modifier.height(24.dp))
            Text("Nueva contraseña", fontFamily = PlayfairDisplay, fontSize = 28.sp, color = TextPrimary)
            Spacer(modifier = Modifier.height(6.dp))
            Text("Introduce tu nuevo código y contraseña.", fontFamily = DMSans, fontSize = 14.sp, color = TextSecondary)
            Spacer(modifier = Modifier.height(40.dp))

            WintamTextField(
                value = code,
                onValueChange = { code = it },
                label = "Código de verificación",
                keyboardType = KeyboardType.Number
            )
            Spacer(modifier = Modifier.height(16.dp))

            WintamTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = "Contraseña",
                keyboardType = KeyboardType.Password,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility, contentDescription = null, tint = TextSecondary)
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))

            WintamTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = "Confirmar contraseña",
                keyboardType = KeyboardType.Password,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility, contentDescription = null, tint = TextSecondary)
                    }
                }
            )
            if (!passwordsMatch && confirmPassword.isNotBlank()) {
                Text("Las contraseñas no coinciden", fontFamily = DMSans, fontSize = 12.sp, color = Error)
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.resetPassword(ResetPasswordRequest(email = pendingEmail, code = code, newPassword = newPassword)) },
                enabled = code.isNotBlank() && passwordsMatch && newPassword.isNotBlank() && uiState !is AuthUiState.Loading,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Burgundy, disabledContainerColor = BurgundySoft)
            ) {
                if (uiState is AuthUiState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(22.dp), color = Cream, strokeWidth = 2.dp)
                } else {
                    Text("Confirmar", fontFamily = DMSans, fontSize = 15.sp, color = Cream)
                }
            }
        }
    }
}