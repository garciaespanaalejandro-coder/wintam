package com.wintam.ui.screens

import android.window.SplashScreen
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import kotlinx.coroutines.delay
import com.wintam.R
import com.wintam.ui.theme.*

@Composable
fun SplashScreen(onNavigateToLogin: ()-> Unit){
    var visible by remember { mutableStateOf(false) }

    val alpha by animateFloatAsState(
        targetValue = if(visible) 1f else 0f,
        animationSpec = tween(durationMillis = 1000),
        label= "splash_alpha"
    )

    LaunchedEffect(Unit) {
        visible= true
        delay(2500)
        onNavigateToLogin()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors= listOf(BurgundyDark, Burgundy)
                )
            )
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            androidx.compose.foundation.Image(
                painter = painterResource(id = R.drawable.logo_cream),
                contentDescription = "Wintam logo",
                modifier = Modifier
                    .size(120.dp)
                    .alpha(alpha)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Wintam",
                color = Cream,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = PlayfairDisplay,
                modifier = Modifier.alpha(alpha)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Descubre catas cerca de ti",
                color = Cream.copy(alpha = 0.7f),
                fontSize = 14.sp,
                fontFamily = DMSans,
                modifier = Modifier.alpha(alpha)
            )
        }
    }
}