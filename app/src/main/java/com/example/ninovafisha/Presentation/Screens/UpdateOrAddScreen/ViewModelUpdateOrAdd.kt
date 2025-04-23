package com.example.ninovafisha.Presentation.Screens.UpdateOrAddScreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ninovafisha.Domain.Constant
import com.example.ninovafisha.Domain.Models.EventCard
import com.example.ninovafisha.Domain.States.ActualState
import com.example.ninovafisha.Domain.States.EventState
import io.github.jan.supabase.auth.exception.AuthRestException
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ViewModelUpdateOrAdd(id:String?): ViewModel() {
    private val _actualState = MutableStateFlow<ActualState>(ActualState.Initialized)
    val actualState: StateFlow<ActualState> get() = _actualState.asStateFlow()

    private val _eventState = MutableStateFlow<EventState>(EventState.Initialized)
    val eventState: StateFlow<EventState> get() = _eventState.asStateFlow()

    private val _eventCard = mutableStateOf(EventCard(id = ""))
    val eventCard: EventCard get() = _eventCard.value



    init {
        if(id != null)
        {
            loadEvent(id)
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

    fun updateEventInfo(newCard: EventCard){
        _eventCard.value = newCard
    }

    fun updateEvent(){
        _eventState.value = EventState.Loading
        viewModelScope.launch {
            if(eventCard.title != "" && eventCard.desc != "" && eventCard.descLong != ""){
                if(eventCard.cost == 0f){
                    updateEventInfo(eventCard.copy(cost = null))
                }
                try {
                    Constant.supabase.postgrest.from("Events").update(eventCard){filter { eq("id", eventCard.id) }}
                    _eventState.value = EventState.Updated("")
                }
                catch (e:AuthRestException){
                    _eventState.value = EventState.Error("${e.errorDescription} ")
                }
                catch (e:Exception){
                    _eventState.value = EventState.Error("${e.message} ")
                }
            }
            else {
                _eventState.value = EventState.Error("Не все поля заполнены")
            }
        }
    }

    fun addEvent(){
        _eventState.value = EventState.Loading
        viewModelScope.launch {
            if(eventCard.title != "" && eventCard.desc != "" && eventCard.descLong != ""){
                if(eventCard.cost == 0f){
                    updateEventInfo(eventCard.copy(cost = null))
                }
                try {
                    Constant.supabase.postgrest.from("Events").insert(eventCard)
                    _eventState.value = EventState.DeleteOrAdd("Event added")
                }
                catch (e:AuthRestException){
                    _eventState.value = EventState.Error("${e.errorDescription} ")
                }
                catch (e:Exception){
                    _eventState.value = EventState.Error("${e.message} ")
                }
            }
            else {
                _eventState.value = EventState.Error("Не все поля заполнены")
            }
        }
    }
}