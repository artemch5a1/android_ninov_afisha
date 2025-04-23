package com.example.ninovafisha.Presentation.Screens.Components

import android.text.method.PasswordTransformationMethod
import android.widget.AutoCompleteTextView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lint.kotlin.metadata.Visibility
import com.example.ninovafisha.R


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
        modifier = Modifier.padding(horizontal = 0.05f.dp)
    )
}



@Composable
fun DropdownField(
    label: String,
    items: List<String>,
    selectedItem: String,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        OutlinedTextField(
            value = selectedItem,
            onValueChange = {},  // Запрещаем ручное редактирование
            readOnly = true,     // Только выбор из списка
            label = { Text(label) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                unfocusedIndicatorColor = Color.White,
                focusedIndicatorColor = Color.White,
            ),
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .padding(horizontal = 0.05f.dp)
                .clickable { expanded = true }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun myFieldCost(myText: String, text: String, onValueChange: (String) -> Unit){
    /*var text by remember { mutableStateOf("") }*/
    OutlinedTextField(
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            unfocusedIndicatorColor = Color.White,
            focusedIndicatorColor = Color.White,
        ),
        value = text,
        onValueChange = { newValue ->
            // Фильтруем ввод: разрешаем только цифры и знак минуса в начале
            val filteredValue = newValue.filter { it.isDigit() || it == '.'}
            onValueChange(filteredValue)
        },
        label = { Text(myText) },
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier.padding(horizontal = 0.05f.dp)
    )
}


@Composable
fun myFieldPass(myText: String, text: String, onValueChange: (String) -> Unit) {
    var passSee by remember { mutableStateOf(false) }

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
        modifier = Modifier.padding(horizontal = 0.5f.dp),
        visualTransformation = if (passSee) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        trailingIcon = {
            // Кнопка для переключения видимости
            IconButton(onClick = { passSee = !passSee }) {
                Icon(
                    painter = if (passSee) painterResource(id = R.drawable.eye_open) else painterResource(
                        id = R.drawable.eye_close
                    ),
                    contentDescription = if (passSee) "Hide password" else "Show password"
                )
            }
        }
    )
}

@Composable
fun myFieldSearch(myText: String, text: String, onValueChange: (String) -> Unit, OnClick: () -> Unit) {
    var passSee by remember { mutableStateOf(false) }

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
        modifier = Modifier.fillMaxWidth(),
        trailingIcon = {
            IconButton(onClick = OnClick) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "перезагрузка"
                )
            }
        }
    )
}
