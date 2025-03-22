package com.example.ninovafisha.Domain.States

sealed class ActualState {
    data object Loading: ActualState()
    data object Initialized: ActualState()
    data class Success(val message: String): ActualState()
    data class Error(val message: String): ActualState()
}
