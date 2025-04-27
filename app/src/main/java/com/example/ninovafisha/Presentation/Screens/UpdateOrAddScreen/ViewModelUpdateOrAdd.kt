package com.example.ninovafisha.Presentation.Screens.UpdateOrAddScreen

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ninovafisha.Domain.Constant
import com.example.ninovafisha.Domain.Models.EventCard
import com.example.ninovafisha.Domain.States.ActualState
import com.example.ninovafisha.Domain.States.EventState
import io.github.jan.supabase.auth.exception.AuthRestException
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import java.util.UUID

class ViewModelUpdateOrAdd(id:String?): ViewModel() {
    private val _actualState = MutableStateFlow<ActualState>(ActualState.Initialized)
    val actualState: StateFlow<ActualState> get() = _actualState.asStateFlow()

    private val _eventState = MutableStateFlow<EventState>(EventState.Initialized)
    val eventState: StateFlow<EventState> get() = _eventState.asStateFlow()

    private val _eventCard = mutableStateOf(EventCard(id = ""))
    val eventCard: EventCard get() = _eventCard.value

    @Serializable
    data class SupabaseEvent(
        val title: String,
        val desc: String,
        val date_start: String?,
        val cost: Double?,
        val age_const: Int,
        val Long_desc: String?
    )

    init {
        loadEvent(id)
    }

    fun loadEvent(eventId:String?){
        _actualState.value = ActualState.Loading
        if(eventId != "null" && eventId != null){
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
        else{
            _actualState.value = ActualState.Success("")
        }
    }

    fun updateEventInfo(newCard: EventCard){
        _eventCard.value = newCard
    }

    fun updateEvent(uri: Uri?, context: Context){
        _eventState.value = EventState.Loading
        viewModelScope.launch {
            if(eventCard.title != "" && eventCard.desc != "" && eventCard.descLong != ""){
                try {
                    var imageUrl:String? = null
                    Log.d("URIIIIIII", "${uri}")
                    uri?.let {
                        Log.d("URIIIIIII", "${uri}")
                        val byt = withContext(Dispatchers.IO) {
                            context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                        }
                        val fileName = "titleimage/${UUID.randomUUID()}.jpg"
                        Constant.supabase.storage
                            .from("images")
                            .upload(fileName, byt!!)
                        imageUrl = Constant.supabase.storage.from("images").publicUrl(fileName)
                    }
                    updateEventInfo(eventCard.copy(image = imageUrl))
                    Constant.supabase.postgrest.from("Events").update(eventCard){filter { eq("id", eventCard.id ?: "") }}
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
            if(eventCard.title != "" && eventCard.desc != "" && eventCard.descLong != "" && eventCard.date != null && eventCard.cost != null){
                if(eventCard.cost == 0f){
                    updateEventInfo(eventCard.copy(cost = null))
                }
                try {
                    Constant.supabase.postgrest.from("Events").insert(
                        SupabaseEvent(
                        title = eventCard.title,
                        desc = eventCard.desc,
                        date_start = eventCard.date?.toString(),
                        cost = eventCard.cost?.toDouble(),
                        age_const = eventCard.ageConst,
                        Long_desc = eventCard.descLong
                    )
                    )
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