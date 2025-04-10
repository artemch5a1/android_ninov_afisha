package com.example.ninovafisha.Presentation.Screens.CardEventScreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ninovafisha.Domain.Constant
import com.example.ninovafisha.Domain.States.ActualState
import com.example.ninovafisha.Domain.Models.EventCard
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ViewModelCardEventScreen(): ViewModel() {


    private val _actualState = MutableStateFlow<ActualState>(ActualState.Initialized)
    val actualState: StateFlow<ActualState> = _actualState.asStateFlow()

    private val _eventCard = mutableStateOf(EventCard(id = ""))
    val eventCard: EventCard get() = _eventCard.value


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