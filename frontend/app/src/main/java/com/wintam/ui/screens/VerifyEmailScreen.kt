package com.wintam.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalTextStyle
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wintam.R
import com.wintam.data.remote.dto.VerifyRequest
import com.wintam.ui.theme.Border
import com.wintam.ui.theme.Burgundy
import com.wintam.ui.theme.BurgundySoft
import com.wintam.ui.theme.Cream
import com.wintam.ui.theme.DMSans
import com.wintam.ui.theme.PlayfairDisplay
import com.wintam.ui.theme.Surface
import com.wintam.ui.theme.TextPrimary
import com.wintam.ui.theme.TextSecondary
import com.wintam.viewmodel.AuthUiState
import com.wintam.viewmodel.AuthViewModel

@Composable
fun VerifyEmailScreen(
    viewModel: AuthViewModel,
    onVerifySuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var code by remember { mutableStateOf("") }
    val pendingEmail by viewModel.pendingEmail.collectAsState()
    val focusRequesters = remember { List(6) { FocusRequester() } }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Success) {
            viewModel.resetState()
            onVerifySuccess()
        }
        if (uiState is AuthUiState.Error) {
            snackbarHostState.showSnackbar((uiState as AuthUiState.Error).message)
            viewModel.resetState()
        }
    }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {padding ->
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
                text = "Verifica tu email",
                fontFamily = PlayfairDisplay,
                fontSize = 28.sp,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Hemos enviado un código a ${pendingEmail}",
                fontFamily = DMSans,
                fontSize = 14.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(40.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                (0..5).forEach { index ->
                    OutlinedTextField(
                        value = if(index <code.length) code[index].toString() else "",
                        onValueChange = { newValue ->
                            if(newValue.length <= 1 && newValue.all{it.isDigit()}){
                                val newCode = code.toMutableList()
                                if (newValue.isEmpty()) {
                                    if (index < code.length) newCode.removeAt(index)
                                    if (index > 0) focusRequesters[index - 1].requestFocus()
                                } else {
                                    if (index < code.length) newCode[index] = newValue[0]
                                    else newCode.add(newValue[0])
                                    if (index < 5) focusRequesters[index + 1].requestFocus()
                                }
                                code = newCode.joinToString("")
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .focusRequester(focusRequesters[index]),
                        textStyle = LocalTextStyle.current.copy(
                            textAlign = TextAlign.Center,
                            fontFamily = DMSans,
                            fontSize = 20.sp,
                            color = TextPrimary
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp),
                        colors= OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Burgundy,
                            unfocusedBorderColor = Border,
                            cursorColor = Burgundy
                        )

                    )
                }
            }
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    viewModel.verifyEmail(VerifyRequest(email = pendingEmail, code = code))
                },
                enabled = pendingEmail.isNotBlank() && code.length==6 && uiState !is AuthUiState.Loading,
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
                        text = "Verificar",
                        fontFamily = DMSans,
                        fontSize = 15.sp,
                        color = Cream
                    )
                }
            }

        }
    }
}