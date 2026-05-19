package com.wintam.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wintam.data.TokenManager
import com.wintam.data.remote.dto.CataStatus
import com.wintam.data.remote.dto.ConfirmAttendanceRequest
import com.wintam.ui.theme.Border
import com.wintam.ui.theme.Burgundy
import com.wintam.ui.theme.BurgundyDark
import com.wintam.ui.theme.BurgundySoft
import com.wintam.ui.theme.Cream
import com.wintam.ui.theme.DMSans
import com.wintam.ui.theme.PlayfairDisplay
import com.wintam.ui.theme.TextPrimary
import com.wintam.ui.theme.TextSecondary
import com.wintam.viewmodel.CataViewModel
import com.wintam.viewmodel.InscripcionUiState
import com.wintam.viewmodel.InscripcionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CataDetailScreen(
    viewModel: CataViewModel,
    inscripcionViewModel: InscripcionViewModel,
    tokenManager: TokenManager,
    onNavigateBack: () -> Unit,
    onNavigateToStartCata: (Long) -> Unit,
){
    val snackbarHostState= remember { SnackbarHostState() }
    val inscripcionUiState by inscripcionViewModel.uiState.collectAsState()
    var yaInscrito by remember { mutableStateOf(false) }
    val username by tokenManager.username.collectAsState(initial = "")
    val cata by viewModel.selectedCata.collectAsState()
    var showConfirmDialog by remember { mutableStateOf(false) }

    LaunchedEffect(inscripcionUiState) {
        when (inscripcionUiState) {
            is InscripcionUiState.Success -> {
                yaInscrito = true
                snackbarHostState.showSnackbar(
                    message = "¡Inscrito correctamente!",
                    duration = SnackbarDuration.Short
                )
                inscripcionViewModel.resetState()
            }
            is InscripcionUiState.Error -> {
                snackbarHostState.showSnackbar((inscripcionUiState as InscripcionUiState.Error).message)
                inscripcionViewModel.resetState()
            }
            else -> {}
        }
    }
    cata?.let { cata->
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
                }else{
                    Button(onClick = {inscripcionViewModel.joinCata(cata.id)}) {
                        Text(if (yaInscrito) "Ya inscrito" else "Inscribirse")
                    }

                    if(cata.cataStatus== CataStatus.OPEN){
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
                                OutlinedTextField(
                                    value = codigo,
                                    onValueChange = { codigo = it },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    label = { Text("Código", fontFamily = DMSans) },
                                    singleLine = true
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
            }
        }
    }
}


@Composable
fun InfoRow(icon: ImageVector, label: String, value: String){
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint= Burgundy,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width( 12.dp))
        Column {
            Text(
                text=label,
                fontFamily = DMSans,
                fontSize = 11.sp,
                color= TextSecondary
            )

            Text(
                text = value,
                fontFamily = DMSans,
                fontSize = 15.sp,
                color = TextPrimary
            )
        }
    }
    HorizontalDivider(Modifier, DividerDefaults.Thickness, color = Border)
}