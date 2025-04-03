package com.example.ninovafisha.Domain.States

data class FilterState (
    val textSearch: String = "",
    val types: List<Int> = listOf()
)