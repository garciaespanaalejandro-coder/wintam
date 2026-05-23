package com.wintam.ui.screens.dialogs

import android.app.AlertDialog
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import com.wintam.ui.theme.Burgundy
import com.wintam.ui.theme.TextSecondary

@ExperimentalMaterial3Api
@Composable
fun TimePickerDialog(
    timePickerState: TimePickerState,
    onConfirm: (hour: Int, minute: Int) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = { onConfirm(timePickerState.hour, timePickerState.minute) }) {
                Text("Aceptar", color = Burgundy)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = TextSecondary)
            }
        },
        text = { TimePicker(state = timePickerState) }
    )
}