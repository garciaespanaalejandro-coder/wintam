package com.wintam.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wintam.data.repository.CataRepository

class CataViewModelFactory(private val repository: CataRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CataViewModel(repository) as T
    }
}