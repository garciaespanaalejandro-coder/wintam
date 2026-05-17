package com.wintam.data.repository

import com.wintam.data.TokenManager
import com.wintam.data.remote.RetrofitClient
import com.wintam.data.remote.WintamApiService
import com.wintam.data.remote.dto.ConfirmAttendanceRequest
import com.wintam.data.remote.dto.MessageResponse
import kotlinx.coroutines.flow.first

class InscripcionRepository (private val tokenManager: TokenManager){

    private suspend fun api(): WintamApiService{
        val token= tokenManager.token.first() ?: ""
        return RetrofitClient.authenticatedInstance(token)
            .create(WintamApiService::class.java)
    }

    suspend fun joinCata(id: Long): Result<MessageResponse>{
        return try {
            Result.success(api().joinCata(id))
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    suspend fun cancelCata(id: Long): Result<MessageResponse>{
        return try{
            Result.success(api().cancelCata(id))
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    suspend fun confirmAttendance(request: ConfirmAttendanceRequest): Result<MessageResponse>{
        return try {
            Result.success(api().confirmAttendance(request))
        }catch (e: Exception){
            Result.failure(e)
        }
    }
}