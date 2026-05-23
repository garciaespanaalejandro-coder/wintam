package com.wintam.ui.dialogs

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.wintam.ui.theme.Burgundy
import com.wintam.ui.theme.TextPrimary

@Composable
fun WintamDatePickerDialog(
    datePickerState: DatePickerState,
    onConfirm: (millis: Long) -> Unit,
    onDismiss: () -> Unit
) {
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                datePickerState.selectedDateMillis?.let { onConfirm(it) }
                onDismiss()
            }) {
                Text("Aceptar", color = Burgundy)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = TextPrimary)
            }
        }
    ) { DatePicker(state = datePickerState) }
}