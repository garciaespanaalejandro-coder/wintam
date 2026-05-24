package com.wintam.ui.screens.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wintam.ui.theme.Border
import com.wintam.ui.theme.Burgundy
import com.wintam.ui.theme.DMSans
import com.wintam.ui.theme.TextPrimary
import com.wintam.ui.theme.TextSecondary

@Composable
fun ProfileField(
    label: String,
    value: String,
    singleLine: Boolean = true,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, fontFamily = DMSans, fontSize = 14.sp) },
        singleLine = singleLine,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Burgundy,
            unfocusedBorderColor = Border,
            focusedLabelColor = Burgundy,
            unfocusedLabelColor = TextSecondary,
            cursorColor = Burgundy,
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary
        )
    )
}