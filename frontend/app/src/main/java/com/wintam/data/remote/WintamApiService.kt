package com.wintam.data.remote

import com.wintam.data.remote.dto.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface WintamApiService {

    //--------------------FLUJO DE AUTENTICACIÓN----------------------------------------------------

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





    //-------------------CATAS----------------------------------------------------------------------

    @POST("/api/cata/createCata")
    suspend fun createCata(@Body request: CreateCataRequest): MessageResponse

    @GET("api/cata/searchCata")
    suspend fun searchCata(
        @Query("title") title: String? = null,
        @Query("wineType") wineType: String? = null,
        @Query("experienceLevel") experienceLevel: String? = null,
        @Query("location") location: String? = null,
        @Query("cataStatus") cataStatus: String? = null
    ): List<CataResponse>

    @PATCH("api/cata/cancel/{id}")
    suspend fun cancelCata(@Path("id") id: Long): MessageResponse

    @PATCH("api/cata/startCata/{id}")
    suspend fun startCata(@Path("id") id: Long): AttendanceCodeResponse

    @PATCH("api/cata/finalizeCata/{id}")
    suspend fun finalizeCata(@Path("id") id: Long): MessageResponse

    @GET("api/cata/getAllCatas")
    suspend fun getAllCatas(): List<CataResponse>

    //-------------------USUARIOS----------------------------------------------------------------------

    @PATCH("api/user/updateProfile")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): MessageResponse

    @GET("api/user/getProfile")
    suspend fun getProfile(): UserProfileResponse


    //-------------------INSCRIPCIONES----------------------------------------------------------------------

    @POST("api/inscripcion/joinCata/{id}")
    suspend fun joinCata(@Path("id") id: Long): MessageResponse

    @PATCH("api/inscripcion/cancelCata/{id}")
    suspend fun cancelInscripcion(@Path("id") id: Long): MessageResponse

    @PATCH("api/inscripcion/confirmAttendance")
    suspend fun confirmAttendance (@Body request: ConfirmAttendanceRequest): MessageResponse

    @GET("api/inscripcion/getAttendees/{cataId}")
    suspend fun getAttendees(@Path("cataId") cataId: Long): List<AttendeeResponse>

    @GET("api/inscripcion/getRegistered/{cataId}")
    suspend fun getRegistered(@Path("cataId") cataId: Long): List<AttendeeResponse>

    //-------------------REPORTES----------------------------------------------------------------------

    @POST("api/report/reportUser")
    suspend fun reportUser(@Body request: ReportRequest): MessageResponse

    @GET("api/report/getReport")
    suspend fun getReport(): List<ReportProfileResponse>

    @PATCH("api/report/resolveReport")
    suspend fun resolveReport(@Body request: ResolveReportRequest): MessageResponse

    @PATCH("api/report/dismissReport/{id}")
    suspend fun dismissReport(@Path("id") id: Long): MessageResponse
}