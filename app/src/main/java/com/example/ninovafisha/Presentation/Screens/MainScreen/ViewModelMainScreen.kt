package com.example.ninovafisha.Presentation.Screens.MainScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ninovafisha.Domain.Constant
import com.example.ninovafisha.Domain.Models.Event
import com.example.ninovafisha.Domain.Models.Profile
import com.example.ninovafisha.Domain.Models.typeEvent
import com.example.ninovafisha.Domain.States.ActualState
import com.example.ninovafisha.Domain.States.FilterState
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.exception.AuthRestException
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.wait


class ViewModelMainScreen: ViewModel() {

    /*viewModelScope.launch {
        val prof = Constant.supabase.postgrest.from("public", "Profile").select().decodeList<Profile>()
        val filt = prof.firstOrNull { p -> p.id == Constant.supabase.auth.currentUserOrNull()?.id}
        var name = filt?.name
    }*/
    private val _actualState = MutableStateFlow<ActualState>(ActualState.Initialized)
    val actualState: StateFlow<ActualState> = _actualState.asStateFlow()

    private val _types = MutableLiveData<List<typeEvent>>()
    val types: LiveData<List<typeEvent>> get() = _types

    private val _events = MutableLiveData<List<Event>>()
    val events: LiveData<List<Event>> get() = _events

    private val _eventsState = MutableStateFlow<Boolean>(false)
    val eventsState: StateFlow<Boolean> = _eventsState.asStateFlow()



    private var FiltEvent = MutableLiveData<List<Event>>()
    val filtEvent: LiveData<List<Event>> get() = FiltEvent

    private var filtType: MutableList<Int> = mutableListOf()

    private var filterState = mutableStateOf(FilterState())


    fun RememberFiltState(textSearch:String){
        filterState.value = filterState.value.copy(textSearch = textSearch, types = filtType)
    }
    fun RemoveType(id:Int){
        filtType.remove(id)
    }
    fun AddType(id:Int){
        filtType.add(id)
    }
    fun refresh() {
        loadEvents()
        loadTypes()
    }

    init {
        refresh()
    }


    fun loadEvents(){
        _actualState.value = ActualState.Loading
        viewModelScope.launch {
            try{
                FiltEvent.value = Constant.supabase.postgrest.from("Events").select().decodeList()
                _eventsState.value = !_eventsState.value
                _events.value = FiltEvent.value
                _actualState.value = ActualState.Initialized
            }
            catch (ex: Exception) {
                _actualState.value = ActualState.Error(ex.message ?: "Ошибка получения данных")
            }
        }
    }

    fun loadTypes(){
        _actualState.value = ActualState.Loading
        viewModelScope.launch {
            try{
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
        if(filtType.isNotEmpty()){
            var filtered = FiltEvent.value?.filter { x -> x.title.contains(filtString) || x.desc.contains(filtString) }
            filtered = filtered?.filter { x -> filtType.contains(x.typeEvent)}
            _events.value = filtered ?: emptyList()
        }
        else{
            val filtered = FiltEvent.value?.filter { x -> x.title.contains(filtString) || x.desc.contains(filtString) }
            _events.value = filtered ?: emptyList()
        }
    }

    fun filtevent(){
        if(filtType.isNotEmpty()){
            var filtered = FiltEvent.value?.filter { x -> x.title.contains(filterState.value.textSearch) || x.desc.contains(filterState.value.textSearch) }
            filtered = filtered?.filter { x -> filterState.value.types.contains(x.typeEvent)}
            _events.value = filtered ?: emptyList()
        }
        else{
            val filtered = FiltEvent.value?.filter { x -> x.title.contains(filterState.value.textSearch) || x.desc.contains(filterState.value.textSearch) }
            _events.value = filtered ?: emptyList()
        }
    }



    /*suspend fun GetName(): String{
        return withContext(Dispatchers.IO) {
            try {
                var name = Constant.supabase.postgrest.from("public", "Profile").select().decodeList<Profile>().firstOrNull { p -> p.id == Constant.supabase.auth.currentUserOrNull()!!.id }!!.name
                name
            } catch (ex: AuthRestException) {
                "Ошибка: " + ex.message
            }
            catch (ex: Exception){
                "авторизация не выполнена"
            }
        }
    }*/
    fun GetOut(){
        viewModelScope.launch {
            Constant.supabase.auth.signOut()
        }
    }
}