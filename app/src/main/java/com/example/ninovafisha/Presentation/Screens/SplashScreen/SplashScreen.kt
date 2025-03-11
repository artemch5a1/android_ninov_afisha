package com.example.ninovafisha.Presentation.Screens.SplashScreen

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ninovafisha.R
import kotlinx.coroutines.delay

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun SplashScreen(controlNav: NavHostController) {
    val rotation = remember { Animatable(0f) }

    // Запуск анимации
    LaunchedEffect(Unit) {
        rotation.animateTo(
            targetValue = 360f,
            animationSpec = repeatable(
                iterations = 2, // Количество повторений
                animation = tween(durationMillis = 878, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )
        delay(1500L)
        controlNav.navigate("sigin"){
            popUpTo("splash") {
                inclusive = true
            }
        }
    }



    BoxWithConstraints {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp))
        {
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            Arrangement.Center,
                Alignment.CenterHorizontally
            )
            {
                Image(painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(200.dp).graphicsLayer{
                            rotationZ = rotation.value // Применяем анимацию вращения
                        })

                Spacer(modifier = Modifier.padding(16.dp))

                Text(
                    text = "NiNovAfisha",
                    style = TextStyle(
                        fontSize = 20.sp, // Размер шрифта
                        fontFamily = FontFamily.SansSerif, // Шрифт
                        fontWeight = FontWeight.Bold, // Жирный шрифт
                        textAlign = TextAlign.Center, // Выравнивание текста по центру
                        color = Color.Black // Цвет текста
                    ),
                    modifier = Modifier
                        .fillMaxWidth(0.8f) // Занимает 80% ширины экрана
                )
            }

        }
    }
}