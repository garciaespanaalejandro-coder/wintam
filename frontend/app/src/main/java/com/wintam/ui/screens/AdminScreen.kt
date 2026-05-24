package com.wintam.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wintam.data.remote.dto.ResolveReportRequest
import com.wintam.data.remote.dto.SanctionType
import com.wintam.ui.theme.Border
import com.wintam.ui.theme.Burgundy
import com.wintam.ui.theme.BurgundySoft
import com.wintam.ui.theme.Cream
import com.wintam.ui.theme.DMSans
import com.wintam.ui.theme.PlayfairDisplay
import com.wintam.ui.theme.TextPrimary
import com.wintam.ui.theme.TextSecondary
import com.wintam.ui.theme.White
import com.wintam.viewmodel.ReportUiState
import com.wintam.viewmodel.ReportViewModel

@Composable
fun AdminScreen(
    viewModel: ReportViewModel,
    onNavigateBack: () -> Unit
){
    val reports by viewModel.reports.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadReport()
    }

    LaunchedEffect(uiState) {
        when (uiState) {
            is ReportUiState.Success -> {
                val msg=(uiState as ReportUiState.Success).message ?:"Acción realizada correctamente"
                viewModel.resetState()
                viewModel.loadReport()
                snackbarHostState.showSnackbar(msg)
            }
            is ReportUiState.Error -> {
                snackbarHostState.showSnackbar((uiState as ReportUiState.Error).message)
                viewModel.resetState()
            }
            else -> {}
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar={
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                }
                Text(
                    text = "Administración",
                    fontFamily = PlayfairDisplay,
                    fontSize = 24.sp,
                    color = TextPrimary
                )
            }
        }
    ){ paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            items(reports){report->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors= CardDefaults.cardColors(containerColor = White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "@${report.reporter} reportó a @${report.reported}",
                            fontFamily = DMSans,
                            fontSize = 14.sp,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Motivo: ${report.reason}",
                            fontFamily = DMSans,
                            fontSize = 13.sp,
                            color = TextSecondary
                        )
                        Text(text = "Fecha: ${report.date}", fontFamily = DMSans, fontSize = 13.sp, color = TextSecondary)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("SANCIÓN", fontFamily = DMSans, fontSize = 11.sp, color = TextSecondary)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Button(
                                onClick = { viewModel.resolveReport(
                                    ResolveReportRequest(
                                        report.id,
                                        SanctionType.WARNING
                                    )
                                ) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = BurgundySoft)
                            ) { Text("Aviso", fontFamily = DMSans, fontSize = 11.sp, color = Cream) }
                            Button(
                                onClick = { viewModel.resolveReport(ResolveReportRequest(report.id, SanctionType.KARMA_PENALTY)) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = BurgundySoft)
                            ) { Text("Karma", fontFamily = DMSans, fontSize = 11.sp, color = Cream) }
                            Button(
                                onClick = { viewModel.resolveReport(ResolveReportRequest(report.id, SanctionType.BAN)) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Burgundy)
                            ) { Text("Baneo", fontFamily = DMSans, fontSize = 11.sp, color = Cream) }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Button(
                            onClick = { viewModel.dismissReport(report.id) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Border)
                        ) {
                            Text("Descartar", fontFamily = DMSans, color = TextSecondary)
                        }
                    }
                }
            }
        }
    }
}