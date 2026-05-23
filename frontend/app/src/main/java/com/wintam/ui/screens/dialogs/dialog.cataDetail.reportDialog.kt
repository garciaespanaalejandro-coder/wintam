package com.wintam.ui.screens.dialogs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wintam.ui.theme.Border
import com.wintam.ui.theme.Burgundy
import com.wintam.ui.theme.Cream
import com.wintam.ui.theme.DMSans
import com.wintam.ui.theme.TextPrimary
import com.wintam.ui.theme.TextSecondary

@Composable
fun ReportDialog(
    username: String,
    onConfirm: (reason: String) -> Unit,
    onDismiss: () -> Unit
){
    var reportReason by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Reportar a @$username", fontFamily = DMSans) },
        text={
            OutlinedTextField(
                value = reportReason,
                onValueChange = {reportReason = it},
                label = {Text("Motivo", fontFamily = DMSans)},
                modifier = Modifier.fillMaxWidth(),
                shape= RoundedCornerShape(12.dp),
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
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(reportReason)},
                colors= ButtonDefaults.buttonColors(containerColor = Burgundy)
            ) {
                Text("Enviar", color= Cream, fontFamily = DMSans)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = TextSecondary, fontFamily = DMSans)
            }
        }
    )
}