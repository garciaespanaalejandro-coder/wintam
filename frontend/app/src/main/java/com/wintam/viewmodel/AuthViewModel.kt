package com.wintam.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wintam.data.repository.AuthRepository
import com.wintam.data.remote.dto.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AuthUiState{
    object Idle: AuthUiState()
    object Loading: AuthUiState()
    data class Success(val message: String): AuthUiState()
    data class Error(val message: String): AuthUiState()
}

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState
    private val _pendingEmail = MutableStateFlow("")
    val pendingEmail: StateFlow<String> = _pendingEmail

    fun register(request: RegisterRequest) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            _pendingEmail.value = request.email
            repository.register(request).fold(
                onSuccess = { _uiState.value = AuthUiState.Success(it.message) },
                onFailure = { _uiState.value = AuthUiState.Error(it.message ?: "Error desconocido") }
            )
        }
    }

    fun signIn(request: LoginRequest) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            repository.signIn(request).fold(
                onSuccess = { _uiState.value = AuthUiState.Success("Login correcto") },
                onFailure = { _uiState.value = AuthUiState.Error(it.message ?: "Error desconocido") }
            )
        }
    }

    fun verifyEmail(request: VerifyRequest) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            repository.verifyEmail(request).fold(
                onSuccess = { _uiState.value = AuthUiState.Success("Cuenta verificada") },
                onFailure = { _uiState.value = AuthUiState.Error(it.message ?: "Error desconocido") }
            )
        }
    }

    fun recoverPassword(request: RecoverRequest) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            _pendingEmail.value = request.email
            repository.recoverPassword(request).fold(
                onSuccess = { _uiState.value = AuthUiState.Success(it.message) },
                onFailure = { _uiState.value = AuthUiState.Error(it.message ?: "Error desconocido") }
            )
        }
    }

    fun resetPassword(request: ResetPasswordRequest) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            repository.resetPassword(request).fold(
                onSuccess = { _uiState.value = AuthUiState.Success(it.message) },
                onFailure = { _uiState.value = AuthUiState.Error(it.message ?: "Error desconocido") }
            )
        }
    }

    fun resetState() {
        _uiState.value = AuthUiState.Idle
    }
}