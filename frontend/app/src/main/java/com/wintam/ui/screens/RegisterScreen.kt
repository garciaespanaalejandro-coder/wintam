package com.wintam.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import com.wintam.ui.theme.Surface
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
import androidx.compose.material.icons.filled.Visibility
import com.wintam.R
import com.wintam.ui.theme.Border
import com.wintam.ui.theme.Burgundy
import com.wintam.ui.theme.DMSans
import com.wintam.ui.theme.PlayfairDisplay
import com.wintam.ui.theme.TextPrimary
import com.wintam.ui.theme.TextSecondary
import com.wintam.viewmodel.AuthUiState
import com.wintam.viewmodel.AuthViewModel
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import com.wintam.data.remote.dto.RegisterRequest
import com.wintam.ui.components.WintamTextField
import com.wintam.ui.dialogs.WintamDatePickerDialog
import com.wintam.ui.theme.BurgundySoft
import com.wintam.ui.theme.Cream
import com.wintam.ui.theme.Error
import com.wintam.utils.formatDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onNavigateToVerify: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var birthdate by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPassword by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialDisplayMode = DisplayMode.Picker
    )
    val passwordsMatch = password == confirmPassword

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Success) {
            viewModel.resetState()
            onNavigateToVerify()
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
            Image(
                painter = painterResource(id = R.drawable.logo_burgundy),
                contentDescription = "Wintam logo",
                modifier = Modifier.size(72.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Crear cuenta",
                fontFamily = PlayfairDisplay,
                fontSize = 28.sp,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Únete a la comunidad",
                fontFamily = DMSans,
                fontSize = 14.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(40.dp))

            WintamTextField(value = name, onValueChange = { name = it }, label = "Nombre")

            Spacer(modifier = Modifier.height(16.dp))

            WintamTextField(value = surname, onValueChange = { surname = it }, label = "Apellidos")

            Spacer(modifier = Modifier.height(16.dp))

            WintamTextField(
                value = username,
                onValueChange = { username = it },
                label = "Nombre de usuario"
            )


            Spacer(modifier = Modifier.height(16.dp))

            WintamTextField(
                value = email,
                onValueChange = {email = it},
                label = "Email",
                keyboardType = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(16.dp))

            WintamTextField(
                value = password,
                onValueChange = { password = it },
                label = "Contraseña",
                keyboardType = KeyboardType.Password,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = null,
                            tint = TextSecondary
                        )
                    }
                }
            )
            if (password.isNotBlank() && password.length < 6) {
                Text(
                    text = "La contraseña debe tener al menos 6 caracteres",
                    fontFamily = DMSans,
                    fontSize = 12.sp,
                    color = Error
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            WintamTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = "Confirmar contraseña",
                keyboardType = KeyboardType.Password,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = null,
                            tint = TextSecondary
                        )
                    }
                }
            )

            if (!passwordsMatch && confirmPassword.isNotBlank()) {
                Text(
                    text = "Las contraseñas no coinciden",
                    fontFamily = DMSans,
                    fontSize = 12.sp,
                    color = Error
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            WintamTextField(
                value = birthdate,
                onValueChange = {},
                label = "Fecha de nacimiento",
                readOnly = true,
                modifier = Modifier.clickable { showDatePicker = true },
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(
                            Icons.Default.DateRange,
                            contentDescription = null,
                            tint = TextSecondary
                        )
                    }
                }
            )

            if (showDatePicker) {
                WintamDatePickerDialog(
                    datePickerState = datePickerState,
                    onConfirm = { millis -> birthdate = formatDate(millis) },
                    onDismiss = { showDatePicker = false }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.register(
                        RegisterRequest(
                            name = name.trim(),
                            surname = surname.trim(),
                            username = username.trim(),
                            email = email.trim(),
                            password = password,
                            birthdate = birthdate
                        )
                    )
                },
                enabled = name.isNotBlank() && surname.isNotBlank() && username.isNotBlank()
                        && email.isNotBlank() && password.isNotBlank() && birthdate.isNotBlank()
                        && passwordsMatch && password.length >= 6 && uiState !is AuthUiState.Loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Burgundy,
                    disabledContainerColor = BurgundySoft
                )
            ) {
                if (uiState is AuthUiState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = Cream,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Crear cuenta",
                        fontFamily = DMSans,
                        fontSize = 15.sp,
                        color = Cream
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))


            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "¿Ya tienes cuenta? ",
                    fontFamily = DMSans,
                    fontSize = 14.sp,
                    color = TextSecondary
                )
                Text(
                    text = "Inicia sesión",
                    fontFamily = DMSans,
                    fontSize = 14.sp,
                    color = Burgundy,
                    modifier = Modifier.clickable { onNavigateToLogin() }
                )
            }
        }
    }
}