package com.example.ninovafisha.Presentation.Screens.ListEventsScreen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.ninovafisha.Domain.Constant
import com.example.ninovafisha.Domain.Models.Event
import com.example.ninovafisha.Domain.Models.Profile
import com.example.ninovafisha.Domain.Models.typeEvent
import com.example.ninovafisha.Domain.States.ActualState
import com.example.ninovafisha.Domain.States.FilterState
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class ViewModelListEventsScreen: ViewModel() {

    private val _actualState = MutableStateFlow<ActualState>(ActualState.Initialized)
    val actualState: StateFlow<ActualState> = _actualState.asStateFlow()

    private val _types = MutableLiveData<List<typeEvent>>()
    val types: LiveData<List<typeEvent>> get() = _types

    private val _events = MutableLiveData<List<Event>>()
    val events: LiveData<List<Event>> get() = _events

    private val _eventsState = MutableStateFlow<Boolean>(false)
    val eventsState: StateFlow<Boolean> = _eventsState.asStateFlow()


    private var FiltEvent = MutableLiveData<List<Event>>()

    private var _filtType: MutableList<Int> = mutableListOf()
    val filtType: List<Int> get() = _filtType

    private val _user = mutableStateOf<String?>(null)
    val user:String? get() = _user.value
    private val _userRole = mutableStateOf<Profile?>(null)
    val userRole:Profile? get() = _userRole.value

    fun toggleType(typeId: Int, textSearch: String) {
        if (_filtType.contains(typeId)) {
            _filtType.remove(typeId)
        } else {
            _filtType.add(typeId)
        }
        filtevent(textSearch)
    }

    private var filterState = mutableStateOf(FilterState())


    fun RememberFiltState(textSearch:String){
        filterState.value = filterState.value.copy(textSearch = textSearch, types = _filtType)
    }
    fun refresh() {
        loadEvents()
        loadUser()
    }

    fun loadUser(){
        _actualState.value = ActualState.Loading
        try {
            viewModelScope.launch {
                _user.value = Constant.supabase.auth.currentUserOrNull()?.id
                val id = Constant.supabase.auth.currentUserOrNull()?.id ?: ""
                if(id != ""){
                    _userRole.value = Constant.supabase.postgrest.from("Profile").select{ filter { eq("id", value = id) }}.decodeSingleOrNull()
                }
                else{
                    _userRole.value = null
                }
                Log.d("role = ", "${_userRole.value?.role}")
            }
        }
        catch (e:Exception){
            _actualState.value = ActualState.Error("${e.message} ")
        }
    }

    init {
        refresh()
    }


    fun loadEvents(){
        _actualState.value = ActualState.Loading
        viewModelScope.launch {
            try{
                FiltEvent.value = Constant.supabase.postgrest.from("Events").select{ filter { eq("hide", value = true) }}.decodeList()
                _eventsState.value = !_eventsState.value
                _events.value = FiltEvent.value
                _types.value = Constant.supabase.postgrest.from("type_event").select().decodeList()
                _eventsState.value = !_eventsState.value
                _actualState.value = ActualState.Initialized
            }
            catch (ex: Exception) {
                _actualState.value = ActualState.Error(ex.message ?: "Ошибка получения данных")
            }
        }
    }

    fun filtevent(filtString:String){
        var filtered = FiltEvent.value?.filter { x -> x.title.contains(filtString) || x.desc.contains(filtString) }
        if(_filtType.isNotEmpty()){
            filtered = filtered?.filter { x -> _filtType.contains(x.typeEvent)}
        }
        _events.value = filtered ?: emptyList()
    }

    fun GetOut(controller: NavHostController){
        viewModelScope.launch {
            Constant.supabase.auth.signOut()
            controller.navigate("sigin"){
                popUpTo("main") { inclusive = true }
            }
        }
    }
}