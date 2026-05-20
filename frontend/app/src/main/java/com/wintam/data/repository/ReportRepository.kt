package com.wintam.data.repository

import com.wintam.data.TokenManager
import com.wintam.data.remote.RetrofitClient
import com.wintam.data.remote.WintamApiService
import com.wintam.data.remote.dto.MessageResponse
import com.wintam.data.remote.dto.ReportProfileResponse
import com.wintam.data.remote.dto.ReportRequest
import com.wintam.data.remote.dto.ResolveReportRequest
import kotlinx.coroutines.flow.first

class ReportRepository(private val tokenManager: TokenManager) {

    private suspend fun api(): WintamApiService{
        val token= tokenManager.token.first()?:""
        return RetrofitClient.authenticatedInstance(token)
            .create(WintamApiService::class.java)
    }

    suspend fun getReports(): Result<List<ReportProfileResponse>> {
        return try {
            Result.success(api().getReport())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun resolveReport(request: ResolveReportRequest): Result<MessageResponse> {
        return try {
            Result.success(api().resolveReport(request))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun dismissReport(id: Long): Result<MessageResponse> {
        return try {
            Result.success(api().dismissReport(id))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun reportUser(request: ReportRequest): Result<MessageResponse> {
        return try {
            Result.success(api().reportUser(request))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}