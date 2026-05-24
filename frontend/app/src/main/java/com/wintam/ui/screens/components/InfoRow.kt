package com.wintam.ui.screens.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wintam.ui.theme.Border
import com.wintam.ui.theme.Burgundy
import com.wintam.ui.theme.DMSans
import com.wintam.ui.theme.TextPrimary
import com.wintam.ui.theme.TextSecondary

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