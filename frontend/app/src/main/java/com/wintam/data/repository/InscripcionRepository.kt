package com.wintam.data.repository

import com.wintam.data.TokenManager
import com.wintam.data.remote.RetrofitClient
import com.wintam.data.remote.WintamApiService
import com.wintam.data.remote.dto.AttendeeResponse
import com.wintam.data.remote.dto.ConfirmAttendanceRequest
import com.wintam.data.remote.dto.MessageResponse
import com.wintam.utils.safeApiCall
import kotlinx.coroutines.flow.first

class InscripcionRepository (private val tokenManager: TokenManager){

    private suspend fun api(): WintamApiService{
        val token= tokenManager.token.first() ?: ""
        return RetrofitClient.authenticatedInstance(token)
            .create(WintamApiService::class.java)
    }

    suspend fun joinCata(id: Long): Result<MessageResponse> {
        return safeApiCall(
            errorMessages = mapOf(
                409 to "Ya estás inscrito en esta cata"
            )
        ) { api().joinCata(id) }
    }

    suspend fun cancelCata(id: Long): Result<MessageResponse>{
        return safeApiCall { api().cancelCata(id) }
    }

    suspend fun confirmAttendance(request: ConfirmAttendanceRequest): Result<MessageResponse>{
        return safeApiCall { api().confirmAttendance(request) }
    }

    suspend fun getAttendees(id: Long): Result<List<AttendeeResponse>> {
        return safeApiCall { api().getAttendees(id) }
    }
}