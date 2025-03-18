package com.example.ninovafisha.Presentation.Screens.SiginUpScreen

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
fun SignUpScreen(controlNav: NavHostController){
    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = "Регистрация",
                fontSize = 32.sp,
                color = Color.Black,
                fontWeight = FontWeight.W800,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .align(Alignment.CenterHorizontally))

            /*myField(myText = "Введите email")
            myField(myText = "Введите ваше имя")
            myField(myText = "Введите вашу фамилию")
            myField(myText = "Введите пароль")
            myField(myText = "Введите подтверждение пароля")*/

            Spacer(modifier = Modifier.padding(10.dp))


            Button(
                onClick = {
                    controlNav.navigate("sigin")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Gray,
                    contentColor = Color.White
                ),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {

                Text(text = "Зарегистрироваться", fontSize = 18.sp)
            }
        }
    }
}