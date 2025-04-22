package com.example.ninovafisha.Presentation.Screens.SignInScreen


import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ninovafisha.Domain.Constant
import com.example.ninovafisha.Domain.States.ActualState
import com.example.ninovafisha.Domain.States.SignInState
import com.example.ninovafisha.Domain.Utils.isEmailValid
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.exception.AuthRestException
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout


class SignInViewModel: ViewModel() {
    private val _actualState = MutableStateFlow<ActualState>(ActualState.Initialized)
    val actualState: StateFlow<ActualState> = _actualState.asStateFlow()

    private val _signInState = mutableStateOf(SignInState())
    val signInState: SignInState get() = _signInState.value

    fun updateSign(newSign: SignInState){
        _signInState.value = newSign
    }



    fun SignInLogic() {
        _actualState.value = ActualState.Loading
        viewModelScope.launch {
            if(!signInState.email.isEmailValid()) {
                _actualState.value = ActualState.Error("Неправильный email")
            }
            else{
                try {
                    Log.d("SignInViewModel", "Попытка входа: email=${signInState.email}, password=${signInState.password}")
                    withTimeout(8000){
                        Constant.supabase.auth.signInWith(Email)
                        {
                            email = signInState.email
                            password = signInState.password
                        }
                    }
                    _actualState.value = ActualState.Success("Норм")
                }
                catch (_ex: AuthRestException){
                    _actualState.value = ActualState.Error(_ex.errorDescription?: "Ошибка получения данных")
                }
                catch(ex: TimeoutCancellationException){
                    _actualState.value = ActualState.Error("${ex.message}")
                }
                catch (ex: Exception) {
                    Log.e("SignInViewModel", "Неожиданная ошибка", ex)
                    _actualState.value = ActualState.Error("Неожиданная ошибка: ${ex.message}")
                }
            }
        }
    }
}