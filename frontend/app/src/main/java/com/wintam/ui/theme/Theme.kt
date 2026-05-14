package com.wintam.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.wintam.ui.theme.Typography

private val WintamColorScheme = lightColorScheme(
    primary = Burgundy,
    onPrimary = Cream,
    primaryContainer = BurgundySoft,
    background = Surface,
    surface = Surface,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    error = Error,
    onError = White
)

@Composable
fun WintamTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = WintamColorScheme,
        typography = Typography,
        content = content
    )
}

