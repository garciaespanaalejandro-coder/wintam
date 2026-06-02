package com.wintam.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wintam.data.remote.dto.AttendeeResponse
import com.wintam.data.remote.dto.ConfirmAttendanceRequest
import com.wintam.data.repository.InscripcionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class InscripcionUiState{
    object Idle: InscripcionUiState()
    object Loading: InscripcionUiState()
    data class Success(val message: String?): InscripcionUiState()
    data class Error(val message: String): InscripcionUiState()
}

class InscripcionViewModel (private val repository: InscripcionRepository): ViewModel(){
    private val _uiState = MutableStateFlow<InscripcionUiState>(InscripcionUiState.Idle)
    val uiState: StateFlow<InscripcionUiState> = _uiState
    private val _attendees = MutableStateFlow<List<AttendeeResponse>>(emptyList())
    val attendees: StateFlow<List<AttendeeResponse>> = _attendees

    private val _registered = MutableStateFlow<List<AttendeeResponse>>(emptyList())
    val registered: StateFlow<List<AttendeeResponse>> = _registered

    private val _yaInscrito = MutableStateFlow(false)
    val yaInscrito: StateFlow<Boolean> = _yaInscrito
    fun joinCata(id: Long){
        viewModelScope.launch {
            _uiState.value= InscripcionUiState.Loading
            repository.joinCata(id).fold(
                onSuccess = {
                    _yaInscrito.value = true
                    _uiState.value= InscripcionUiState.Success(it.message)
                },
                onFailure = {_uiState.value= InscripcionUiState.Error(it.message ?: "Error desconocido")}
            )
        }
    }

    fun cancelCata(id: Long){
        viewModelScope.launch {
            _uiState.value= InscripcionUiState.Loading
            repository.cancelCata(id).fold(
                onSuccess = {
                    _yaInscrito.value = false
                    _uiState.value= InscripcionUiState.Success(it.message)
                },
                onFailure = {_uiState.value= InscripcionUiState.Error(it.message ?: "Error desconocido")}
            )
        }
    }

    fun resetYaInscrito() {
        _yaInscrito.value = false
    }

    fun setYaInscrito(value: Boolean) {
        _yaInscrito.value = value
    }

    fun confirmAttendance(request: ConfirmAttendanceRequest){
        viewModelScope.launch {
            _uiState.value= InscripcionUiState.Loading
            repository.confirmAttendance(request).fold(
                onSuccess = {_uiState.value= InscripcionUiState.Success(it.message) },
                onFailure = {_uiState.value= InscripcionUiState.Error(it.message ?: "Error desconocido")}
            )
        }
    }

    fun loadAttendees(cataId: Long) {
        viewModelScope.launch {
            repository.getAttendees(cataId).fold(
                onSuccess = { _attendees.value = it },
                onFailure = { }
            )
        }
    }

    fun loadRegistered(cataId: Long) {
        viewModelScope.launch {
            repository.getRegistered(cataId).fold(
                onSuccess = { _registered.value = it },
                onFailure = { }
            )
        }
    }


    fun resetState() {
        _uiState.value = InscripcionUiState.Idle
    }
}