package com.wintam.data.repository

import com.wintam.data.TokenManager
import com.wintam.data.remote.RetrofitClient
import com.wintam.data.remote.WintamApiService
import com.wintam.data.remote.dto.AttendanceCodeResponse
import com.wintam.data.remote.dto.CataResponse
import com.wintam.data.remote.dto.CreateCataRequest
import com.wintam.data.remote.dto.MessageResponse
import com.wintam.utils.safeApiCall
import kotlinx.coroutines.flow.first

class CataRepository(private val tokenManager: TokenManager) {

    private suspend fun api(): WintamApiService {
        val token = tokenManager.token.first() ?: ""
        return RetrofitClient.authenticatedInstance(token)
            .create(WintamApiService::class.java)
    }

    suspend fun createCata(request: CreateCataRequest): Result<MessageResponse> {
        return safeApiCall { api().createCata(request) }
    }

    suspend fun searchCata(
        title: String? = null,
        wineType: String? = null,
        experienceLevel: String? = null,
        location: String? = null,
        cataStatus: String? = null
    ): Result<List<CataResponse>> {
        return safeApiCall {
            api().searchCata(
                title,
                wineType,
                experienceLevel,
                location,
                cataStatus
            )
        }
    }

    suspend fun cancelCata(id: Long): Result<MessageResponse> {
        return safeApiCall { api().cancelCata(id) }
    }

    suspend fun startCata(id: Long): Result<AttendanceCodeResponse> {
        return safeApiCall { api().startCata(id) }
    }

    suspend fun getAllCatas(): Result<List<CataResponse>> {
        return safeApiCall { api().getAllCatas() }
    }

}