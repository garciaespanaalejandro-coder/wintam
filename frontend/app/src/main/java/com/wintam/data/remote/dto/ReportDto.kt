package com.wintam.data.remote.dto

data class ReportRequest(
    val username: String,
    val reason: String
)

data class ReportProfileResponse(
    val id: Long,
    val reporter: String,
    val reported: String,
    val reason: String,
    val date: String
)

data class ResolveReportRequest(
    val reportId: Long,
    val sanctionType: SanctionType
)

enum class SanctionType{
    WARNING, KARMA_PENALTY, BAN
}