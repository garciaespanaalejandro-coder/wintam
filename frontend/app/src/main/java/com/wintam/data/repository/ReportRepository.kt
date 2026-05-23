package com.wintam.data.repository

import com.wintam.data.TokenManager
import com.wintam.data.remote.RetrofitClient
import com.wintam.data.remote.WintamApiService
import com.wintam.data.remote.dto.MessageResponse
import com.wintam.data.remote.dto.ReportProfileResponse
import com.wintam.data.remote.dto.ReportRequest
import com.wintam.data.remote.dto.ResolveReportRequest
import com.wintam.utils.safeApiCall
import kotlinx.coroutines.flow.first

class ReportRepository(private val tokenManager: TokenManager) {

    private suspend fun api(): WintamApiService{
        val token= tokenManager.token.first()?:""
        return RetrofitClient.authenticatedInstance(token)
            .create(WintamApiService::class.java)
    }

    suspend fun getReports(): Result<List<ReportProfileResponse>> {
        return safeApiCall { api().getReport() }
    }

    suspend fun resolveReport(request: ResolveReportRequest): Result<MessageResponse> {
        return safeApiCall { api().resolveReport(request) }
    }

    suspend fun dismissReport(id: Long): Result<MessageResponse> {
        return safeApiCall { api().dismissReport(id) }
    }

    suspend fun reportUser(request: ReportRequest): Result<MessageResponse> {
        return safeApiCall { api().reportUser(request) }
    }
}