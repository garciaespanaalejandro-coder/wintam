package com.wintam.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wintam.data.TokenManager
import com.wintam.data.repository.UserRepository

class UserViewModelFactory(private val repository: UserRepository, private val tokenManager: TokenManager) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UserViewModel(repository, tokenManager) as T
    }
}