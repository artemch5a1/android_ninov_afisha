package com.example.ninovafisha.Presentation.Screens.MainScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.ninovafisha.Domain.Constant
import com.example.ninovafisha.Domain.States.SignInState
import com.example.ninovafisha.Presentation.Screens.Components.myField
import com.example.ninovafisha.Presentation.Screens.SignInScreen.SignInViewModel
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
// Или комбинированный импорт:
import androidx.compose.runtime.*

@Composable
fun MainScreen(controlNav: NavHostController, viewModelMainScreen: ViewModelMainScreen = viewModel()) {

    var greetingText by remember { mutableStateOf<String?>(null) }

// 2. Загружаем данные при инициализации или по необходимости
    LaunchedEffect(Unit) {
        val part1 = viewModelMainScreen.GetSignin() ?: ""
        greetingText = part1
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = "Привет, " + greetingText,
                fontSize = 32.sp,
                color = Color.Black,
                fontWeight = FontWeight.W800,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .align(alignment = Alignment.CenterHorizontally)
            )

            Button(
                onClick = {
                    controlNav.navigate("sigin")
                    viewModelMainScreen.GetOut()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Gray,
                    contentColor = Color.White
                ),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {

                Text(text = "Назад", fontSize = 18.sp)
            }
        }
    }
}