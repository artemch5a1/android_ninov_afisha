package com.example.ninovafisha.Presentation.Screens.CardEventScreen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

class ViewModelCardEventScreen(eventId:String): ViewModel() {


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
        viewModelScope.launch {
            _user.value = Constant.supabase.auth.currentUserOrNull()?.id
        }
    }

    fun DeleteLogic(eventId:String){
        _eventState.value = EventState.Loading
        viewModelScope.launch {
            try {
                supabase.postgrest.from("Events").delete { filter { eq("id", eventId) } }
                if(eventCard.image != null){
                    val path = eventCard.image!!.substringAfter("/object/public/images/")
                    Log.d("URIIIIIII", "${path}")
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
    }

    fun loadEvent(eventId:String){
        _actualState.value = ActualState.Loading
        viewModelScope.launch {
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
    }
}