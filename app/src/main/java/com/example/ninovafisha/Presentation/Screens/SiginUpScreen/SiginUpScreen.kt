package com.example.ninovafisha.Presentation.Screens.SiginUpScreen

import android.app.DatePickerDialog
import android.widget.DatePicker
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.ninovafisha.Domain.States.ActualState
import com.example.ninovafisha.Presentation.Screens.Components.myField
import com.example.ninovafisha.Presentation.Screens.Components.myFieldPass
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Calendar
import java.util.Date

@Composable
fun SignUpScreen(controlNav: NavHostController, signUpViewModel: SignUpViewModel = viewModel()){
    val actualState by signUpViewModel.actualState.collectAsState()
    val signupState = signUpViewModel.signupstate

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

            myField(myText = "Введите email",
                text = signupState.email,
                onValueChange = {it -> signUpViewModel.updateSignup(signupState.copy(email = it))})

            myField(myText = "Введите ваше имя",
                    text = signupState.name,
                onValueChange = {it -> signUpViewModel.updateSignup(signupState.copy(name = it))})

            myField(myText = "Введите вашу фамилию",
                text = signupState.surname,
                onValueChange = {it -> signUpViewModel.updateSignup(signupState.copy(surname = it))})



            myFieldPass(myText = "Введите пароль",
                text = signupState.password,
                onValueChange = {it -> signUpViewModel.updateSignup(signupState.copy(password = it))})


            myFieldPass(myText = "Введите подтверждение пароля",
                text = signupState.confirmPass,
                onValueChange = {it -> signUpViewModel.updateSignup(signupState.copy(confirmPass = it))})

            val mDatePickerDialog = DatePickerDialog(
                mContext,
                { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
                    mDate.value = "$mYear-${mMonth+1}-$mDayOfMonth"
                    signUpViewModel.updateSignup(signupState.copy(datebith = "$mYear-${mMonth+1}-$mDayOfMonth"))
                }, mYear, mMonth, mDay
            )

            Spacer(modifier = Modifier.padding(10.dp))

            Text(
                text = mDate.value,
                fontSize = 18.sp,
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally).clickable {
                    mDatePickerDialog.show()
                }
            )

            Spacer(modifier = Modifier.padding(10.dp))

            when(actualState){
                is ActualState.Initialized -> {
                    Button(
                        onClick = {
                            signUpViewModel.signupLogic()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Gray,
                            contentColor = Color.White
                        ),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {

                        Text(text = "Зарегистрироваться", fontSize = 18.sp)
                    }
                    Spacer(modifier = Modifier.padding(10.dp))
                    Text(
                        text = "Уже есть аккаунт? Войдите...",
                        fontSize = 16.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.W400,
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                            .align(Alignment.CenterHorizontally)
                            .clickable { controlNav.navigate("sigin") }
                    )

                }
                is ActualState.Loading ->
                {
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp).align(Alignment.CenterHorizontally), // Размер индикатора
                        color = Color.Blue, // Цвет индикатора
                        strokeWidth = 4.dp // Толщина линии
                    )
                }
                is ActualState.Error ->
                {
                    Button(
                        onClick = {
                            signUpViewModel.signupLogic()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Gray,
                            contentColor = Color.White
                        ),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {

                        Text(text = "Зарегистрироваться", fontSize = 18.sp)
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
                    Spacer(modifier = Modifier.padding(10.dp))
                    Text(
                        text = "Уже есть аккаунт? Войдите...",
                        fontSize = 16.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.W400,
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                            .align(Alignment.CenterHorizontally)
                            .clickable { controlNav.navigate("sigin") }
                    )
                }
                is ActualState.Success ->
                {
                    controlNav.navigate("sigin"){
                        popUpTo("siginup") { inclusive = true }
                    }
                }
            }


        }
    }
}