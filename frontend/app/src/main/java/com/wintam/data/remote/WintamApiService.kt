package com.wintam.data.remote

import com.wintam.data.remote.dto.*
import retrofit2.http.Body
import retrofit2.http.POST

interface WintamApiService {

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): MessageResponse

    @POST("api/auth/signIn")
    suspend fun signIn(@Body request: LoginRequest): AuthResponse

    @POST("api/auth/verify")
    suspend fun verifyEmail(@Body request: VerifyRequest): AuthResponse

    @POST("api/auth/recover-password")
    suspend fun recoverPassword(@Body request: RecoverRequest): MessageResponse

    @POST("api/auth/reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): MessageResponse
}