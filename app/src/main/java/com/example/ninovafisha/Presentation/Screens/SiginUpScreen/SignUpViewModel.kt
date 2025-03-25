package com.example.ninovafisha.Presentation.Screens.SiginUpScreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ninovafisha.Domain.Constant
import com.example.ninovafisha.Domain.Constant.supabase
import com.example.ninovafisha.Domain.Models.Profile
import com.example.ninovafisha.Domain.States.ActualState
import com.example.ninovafisha.Domain.States.Signupstate
import com.example.ninovafisha.Domain.Utils.isEmailValid
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.exception.AuthRestException
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SignUpViewModel: ViewModel() {
    private val _actualState = MutableStateFlow<ActualState>(ActualState.Initialized)
    val actualState: StateFlow<ActualState> = _actualState.asStateFlow()

    private val _signupState = mutableStateOf(Signupstate())
    val signupstate: Signupstate get() = _signupState.value


    fun updateSignup(newState: Signupstate){
        _signupState.value = newState
    }

    fun signupLogic() {
        _actualState.value = ActualState.Loading
        if(signupstate.email != "" && signupstate.name != "" && signupstate.surname != ""){
            if(signupstate.password == signupstate.confirmPass){
                if(signupstate.email.isEmailValid()){
                    viewModelScope.launch {
                        try {
                            supabase.auth.signUpWith(Email)
                            {
                                email = signupstate.email
                                password = signupstate.password
                            }
                            val userId = Constant.supabase.auth.currentUserOrNull()
                            if(userId != null){
                                val profile = Profile(signupstate.name, signupstate.surname, signupstate.datebith, userId.id)
                                supabase.from("Profile").insert(profile)
                                _actualState.value = ActualState.Success("")
                            }
                        }
                        catch (ex: Exception){
                            _actualState.value = ActualState.Error(ex.message ?: "Ошибка получения данных")
                        }
                        catch (ex: AuthRestException){
                            _actualState.value = ActualState.Error(ex.message ?: "Ошибка получения данных")
                        }

                    }
                }
                else{
                    _actualState.value = ActualState.Error("Неправильный email")
                }
            }
            else{
                _actualState.value = ActualState.Error("Пароли не совпадают")
            }
        }
        else{
            _actualState.value = ActualState.Error("Ключевые поля пустые")
        }
    }
}