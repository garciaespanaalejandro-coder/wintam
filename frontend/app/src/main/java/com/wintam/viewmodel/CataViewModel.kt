package com.wintam.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wintam.data.remote.dto.CataResponse
import com.wintam.data.repository.CataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


sealed class CataUiState{
    object Idle: CataUiState()
    object Loading: CataUiState()
    data class Success(val message: String?): CataUiState()
    data class Error(val message: String): CataUiState()
}

class CataViewModel(private val repository: CataRepository): ViewModel(){

    private val _uiState = MutableStateFlow<CataUiState>(CataUiState.Idle)
    val uiState: StateFlow<CataUiState> = _uiState

    private val _catas= MutableStateFlow<List<CataResponse>>(emptyList())
    val catas: StateFlow<List<CataResponse>> = _catas
    private val _selectedCata = MutableStateFlow<CataResponse?>(null)
    val selectedCata: StateFlow<CataResponse?> = _selectedCata
    fun loadCatas(){
        viewModelScope.launch {
            _uiState.value = CataUiState.Loading
            repository.getAllCatas().fold(
                onSuccess = {
                    _catas.value= it
                    _uiState.value= CataUiState.Idle
                },
                onFailure = { _uiState.value = CataUiState.Error(it.message ?: "Error desconocido") }
            )
        }
    }

    fun searchCata(
        title: String? = null,
        wineType: String? = null,
        experienceLevel: String? = null,
        location: String? = null,
        cataStatus: String? = null
    ) {
        viewModelScope.launch {
            _uiState.value = CataUiState.Loading
            repository.searchCata(title, wineType, experienceLevel, location, cataStatus).fold(
                onSuccess = {
                    _catas.value = it
                    _uiState.value = CataUiState.Idle
                },
                onFailure = { _uiState.value = CataUiState.Error(it.message ?: "Error desconocido") }
            )
        }
    }

    fun selectCata(id: Long) {
        _selectedCata.value = _catas.value.find { it.id == id }
    }
}