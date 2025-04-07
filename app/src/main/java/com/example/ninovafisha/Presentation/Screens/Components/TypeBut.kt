package com.example.ninovafisha.Presentation.Screens.Components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.example.ninovafisha.Domain.Models.Event
import com.example.ninovafisha.Domain.Models.typeEvent
import com.example.ninovafisha.R

@Composable
fun TypeBut(typeEv: typeEvent?, isSelected: Boolean, OnClick: () -> Unit) {
    var isSelectesNow by remember{ mutableStateOf(isSelected) }
    Button(
        onClick = {
            OnClick()
            isSelectesNow = !isSelectesNow
        },
        modifier = Modifier
            .padding(vertical = 16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelectesNow) Color.Blue else Color.Gray,
            contentColor = Color.White
        )
    ) {
        Text(typeEv?.title ?: "", fontSize = 18.sp)
    }
}