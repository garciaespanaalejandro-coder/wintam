package com.wintam.data.remote.dto

data class CreateCataRequest(
    val title: String,
    val wineType: String,
    val experienceLevel: ExperienceLevel,
    val location: String,
    val scheduleDate: String,
    val scheduledTime: String,
    val maxAttendees: Int
)

data class CataResponse(
    val id: Long,
    val cataStatus: CataStatus,
    val hostUsername: String,
    val wineType: String,
    val title: String,
    val experienceLevel: ExperienceLevel,
    val location: String,
    val scheduleDate: String,
    val scheduledTime: String,
    val maxAttendees: Int
)

data class AttendanceCodeResponse(
    val code: String,
    val generatedAt: String
)

enum class CataStatus {
    SCHEDULED, ACTIVE, CANCELLED, FINISHED
}

enum class ExperienceLevel {
    BEGINNER, INTERMEDIATE, ADVANCED
}

