package com.example.ninovafisha.Presentation.Screens.Components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.ninovafisha.Domain.Models.Event
import com.example.ninovafisha.R

@Composable
fun EventCard(event: Event?, controlNav: NavHostController, isAuthor:Boolean, isAdmin:Boolean){
    Column(modifier = Modifier
        .padding(16.dp)
        .padding(top = 50.dp)
        .padding(end = 16.dp)) {

        val imageModel = event?.image.takeIf { !it.isNullOrEmpty() }
            ?: R.drawable.empty // Запасное изображение

        val configuration = LocalConfiguration.current
        val screenHeight = configuration.screenHeightDp.dp
        val screenWidth = configuration.screenWidthDp.dp

        // Вычисляем высоту (30% от высоты экрана, но не более 60% от ширины)
        val imageHeight = remember(screenHeight, screenWidth) {
            minOf(
                screenHeight * 0.23f,  // 30% от высоты экрана
                screenWidth * 0.6f     // Ограничение: не более 60% от ширины
            )
        }
        if(isAuthor){
            Text(
                text = "Это ваше событие",
                fontSize = 20.sp,
                color = Color.Black,
                fontWeight = FontWeight.W800,
                modifier = Modifier
                    .padding(bottom = 16.dp)
            )
        }
        Spacer(modifier = Modifier.padding(5.dp))
        AsyncImage(
            model = imageModel,
            contentDescription = "Концертное изображение",
            modifier = Modifier
                .widthIn(max = 310.dp)
                .fillMaxWidth()
                .height(imageHeight)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop,
            error = painterResource(R.drawable.empty), // Если ошибка загрузки
            placeholder = painterResource(R.drawable.empty) // Плейсхолдер при загрузке
        )

        Spacer(modifier = Modifier.padding(10.dp))

        Row(modifier = Modifier.align(alignment = Alignment.Start)) {
            Text(
                text = "" + event?.title,
                fontSize = 25.sp,
                color = Color.Black,
                fontWeight = FontWeight.W800,
                modifier = Modifier
                    .padding(bottom = 16.dp)

            )
        }

        Text(
            text = "" + event?.desc,
            fontSize = 15.sp,
            color = Color.Black,
            fontWeight = FontWeight.W800,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(alignment = Alignment.Start)
        )
        Row(){
            Button(
                onClick = {
                    controlNav.navigate("eventCard/${event?.id}")
                },
                modifier = Modifier,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Gray,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = if (isAuthor || isAdmin) "Редактировать" else "Подробнее",
                    fontSize = 15.sp
                )
            }
            Spacer(modifier = Modifier.padding(10.dp))
            Text(
                text = "" + event?.date,
                fontSize = 15.sp,
                color = Color.Black,
                fontWeight = FontWeight.W800,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.padding(10.dp))
            Text(
                text = "Билет от: " + event?.cost?.toInt() + " рублей",
                fontSize = 15.sp,
                color = Color.Black,
                fontWeight = FontWeight.W800,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .align(Alignment.CenterVertically)
            )
        }
    }
}