package com.wintam.data.remote.dto

data class RegisterRequest(
    val name: String,
    val surname: String,
    val username: String,
    val email: String,
    val password: String,
    val birthdate: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class VerifyRequest(
    val email: String,
    val code: String
)

data class RecoverRequest(
    val email: String
)

data class ResetPasswordRequest(
    val email: String,
    val code: String,
    val newPassword: String
)

data class AuthResponse(
    val token: String,
    val email: String,
    val username: String,
    val role: String
)

data class MessageResponse(
    val message: String
)

data class UpdateProfileRequest(
    val name: String? = null,
    val surname: String? = null,
    val username: String? = null,
    val description: String? = null
)

data class UserProfileResponse(
    val name: String,
    val username: String,
    val surname: String,
    val email: String,
    val description: String?,
    val karma: Int
)

data class AttendeeResponse(
    val karma: Int,
    val username: String
)