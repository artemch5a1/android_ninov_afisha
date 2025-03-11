package com.example.ninovafisha.Presentation.Screens.SignInScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ninovafisha.Presentation.Screens.Components.myField

@Composable
fun SigninScreen(controlNav: NavHostController){
    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = "Авторизация",
                fontSize = 32.sp,
                color = Color.Black,
                fontWeight = FontWeight.W800,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .align(Alignment.CenterHorizontally))

            myField(myText = "Введите email")
            myField(myText = "Введите пароль")

            Spacer(modifier = Modifier.padding(10.dp))

            Text(
                text = "Нет аккаунта? Зарегестрируйтесь.",
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.W400,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .align(Alignment.CenterHorizontally).clickable { controlNav.navigate("siginup") })

            Button(
                onClick = {
                    controlNav.navigate("main")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Gray,
                    contentColor = Color.White
                ),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {

                Text(text = "Войти", fontSize = 18.sp)
            }
        }
    }
}