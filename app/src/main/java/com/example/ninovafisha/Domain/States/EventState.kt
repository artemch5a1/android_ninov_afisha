package com.example.ninovafisha.Domain.States

sealed class EventState {
    data object Loading: EventState()
    data object Initialized: EventState()
    data class DeleteOrAdd(val message: String): EventState()
    data class Error(val message: String): EventState()
    data class Updated(val message: String): EventState()
}
