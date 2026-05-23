package com.wintam.data.repository

import com.wintam.data.TokenManager
import com.wintam.data.remote.RetrofitClient
import com.wintam.data.remote.WintamApiService
import com.wintam.data.remote.dto.MessageResponse
import com.wintam.data.remote.dto.UpdateProfileRequest
import com.wintam.data.remote.dto.UserProfileResponse
import com.wintam.utils.safeApiCall
import kotlinx.coroutines.flow.first

class UserRepository(private val tokenManager: TokenManager) {

    private suspend fun api(): WintamApiService {
        val token = tokenManager.token.first() ?: ""
        return RetrofitClient.authenticatedInstance(token)
            .create(WintamApiService::class.java)
    }

    suspend fun getProfile(): Result<UserProfileResponse> {
        return safeApiCall { api().getProfile() }
    }

    suspend fun updateProfile(request: UpdateProfileRequest): Result<MessageResponse> {
        return safeApiCall { api().updateProfile(request) }
    }
}