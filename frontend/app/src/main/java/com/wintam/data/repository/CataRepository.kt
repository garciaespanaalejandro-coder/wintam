package com.wintam.data.repository

import com.wintam.data.TokenManager
import com.wintam.data.remote.RetrofitClient
import com.wintam.data.remote.WintamApiService
import com.wintam.data.remote.dto.AttendanceCodeResponse
import com.wintam.data.remote.dto.CataResponse
import com.wintam.data.remote.dto.CreateCataRequest
import com.wintam.data.remote.dto.MessageResponse
import kotlinx.coroutines.flow.first

class CataRepository(private val tokenManager: TokenManager) {

    private suspend fun api(): WintamApiService{
        val token= tokenManager.token.first() ?: ""
        return RetrofitClient.authenticatedInstance(token)
            .create(WintamApiService::class.java)
    }

    suspend fun createCata(request: CreateCataRequest): Result<MessageResponse>{
        return try {
            Result.success(api().createCata(request))
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    suspend fun searchCata(
        title: String? = null,
        wineType: String? = null,
        experienceLevel: String? = null,
        location: String? = null,
        cataStatus: String? = null
    ): Result<List<CataResponse>> {
        return try {
            Result.success(api().searchCata(title, wineType, experienceLevel, location, cataStatus))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun cancelCata(id: Long): Result<MessageResponse>{
        return try {
            Result.success(api().cancelCata(id))
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    suspend fun startCata(id: Long): Result<AttendanceCodeResponse>{
        return try {
            Result.success(api().startCata(id))
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    suspend fun getAllCatas(): Result<List<CataResponse>> {
        return try {
            Result.success(api().getAllCatas())
        }catch (e: Exception){
            Result.failure(e)
        }
    }

}