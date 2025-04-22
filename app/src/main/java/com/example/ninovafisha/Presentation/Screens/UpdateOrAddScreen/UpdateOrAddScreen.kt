package com.example.ninovafisha.Presentation.Screens.UpdateOrAddScreen

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ninovafisha.Domain.Models.EventCard
import com.example.ninovafisha.Domain.States.ActualState
import com.example.ninovafisha.Presentation.Screens.Components.myField
import com.example.ninovafisha.Presentation.Screens.Components.myFieldCost
import com.example.ninovafisha.Presentation.Screens.Components.myFieldPass

@Composable
fun UpdateOrDeleteString(id:String?, viewModelUpdateOrDelete: ViewModelUpdateOrDelete = viewModel {
    ViewModelUpdateOrDelete(id = id)
}){
    val actualState by viewModelUpdateOrDelete.actualState.collectAsState()
    val eventState by viewModelUpdateOrDelete.eventState.collectAsState()
    val eventCard: EventCard = viewModelUpdateOrDelete.eventCard

    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = "Изменение события",
                fontSize = 32.sp,
                color = Color.Black,
                fontWeight = FontWeight.W800,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .align(Alignment.CenterHorizontally))

            myField(myText = "Название события",
                text = eventCard.title,
                onValueChange = {it -> viewModelUpdateOrDelete.updateEvent(eventCard.copy(title = it))})

            myField(myText = "Описание события",
                text = eventCard.desc,
                onValueChange = {it -> viewModelUpdateOrDelete.updateEvent(eventCard.copy(desc = it))})

            myField(myText = "Ограничение по возрасту",
                text = eventCard.ageConst.toString(),
                onValueChange = {it -> viewModelUpdateOrDelete.updateEvent(eventCard.copy(ageConst = it.toInt()))})

            myFieldCost(myText = "Цена (в рублях)",
                text = eventCard.cost.toString(),
                onValueChange = {it -> viewModelUpdateOrDelete.updateEvent(eventCard.copy(cost = it.toFloat()))})
        }
    }
}