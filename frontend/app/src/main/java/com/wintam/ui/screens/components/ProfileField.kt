package com.wintam.ui.screens.components

import androidx.compose.runtime.Composable
import com.wintam.ui.components.WintamTextField

@Composable
fun ProfileField(
    label: String,
    value: String,
    singleLine: Boolean = true,
    onValueChange: (String) -> Unit
) {
    WintamTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        singleLine = singleLine
    )
}