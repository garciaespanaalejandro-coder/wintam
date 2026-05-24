package com.wintam.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wintam.R
import com.wintam.ui.components.WintamTextField
import com.wintam.ui.theme.Burgundy
import com.wintam.ui.theme.BurgundySoft
import com.wintam.ui.theme.Cream
import com.wintam.ui.theme.DMSans
import com.wintam.ui.theme.PlayfairDisplay
import com.wintam.ui.theme.TextPrimary
import com.wintam.ui.theme.TextSecondary
import com.wintam.ui.theme.White
import com.wintam.viewmodel.CataViewModel

@Composable
fun FeedScreen(
    viewModel: CataViewModel,
    onNavigateToCataDetail: (Long) -> Unit,
    onNavigateToCreateCata: () -> Unit,
    onNavigateToProfile: () -> Unit,
){
    val uiState by viewModel.uiState.collectAsState();
    val catas by viewModel.catas.collectAsState();
    var searchQuery by remember { mutableStateOf("") }
    var selectedWineType by remember { mutableStateOf<String?>(null)}
    var selectedLevel by remember{ mutableStateOf<String?>(null)}
    var selectedLocation by remember { mutableStateOf<String?>(null) }
    var selectedStatus by remember {mutableStateOf<String?>(null)}
    var showFilters by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        viewModel.loadCatas()
    }

    Scaffold(
        topBar = {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp)
            ) {
                Image(
                    painter = painterResource(id= R.drawable.logo_burgundy),
                    contentDescription= "Wintam logo",
                    modifier = Modifier.size(72.dp)
                )
                Text(
                    text = "Wintam",
                    fontFamily = PlayfairDisplay,
                    fontSize = 28.sp,
                    color = TextPrimary
                )
            }
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text("Inicio") },
                    selected = true,
                    onClick = {}
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Add, contentDescription = null) },
                    label = { Text("Crear") },
                    selected = false,
                    onClick = { onNavigateToCreateCata() }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    label = { Text("Perfil") },
                    selected = false,
                    onClick = { onNavigateToProfile() }
                )
            }
        }
    ){paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            WintamTextField(
                value = searchQuery,
                onValueChange = {searchQuery = it},
                label = "Buscar por título",
                keyboardActions = KeyboardActions ( onSearch = {
                    viewModel.searchCata(
                        wineType = selectedWineType,
                        title = searchQuery,
                        location = selectedLocation,
                        experienceLevel = selectedLevel
                    )
                    showFilters = false
                }),
                trailingIcon = {
                    IconButton(onClick = {
                        viewModel.searchCata(
                            wineType = selectedWineType,
                            title = searchQuery,
                            cataStatus = selectedStatus,
                            location = selectedLocation,
                            experienceLevel = selectedLevel
                        )
                        showFilters = false
                    }) {
                        Icon(Icons.Default.Search, contentDescription = null, tint = TextSecondary)
                    }
                }
            )

            TextButton(onClick = {showFilters=!showFilters}){
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = null,
                    tint = Burgundy
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text("Filtros", color=Burgundy, fontFamily = DMSans)
            }


            if (showFilters){
                Column (modifier = Modifier.padding(horizontal = 16.dp)) {
                    WintamTextField(
                        value = selectedWineType ?: "",
                        onValueChange = { selectedWineType = it.ifBlank { null } },
                        label = "Tipo de vino"
                    )

                    WintamTextField(
                        value = selectedLevel ?: "",
                        onValueChange = { selectedLevel = it.ifBlank { null } },
                        label = "Nivel"
                    )

                    WintamTextField(
                        value = selectedLocation ?: "",
                        onValueChange = { selectedLocation = it.ifBlank { null } },
                        label = "Localización"
                    )
                    Button(
                        onClick = {
                            viewModel.searchCata(
                                wineType = selectedWineType,
                                title = searchQuery,
                                cataStatus = selectedStatus,
                                location = selectedLocation,
                                experienceLevel = selectedLevel
                            )
                            showFilters = false
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Burgundy,
                            disabledContainerColor = BurgundySoft
                        )
                    ) {
                        Text("Buscar", color = Cream, fontFamily = DMSans)
                    }
                }
            }

            LazyColumn {
                items(catas){cata ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .clickable{onNavigateToCataDetail(cata.id)},
                        shape = RoundedCornerShape(16.dp),
                        colors= CardDefaults.cardColors(containerColor = White)
                    ) {

                        Column(modifier = Modifier.padding(16.dp)){
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text="@ ${cata.hostUsername}",
                                    fontFamily = DMSans,
                                    fontSize = 13.sp,
                                    color= TextSecondary
                                )
                                Text(
                                    text= cata.wineType,
                                    fontFamily = DMSans,
                                    fontSize = 12.sp,
                                    color= Burgundy
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = cata.title,
                                fontFamily = PlayfairDisplay,
                                fontSize = 18.sp,
                                color= TextPrimary
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            //Lugsar, fecha y hora.
                            Row {
                                Icon(Icons.Default.LocationOn, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(14.dp))
                                Text(text = cata.location, fontFamily = DMSans, fontSize = 13.sp, color = TextSecondary)
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(Icons.Default.CalendarToday, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(14.dp))
                                Text(text = cata.scheduleDate, fontFamily = DMSans, fontSize = 13.sp, color = TextSecondary)
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(Icons.Default.Schedule, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(14.dp))
                                Text(text = cata.scheduledTime, fontFamily = DMSans, fontSize = 13.sp, color = TextSecondary)
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row {
                                Text(
                                    text = cata.experienceLevel.name,
                                    fontFamily = DMSans,
                                    fontSize = 12.sp,
                                    color = Burgundy
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Text(
                                    text= cata.cataStatus.name,
                                    fontFamily = DMSans,
                                    fontSize = 12.sp,
                                    color= TextSecondary
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = { onNavigateToCataDetail(cata.id) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Burgundy)
                            ) {
                                Text("Ver cata", color=Cream, fontFamily = DMSans)
                            }
                        }
                    }
                }
            }

        }

    }
}