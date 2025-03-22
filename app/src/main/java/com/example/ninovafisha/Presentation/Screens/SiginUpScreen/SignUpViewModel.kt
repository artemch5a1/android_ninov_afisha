package com.example.ninovafisha.Presentation.Screens.SiginUpScreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ninovafisha.Domain.Constant.supabase
import com.example.ninovafisha.Domain.Models.Profile
import com.example.ninovafisha.Domain.States.ActualState
import com.example.ninovafisha.Domain.States.Signupstate
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
                viewModelScope.launch {
                    try {
                        val authResponse = supabase.auth.signUpWith(Email)
                        {
                            email = signupstate.email
                            password = signupstate.password
                        }
                        val userId: String? = authResponse?.id

                        val profile = Profile(signupstate.name, signupstate.surname, signupstate.datebith, userId)
                        supabase.from("Profile").insert(profile)
                        _actualState.value = ActualState.Success("")
                    }
                    catch (ex: Exception){
                        _actualState.value = ActualState.Error(ex.message ?: "Ошибка получения данных")
                    }
                    catch (ex: AuthRestException){
                        _actualState.value = ActualState.Error(ex.message ?: "Ошибка получения данных")
                    }

                }
            }
        }
    }


}