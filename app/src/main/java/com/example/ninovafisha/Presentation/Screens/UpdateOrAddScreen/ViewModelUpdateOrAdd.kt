package com.example.ninovafisha.Presentation.Screens.UpdateOrAddScreen

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ninovafisha.Domain.Constant
import com.example.ninovafisha.Domain.Models.EventCard
import com.example.ninovafisha.Domain.Models.typeEvent
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
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

class ViewModelUpdateOrAdd(id:String?): ViewModel() {
    private val _actualState = MutableStateFlow<ActualState>(ActualState.Initialized)
    val actualState: StateFlow<ActualState> get() = _actualState.asStateFlow()

    private val _eventState = MutableStateFlow<EventState>(EventState.Initialized)
    val eventState: StateFlow<EventState> get() = _eventState.asStateFlow()

    private val _eventCard = mutableStateOf(EventCard(id = ""))
    val eventCard: EventCard get() = _eventCard.value

    private val _types = MutableLiveData<List<typeEvent>>()
    val types: LiveData<List<typeEvent>> get() = _types





    @Serializable
    data class SupabaseEvent(
        val title: String,
        val desc: String,
        val date_start: String?,
        val cost: Float?,
        val age_const: Int,
        val Long_desc: String?,
        val image:String?,
        @SerialName("type_event")
        val typeId:Int
    )

    init {
        loadEvent(id)
        /*loadTypes()*/
    }

    fun loadEvent(eventId:String?){
        _actualState.value = ActualState.Loading
        viewModelScope.launch {
            if (eventId != "null" && eventId != null) {
                try {
                    _eventCard.value = Constant.supabase
                        .from("Events")
                        .select { filter { eq("id", value = eventId) } }
                        .decodeSingle<EventCard>()
                    _types.value = Constant.supabase.postgrest.from("type_event").select().decodeList()
                    _actualState.value = ActualState.Success("")
                } catch (ex: Exception) {
                    _actualState.value = ActualState.Error("Ошибка загрузки данных: ${ex.message}")
                }

            } else {
                _types.value = Constant.supabase.postgrest.from("type_event").select().decodeList()
                _actualState.value = ActualState.Success("")
            }
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
                        if(eventCard.image != null){
                            val path = eventCard.image!!.substringAfter("/object/public/images/")
                            Log.d("URIIIIIII", path)
                            Constant.supabase.storage
                                .from("images").delete(path)
                        }
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

    fun addEvent(uri: Uri?, context: Context){
        _eventState.value = EventState.Loading
        viewModelScope.launch {
            if(eventCard.title != "" && eventCard.desc != "" && eventCard.descLong != "" && eventCard.date != null && eventCard.cost != null){
                if(eventCard.cost == 0f){
                    updateEventInfo(eventCard.copy(cost = null))
                }
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
                    Constant.supabase.postgrest.from("Events").insert(
                        SupabaseEvent(
                        title = eventCard.title,
                        desc = eventCard.desc,
                        date_start = eventCard.date,
                        cost = eventCard.cost,
                        age_const = eventCard.ageConst,
                        Long_desc = eventCard.descLong, image = eventCard.image,
                            typeId = eventCard.typeEvent
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