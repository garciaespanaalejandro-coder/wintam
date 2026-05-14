package com.wintam.data.repository

import com.wintam.data.TokenManager
import com.wintam.data.remote.RetrofitClient
import com.wintam.data.remote.WintamApiService
import com.wintam.data.remote.dto.*

class AuthRepository(private val tokenManager: TokenManager){
    private val api= RetrofitClient.instance.create(WintamApiService::class.java)

    suspend fun register(request: RegisterRequest): Result<MessageResponse>{
        return try {
            val response= api.register(request)
            Result.success(response)
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    suspend fun signIn(request: LoginRequest): Result<AuthResponse>{
        return try {
            val response= api.signIn(request)
            tokenManager.saveAuthData(response.token,response.username,response.role,)
            Result.success(response)
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    suspend fun verifyEmail(request: VerifyRequest): Result<AuthResponse>{
        return try {
            val response= api.verifyEmail(request)
            tokenManager.saveAuthData(response.token,response.username,response.role)
            Result.success(response)
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    suspend fun recoverPassword(request: RecoverRequest): Result<MessageResponse>{
        return try {
            Result.success(api.recoverPassword(request))
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    suspend fun resetPassword(request: ResetPasswordRequest): Result<MessageResponse>{
        return try {
            Result.success(api.resetPassword(request))
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    suspend fun logout(){
        tokenManager.clearAuthData()
    }
}