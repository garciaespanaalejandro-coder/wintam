package com.wintam.ui.screens

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wintam.ui.theme.Burgundy
import com.wintam.ui.theme.Cream
import com.wintam.ui.theme.DMSans
import com.wintam.ui.theme.PlayfairDisplay
import com.wintam.ui.theme.TextPrimary
import com.wintam.ui.theme.TextSecondary
import com.wintam.viewmodel.CataUiState
import com.wintam.viewmodel.CataViewModel
import com.wintam.viewmodel.InscripcionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartCataScreen(
    viewModel: CataViewModel,
    inscripcionViewModel: InscripcionViewModel,
    onNavigateBack: () -> Unit,
    onCataFinalized: () -> Unit
){
    val uiState by viewModel.uiState.collectAsState()
    val attendanceCode by viewModel.attendanceCode.collectAsState()
    val cata by viewModel.selectedCata.collectAsState()
    val attendees by inscripcionViewModel.attendees.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState) {
        when (uiState) {
            is CataUiState.Error -> {
                snackbarHostState.showSnackbar((uiState as CataUiState.Error).message)
                viewModel.resetState()
            }
            is CataUiState.Success -> {
                viewModel.resetState()
                onCataFinalized()
            }
            else -> {}
        }
    }

    LaunchedEffect(attendanceCode) {
        if (attendanceCode != null) {
            cata?.let {
                while (true) {
                    inscripcionViewModel.loadAttendees(it.id)
                    delay(5000)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Empezar cata", fontFamily = DMSans) },
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
            .padding(paddingValues)) {

            cata?.let { cata->
                Text(
                    text = cata.title,
                    fontFamily = PlayfairDisplay,
                    fontSize = 24.sp,
                    color= TextPrimary,
                    modifier = Modifier.padding(16.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                if (attendanceCode == null){
                    Button(
                        onClick = {viewModel.startCata(cata.id)},
                        enabled = uiState !is CataUiState.Loading,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors= ButtonDefaults.buttonColors(containerColor = Burgundy)
                    ) {
                        if(uiState is CataUiState.Loading){
                            CircularProgressIndicator(modifier = Modifier.size(22.dp), color= Cream, strokeWidth = 2.dp)
                        }else{
                            Text("Iniciar cata", color= Cream, fontFamily = DMSans)
                        }
                    }
                }else{
                    Text(
                        text= "Código de asistencia",
                        fontFamily = DMSans,
                        fontSize = 14.sp,
                        color= TextSecondary,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Text(
                        text = attendanceCode!!,
                        fontFamily = PlayfairDisplay,
                        fontSize = 48.sp,
                        color= Burgundy,
                        modifier = Modifier.padding(16.dp)
                    )

                    if (attendees.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Asistentes verificados (${attendees.size})",
                            fontFamily = DMSans,
                            fontSize = 14.sp,
                            color = TextSecondary,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        attendees.forEach { attendee ->
                            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.CheckCircle,
                                    contentDescription = null,
                                    tint = Burgundy,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = attendee.username,
                                    fontFamily = DMSans,
                                    fontSize = 15.sp,
                                    color = TextPrimary
                                )
                            }
                        }

                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = { viewModel.finalizeCata(cata.id) },
                            enabled = uiState !is CataUiState.Loading,
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(52.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Burgundy)
                        ) {
                            if (uiState is CataUiState.Loading) {
                                CircularProgressIndicator(modifier = Modifier.size(22.dp), color = Cream, strokeWidth = 2.dp)
                            } else {
                                Text("Finalizar cata", color = Cream, fontFamily = DMSans)
                            }
                        }
                    }
                }
            }
        }

    }
}
