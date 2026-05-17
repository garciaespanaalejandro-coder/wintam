package com.wintam.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wintam.data.repository.InscripcionRepository

class InscripcionViewModelFactory(private val repository: InscripcionRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return InscripcionViewModel(repository) as T
    }
}