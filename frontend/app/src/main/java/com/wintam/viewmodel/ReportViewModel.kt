package com.wintam.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wintam.data.remote.dto.ReportProfileResponse
import com.wintam.data.remote.dto.ReportRequest
import com.wintam.data.remote.dto.ResolveReportRequest
import com.wintam.data.repository.ReportRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class ReportUiState{
    object Idle: ReportUiState()
    object Loading: ReportUiState()
    data class Success(val message: String? = null) : ReportUiState()
    data class Error(val message: String) : ReportUiState()
}

class ReportViewModel (private val repository: ReportRepository) : ViewModel(){
    private val _uiState= MutableStateFlow<ReportUiState>(ReportUiState.Idle)
    val uiState: StateFlow<ReportUiState> = _uiState

    private val _reports = MutableStateFlow<List<ReportProfileResponse>>(emptyList())
    val reports: StateFlow<List<ReportProfileResponse>> = _reports

    fun loadReport(){
        viewModelScope.launch {
            _uiState.value= ReportUiState.Loading
            repository.getReports().fold(
                onSuccess = {
                    _reports.value= it
                    _uiState.value= ReportUiState.Idle
                },
                onFailure = {_uiState.value= ReportUiState.Error(it.message?: "Error desconocido")}
            )
        }
    }

    fun resolveReport(request: ResolveReportRequest) {
        viewModelScope.launch {
            _uiState.value = ReportUiState.Loading
            repository.resolveReport(request).fold(
                onSuccess = { _uiState.value = ReportUiState.Success(it.message) },
                onFailure = { _uiState.value = ReportUiState.Error(it.message ?: "Error desconocido") }
            )
        }
    }

    fun dismissReport(id: Long) {
        viewModelScope.launch {
            _uiState.value = ReportUiState.Loading
            repository.dismissReport(id).fold(
                onSuccess = { _uiState.value = ReportUiState.Success(it.message) },
                onFailure = { _uiState.value = ReportUiState.Error(it.message ?: "Error desconocido") }
            )
        }
    }

    fun reportUser(request: ReportRequest) {
        viewModelScope.launch {
            _uiState.value = ReportUiState.Loading
            repository.reportUser(request).fold(
                onSuccess = { _uiState.value = ReportUiState.Success(it.message) },
                onFailure = { _uiState.value = ReportUiState.Error(it.message ?: "Error desconocido") }
            )
        }
    }

    fun resetState() {
        _uiState.value = ReportUiState.Idle
    }
}