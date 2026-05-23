package com.wintam.data.repository

import com.wintam.data.TokenManager
import com.wintam.data.remote.RetrofitClient
import com.wintam.data.remote.WintamApiService
import com.wintam.data.remote.dto.*
import com.wintam.utils.safeApiCall

class AuthRepository(private val tokenManager: TokenManager) {
    private val api = RetrofitClient.instance.create(WintamApiService::class.java)

    suspend fun register(request: RegisterRequest): Result<MessageResponse> {
        return safeApiCall { api.register(request) }
    }

    suspend fun signIn(request: LoginRequest): Result<AuthResponse> {
        return safeApiCall(
            errorMessages = mapOf(
                401 to "Email o contraseña incorrectos"
            )
        ) { api.signIn(request) }
    }

    suspend fun verifyEmail(request: VerifyRequest): Result<AuthResponse> {
        return safeApiCall { api.verifyEmail(request) }
    }

    suspend fun recoverPassword(request: RecoverRequest): Result<MessageResponse> {
        return safeApiCall { api.recoverPassword(request) }
    }

    suspend fun resetPassword(request: ResetPasswordRequest): Result<MessageResponse> {
        return safeApiCall { api.resetPassword(request) }
    }


    suspend fun logout() {
        tokenManager.clearAuthData()
    }
}