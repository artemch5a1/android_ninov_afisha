package com.example.ninovafisha.Presentation.Screens.Components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ninovafisha.Domain.Models.Event
import com.example.ninovafisha.R

@Composable
fun EventCard(event: Event?){
    Column(modifier = Modifier
        .padding(16.dp)
        .padding(top = 50.dp)
        .padding(end = 16.dp)) {

        val imageModel = event?.image.takeIf { !it.isNullOrEmpty() }
            ?: R.drawable.empty // Запасное изображение

        AsyncImage(
            model = imageModel,
            contentDescription = "Концертное изображение",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(bottom = 16.dp),
            contentScale = ContentScale.Crop,
            error = painterResource(R.drawable.empty), // Если ошибка загрузки
            placeholder = painterResource(R.drawable.empty) // Плейсхолдер при загрузке
        )

        Text(
            text = "" + event?.title,
            fontSize = 25.sp,
            color = Color.Black,
            fontWeight = FontWeight.W800,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(alignment = Alignment.Start)
        )
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

                },
                modifier = Modifier,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Gray,
                    contentColor = Color.White
                )
            ) {
                Text("Подробнее", fontSize = 15.sp)
            }
            Spacer(modifier = Modifier.padding(10.dp))
            Text(
                text = "" + event?.date,
                fontSize = 15.sp,
                color = Color.Black,
                fontWeight = FontWeight.W800,
                modifier = Modifier
                    .padding(bottom = 16.dp).align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.padding(10.dp))
            Text(
                text = "Билет от: " + event?.cost?.toInt() + " рублей",
                fontSize = 15.sp,
                color = Color.Black,
                fontWeight = FontWeight.W800,
                modifier = Modifier
                    .padding(bottom = 16.dp).align(Alignment.CenterVertically)
            )
        }
    }
}