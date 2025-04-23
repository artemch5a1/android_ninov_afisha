package com.example.ninovafisha.Presentation.Screens.UpdateOrAddScreen

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.ninovafisha.Domain.Models.EventCard
import com.example.ninovafisha.Domain.States.ActualState
import com.example.ninovafisha.Domain.States.EventState
import com.example.ninovafisha.Presentation.Screens.Components.Dialog
import com.example.ninovafisha.Presentation.Screens.Components.DropdownField
import com.example.ninovafisha.Presentation.Screens.Components.myField
import com.example.ninovafisha.Presentation.Screens.Components.myFieldCost
import java.util.Calendar
import java.util.Date

@Composable
fun UpdateOrAddScreen(id:String?, controlNav: NavHostController, viewModelUpdateOrDelete: ViewModelUpdateOrDelete = viewModel {
    ViewModelUpdateOrDelete(id = id)
}){
    val actualState by viewModelUpdateOrDelete.actualState.collectAsState()
    val eventState by viewModelUpdateOrDelete.eventState.collectAsState()
    val eventCard: EventCard = viewModelUpdateOrDelete.eventCard
    var showConfirmationDialog by remember { mutableStateOf(false) }

    val mContext = LocalContext.current // Получение контекста Android

    val mYear: Int
    val mMonth: Int
    val mDay: Int

    val mCalendar = Calendar.getInstance()

    mYear = mCalendar.get(Calendar.YEAR)
    mMonth = mCalendar.get(Calendar.MONTH)
    mDay = mCalendar.get(Calendar.DAY_OF_MONTH)

    mCalendar.time = Date()

    val mDate = remember { mutableStateOf("Выберите дату") }
    var textAction:String = ""
    var confirmation:String = ""
    var textButton:String = ""
    var onClick: () -> Unit

    if(id == null){
        textAction = "Добавление"
        confirmation = "Вы точно хотите добавить событие?"
        textButton = "Добавить"
        onClick = {  }
    }
    else {
        textAction = "Изменение"
        confirmation = "Вы точно хотите изменить событие?"
        textButton = "Изменить"
        onClick = { viewModelUpdateOrDelete.updateEvent() }
    }


    when(actualState)
    {
        is ActualState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ){
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
        is ActualState.Success, ActualState.Initialized -> {
            Box(modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Text(
                        text = textAction,
                        fontSize = 32.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.W800,
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                            .align(Alignment.CenterHorizontally))

                    myField(myText = "Название события",
                        text = eventCard.title,
                        onValueChange = {it -> viewModelUpdateOrDelete.updateEventInfo(eventCard.copy(title = it))})

                    myField(myText = "Описание события",
                        text = eventCard.desc,
                        onValueChange = {it -> viewModelUpdateOrDelete.updateEventInfo(eventCard.copy(desc = it))})


                    myFieldCost(myText = "Цена (в рублях)",
                        text = eventCard.cost.toString(),
                        onValueChange = {it -> viewModelUpdateOrDelete.updateEventInfo(eventCard.copy(cost = it.toFloat()))})

                    var selectedAgeConst by remember { mutableStateOf("+14") }

                    DropdownField(
                        label = "Выберите возрастное исключение",
                        items = listOf("+14", "+16", "+18"),
                        selectedItem = selectedAgeConst,
                        onItemSelected = { selectedAgeConst = it;
                            viewModelUpdateOrDelete.updateEventInfo(eventCard.copy(ageConst = it.removePrefix("+").toInt()))}
                    )

                    val mDatePickerDialog = DatePickerDialog(
                        mContext,
                        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
                            mDate.value = "$mYear-${mMonth+1}-$mDayOfMonth"
                            viewModelUpdateOrDelete.updateEventInfo(eventCard.copy(date = "$mYear-${mMonth+1}-$mDayOfMonth"))
                        }, mYear, mMonth, mDay
                    )
                    Spacer(modifier = Modifier.padding(10.dp))

                    myField(myText = "Длинное описание события", text = eventCard.descLong + "",
                        onValueChange = {it -> viewModelUpdateOrDelete.updateEventInfo(eventCard.copy(descLong = it))})

                    Spacer(modifier = Modifier.padding(10.dp))

                    Text(
                        text = mDate.value,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .align(alignment = Alignment.CenterHorizontally)
                            .clickable {
                                mDatePickerDialog.show()
                            }
                    )
                    Spacer(modifier = Modifier.padding(10.dp))
                    when(eventState)
                    {
                        is EventState.Initialized -> {
                            Row (modifier = Modifier.align(alignment = Alignment.CenterHorizontally)){
                                Button(
                                    onClick = {
                                        showConfirmationDialog = true
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.Gray,
                                        contentColor = Color.White
                                    ),
                                    modifier = Modifier
                                ) {

                                    Text(text = textButton, fontSize = 18.sp)
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(
                                    onClick = {
                                        controlNav.navigate("eventCard/${id}"){
                                            popUpTo("EventCard/${id}") { inclusive = true }
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.Gray,
                                        contentColor = Color.White
                                    ),
                                    modifier = Modifier
                                ) {

                                    Text(text = "Отмена", fontSize = 18.sp)
                                }
                                if(showConfirmationDialog){
                                    Dialog(
                                        showConfirmationDialog = showConfirmationDialog,
                                        onClick = { showConfirmationDialog = false
                                            onClick() },
                                        onDismissRequest = { showConfirmationDialog = false },
                                        onClickNo = { showConfirmationDialog = false },
                                        title = "Подтверждение",
                                        desc = confirmation
                                    )
                                }
                            }
                        }
                        is EventState.Loading -> {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                        }
                        is EventState.Error -> {
                            Column (){
                                Row (modifier = Modifier.align(alignment = Alignment.CenterHorizontally)){
                                    Button(
                                        onClick = {
                                            showConfirmationDialog = true
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color.Gray,
                                            contentColor = Color.White
                                        ),
                                        modifier = Modifier
                                    ) {

                                        Text(text = textButton, fontSize = 18.sp)
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Button(
                                        onClick = {
                                            controlNav.navigate("eventCard/${id}"){
                                                popUpTo("EventCard/${id}") { inclusive = true }
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color.Gray,
                                            contentColor = Color.White
                                        ),
                                        modifier = Modifier
                                    ) {

                                        Text(text = "Отмена", fontSize = 18.sp)
                                    }
                                }
                                Spacer(modifier = Modifier.padding(10.dp))
                                Text(
                                    text = (eventState as EventState.Error).message ?: "",
                                    color = Color.Black,
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontSize = 10.sp,
                                        shadow = Shadow(
                                            color = Color.Black.copy(alpha = 0.5f),
                                            offset = Offset(1f, 1f),
                                            blurRadius = 4f
                                        )
                                    ),
                                    textAlign = TextAlign.Center, // Центрирование по горизонтали
                                    modifier = Modifier.fillMaxWidth() // Занимает всю доступную ширину
                                )
                                if(showConfirmationDialog){
                                    Dialog(
                                        showConfirmationDialog = showConfirmationDialog,
                                        onClick = { showConfirmationDialog = false
                                            onClick() },
                                        onDismissRequest = { showConfirmationDialog = false },
                                        onClickNo = { showConfirmationDialog = false },
                                        title = "Подтверждение",
                                        desc = confirmation
                                    )
                                }
                            }
                        }
                        is EventState.Updated -> {
                            controlNav.navigate("eventCard/${id}"){
                                popUpTo("UpdateOrAdd/${id}") { inclusive = true }
                            }
                        }
                        is EventState.DeleteOrAdd -> {
                            controlNav.navigate("main"){
                                popUpTo("UpdateOrAdd/${id}") { inclusive = true }
                            }
                        }
                    }
                }
            }
        }
        is ActualState.Error -> {
            Text(
                text = (actualState as ActualState.Error).message  ?: "",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.5f),
                        offset = Offset(1f, 1f),
                        blurRadius = 4f
                    )
                )
            )
        }
    }
}