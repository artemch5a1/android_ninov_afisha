package com.example.ninovafisha.Presentation.Screens.SignInScreen

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.ninovafisha.Domain.States.ActualState
import com.example.ninovafisha.Presentation.Screens.Components.myField
import com.example.ninovafisha.Presentation.Screens.Components.myFieldPass

@Composable
fun SigninScreen(controlNav: NavHostController, signInViewModel: SignInViewModel = viewModel()){

    val actualState by signInViewModel.actualState.collectAsState()
    val signinState = signInViewModel.signInState


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

            myField(myText = "Введите email",
                text = signinState.email,
                onValueChange = {it -> signInViewModel.updateSign(signinState.copy(email = it))})

            myFieldPass(myText = "Введите пароль",
                text = signinState.password,
                onValueChange = {it -> signInViewModel.updateSign(signinState.copy(password = it))})

            Spacer(modifier = Modifier.padding(10.dp))

            Text(
                text = "Нет аккаунта? Зарегистрируйтесь.",
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.W400,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .align(Alignment.CenterHorizontally)
                    .clickable { controlNav.navigate("siginup") })


            when (actualState){
                is ActualState.Initialized -> {
                    Button(
                        onClick = {
                            signInViewModel.SignInLogic()
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
                is ActualState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp).align(Alignment.CenterHorizontally), // Размер индикатора
                        color = Color.Blue, // Цвет индикатора
                        strokeWidth = 4.dp // Толщина линии
                    )
                }
                is ActualState.Error -> {
                    Button(
                        onClick = {
                            signInViewModel.SignInLogic()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Gray,
                            contentColor = Color.White
                        ),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {

                        Text(text = "Войти", fontSize = 18.sp)
                    }
                    Text(
                        text = (actualState as ActualState.Error).message,
                        fontSize = 16.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.W400,
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                            .align(Alignment.CenterHorizontally)
                            )

                }
                is ActualState.Success -> {
                    controlNav.navigate("main")
                }
            }

        }
    }
}