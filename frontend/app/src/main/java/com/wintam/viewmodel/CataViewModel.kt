package com.wintam.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wintam.data.remote.dto.CataResponse
import com.wintam.data.remote.dto.CreateCataRequest
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

    private val _attendanceCode= MutableStateFlow<String?>(null)
    val attendanceCode: StateFlow<String?> = _attendanceCode

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

    fun createCata(request: CreateCataRequest){
        viewModelScope.launch {
            _uiState.value= CataUiState.Loading
            repository.createCata(request).fold(
                onSuccess = {_uiState.value= CataUiState.Success(it.message)},
                onFailure = {_uiState.value= CataUiState.Error(it.message ?: "Error desconocido")}
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

    fun startCata(id: Long){
        viewModelScope.launch {
            _uiState.value= CataUiState.Loading
            repository.startCata(id).fold(
                onSuccess = {
                    _attendanceCode.value=it.code
                    _uiState.value= CataUiState.Idle
                },
                onFailure = {_uiState.value= CataUiState.Error(it.message?: "Error desconocido")}
            )
        }
    }

    fun finalizeCata(id: Long) {
        viewModelScope.launch {
            _uiState.value = CataUiState.Loading
            repository.finalizeCata(id).fold(
                onSuccess = { _uiState.value = CataUiState.Success(it.message) },
                onFailure = { _uiState.value = CataUiState.Error(it.message ?: "Error desconocido") }
            )
        }
    }

    fun cancelCata(id: Long) {
        viewModelScope.launch {
            _uiState.value = CataUiState.Loading
            repository.cancelCata(id).fold(
                onSuccess = { _uiState.value = CataUiState.Success(it.message) },
                onFailure = { _uiState.value = CataUiState.Error(it.message ?: "Error desconocido") }
            )
        }
    }



    fun resetState() {
        _uiState.value = CataUiState.Idle
    }

    fun refreshSelectedCata(id: Long) {
        viewModelScope.launch {
            repository.getAllCatas().fold(
                onSuccess = { cataList ->
                    cataList.find { it.id == id }?.let { _selectedCata.value = it }
                },
                onFailure = { }
            )
        }
    }

    fun resetAttendanceCode() {
        _attendanceCode.value = null
    }

    fun selectCata(id: Long) {
        if (_selectedCata.value?.id != id) {
            _attendanceCode.value = null
        }
        _selectedCata.value = _catas.value.find { it.id == id }
    }
}