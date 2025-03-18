package com.example.ninovafisha.Presentation.Screens.SignInScreen


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ninovafisha.Domain.Constant
import com.example.ninovafisha.Domain.States.ActualState
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.exception.AuthRestException
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class SignInViewModel: ViewModel() {
    private val _actualState = MutableStateFlow<ActualState>(ActualState.Ready)
    val actualState: StateFlow<ActualState> = _actualState.asStateFlow()

    fun SignInLogic(_email:String, _password:String) {
        _actualState.value = ActualState.Loading
        viewModelScope.launch {
            try {
                Log.d("SignInViewModel", "Попытка входа: email=$_email, password=$_password")
                Constant.supabase.auth.signInWith(Email)
                {
                    email = _email
                    password = _password
                }
                _actualState.value = ActualState.Success("Норм")
            }
            catch (_ex: AuthRestException){
                _actualState.value = ActualState.Error(_ex.errorDescription)
            }
        }

    }
}