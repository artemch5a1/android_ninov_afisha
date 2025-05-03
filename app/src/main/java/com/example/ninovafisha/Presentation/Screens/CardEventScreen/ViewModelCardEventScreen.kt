package com.example.ninovafisha.Presentation.Screens.CardEventScreen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.ninovafisha.Domain.AuthErrorInterceptor.isTokenValid
import com.example.ninovafisha.Domain.Constant
import com.example.ninovafisha.Domain.Constant.supabase
import com.example.ninovafisha.Domain.States.ActualState
import com.example.ninovafisha.Domain.Models.EventCard
import com.example.ninovafisha.Domain.States.EventState
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.exception.AuthRestException
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ViewModelCardEventScreen(eventId:String, controlNav: NavController): ViewModel() {

    val navCon = controlNav

    private val _actualState = MutableStateFlow<ActualState>(ActualState.Initialized)
    val actualState: StateFlow<ActualState> = _actualState.asStateFlow()

    private val _eventState = MutableStateFlow<EventState>(EventState.Initialized)
    val eventState: StateFlow<EventState> = _eventState.asStateFlow()

    private val _eventCard = mutableStateOf(EventCard(id = ""))
    val eventCard: EventCard get() = _eventCard.value

    private val _user = mutableStateOf<String?>(null)
    val user:String? get() = _user.value

    init {
        loadEvent(eventId)
        userLoad(eventId)
    }

    private fun userLoad(eventId:String){
        _actualState.value = ActualState.Loading
        viewModelScope.launch {
            if(isTokenValid()){
                try {
                    _user.value = Constant.supabase.auth.currentUserOrNull()?.id
                    _actualState.value = ActualState.Initialized
                }
                catch (e:Exception){
                    _actualState.value = ActualState.Error("Ошибка: ${e.message}")
                }
            }
            else{
                Constant.supabase.auth.signOut()
                navCon.navigate("sigin"){
                    popUpTo("eventCard/${eventId}")
                }
            }
        }
    }

    fun deleteLogic(eventId:String){
        _eventState.value = EventState.Loading
        viewModelScope.launch {
            if(isTokenValid()){
                try {
                    supabase.postgrest.from("Events").delete { filter { eq("id", eventId) } }
                    if(eventCard.image != null){
                        val path = eventCard.image!!.substringAfter("/object/public/images/")
                        Log.d("URIIIIIII", "${path} ")
                        Constant.supabase.storage
                            .from("images").delete(path)
                    }
                    _eventState.value = EventState.DeleteOrAdd("")
                    Log.d("Delete","Success")
                }
                catch (ex: AuthRestException){
                    _eventState.value = EventState.Error("Ошибка: ${ex.message}")
                    Log.d("Delete",ex.message.toString())

                }
                catch (ex: Exception){
                    _eventState.value = EventState.Error("Ошибка: ${ex.message}")
                    Log.d("Delete",ex.message.toString())

                }
            }
            else{
                Constant.supabase.auth.signOut()
                navCon.navigate("sigin"){
                    popUpTo("eventCard/${eventId}")
                }
            }
        }
    }

    private fun loadEvent(eventId:String){
        _actualState.value = ActualState.Loading
        viewModelScope.launch {
            if(isTokenValid()){
                try{
                    _eventCard.value = Constant.supabase
                        .from("Events")
                        .select{ filter { eq("id", value = eventId) }}
                        .decodeSingle<EventCard>()
                    _actualState.value = ActualState.Success("")
                }
                catch (ex: Exception){
                    _actualState.value = ActualState.Error("Ошибка загрузки данных: ${ex.message}")
                }
            }
            else{
                Constant.supabase.auth.signOut()
                navCon.navigate("sigin"){
                    popUpTo("eventCard/${eventId}")
                }
            }
        }
    }
}