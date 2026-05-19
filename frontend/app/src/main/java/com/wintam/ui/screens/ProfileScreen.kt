package com.wintam.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.wintam.data.remote.dto.UpdateProfileRequest
import com.wintam.ui.theme.DMSans
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wintam.ui.theme.Border
import com.wintam.ui.theme.Burgundy
import com.wintam.ui.theme.Cream
import com.wintam.ui.theme.PlayfairDisplay
import com.wintam.ui.theme.TextPrimary
import com.wintam.ui.theme.TextSecondary
import com.wintam.viewmodel.UserUiState
import com.wintam.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: UserViewModel,
    onNavigateToFeed: () -> Unit,
    onNavigateToCreateCata: () -> Unit
){
    val uiState by viewModel.uiState.collectAsState()
    val profile by viewModel.profile.collectAsState()
    var editMode by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    LaunchedEffect(profile) {
        profile?.let {
            name = it.name
            surname = it.surname
            username = it.username
            description = it.description ?: ""
        }
    }

    LaunchedEffect(uiState) {
        when (uiState) {
            is UserUiState.Success -> {
                snackbarHostState.showSnackbar("Perfil actualizado correctamente")
                editMode = false
                viewModel.loadProfile()
                viewModel.resetState()
            }
            is UserUiState.Error -> {
                snackbarHostState.showSnackbar((uiState as UserUiState.Error).message)
                viewModel.resetState()
            }
            else -> {}
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Mi perfil", fontFamily = DMSans) },
                actions = {
                    TextButton(onClick = {
                        if (editMode) {
                            viewModel.updateProfile(
                                UpdateProfileRequest(
                                    name = name,
                                    surname = surname,
                                    username = username,
                                    description = description
                                )
                            )
                        } else {
                            editMode = true
                        }
                    }) {
                        Text(
                            if (editMode) "Guardar" else "Editar",
                            color = Burgundy,
                            fontFamily = DMSans
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text("Inicio") },
                    selected = false,
                    onClick = { onNavigateToFeed() }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Add, contentDescription = null) },
                    label = { Text("Crear") },
                    selected = false,
                    onClick = { onNavigateToCreateCata() }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    label = { Text("Perfil") },
                    selected = true,
                    onClick = {}
                )
            }
        }
    ){paddingValues ->
        profile?.let {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .background(Burgundy, RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = it.username.take(1).uppercase(),
                        fontFamily = PlayfairDisplay,
                        fontSize = 48.sp,
                        color = Cream
                    )
                }

                Text(
                    text = "Karma: ${it.karma}",
                    fontFamily= DMSans,
                    fontSize = 14.sp,
                    color = TextSecondary
                )

                if (editMode) {
                    ProfileField("Nombre", name) { name = it }
                    ProfileField("Apellido", surname) { surname = it }
                    ProfileField("Username", username) { username = it }
                    ProfileField("Descripción", description, singleLine = false) { description = it }
                } else {
                    InfoRow(Icons.Default.Person, "NOMBRE", "${it.name} ${it.surname}")
                    InfoRow(Icons.Default.AlternateEmail, "USERNAME", "@${it.username}")
                    InfoRow(Icons.Default.Email, "EMAIL", it.email)
                    InfoRow(Icons.Default.Info, "DESCRIPCIÓN", it.description ?: "Sin descripción")
                }
            }
        }

    }

}

@Composable
fun ProfileField(
    label: String,
    value: String,
    singleLine: Boolean = true,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, fontFamily = DMSans, fontSize = 14.sp) },
        singleLine = singleLine,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Burgundy,
            unfocusedBorderColor = Border,
            focusedLabelColor = Burgundy,
            unfocusedLabelColor = TextSecondary,
            cursorColor = Burgundy,
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary
        )
    )
}