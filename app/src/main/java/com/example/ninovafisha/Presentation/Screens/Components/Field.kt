package com.example.ninovafisha.Presentation.Screens.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun myField(myText: String, text: String, onValueChange: (String) -> Unit){
    /*var text by remember { mutableStateOf("") }*/
    OutlinedTextField(
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            unfocusedIndicatorColor = Color.White,
            focusedIndicatorColor = Color.White,
        ),
        value = text,
        onValueChange = onValueChange,
        label = { Text(myText) },
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier.fillMaxWidth()
    )
}