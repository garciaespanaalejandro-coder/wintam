package com.wintam.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wintam.data.remote.dto.CreateCataRequest
import com.wintam.data.remote.dto.ExperienceLevel
import com.wintam.ui.dialogs.WintamDatePickerDialog
import com.wintam.ui.screens.dialogs.TimePickerDialog
import com.wintam.ui.theme.Border
import com.wintam.ui.theme.Burgundy
import com.wintam.ui.theme.BurgundySoft
import com.wintam.ui.theme.Cream
import com.wintam.ui.theme.DMSans
import com.wintam.ui.theme.TextPrimary
import com.wintam.ui.theme.TextSecondary
import com.wintam.utils.formatDate
import com.wintam.utils.formatTime
import com.wintam.viewmodel.CataUiState
import com.wintam.viewmodel.CataViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCataScreen(
    viewModel: CataViewModel,
    onNavigateBack: () -> Unit,
    onCataCreated: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var title by remember { mutableStateOf("") }
    var wineType by remember { mutableStateOf("") }
    var experienceLevel by remember { mutableStateOf(ExperienceLevel.BEGINNER) }
    var location by remember { mutableStateOf("") }
    var scheduleDate by remember { mutableStateOf("") }
    var scheduledTime by remember { mutableStateOf("") }
    var maxAttendees by remember { mutableStateOf("") }
    val timePickerState = rememberTimePickerState(
        initialHour = 12,
        initialMinute = 0
    )
    var showTimePicker by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    LaunchedEffect(uiState) {
        if (uiState is CataUiState.Success) {
            viewModel.resetState()
            onCataCreated()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear cata", fontFamily = DMSans) },
                navigationIcon = {
                    IconButton(onClick = { onNavigateBack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(horizontal = 28.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Título", fontFamily = DMSans) },
                singleLine = true,
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

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = wineType,
                onValueChange = { wineType = it },
                label = { Text("Tipo de vino", fontFamily = DMSans) },
                singleLine = true,
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

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Nivel de experiencia",
                fontFamily = DMSans,
                fontSize = 14.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ExperienceLevel.entries.forEach { level ->
                    FilterChip(
                        selected = experienceLevel == level,
                        onClick = { experienceLevel = level },
                        label = { Text(level.name, fontFamily = DMSans, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Burgundy,
                            selectedLabelColor = Cream
                        )
                    )

                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Ubicación", fontFamily = DMSans) },
                singleLine = true,
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

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = scheduleDate,
                onValueChange = {},
                readOnly = true,
                label = { Text("Fecha", fontFamily = DMSans) },
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(
                            Icons.Default.DateRange,
                            contentDescription = null,
                            tint = TextSecondary
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth().clickable { showDatePicker = true },
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

            if (showDatePicker) {
                WintamDatePickerDialog(
                    datePickerState=datePickerState,
                    onConfirm = {millis ->scheduleDate = formatDate(millis)},
                    onDismiss = {showDatePicker= false}
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = scheduledTime,
                onValueChange = {},
                readOnly = true,
                label = { Text("Hora", fontFamily = DMSans) },
                trailingIcon = {
                    IconButton(onClick = { showTimePicker = true }) {
                        Icon(Icons.Default.Schedule, contentDescription = null, tint = TextSecondary)
                    }
                },
                modifier = Modifier.fillMaxWidth().clickable { showTimePicker = true },
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
            if (showTimePicker) {
                TimePickerDialog(
                    timePickerState= timePickerState,
                    onConfirm = {hour, minute -> scheduledTime= formatTime(hour, minute)},
                    onDismiss = {showTimePicker=false}
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = maxAttendees,
                onValueChange = { maxAttendees = it },
                label = { Text("Plazas máximas", fontFamily = DMSans) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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

            Spacer(modifier = Modifier.height(28.dp))

            if (uiState is CataUiState.Error) {
                Text(
                    text = (uiState as CataUiState.Error).message,
                    color = com.wintam.ui.theme.Error,
                    fontFamily = DMSans,
                    fontSize = 13.sp,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            Button(
                onClick = {
                    viewModel.createCata(
                        CreateCataRequest(
                            title = title.trim(),
                            wineType = wineType.trim(),
                            experienceLevel = experienceLevel,
                            location = location.trim(),
                            scheduleDate = scheduleDate,
                            scheduledTime = scheduledTime,
                            maxAttendees = maxAttendees.toIntOrNull() ?: 0
                        )
                    )
                },
                enabled = title.isNotBlank() && wineType.isNotBlank() && location.isNotBlank()
                        && scheduleDate.isNotBlank() && scheduledTime.isNotBlank()
                        && maxAttendees.isNotBlank() && uiState !is CataUiState.Loading,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Burgundy,
                    disabledContainerColor = BurgundySoft
                )
            ) {
                if (uiState is CataUiState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = Cream,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Crear cata", fontFamily = DMSans, fontSize = 15.sp, color = Cream)
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
