package com.wintam.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wintam.data.remote.dto.UpdateProfileRequest
import com.wintam.data.remote.dto.UserProfileResponse
import com.wintam.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class UserUiState {
    object Idle : UserUiState()
    object Loading : UserUiState()
    data class Success(val message: String? = null) : UserUiState()
    data class Error(val message: String) : UserUiState()
}

class UserViewModel(private val repository: UserRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<UserUiState>(UserUiState.Idle)
    val uiState: StateFlow<UserUiState> = _uiState

    private val _profile = MutableStateFlow<UserProfileResponse?>(null)
    val profile: StateFlow<UserProfileResponse?> = _profile

    fun loadProfile() {
        viewModelScope.launch {
            _uiState.value = UserUiState.Loading
            repository.getProfile().fold(
                onSuccess = {
                    _profile.value = it
                    _uiState.value = UserUiState.Idle
                },
                onFailure = { _uiState.value = UserUiState.Error(it.message ?: "Error desconocido") }
            )
        }
    }

    fun updateProfile(request: UpdateProfileRequest) {
        viewModelScope.launch {
            _uiState.value = UserUiState.Loading
            repository.updateProfile(request).fold(
                onSuccess = { _uiState.value = UserUiState.Success("Perfil actualizado") },
                onFailure = { _uiState.value = UserUiState.Error(it.message ?: "Error desconocido") }
            )
        }
    }

    fun resetState() {
        _uiState.value = UserUiState.Idle
    }
}