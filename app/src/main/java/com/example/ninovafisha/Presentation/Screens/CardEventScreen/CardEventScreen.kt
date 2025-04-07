package com.example.ninovafisha.Presentation.Screens.CardEventScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.ninovafisha.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CardEventScreen(controlNav: NavController, eventId:String, viewModelCardEventScreen: ViewModelCardEventScreen = viewModel()
){
    LaunchedEffect(eventId) {
        viewModelCardEventScreen.loadEvent(eventId)
    }

    val actualState by viewModelCardEventScreen.actualState.collectAsState()
    val eventCard = viewModelCardEventScreen.eventCard

    /*val formattedDate = remember(eventCard.date) {
        SimpleDateFormat("d MMMM yyyy 'года'", Locale("ru"))
            .format(SimpleDateFormat("yyyy-MM-dd").parse(eventCard.date.toString()) ?: Date())
    }*/

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

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f)// Вы можете настроить высоту по своему усмотрению
    ) {
        // Фоновое изображение
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // 1. Сначала изображение
            AsyncImage(
                model = eventCard.image.takeIf { !it.isNullOrEmpty() } ?: R.drawable.empty,
                contentDescription = "Изображение события",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // 2. Затем градиент поверх изображения
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.8f) // Корректное значение alpha (0-1)
                            ),
                            startY = 0f,
                            endY = 1000f // Фиксированное значение вместо Infinity для лучшей производительности
                        )
                    )
            )
        }

        // Текст поверх изображения
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {

            Text(
                text = formatDate(eventCard.title), // Функция форматирования ниже
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

            // Дата (формат "2025-05-02" → "2 мая 2025")
            Text(
                text = formatDate(eventCard.date), // Функция форматирования ниже
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

            // Цена ("2500.0 P" → "2 500 ₽")
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