package com.example.ninovafisha.Presentation.Screens.CardEventScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.ninovafisha.Domain.Constant
import com.example.ninovafisha.Domain.States.ActualState
import com.example.ninovafisha.Domain.States.EventState
import com.example.ninovafisha.Presentation.Screens.Components.Dialog
import com.example.ninovafisha.R
import io.github.jan.supabase.auth.auth
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun CardEventScreen(controlNav: NavController, eventId:String, viewModelCardEventScreen: ViewModelCardEventScreen = viewModel{ ViewModelCardEventScreen(eventId, controlNav) }
){


    val actualState by viewModelCardEventScreen.actualState.collectAsState()
    val eventState by viewModelCardEventScreen.eventState.collectAsState()
    val eventCard = viewModelCardEventScreen.eventCard
    val user = viewModelCardEventScreen.user
    var showConfirmationDialog by remember { mutableStateOf(false) }

    /*val formattedDate = remember(eventCard.date) {
        SimpleDateFormat("d MMMM yyyy 'года'", Locale("ru"))
            .format(SimpleDateFormat("yyyy-MM-dd").parse(eventCard.date.toString()) ?: Date())
    }*/

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    // Вычисляем высоту (30% от высоты экрана, но не более 60% от ширины)
    val imageHeight = remember(screenHeight, screenWidth) {
        minOf(
            screenHeight * 0.5f  // Ограничение: не более 60% от ширины
        )
    }

    fun formatDate(rawDate: String?): String {
        if (rawDate.isNullOrEmpty()) return ""
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = inputFormat.parse(rawDate)
            val outputFormat = SimpleDateFormat("d MMMM yyyy", Locale("ru"))
            outputFormat.format(date)
        } catch (e: Exception) {
            rawDate
        }
    }

    // Функция форматирования цены
    fun formatPrice(price: Float?): String {
        return if (price != null) {
            "%,d ₽".format(price.toInt()).replace(',', ' ')
        } else {
            "0 ₽"
        }
    }

    when(actualState){
        is ActualState.Error ->{
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
        is ActualState.Success, ActualState.Initialized ->{
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    LazyColumn(
                        modifier = Modifier

                    ) {
                        items(1) {
                            Box(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                // Фоновое изображение
                                AsyncImage(
                                    model = eventCard.image.takeIf { !it.isNullOrEmpty() }
                                        ?: R.drawable.empty,
                                    contentDescription = "Изображение события",
                                    modifier = Modifier.fillMaxSize().height(imageHeight),
                                    contentScale = ContentScale.Crop
                                )


                                // Градиент поверх изображения
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            brush = Brush.verticalGradient(
                                                colors = listOf(
                                                    Color.Transparent,
                                                    Color.Black.copy(alpha = 0.8f)
                                                ),
                                                startY = 0f,
                                                endY = 1000f
                                            )
                                        )
                                )

                                // Текст поверх изображения (название, дата, цена)
                                Column(
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .padding(16.dp)
                                ) {
                                    Text(
                                        text = eventCard.title ?: "",
                                        color = Color.White,
                                        style = MaterialTheme.typography.titleLarge.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 30.sp,
                                            shadow = Shadow(
                                                color = Color.Black.copy(alpha = 0.5f),
                                                offset = Offset(1f, 1f),
                                                blurRadius = 4f
                                            )
                                        )
                                    )

                                    Text(
                                        text = formatDate(eventCard.date),
                                        color = Color.White,
                                        style = MaterialTheme.typography.titleLarge.copy(
                                            fontWeight = FontWeight.Bold,
                                            shadow = Shadow(
                                                color = Color.Black.copy(alpha = 0.5f),
                                                offset = Offset(1f, 1f),
                                                blurRadius = 4f
                                            )
                                        )
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    if (Constant.supabase.auth.currentUserOrNull() != null) {
                                        Button(
                                            onClick = { /* Действие при нажатии */ },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color.Gray,
                                                contentColor = Color.White
                                            ),
                                            elevation = ButtonDefaults.buttonElevation(
                                                defaultElevation = 4.dp,
                                                pressedElevation = 2.dp
                                            ),
                                            modifier = Modifier.padding(top = 8.dp)
                                        ) {
                                            Text(
                                                text = formatPrice(eventCard.cost),
                                                style = MaterialTheme.typography.bodyLarge.copy(
                                                    fontWeight = FontWeight.Bold
                                                )
                                            )
                                        }
                                    }
                                }
                            }

                            // Блок с жанром и возрастом (под изображением)
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                // Строка с жанром и возрастом
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Блок "Жанр"
                                    Box(
                                        modifier = Modifier
                                            .background(
                                                color = Color.LightGray.copy(alpha = 0.2f),
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            .padding(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                        Column() {
                                            Text(
                                                text = "Рейтинг",
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.Normal,
                                                color = Color.Gray,
                                                fontSize = 20.sp
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "${eventCard.rating}/10 звезд",
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.Black
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.padding(5.dp))

                                    // Блок "Возраст"
                                    Box(
                                        modifier = Modifier
                                            .background(
                                                color = Color.LightGray.copy(alpha = 0.2f),
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            .padding(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                        Column() {
                                            Text(
                                                text = "Возраст",
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.Normal,
                                                color = Color.Gray,
                                                fontSize = 20.sp
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "${eventCard.ageConst}+",
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.Black
                                            )
                                        }
                                    }
                                }
                                if (eventCard.descLong != null) {
                                    Column {
                                        Spacer(Modifier.padding(10.dp))
                                        Text(
                                            text = eventCard.descLong ?: "",
                                            color = Color.Black,
                                            style = MaterialTheme.typography.titleLarge.copy(
                                                fontSize = 15.sp,
                                                shadow = Shadow(
                                                    color = Color.Black.copy(alpha = 0.5f),
                                                    offset = Offset(1f, 1f),
                                                    blurRadius = 4f
                                                )
                                            )
                                        )
                                        Spacer(Modifier.padding(10.dp))
                                    }
                                }
                                Spacer(modifier = Modifier.padding(15.dp))
                                when (eventState) {
                                    is EventState.Initialized -> {
                                        if (eventCard.author == user) {
                                            Row {
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

                                                    Text(text = "Удалить", fontSize = 18.sp)
                                                }
                                                if (showConfirmationDialog) {
                                                    Dialog(
                                                        showConfirmationDialog = showConfirmationDialog,
                                                        onClick = {
                                                            showConfirmationDialog = false
                                                            viewModelCardEventScreen.deleteLogic(
                                                                eventId
                                                            )
                                                        },
                                                        onDismissRequest = {
                                                            showConfirmationDialog = false
                                                        },
                                                        onClickNo = {
                                                            showConfirmationDialog = false
                                                        },
                                                        title = "Подтверждение",
                                                        desc = "Вы уверены, что хотите изменить это событие?"
                                                    )
                                                }
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Button(
                                                    onClick = {
                                                        controlNav.navigate("UpdateOrAdd/${eventId}") {
                                                            popUpTo("eventCard/${eventId}") {
                                                                inclusive = true
                                                            }
                                                        }
                                                    },
                                                    colors = ButtonDefaults.buttonColors(
                                                        containerColor = Color.Gray,
                                                        contentColor = Color.White
                                                    ),
                                                    modifier = Modifier
                                                ) {

                                                    Text(text = "Изменить", fontSize = 18.sp)
                                                }
                                            }
                                        }
                                    }

                                    is EventState.Loading -> {
                                        CircularProgressIndicator(
                                            modifier = Modifier
                                                .size(48.dp)
                                                .align(Alignment.CenterHorizontally), // Размер индикатора
                                            color = Color.Blue, // Цвет индикатора
                                            strokeWidth = 4.dp // Толщина линии
                                        )
                                    }

                                    is EventState.DeleteOrAdd -> {
                                        controlNav.navigate("main") {
                                            popUpTo("eventCard/${eventId}") { inclusive = true }
                                        }
                                    }

                                    is EventState.Updated -> {

                                    }

                                    is EventState.Error -> {
                                        Button(
                                            onClick = {
                                                viewModelCardEventScreen.deleteLogic(eventId)
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color.Gray,
                                                contentColor = Color.White
                                            ),
                                            modifier = Modifier.align(Alignment.CenterHorizontally)
                                        ) {

                                            Text(text = "Удалить", fontSize = 18.sp)
                                        }
                                        Spacer(modifier = Modifier.padding(10.dp))
                                        Text(
                                            text = (eventState as EventState.Error).message,
                                            fontSize = 16.sp,
                                            color = Color.Black,
                                            fontWeight = FontWeight.W400,
                                            modifier = Modifier
                                                .padding(bottom = 16.dp)
                                                .align(Alignment.CenterHorizontally)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .zIndex(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Выход",
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(16.dp)
                                .align(Alignment.TopStart)
                                .clickable {
                                    controlNav.navigate("main") {
                                        popUpTo("eventCard/${eventId}")
                                    }
                                },
                            tint = Color.Black
                        )
                    }
            }
        }
    }
}