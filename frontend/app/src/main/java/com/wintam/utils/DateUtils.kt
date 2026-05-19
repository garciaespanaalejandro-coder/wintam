package com.wintam.utils

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.O)
fun formatDate(millis: Long): String {
    val formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return java.time.Instant.ofEpochMilli(millis)
        .atZone(java.time.ZoneId.systemDefault())
        .toLocalDate()
        .format(formatter)
}

@SuppressLint("DefaultLocale")
fun formatTime(hour: Int, minute: Int): String {
    return String.format("%02d:%02d:00", hour, minute)
}