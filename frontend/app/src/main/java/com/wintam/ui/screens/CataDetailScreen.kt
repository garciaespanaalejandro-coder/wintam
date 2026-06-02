package com.wintam.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.WineBar
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import com.wintam.ui.components.WintamTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wintam.data.TokenManager
import com.wintam.data.remote.dto.CataStatus
import com.wintam.data.remote.dto.ConfirmAttendanceRequest
import com.wintam.data.remote.dto.ReportRequest
import com.wintam.ui.screens.components.InfoRow
import com.wintam.ui.screens.dialogs.ReportDialog
import com.wintam.ui.theme.Burgundy
import com.wintam.ui.theme.BurgundyDark
import com.wintam.ui.theme.Cream
import com.wintam.ui.theme.DMSans
import com.wintam.ui.theme.PlayfairDisplay
import com.wintam.ui.theme.TextPrimary
import com.wintam.ui.theme.TextSecondary
import com.wintam.viewmodel.CataUiState
import com.wintam.viewmodel.CataViewModel
import com.wintam.viewmodel.InscripcionUiState
import com.wintam.viewmodel.InscripcionViewModel
import com.wintam.viewmodel.ReportUiState
import com.wintam.viewmodel.ReportViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CataDetailScreen(
    reportViewModel: ReportViewModel,
    viewModel: CataViewModel,
    inscripcionViewModel: InscripcionViewModel,
    tokenManager: TokenManager,
    onNavigateBack: () -> Unit,
    onNavigateToStartCata: (Long) -> Unit,
){
    val snackbarHostState= remember { SnackbarHostState() }
    val inscripcionUiState by inscripcionViewModel.uiState.collectAsState()
    val yaInscrito by inscripcionViewModel.yaInscrito.collectAsState()
    val username by tokenManager.username.collectAsState(initial = "")
    val cata by viewModel.selectedCata.collectAsState()
    var showConfirmDialog by remember { mutableStateOf(false) }
    val attendees by inscripcionViewModel.registered.collectAsState()
    var showReportDialog by remember { mutableStateOf(false) }
    var selectedUsername by remember { mutableStateOf("") }
    val reportUiState by reportViewModel.uiState.collectAsState()

    val cataUiState by viewModel.uiState.collectAsState()


    cata?.let { cata->
        LaunchedEffect(inscripcionUiState) {
            when (inscripcionUiState) {
                is InscripcionUiState.Success -> {
                    snackbarHostState.showSnackbar(
                        message = (inscripcionUiState as InscripcionUiState.Success).message ?: "Hecho",
                        duration = SnackbarDuration.Short
                    )
                    inscripcionViewModel.loadRegistered(cata.id)
                    inscripcionViewModel.resetState()
                }
                is InscripcionUiState.Error -> {
                    snackbarHostState.showSnackbar((inscripcionUiState as InscripcionUiState.Error).message)
                    inscripcionViewModel.resetState()
                }
                else -> {}
            }
        }
        LaunchedEffect(cata.id) {
            inscripcionViewModel.loadRegistered(cata.id)
            while (true) {
                delay(5000)
                inscripcionViewModel.loadRegistered(cata.id)
                viewModel.refreshSelectedCata(cata.id)
            }
        }
        LaunchedEffect(attendees, username) {
            inscripcionViewModel.setYaInscrito(attendees.any { it.username == username })
        }
        LaunchedEffect(cataUiState) {
            when (cataUiState) {
                is CataUiState.Success -> {
                    snackbarHostState.showSnackbar((cataUiState as CataUiState.Success).message ?: "Hecho")
                    viewModel.resetState()
                    onNavigateBack()
                }
                is CataUiState.Error -> {
                    snackbarHostState.showSnackbar((cataUiState as CataUiState.Error).message)
                    viewModel.resetState()
                }
                else -> {}
            }
        }

        LaunchedEffect(reportUiState) {
            when (reportUiState) {
                is ReportUiState.Success -> {
                    snackbarHostState.showSnackbar("Reporte enviado correctamente")
                    reportViewModel.resetState()
                }
                is ReportUiState.Error -> {
                    snackbarHostState.showSnackbar((reportUiState as ReportUiState.Error).message)
                    reportViewModel.resetState()
                }
                else -> {}
            }
        }
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                TopAppBar(
                    title = { Text("Detalle de cata", fontFamily = DMSans) },
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
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(Burgundy)
                ) {
                    Text(
                        text = cata.wineType,
                        fontFamily = DMSans,
                        fontSize = 12.sp,
                        color = Cream,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(12.dp)
                            .background(BurgundyDark, RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                    Text(
                        text = cata.title,
                        fontFamily = PlayfairDisplay,
                        fontSize = 24.sp,
                        color = Cream,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    )
                }

                Card (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp)
                ){
                    Column (modifier = Modifier.padding(16.dp)){
                        InfoRow(Icons.Default.WineBar, "TIPO", cata.wineType)
                        InfoRow(Icons.Default.School, "NIVEL", cata.experienceLevel.name)
                        InfoRow(Icons.Default.LocationOn, "UBICACIÓN", cata.location)
                        InfoRow(Icons.Default.CalendarToday, "FECHA", cata.scheduleDate)
                        InfoRow(Icons.Default.Schedule, "HORA", cata.scheduledTime)
                        InfoRow(Icons.Default.Person, "ANFITRIÓN", "@${cata.hostUsername}")
                        InfoRow(Icons.Default.Info, "ESTADO", cata.cataStatus.name)
                    }
                }

                if(cata.hostUsername== username){
                    Button(onClick = {onNavigateToStartCata(cata.id)}){
                        Text("Iniciar Cata")
                    }
                    Button(
                        onClick = { viewModel.cancelCata(cata.id) },
                        colors = ButtonDefaults.buttonColors(containerColor = BurgundyDark)
                    ) {
                        Text("Cancelar cata", color = Cream, fontFamily = DMSans)
                    }
                }else{
                    if (cata.cataStatus == CataStatus.OPEN || cata.cataStatus == CataStatus.FULL) {
                        Button(onClick = {inscripcionViewModel.joinCata(cata.id)}) {
                            Text(if (yaInscrito) "Ya inscrito" else "Inscribirse")
                    }

                    }
                    if (yaInscrito) {
                        Button(
                            onClick = { inscripcionViewModel.cancelCata(cata.id) },
                            colors = ButtonDefaults.buttonColors(containerColor = BurgundyDark)
                        ) {
                            Text("Cancelar inscripción", color = Cream, fontFamily = DMSans)
                        }
                    }
                    if(cata.cataStatus== CataStatus.ACTIVE){
                        Button(onClick = {showConfirmDialog=true}) {
                            Text("Confirmar asistencia")
                        }
                    }

                    if (showConfirmDialog) {
                        var codigo by remember { mutableStateOf("") }
                        AlertDialog(
                            onDismissRequest = { showConfirmDialog = false },
                            title = { Text("Confirmar asistencia", fontFamily = DMSans) },
                            text = {
                                WintamTextField(
                                    value = codigo,
                                    onValueChange = { codigo = it },
                                    label = "Código",
                                    keyboardType = KeyboardType.Decimal
                                )
                            },
                            confirmButton = {
                                TextButton(onClick = {
                                    inscripcionViewModel.confirmAttendance(
                                        ConfirmAttendanceRequest(cataId = cata.id, code = codigo)
                                    )
                                    showConfirmDialog = false
                                }) { Text("Confirmar", color = Burgundy) }
                            },
                            dismissButton = {
                                TextButton(onClick = { showConfirmDialog = false }) {
                                    Text("Cancelar", color = TextSecondary)
                                }
                            }
                        )
                    }
                }
                Text(
                    "ASISTENTES",
                    fontFamily = DMSans,
                    fontSize = 11.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(start = 16.dp)
                )
                attendees.forEach { attendee ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "@${attendee.username}",
                                fontFamily = DMSans,
                                fontSize = 14.sp,
                                color = TextPrimary
                            )
                            Text(
                                text = "Karma: ${attendee.karma}",
                                fontFamily = DMSans,
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }
                        TextButton(onClick = {
                            selectedUsername = attendee.username
                            showReportDialog = true
                        }) {
                            Text("Reportar", color = Burgundy, fontFamily = DMSans, fontSize = 12.sp)
                        }
                    }
                }
            }
            if(showReportDialog){
                ReportDialog(
                    username= selectedUsername,
                    onConfirm = { reason ->
                        reportViewModel.reportUser(ReportRequest(selectedUsername, reason))
                        showReportDialog  = false
                    },
                    onDismiss = {showReportDialog =false}
                )
            }
        }
    }
}


